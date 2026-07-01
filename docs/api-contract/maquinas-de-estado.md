# Máquinas de estado

Referencia de todos los enums de estado del dominio con sus transiciones válidas.  
Usar estos valores exactos en badges, guards de UI y validaciones de formulario.

---

## Ruta (`EstadoRuta`)

```
BORRADOR ──aprobar──→ APROBADA ──iniciar-ejecucion──→ EN_EJECUCION ──finalizar──→ FINALIZADA
    │                    │
    └──cancelar──→ CANCELADA ←──cancelar──┘
```

| Estado | Color sugerido | Acciones disponibles |
|--------|---------------|---------------------|
| `BORRADOR` | gris | Aprobar, Cancelar |
| `APROBADA` | azul | Iniciar ejecución, Cancelar |
| `EN_EJECUCION` | verde | Finalizar |
| `FINALIZADA` | verde oscuro | Solo lectura |
| `CANCELADA` | rojo | Solo lectura |

**Endpoints de transición**:
- `PATCH /api/v1/rutas/{id}/aprobar`
- `PATCH /api/v1/rutas/{id}/iniciar-ejecucion`
- `PATCH /api/v1/rutas/{id}/finalizar`
- `PATCH /api/v1/rutas/{id}/cancelar`

---

## Parada de ruta (`EstadoParada`)

```
PENDIENTE ──iniciar──→ EN_ATENCION ──marcar atendida──→ ATENDIDA
    │                       │
    └──omitir──→ OMITIDA ←──┘
```

| Estado | Color sugerido |
|--------|---------------|
| `PENDIENTE` | gris |
| `EN_ATENCION` | amarillo |
| `ATENDIDA` | verde |
| `OMITIDA` | naranja |

**Endpoint**: `PATCH /api/v1/rutas/{rutaId}/paradas/{paradaId}` con body `{ nuevoEstado, horaLlegada?, horaSalida? }`

---

## Alerta ciudadana (`EstadoAlerta`)

```
REGISTRADA ──validar──→ VALIDADA ──asignar──→ EN_ATENCION ──resolver──→ ATENDIDA
     │                    │                       │
     └──descartar──→ DESCARTADA ←──descartar──────┘
     └──validar──→ DUPLICADA
```

| Estado | Color sugerido | Descripción |
|--------|---------------|-------------|
| `REGISTRADA` | gris | Recién ingresada, pendiente de validación |
| `VALIDADA` | azul | Verificada como legítima |
| `EN_ATENCION` | amarillo | Operativo en camino |
| `ATENDIDA` | verde | Resuelta |
| `DESCARTADA` | rojo | Falsa o duplicada descartada |
| `DUPLICADA` | naranja | Consolidada con alerta original |

**Transiciones válidas (del dominio)**:
- `REGISTRADA → VALIDADA, DESCARTADA, DUPLICADA`
- `VALIDADA → EN_ATENCION, DESCARTADA`
- `EN_ATENCION → ATENDIDA, DESCARTADA`
- `ATENDIDA, DESCARTADA, DUPLICADA` → estados terminales (no se modifican)

**Endpoint**: `PATCH /api/v1/ciudadanos/alertas/{id}/estado` con body `{ nuevoEstado, comentario?, cambiadoPorUsuarioId? }`

---

## Turno (`EstadoTurno`)

```
PROGRAMADO ──iniciar──→ EN_CURSO ──finalizar──→ FINALIZADO
     └──cancelar──→ CANCELADO ←──cancelar──┘
```

| Estado | Color sugerido |
|--------|---------------|
| `PROGRAMADO` | gris |
| `EN_CURSO` | verde |
| `FINALIZADO` | verde oscuro |
| `CANCELADO` | rojo |

**Endpoints**: `PATCH /api/v1/operacion/turnos/{id}/iniciar`, `/finalizar`, `/cancelar`

---

## Unidad — estado de movimiento (telemetría, `EstadoMovimiento`)

Estado en vivo, actualizado por WebSocket o polling de `/api/v1/telemetria/estado-unidades`.

| Estado | Color de marcador | Icono sugerido |
|--------|-----------------|---------------|
| `EN_RUTA` | verde | camión en movimiento |
| `DETENIDA` | amarillo | camión estático |
| `SIN_SENAL` | gris | camión con X |
| `DESCARGANDO` | azul | camión con flecha abajo |
| `FUERA_DE_RUTA` | rojo | camión con alerta |

---

## Unidad — estado operativo (`EstadoOperativoUnidad`)

Estado administrativo de la flota, gestionado en el módulo operación.

| Estado | Disponible para turnos |
|--------|----------------------|
| `OPERATIVA` | ✓ |
| `MANTENIMIENTO` | — |
| `INACTIVA` | — |
| `AVERIADA` | — |

---

## Chofer (`EstadoChofer`)

| Estado | Disponible para turnos |
|--------|----------------------|
| `ACTIVO` | ✓ |
| `SUSPENDIDO` | — |
| `INACTIVO` | — |

---

## Contenedor (`EstadoContenedor`)

| Estado | Descripción |
|--------|-------------|
| `VACIO` | Sin residuos |
| `PARCIAL` | Parcialmente lleno |
| `LLENO` | Lleno, recolección urgente |
| `DESBORDADO` | Desbordado, alerta crítica |

---

## Otros enums relevantes para formularios

### TipoUnidad
`COMPACTADOR, BARANDA, VOLQUETE, MOTOFURGON, OTRO`

### TipoTurno
`MANANA, TARDE, NOCHE`

### TipoZona
`RESIDENCIAL, COMERCIAL, MIXTA, MERCADO, INDUSTRIAL, OTRA`

### TipoDeposito
`BASE, TRANSFERENCIA, RELLENO, OTRO`

### NivelCriticidad (alertas)
`NORMAL, CRITICA`

### VolumenEstimado (alertas)
`BAJO, MEDIO, ALTO`

### FuenteAlerta
`APP, WEB, OEFA, OPERADOR`

### TipoRuta
`HISTORICA, OPTIMIZADA, REOPTIMIZADA`

### MotivoVersion (re-optimización)
`INICIAL, ALERTA_CRITICA, DESVIO, MANUAL, RECALCULO`

### SeveridadDesvio
`BAJA, MEDIA, ALTA`

### OrigenPing (telemetría)
`GPS_REAL, SIMULADOR, APP`  
→ La app móvil debe enviar `APP`.
