# Estandarización de columnas de auditoría — Design Spec
**Fecha:** 2026-06-29
**Estado:** Aprobado

---

## Objetivo

Estandarizar todas las tablas de la base de datos y sus entidades JPA para que tengan un conjunto uniforme de columnas de auditoría: `id`, `creado_en`, `creado_por`, `actualizado_en`, `actualizado_por`; y mantener `estado` solo donde ya existe. Eliminar inconsistencias de PKs, campos duplicados y entidades sin clase base.

---

## Decisiones de diseño

| Decisión | Valor |
|----------|-------|
| Campos universales | `id`, `creado_en`, `creado_por`, `actualizado_en`, `actualizado_por` |
| `estado` | Solo tablas que ya lo tienen (sin añadir a tablas nuevas) |
| `creado_por` / `actualizado_por` | UUID del usuario autenticado (del JWT); sin FK constraint |
| Operaciones del sistema | UUID centinela `00000000-0000-0000-0000-000000000001` |
| Telemetría y KPI | Excluidas de `actualizado_en`, `actualizado_por`, `estado`; solo reciben `creado_por` |
| PKs | Todas renombradas a `id` (elimina `usuario_id`, `rol_id`, `distrito_id`, etc.) |
| Excepción PK | `telemetria.estado_unidad_actual` mantiene `unidad_id_externo` (PK natural, upsert) |
| Enfoque de migración | Incremental por módulo (V007–V013) |
| `ddl-auto` docker | `none` — Flyway es única fuente de verdad |

---

## Sección 1: UUID centinela del sistema

```
SISTEMA_USER_ID = 00000000-0000-0000-0000-000000000001
```

- Se inserta en V007 como usuario SISTEMA en `auth.usuario`
- Se usa como `DEFAULT` en todas las columnas `creado_por` de las migraciones
- Se define en `shared-kernel` como `SystemConstants.SISTEMA_USER_ID`
- Sin FK constraint hacia `auth.usuario`

---

## Sección 2: Cambios en `shared`

### `BaseJpaEntity` (shared-persistence)

Campos tras el refactor:

| Campo Java | Columna BD | Nullable | Updatable |
|------------|-----------|----------|-----------|
| `id` | `id` | false | false |
| `creadoEn` | `creado_en` | false | false |
| `actualizadoEn` | `actualizado_en` | true | true |
| `creadoPor` | `creado_por` | false | false |
| `actualizadoPor` | `actualizado_por` | true | true |

- `@PrePersist` fija `id`, `creadoEn`, `actualizadoEn`
- `@PreUpdate` fija `actualizadoEn`
- `creadoPor` y `actualizadoPor` son inyectados desde el command handler antes de `save()`

### `TenantAwareJpaEntity` — sin cambios estructurales
Hereda los nuevos campos automáticamente.

### `SystemConstants` (shared-kernel)
```java
public final class SystemConstants {
    public static final UUID SISTEMA_USER_ID =
        UUID.fromString("00000000-0000-0000-0000-000000000001");
}
```

---

## Sección 3: Migraciones Flyway (V007–V013)

### V007 — `sistema_user_seed`
Inserta usuario SISTEMA con UUID centinela en `auth.usuario`.

### V008 — `auth_audit_columns`
Tablas: `auth.usuario`, `auth.rol`, `auth.permiso`, `auth.refresh_token`, `auth.login_auditoria`, `auth.rol_permiso`

Operaciones por tabla:
- Renombrar PK `<tabla>_id` → `id`
- `ADD COLUMN creado_por UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001'`
- `ADD COLUMN actualizado_por UUID`
- `ADD COLUMN actualizado_en TIMESTAMPTZ` (donde no exista)

### V009 — `operacion_audit_columns`
Tablas: `operacion.distrito`, `operacion.zona`, `operacion.deposito`, `operacion.contenedor`, `operacion.unidad`, `operacion.chofer`, `operacion.turno`, `operacion.ruta`, `operacion.ruta_parada`, `operacion.ruta_version`, `operacion.ruta_evento`, `operacion.horario_recoleccion`

Mismo patrón que V008.

### V010 — `ciudadano_audit_columns`
Tablas: `ciudadano.ciudadano`, `ciudadano.alerta_ciudadana`, `ciudadano.alerta_foto`, `ciudadano.alerta_estado_historial`, `ciudadano.validacion_alerta`

Caso especial `alerta_ciudadana`:
- `RENAME COLUMN registrada_en TO creado_en`
- `RENAME COLUMN actualizada_en TO actualizado_en`

### V011 — `kpi_audit_columns`
Tablas: `kpi.resumen_operativo_diario`, `kpi.kpi_ruta`, `kpi.kpi_unidad`, `kpi.kpi_zona`, `kpi.kpi_alerta`

Solo añade:
- Renombrar PK → `id`
- `ADD COLUMN creado_por UUID NOT NULL DEFAULT '...'`
- **No** `actualizado_en`, **no** `actualizado_por`, **no** `estado`

### V012 — `auditoria_telemetria_audit_columns`
Tablas: `auditoria.evento_auditoria`, `auditoria.outbox_event`, `telemetria.dispositivo_gps`, `telemetria.telemetria_gps`, `telemetria.estado_unidad_actual` (excepción PK), `telemetria.evento_desvio_ruta`, `telemetria.evento_conectividad`

Solo añade:
- Renombrar PK → `id` (excepto `estado_unidad_actual`)
- `ADD COLUMN creado_por UUID NOT NULL DEFAULT '...'`

### V013 — `rutas_integracion_audit_columns`
Tablas de módulos `rutas` e `integracion`. Mismo patrón que V008/V009.

---

## Sección 4: Refactor de entidades JPA

### Regla general
Todas las entidades:
- Eliminan campos propios `creadoEn`, `actualizadoEn`, `@PrePersist`/`@PreUpdate` duplicados
- Eliminan `@AttributeOverride` de PKs (ya no necesarios)
- No declaran `creadoEn`/`actualizadoEn`/`creadoPor`/`actualizadoPor` — los heredan

### Entidades standalone que adoptan clase base

| Entidad | Nueva base |
|---------|-----------|
| `RutaVersionJpaEntity` | `TenantAwareJpaEntity` |
| `RutaEventoJpaEntity` | `TenantAwareJpaEntity` |
| `AlertaHistorialJpaEntity` | `TenantAwareJpaEntity` |
| `ValidacionAlertaJpaEntity` | `TenantAwareJpaEntity` |
| `ResumenOperativoDiarioJpaEntity` | `TenantAwareJpaEntity` |
| `KpiRutaJpaEntity` | `TenantAwareJpaEntity` |
| `KpiUnidadJpaEntity` | `TenantAwareJpaEntity` |
| `KpiZonaJpaEntity` | `TenantAwareJpaEntity` |
| `KpiAlertaJpaEntity` | `TenantAwareJpaEntity` |
| `EventoAuditoriaJpaEntity` | `BaseJpaEntity` |
| `OutboxEventJpaEntity` | `BaseJpaEntity` |

### Inyección de `creadoPor`/`actualizadoPor` en handlers

```java
// En command handlers con usuario autenticado:
entity.setCreadoPor(currentUserProvider.requireCurrentUser().userId());

// En operaciones sin usuario (jobs, eventos internos):
entity.setCreadoPor(SystemConstants.SISTEMA_USER_ID);
```

### `AlertaCiudadanaJpaEntity` — caso especial
Tras V010, hereda `creado_en`/`actualizado_en` desde `TenantAwareJpaEntity`. Elimina campos `registradaEn`/`actualizadaEn` propios.

---

## Sección 5: Verificación por módulo

### Ciclo por HU
```
Migración aplicada → ./gradlew :<modulo>:compileJava → docker compose up → logs sin SchemaManagementException
```

### Verificación final
```bash
./gradlew build -x test
docker compose up -d --build
docker compose logs backend | grep -E "(Started|ERROR)"
```

Criterio de éxito: `Started CiudadSanaApplication` sin ningún `SchemaManagementException`.

---

## Backlog del sprint

| HU | Módulo | Migraciones | Entidades afectadas |
|----|--------|-------------|---------------------|
| HU-1 | shared | — | `BaseJpaEntity`, `SystemConstants` |
| HU-2 | auth | V007, V008 | `UsuarioJpaEntity`, `RolJpaEntity`, `PermisoJpaEntity`, `RefreshTokenJpaEntity`, `LoginAuditoriaJpaEntity` |
| HU-3 | operacion | V009 | `DistritoJpaEntity`, `ZonaJpaEntity`, `DepositoJpaEntity`, `ContenedorJpaEntity`, `UnidadJpaEntity`, `ChoferJpaEntity`, `TurnoJpaEntity`, `RutaJpaEntity`, `RutaParadaJpaEntity`, `RutaVersionJpaEntity`, `RutaEventoJpaEntity`, `HorarioRecoleccionJpaEntity` |
| HU-4 | ciudadano | V010 | `CiudadanoJpaEntity`, `AlertaCiudadanaJpaEntity`, `AlertaFotoJpaEntity`, `AlertaHistorialJpaEntity`, `ValidacionAlertaJpaEntity` |
| HU-5 | kpi | V011 | `ResumenOperativoDiarioJpaEntity`, `KpiRutaJpaEntity`, `KpiUnidadJpaEntity`, `KpiZonaJpaEntity`, `KpiAlertaJpaEntity` |
| HU-6 | auditoria + telemetria | V012 | `EventoAuditoriaJpaEntity`, `OutboxEventJpaEntity`, `DispositivoGpsJpaEntity`, `PingGpsJpaEntity`, `EstadoUnidadJpaEntity`, `DesvioRutaJpaEntity`, `EventoConectividadJpaEntity` |
| HU-7 | rutas + integracion | V013 | Entidades de ambos módulos |
| HU-8 | handlers (todos los módulos) | — | Inyección `creadoPor`/`actualizadoPor` en todos los command handlers |

---

## Incluido en HU-1 (shared)
- Eliminar `AuditableJpaEntity` (sin uso, reemplazada por el nuevo `BaseJpaEntity`)

## Deuda conocida (fuera de este sprint)
- Índices en columnas `creado_por`/`actualizado_por` (decisión de performance futura)
- `application-local.yml` puede mantener `ddl-auto: validate` para detectar regresiones en dev local
