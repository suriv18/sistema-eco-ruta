# Modelo de negocio

Visión de los agregados de dominio, sus campos principales y las relaciones cross-módulo.  
El frontend resuelve los nombres de entidades relacionadas consultando el módulo dueño (los agregados solo guardan el ID externo, no el nombre).

---

## Módulo: auth

### Usuario
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | UUID | Identificador |
| `tenantId` | UUID | Municipalidad |
| `nombresCompletos` | string | Nombre para mostrar |
| `email` | string | Email normalizado |
| `username` | string | Login (minúsculas, 3-80 chars) |
| `telefono` | string? | Teléfono opcional |
| `estado` | EstadoUsuario | ACTIVO / BLOQUEADO / INACTIVO |
| `roles` | UUID[] | IDs de roles asignados |
| `ultimoLoginEn` | Instant? | Último acceso |
| `creadoEn` | Instant | |

### Rol
| Campo | Tipo |
|-------|------|
| `id` | UUID |
| `codigo` | string (único) |
| `nombre` | string |
| `activo` | boolean |

### Permiso
`codigo` (ej. `RUTA.APROBAR`), `modulo`, `descripcion`

---

## Módulo: operacion

### Distrito
`id, tenantId, nombre, ubigeo?, estado (ACTIVO/INACTIVO)`

### Zona
`id, tenantId, distritoId, codigo (CodigoZona), nombre, tipoZona, prioridad (1-5), estado`

### Depósito
`id, tenantId, distritoId, nombre, tipo (TipoDeposito), estado`

### Contenedor
`id, tenantId, zonaId, codigo, capacidadM3, estadoContenedor`

### Unidad
`id, tenantId, placa (ej. CAM-489), codigoInterno, tipoUnidad, capacidadM3, capacidadKg, estadoOperativo`

### Chofer
`id, tenantId, nombres, apellidos, dni?, licencia?, telefono?, estado (EstadoChofer)`

### Turno
`id, tenantId, unidadId, choferId, distritoId, fecha, horaInicio, horaFin, tipoTurno, estado (EstadoTurno)`

### HorarioRecoleccion
`id, tenantId, zonaId, diaSemana (1=lun..7=dom), horaInicio, horaFin, observacion?, estado`

---

## Módulo: rutas

### Ruta
| Campo | Tipo |
|-------|------|
| `id` | UUID |
| `turnoId` | UUID → operacion/turnos |
| `distritoId` | UUID → operacion/distritos |
| `depositoOrigenId` | UUID → operacion/depositos |
| `depositoDestinoId` | UUID → operacion/depositos |
| `fecha` | LocalDate |
| `tipoRuta` | TipoRuta |
| `estado` | EstadoRuta |
| `metricas` | `{ distanciaM, duracionS, cargaKg }` |
| `versionActual` | RutaVersion |
| `historialVersiones` | RutaVersion[] |
| `eventos` | RutaEvento[] |

### RutaVersion
`version (int), motivo (MotivoVersion), generadoPor (GeneradoPor), metricas, paradas[]`

### RutaParada
`orden, zonaId → operacion/zonas, contenedorId?, eta?, horaLlegadaReal?, horaSalidaReal?, demandaEstimadaKg, cargaAcumuladaKg, estado (EstadoParada)`

---

## Módulo: telemetría

### EstadoUnidad (read-model en tiempo real)
`unidadId → operacion/unidades, rutaId?, ultimaPosicion {latitud,longitud}?, ultimaVelocidadKmh?, ultimoPingEn?, estadoMovimiento`

### PingGps
`dispositivoId, unidadExternoId, rutaExternoId?, ts, latitud, longitud, velocidadKmh?, rumboGrados?, precisionM?, origen`

### DesvioRuta
`id, unidadExternoId, rutaExternoId, latitud, longitud, distanciaDesvioM, severidad, estado (EstadoDesvio), detectadoEn`

### DispositivoGps
`id, unidadExternoId, imei?, proveedor?, estado (EstadoDispositivo), ultimoPingEn?`

---

## Módulo: ciudadano

### AlertaCiudadana
| Campo | Tipo |
|-------|------|
| `id` | UUID |
| `ciudadanoId` | UUID? → ciudadanos |
| `distritoExternoId` | UUID → operacion/distritos |
| `zonaExternoId` | UUID? → operacion/zonas |
| `titulo` | string |
| `descripcion` | string? |
| `latitud, longitud` | double |
| `volumenEstimado` | VolumenEstimado |
| `nivelCriticidad` | NivelCriticidad |
| `fuente` | FuenteAlerta |
| `estado` | EstadoAlerta |
| `fotos` | AlertaFoto[] |
| `historial` | AlertaHistorial[] |
| `validacion` | ValidacionAlerta? |

### Ciudadano
`id, nombres?, apellidos?, email?, telefono?, documento?, estado`

---

## Módulo: kpi

### ResumenOperativoDiario
`distritoId, fecha, kmProgramados, kmRecorridos, toneladasRecolectadas, coberturaPorcentaje, alertasRegistradas, alertasAtendidas, tiempoRespuestaPromedioMin?`

---

## Relaciones cross-módulo (cómo resuelve el frontend)

El frontend recibe un ID en la respuesta de un módulo y necesita mostrar el nombre. Debe hacer una consulta separada al módulo dueño.

| Referencia en... | Campo | Módulo dueño | Endpoint |
|-----------------|-------|-------------|---------|
| Ruta.turnoId | TurnoId | operacion | `GET /api/v1/operacion/turnos/{id}` |
| Ruta.distritoId | DistritoId | operacion | `GET /api/v1/operacion/distritos/{id}` |
| Ruta.depositoOrigenId | DepositoId | operacion | `GET /api/v1/operacion/depositos/{id}` |
| RutaParada.zonaId | ZonaId | operacion | `GET /api/v1/operacion/zonas/{id}` |
| Turno.unidadId | UnidadId | operacion | `GET /api/v1/operacion/unidades/{id}` |
| Turno.choferId | ChoferId | operacion | `GET /api/v1/operacion/choferes/{id}` |
| EstadoUnidad.unidadId | UnidadId | operacion | `GET /api/v1/operacion/unidades/{id}` |
| Alerta.distritoExternoId | DistritoId | operacion | `GET /api/v1/operacion/distritos/{id}` |
| Alerta.zonaExternoId | ZonaId | operacion | `GET /api/v1/operacion/zonas/{id}` |

**Recomendación**: Usar TanStack Query con `staleTime` alto para estos recursos maestros — cambian poco y se usan en muchas pantallas. Precargar distritos y zonas al cargar la app.
