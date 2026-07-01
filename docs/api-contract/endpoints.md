# Endpoints REST — Tabla completa

Base URL: `http://localhost:8080` (configurable con `SERVER_PORT`)  
Todos los paths bajo `/api/v1/`.  
Respuesta envuelta en `ApiResponse<T>` — ver [envelopes-y-errores.md](envelopes-y-errores.md).

> ⚠️ Las rutas marcadas con 🔧 tienen [known issues](README.md#known-issues).

---

## AUTH

### POST /api/v1/auth/login 🌐
| | |
|--|--|
| **Público** | Sí — sin JWT |
| **Body** | `{ username: string, password: string, tenantId: UUID }` |
| **Respuesta** | `ApiResponse<LoginResponseDto>` |
| **LoginResponseDto** | `{ accessToken, refreshToken, tokenType, expiresIn, usuarioId, username, roles[] }` |

### POST /api/v1/auth/refresh 🌐
| | |
|--|--|
| **Público** | Sí |
| **Body** | `{ refreshToken: string }` |
| **Respuesta** | `ApiResponse<LoginResponseDto>` |

### POST /api/v1/auth/logout
| | |
|--|--|
| **Roles** | Autenticado |
| **Body** | `{ refreshToken: string }` |
| **Respuesta** | 204 No Content |

### GET /api/v1/auth/me
| | |
|--|--|
| **Roles** | Autenticado |
| **Respuesta** | `ApiResponse<UsuarioResponseDto>` |

---

## USUARIOS

### POST /api/v1/usuarios
| | |
|--|--|
| **Roles** | ADMIN |
| **Body** | `RegistrarUsuarioRequest { nombresCompletos, email, username, password, telefono? }` |
| **Respuesta 201** | `ApiResponse<UsuarioResponseDto>` |

### GET /api/v1/usuarios
| | |
|--|--|
| **Roles** | ADMIN, SUPERVISOR |
| **Query** | `?page&size` |
| **Respuesta** | `ApiResponse<PageResult<UsuarioResponseDto>>` |

### PATCH /api/v1/usuarios/{usuarioId}/bloquear
| | |
|--|--|
| **Roles** | ADMIN |
| **Respuesta** | 204 |

### PATCH /api/v1/usuarios/{usuarioId}/activar
| | |
|--|--|
| **Roles** | ADMIN |
| **Respuesta** | 204 |

### POST /api/v1/usuarios/{usuarioId}/roles
| | |
|--|--|
| **Roles** | ADMIN |
| **Body** | `{ rolId: UUID }` |
| **Respuesta** | 204 |

---

## ROLES

### POST /api/v1/roles
| | |
|--|--|
| **Roles** | ADMIN |
| **Body** | `{ codigo, nombre, descripcion? }` |
| **Respuesta 201** | `ApiResponse<RolResponseDto>` |

### GET /api/v1/roles
`?page&size` → `ApiResponse<PageResult<RolResponseDto>>`

### GET /api/v1/roles/{rolId}
→ `ApiResponse<RolResponseDto>`

### PUT /api/v1/roles/{rolId}
Body `{ nombre, descripcion? }` → `ApiResponse<RolResponseDto>`

### PATCH /api/v1/roles/{rolId}/desactivar
→ 204

### POST /api/v1/roles/{rolId}/permisos
Body `{ permisoId: UUID }` → 204

### DELETE /api/v1/roles/{rolId}/permisos/{permisoId}
→ 204

---

## PERMISOS

### POST /api/v1/permisos
Body `{ codigo, modulo, descripcion? }` → `ApiResponse<PermisoResponseDto>` (201)

### GET /api/v1/permisos
`?page&size` → `ApiResponse<PageResult<PermisoResponseDto>>`

### GET /api/v1/permisos/{permisoId}
→ `ApiResponse<PermisoResponseDto>`

### PUT /api/v1/permisos/{permisoId}
Body `{ modulo, descripcion? }` → `ApiResponse<PermisoResponseDto>`

---

## OPERACIÓN — Distritos

Base: `/api/v1/operacion/distritos`

| Método | Path | Roles | Body / Query | Respuesta |
|--------|------|-------|-------------|-----------|
| POST | `` | ADMIN,SUPERVISOR | `{ nombre, ubigeo? }` | 201 `DistritoResponseDto` |
| GET | `/{id}` | ADMIN,SUPERVISOR,ANALISTA | — | `DistritoResponseDto` |
| GET | `` | ADMIN,SUPERVISOR,ANALISTA | `?page&size` | `PageResult<DistritoResponseDto>` |
| PATCH | `/{id}/desactivar` | ADMIN | — | 204 |
| PATCH | `/{id}/activar` | ADMIN | — | 204 |

### DistritoResponseDto
```json
{ "id": "UUID", "tenantId": "UUID", "nombre": "Miraflores", "ubigeo": "150122",
  "estado": "ACTIVO", "creadoEn": "Instant", "actualizadoEn": "Instant" }
```

---

## OPERACIÓN — Zonas

Base: `/api/v1/operacion/zonas`

| Método | Path | Roles | Body / Query | Respuesta |
|--------|------|-------|-------------|-----------|
| POST | `` | ADMIN,SUPERVISOR | `{ distritoId, codigo, nombre, tipoZona, prioridad }` | 201 `ZonaResponseDto` |
| PUT | `/{id}` | ADMIN,SUPERVISOR | `{ prioridad? }` | `ZonaResponseDto` |
| GET | `/{id}` | todos autenticados | — | `ZonaResponseDto` |
| GET | `` | todos autenticados | `?page&size` | `PageResult<ZonaResponseDto>` |
| PATCH | `/{id}/desactivar` | ADMIN | — | 204 |

---

## OPERACIÓN — Depósitos

Base: `/api/v1/operacion/depositos`

| Método | Path | Roles | Body | Respuesta |
|--------|------|-------|------|-----------|
| POST | `` | ADMIN,SUPERVISOR | `{ distritoId, nombre, tipo }` | 201 `DepositoResponseDto` |
| GET | `/{id}` | todos | — | `DepositoResponseDto` |
| GET | `` | todos | `?page&size` | `PageResult<DepositoResponseDto>` |
| PATCH | `/{id}/desactivar` | ADMIN | — | 204 |

---

## OPERACIÓN — Contenedores

Base: `/api/v1/operacion/contenedores`

| Método | Path | Roles | Body | Respuesta |
|--------|------|-------|------|-----------|
| POST | `` | ADMIN,SUPERVISOR | `{ zonaId, codigo, capacidadM3 }` | 201 `ContenedorResponseDto` |
| GET | `/{id}` | todos | — | `ContenedorResponseDto` |
| GET | `` | todos | `?page&size` | `PageResult<ContenedorResponseDto>` |
| PATCH | `/{id}/estado` | ADMIN,SUPERVISOR,OPERADOR | `{ nuevoEstado }` | 204 |

---

## OPERACIÓN — Unidades

Base: `/api/v1/operacion/unidades`

| Método | Path | Roles | Body | Respuesta |
|--------|------|-------|------|-----------|
| POST | `` | ADMIN,SUPERVISOR | `{ placa, codigoInterno, tipoUnidad, capacidadM3, capacidadKg }` | 201 `UnidadResponseDto` |
| GET | `/{id}` | todos | — | `UnidadResponseDto` |
| GET | `` | todos | `?page&size` | `PageResult<UnidadResponseDto>` |
| PATCH | `/{id}/estado` | ADMIN,SUPERVISOR | `{ nuevoEstado }` | 204 |

### UnidadResponseDto (campos clave)
```json
{ "id": "UUID", "placa": "CAM-489", "codigoInterno": "CAM-4892",
  "tipoUnidad": "COMPACTADOR", "capacidadM3": 6.0, "capacidadKg": 8000.0,
  "estadoOperativo": "OPERATIVA" }
```

---

## OPERACIÓN — Choferes

Base: `/api/v1/operacion/choferes`

| Método | Path | Roles | Body | Respuesta |
|--------|------|-------|------|-----------|
| POST | `` | ADMIN,SUPERVISOR | `{ nombres, apellidos, dni?, licencia?, telefono? }` | 201 `ChoferResponseDto` |
| GET | `/{id}` | todos | — | `ChoferResponseDto` |
| GET | `` | todos | `?page&size` | `PageResult<ChoferResponseDto>` |
| PATCH | `/{id}/estado` | ADMIN,SUPERVISOR | `{ estado }` | `ChoferResponseDto` |

---

## OPERACIÓN — Turnos

Base: `/api/v1/operacion/turnos`

| Método | Path | Roles | Body | Respuesta |
|--------|------|-------|------|-----------|
| POST | `` | ADMIN,SUPERVISOR | `{ unidadId, choferId, distritoId, fecha, horaInicio, horaFin, tipoTurno }` | 201 `TurnoResponseDto` |
| GET | `/{id}` | todos | — | `TurnoResponseDto` |
| GET | `` | todos | `?page&size` | `PageResult<TurnoResponseDto>` |
| PATCH | `/{id}/iniciar` | ADMIN,SUPERVISOR,OPERADOR | — | 204 |
| PATCH | `/{id}/finalizar` | ADMIN,SUPERVISOR,OPERADOR | — | 204 |
| PATCH | `/{id}/cancelar` | ADMIN,SUPERVISOR | — | 204 |

---

## OPERACIÓN — Horarios de recolección

Base: `/api/v1/operacion/horarios-recoleccion`

| Método | Path | Roles | Body / Query | Respuesta |
|--------|------|-------|-------------|-----------|
| POST | `` | ADMIN,SUPERVISOR | `{ zonaId, diaSemana(1-7), horaInicio, horaFin, observacion? }` | 201 `HorarioResponseDto` |
| PUT | `/{horarioId}` | ADMIN,SUPERVISOR | `{ horaInicio, horaFin, observacion? }` | `HorarioResponseDto` |
| DELETE | `/{horarioId}` | ADMIN | — | 204 |
| GET | `/{horarioId}` | todos | — | `HorarioResponseDto` |
| GET | `` | todos | `?zonaId&page&size` | `PageResult<HorarioResponseDto>` |

---

## RUTAS

Base: `/api/v1/rutas`

| Método | Path | Roles | Body / Query | Respuesta |
|--------|------|-------|-------------|-----------|
| POST | `` | ADMIN,SUPERVISOR | `CrearRutaRequest` | 201 `RutaResponseDto` |
| POST | `/optimizar` | ADMIN,SUPERVISOR | `OptimizarRutaRequest` | 201 `RutaResponseDto` |
| POST | `/{id}/reoptimizar` | ADMIN,SUPERVISOR | `ReoptimizarRutaRequest` | 201 `RutaResponseDto` |
| GET | `/{id}` | ADMIN,SUPERVISOR,OPERADOR,ANALISTA | — | `RutaResponseDto` |
| GET | `/{id}/detalle` | ADMIN,SUPERVISOR,OPERADOR,ANALISTA | — | `RutaDetalleResponseDto` |
| GET | `` | ADMIN,SUPERVISOR,OPERADOR,ANALISTA | `?distritoId&fecha&estado&page&size` | `PageResult<RutaResponseDto>` |
| PATCH | `/{id}/aprobar` | ADMIN,SUPERVISOR | — | `RutaResponseDto` |
| PATCH | `/{id}/iniciar-ejecucion` | ADMIN,SUPERVISOR,OPERADOR | — | `RutaResponseDto` |
| PATCH | `/{id}/finalizar` | ADMIN,SUPERVISOR,OPERADOR | — | `RutaResponseDto` |
| PATCH | `/{id}/cancelar` | ADMIN,SUPERVISOR | — | `RutaResponseDto` |
| POST | `/{id}/versiones` | ADMIN,SUPERVISOR | `AgregarVersionRutaRequest` | 201 `RutaResponseDto` |
| PATCH | `/{id}/paradas/{paradaId}` | ADMIN,SUPERVISOR,OPERADOR | `{ nuevoEstado, horaLlegada?, horaSalida? }` | `RutaResponseDto` |
| POST | `/{id}/eventos` | ADMIN,SUPERVISOR,OPERADOR | `{ tipoEvento, descripcion?, datosJson? }` | 201 `RutaResponseDto` |

### OptimizarRutaRequest (campos clave)
```json
{
  "turnoId": "UUID",
  "distritoId": "UUID",
  "depositoOrigenId": "UUID",
  "depositoDestinoId": "UUID",
  "fecha": "2026-07-01",
  "tipoRuta": "OPTIMIZADA",
  "unidades": [
    { "unidadId": "UUID", "capacidadKg": 8000, "inicioDisponibilidad": "05:00", "finDisponibilidad": "13:00" }
  ],
  "zonas": [
    { "zonaId": "UUID", "latitud": -12.1179, "longitud": -77.0330, "demandaKg": 450, "ventanaInicio": "06:00", "ventanaFin": "10:00", "prioridad": 3 }
  ],
  "alertasCriticas": [],
  "parametrosSolver": { "tiempoLimiteS": 30, "objetivo": "DISTANCIA", "penaltaCritica": 1000 }
}
```

---

## TELEMETRÍA

Base: `/api/v1/telemetria`

### Pings GPS
| Método | Path | Roles | Body / Query | Respuesta |
|--------|------|-------|-------------|-----------|
| POST | `/pings` | ADMIN,OPERADOR | `ProcesarPingGpsRequest` | 201 `PingGpsResponseDto` |
| GET | `/pings/unidad/{unidadId}` | ADMIN,SUPERVISOR,OPERADOR,ANALISTA | `?page&size` | `PageResult<PingGpsResponseDto>` |
| GET | `/pings/historico` | ADMIN,SUPERVISOR,ANALISTA | `?unidadId&desde&hasta&page&size` | `PageResult<PingGpsResponseDto>` |

**ProcesarPingGpsRequest** (app móvil chofer):
```json
{
  "dispositivoId": "UUID",
  "unidadExternoId": "UUID",
  "rutaExternoId": "UUID",
  "ts": "2026-07-01T08:30:00Z",
  "latitud": -12.1179,
  "longitud": -77.0330,
  "velocidadKmh": 35.0,
  "rumboGrados": 180,
  "precisionM": 5.0,
  "origen": "APP"
}
```

### Estado de unidades (mapa en tiempo real)
| Método | Path | Roles | Query | Respuesta |
|--------|------|-------|-------|-----------|
| GET | `/estado-unidades` | ADMIN,SUPERVISOR,OPERADOR,ANALISTA | `?page&size` | `PageResult<EstadoUnidadResponseDto>` |
| GET | `/estado-unidades/{unidadId}` | ADMIN,SUPERVISOR,OPERADOR,ANALISTA | — | `EstadoUnidadResponseDto` |

### Desvíos de ruta
| Método | Path | Roles | Body | Respuesta |
|--------|------|-------|------|-----------|
| POST | `/desvios` | ADMIN,OPERADOR | `{ unidadExternoId, rutaExternoId, latitud, longitud, distanciaDesvioM, severidad }` | 201 `DesvioRutaResponseDto` |
| GET | `/desvios/ruta/{rutaId}` | ADMIN,SUPERVISOR,OPERADOR,ANALISTA | `?page&size` | `PageResult<DesvioRutaResponseDto>` |
| PATCH | `/desvios/{id}/atender` | ADMIN,SUPERVISOR | — | 204 |
| PATCH | `/desvios/{id}/descartar` | ADMIN,SUPERVISOR | — | 204 |

### Dispositivos GPS
| Método | Path | Roles | Body | Respuesta |
|--------|------|-------|------|-----------|
| POST | `/dispositivos` | ADMIN,SUPERVISOR | `{ unidadExternoId, imei?, proveedor? }` | 201 `DispositivoResponseDto` |
| GET | `/dispositivos/{id}` | todos | — | `DispositivoResponseDto` |
| GET | `/dispositivos` | todos | `?page&size` | `PageResult<DispositivoResponseDto>` |

### Eventos de conectividad
| Método | Path | Roles | Body | Respuesta |
|--------|------|-------|------|-----------|
| POST | `/eventos-conectividad` | ADMIN,OPERADOR | `{ unidadExternoId, dispositivoId?, tipoEvento, detalle? }` | 201 `EventoConectividadResponseDto` |
| GET | `/eventos-conectividad/unidad/{unidadId}` | todos | `?page&size` | `PageResult<EventoConectividadResponseDto>` |

---

## CIUDADANOS Y ALERTAS

Base: `/api/v1/ciudadanos`

### Ciudadanos
| Método | Path | Roles | Body | Respuesta |
|--------|------|-------|------|-----------|
| POST | `` | ADMIN | `{ nombres?, apellidos?, email?, telefono?, documento? }` | 201 `CiudadanoResponseDto` |
| GET | `/{id}` | ADMIN,SUPERVISOR | — | `CiudadanoResponseDto` |
| GET | `` | ADMIN,SUPERVISOR | `?page&size` | `PageResult<CiudadanoResponseDto>` |

### Alertas ciudadanas
| Método | Path | Roles / Público | Body / Query | Respuesta |
|--------|------|----------------|-------------|-----------|
| POST | `/alertas` | 🌐 Público (+ header `X-Tenant-Id`) | `RegistrarAlertaRequest` | 201 `AlertaResponseDto` |
| GET | `/alertas` | ADMIN,SUPERVISOR,OPERADOR | `?estado&page&size` | `PageResult<AlertaResponseDto>` |
| GET | `/alertas/{id}` | ADMIN,SUPERVISOR,OPERADOR | — | `AlertaResponseDto` |
| GET | `/alertas/zona/{zonaId}` | ADMIN,SUPERVISOR,OPERADOR | `?page&size` | `PageResult<AlertaResponseDto>` |
| GET | `/alertas/criticas` | ADMIN,SUPERVISOR,OPERADOR | `?page&size` | `PageResult<AlertaResponseDto>` |
| PATCH | `/alertas/{id}/estado` | ADMIN,SUPERVISOR,OPERADOR | `{ nuevoEstado, comentario?, cambiadoPorUsuarioId? }` | `AlertaResponseDto` |
| POST | `/alertas/{id}/validar` | ADMIN,SUPERVISOR | `ValidarAlertaRequest` | 201 `AlertaResponseDto` |
| POST | `/alertas/{id}/fotos` | ADMIN,SUPERVISOR,OPERADOR | `{ urlArchivo, tipoMime, tamanioBytes }` | 201 `AlertaResponseDto` |

**RegistrarAlertaRequest**:
```json
{
  "ciudadanoId": "UUID (opcional)",
  "distritoExternoId": "UUID",
  "zonaExternoId": "UUID (opcional)",
  "titulo": "string (max 150 chars)",
  "descripcion": "string (opcional)",
  "latitud": -12.1179,
  "longitud": -77.0330,
  "volumenEstimado": "BAJO|MEDIO|ALTO",
  "nivelCriticidad": "NORMAL|CRITICA",
  "fuente": "APP|WEB|OEFA|OPERADOR"
}
```

---

## KPI 🔧

Base: `/api/v1/kpis` (plural — ver [KI-01](README.md#ki-01))

| Método | Path | Roles | Query | Respuesta |
|--------|------|-------|-------|-----------|
| GET | `/resumen-diario` | ADMIN,SUPERVISOR,ANALISTA | `?distritoId&fecha` | `ApiResponse<ResumenOperativoDto>` |
| POST | `/resumen-diario/calcular` | ADMIN,SUPERVISOR | `?distritoId&fecha` | `ApiResponse<ResumenOperativoDto>` |
| GET | `/rutas` | ADMIN,SUPERVISOR,ANALISTA | `?fechaDesde&fechaHasta&page&size` | `PageResult<KpiRutaDto>` |
| GET | `/unidades` | ADMIN,SUPERVISOR,ANALISTA | `?unidadId&fechaDesde&fechaHasta&page&size` | `PageResult<KpiUnidadDto>` |
| GET | `/zonas` | ADMIN,SUPERVISOR,ANALISTA | `?zonaId&fechaDesde&fechaHasta&page&size` | `PageResult<KpiZonaDto>` |
| GET | `/alertas` | ADMIN,SUPERVISOR,ANALISTA | `?zonaId&fechaDesde&fechaHasta&page&size` | `PageResult<KpiAlertaDto>` |

### ResumenOperativoDto (dashboard)
```json
{
  "distritoId": "UUID",
  "fecha": "2026-07-01",
  "kmProgramados": 45.2,
  "kmRecorridos": 42.8,
  "toneladasRecolectadas": 12.4,
  "coberturaPorcentaje": 94.8,
  "alertasRegistradas": 7,
  "alertasAtendidas": 5,
  "tiempoRespuestaPromedioMin": 23.0
}
```

---

## AUDITORÍA

Base: `/api/v1/auditoria`

| Método | Path | Roles | Query | Respuesta |
|--------|------|-------|-------|-----------|
| GET | `/eventos` | ADMIN,SUPERVISOR,ANALISTA | `?modulo&entidad&usuarioId&fechaDesde&fechaHasta&page&size` | `PageResult<EventoAuditoriaDto>` |
| GET | `/outbox` | ADMIN | `?estado&eventType&page&size` | `PageResult<OutboxEventDto>` |

---

## INTEGRACIÓN

| Método | Path | Roles | Respuesta |
|--------|------|-------|-----------|
| GET | `/api/v1/integracion/health` | ADMIN,SUPERVISOR | `{ optimization: "OK|DOWN", osrm: "OK|DOWN", oefa: "OK|DOWN" }` |
