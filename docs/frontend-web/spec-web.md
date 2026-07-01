# Spec — Panel Web Ciudad Sana

> Actualización de la spec original (`docs/promp-arquitectura-estructura/promp-fronend.txt.txt`).  
> Correcciones marcadas con ✏️. Para el contrato de API completo ver [`docs/api-contract/`](../api-contract/README.md).

## Repositorio

```
ciudad-sana-frontend-web/    (repo separado, hermano de service-ciudad-sana)
```

## Stack

| Tecnología | Versión recomendada | Propósito |
|-----------|--------------------|-----------| 
| React | 19 | UI |
| TypeScript | 5.x estricto | Tipado |
| Vite | 6.x | Bundler / dev server |
| React Router v7 | — | Navegación SPA |
| TanStack Query v5 | — | Server state, cache, refetch |
| Zustand | — | Estado local UI (sin Redux) |
| React Hook Form + Zod | — | Formularios y validación |
| Tailwind CSS v4 | — | Estilos |
| React Leaflet | — | Mapas (OpenStreetMap tiles) |
| @stomp/stompjs + sockjs-client | — | WebSocket STOMP |
| Axios | — | HTTP client (o fetch wrapper) |
| Vitest + React Testing Library | — | Tests unitarios |
| MSW v2 | — | Mocks de API en tests |
| Playwright | — | Tests E2E |
| ESLint + Prettier | — | Calidad de código |

## Variables de entorno

```env
# .env.example
VITE_API_BASE_URL=http://localhost:8080
VITE_WS_URL=ws://localhost:8080/ws
VITE_MAP_TILE_URL=https://tile.openstreetmap.org/{z}/{x}/{y}.png
VITE_APP_NAME=Ciudad Sana
```

## Estructura de carpetas

```
ciudad-sana-frontend-web/
├── src/
│   ├── main.tsx
│   ├── App.tsx
│   ├── app/
│   │   ├── providers/        (AppProviders, QueryProvider, AuthProvider)
│   │   ├── router/           (routes.tsx, ProtectedRoute, RoleRoute, routePaths.ts)
│   │   ├── layouts/          (AuthLayout, DashboardLayout, PublicLayout)
│   │   └── config/           (env.ts, permissions.ts, navigation.ts)
│   │
│   ├── shared/
│   │   ├── api/              (httpClient.ts, apiError.ts, apiResponse.ts, queryKeys.ts)
│   │   ├── websocket/        (websocketClient.ts, websocketEvents.ts, useWebSocketSubscription.ts)
│   │   ├── components/ui/    (Button, Input, Select, Modal, Table, Pagination, Badge, Spinner...)
│   │   ├── hooks/            (useDebounce, usePagination, useDisclosure, usePermissions)
│   │   ├── types/            (common.ts, pagination.ts, geo.ts, auth.ts)
│   │   └── utils/            (dateUtils, formatters, geoUtils, storage)
│   │
│   ├── modules/
│   │   ├── auth/
│   │   ├── dashboard/
│   │   ├── mapa/
│   │   ├── operacion/
│   │   ├── telemetria/
│   │   ├── ciudadano/
│   │   ├── alertas/
│   │   ├── rutas/
│   │   ├── kpi/
│   │   └── auditoria/
│   │
│   └── test/
│       ├── mocks/            (MSW handlers por módulo)
│       └── utils/            (renderWithProviders)
│
└── tests/e2e/
```

Cada módulo sigue esta estructura interna:
```
modules/{modulo}/
├── api/          ({modulo}Api.ts + {modulo}Keys.ts)
├── components/
├── pages/
├── hooks/
├── schemas/      (Zod)
├── types/
├── store/        (Zustand si aplica)
└── utils/
```

## Rutas de la aplicación

### Públicas (sin autenticación)
| Path | Componente | Descripción |
|------|-----------|-------------|
| `/login` | `LoginPage` | Login de usuarios internos |
| `/publico/alertas/nueva` | `RegistrarAlertaPublicaPage` | Registro público de alerta (ciudadano) |
| `/publico/horarios` | `ConsultarHorarioPage` | Horarios de recolección por zona |

### Privadas (requieren JWT)
| Path | Componente | Roles |
|------|-----------|-------|
| `/` o `/dashboard` | `DashboardPage` | todos |
| `/mapa` | `MapaPage` | ADMIN, SUPERVISOR, OPERADOR, ANALISTA |
| `/operacion/distritos` | `DistritosPage` | ADMIN, SUPERVISOR |
| `/operacion/zonas` | `ZonasPage` | ADMIN, SUPERVISOR |
| `/operacion/depositos` | `DepositosPage` | ADMIN, SUPERVISOR |
| `/operacion/contenedores` | `ContenedoresPage` | ADMIN, SUPERVISOR |
| `/operacion/unidades` | `UnidadesPage` | ADMIN, SUPERVISOR |
| `/operacion/choferes` | `ChoferesPage` | ADMIN, SUPERVISOR |
| `/operacion/turnos` | `TurnosPage` | ADMIN, SUPERVISOR |
| `/operacion/horarios` | `HorariosRecoleccionPage` | ADMIN, SUPERVISOR |
| `/alertas` | `AlertasPage` | ADMIN, SUPERVISOR, OPERADOR |
| `/alertas/criticas` | `AlertasCriticasPage` | ADMIN, SUPERVISOR |
| `/alertas/:alertaId` | `AlertaDetallePage` | ADMIN, SUPERVISOR, OPERADOR |
| `/rutas` | `RutasPage` | ADMIN, SUPERVISOR, OPERADOR, ANALISTA |
| `/rutas/optimizar` | `OptimizarRutaPage` | ADMIN, SUPERVISOR |
| `/rutas/:rutaId` | `RutaDetallePage` | ADMIN, SUPERVISOR, OPERADOR, ANALISTA |
| `/rutas/:rutaId/versiones` | `RutaVersionesPage` | ADMIN, SUPERVISOR |
| `/kpis/resumen` | `KpiResumenPage` | ADMIN, SUPERVISOR, ANALISTA |
| `/kpis/rutas` | `KpiRutasPage` | ADMIN, SUPERVISOR, ANALISTA |
| `/kpis/unidades` | `KpiUnidadesPage` | ADMIN, SUPERVISOR, ANALISTA |
| `/kpis/zonas` | `KpiZonasPage` | ADMIN, SUPERVISOR, ANALISTA |
| `/kpis/alertas` | `KpiAlertasPage` | ADMIN, SUPERVISOR, ANALISTA |
| `/auditoria/eventos` | `AuditoriaEventosPage` | ADMIN, SUPERVISOR |
| `/auditoria/outbox` | `AuditoriaOutboxPage` | ADMIN |
| `/unauthorized` | `UnauthorizedPage` | — |

## Correcciones de endpoints frente a la spec anterior

✏️ Los siguientes paths son **distintos** de la spec original (`promp-fronend.txt.txt`):

| Spec antigua (INCORRECTA) | Ruta real del backend |
|--------------------------|----------------------|
| `/api/v1/alertas` | `/api/v1/ciudadanos/alertas` |
| `/api/v1/alertas/criticas` | `/api/v1/ciudadanos/alertas/criticas` |
| `/api/v1/alertas/{id}` | `/api/v1/ciudadanos/alertas/{id}` |
| `/api/v1/kpi/...` | `/api/v1/kpis/...` (con `s`) |
| `/api/v1/rutas/{id}/iniciar` | `/api/v1/rutas/{id}/iniciar-ejecucion` |
| `POST /api/v1/rutas/{id}/aprobar` | `PATCH /api/v1/rutas/{id}/aprobar` |
| `POST /api/v1/rutas/{id}/iniciar` | `PATCH /api/v1/rutas/{id}/iniciar-ejecucion` |

## Módulo mapa — WebSocket

Eventos a suscribir al montar `MapaPage`:

| Topic | Acción en mapa |
|-------|---------------|
| `/topic/unidad.posicion.actualizada` | Mover marcador de unidad |
| `/topic/unidad.sin.senal` | Cambiar icono a `FUERA_DE_RUTA` |
| `/topic/alerta.critica.recibida` | Añadir marcador de alerta roja |
| `/topic/ruta.actualizada` | Invalidar query de ruta, actualizar línea |
| `/topic/ruta.estado.cambiado` | Cambiar color de línea de ruta |

## Estados visuales de UI

Ver [maquinas-de-estado.md](../api-contract/maquinas-de-estado.md) para colores y transiciones.

## Validaciones clave (Zod)

```typescript
// Login
z.object({ username: z.string().min(3), password: z.string().min(1), tenantId: z.string().uuid() })

// Nueva zona
z.object({ distritoId: z.string().uuid(), codigo: z.string().min(2).max(50), nombre: z.string().min(1), tipoZona: z.enum([...]), prioridad: z.number().int().min(1) })

// Nueva unidad
z.object({ placa: z.string().regex(/^[A-Z]{3}-\d{3}$|^[A-Z]\d{4}[A-Z]$/), capacidadKg: z.number().positive(), capacidadM3: z.number().positive() })

// Nuevo turno
z.object({ unidadId: z.string().uuid(), choferId: z.string().uuid(), horaInicio: z.string(), horaFin: z.string() }).refine(data => data.horaInicio < data.horaFin, "Hora fin debe ser posterior a hora inicio")

// Alerta pública
z.object({ titulo: z.string().min(1).max(150), latitud: z.number().min(-90).max(90), longitud: z.number().min(-180).max(180), volumenEstimado: z.enum(["BAJO","MEDIO","ALTO"]), nivelCriticidad: z.enum(["NORMAL","CRITICA"]), fuente: z.literal("APP").or(z.literal("WEB")) })
```

## Manejo de errores

Ver catálogo completo en [envelopes-y-errores.md](../api-contract/envelopes-y-errores.md).

Resumen para UI:
- `401` → redirigir a `/login` (o intentar refresh primero)
- `403` → mostrar `UnauthorizedPage`
- `VALIDACION_ERROR` → mapear `errors[]` a campos del formulario con React Hook Form `setError`
- `OPTIMIZADOR_NO_DISPONIBLE` → toast de error específico, no bloquear la página
- `RUTA_NO_FACTIBLE` → modal con mensaje y botón "Ver detalles"
- Errores de red → snackbar con "Sin conexión — Reintentar"

## Orden de implementación

1. Setup: Vite + React + TypeScript + ESLint + Prettier + Tailwind
2. Estructura `app/shared/modules` de carpetas
3. `shared/api/httpClient.ts` — interceptores JWT, refresh automático, manejo de ApiResponse\<T\>
4. Tipos comunes: `ApiResponse`, `PageResult`, enums de estado
5. Módulo `auth` — login, logout, refresh, `ProtectedRoute`, `RoleRoute`, `authStore`
6. `DashboardLayout` con sidebar y topbar
7. `DashboardPage` — KPI summary cards (datos del `/api/v1/kpis/resumen-diario`)
8. Módulo `operacion` — CRUD de choferes, unidades, distritos, zonas, depósitos, contenedores, turnos, horarios
9. Módulo `alertas` — listado, detalle, cambio de estado, fotos, alertas críticas
10. `shared/websocket/websocketClient.ts` + `useWebSocketSubscription`
11. Módulo `mapa` — `MapaOperativo`, marcadores de unidades, capas de rutas y alertas, actualización en tiempo real
12. Módulo `rutas` — optimizar, listar, detalle, aprobar, iniciar, finalizar, cancelar, versiones, paradas
13. Módulo `kpi` — resumen diario, rutas, unidades, zonas, alertas
14. Módulo `auditoria` — eventos y outbox
15. Portal público ciudadano — `RegistrarAlertaPublicaPage`, `ConsultarHorarioPage`
16. MSW handlers para todos los módulos
17. Pruebas unitarias: `LoginForm`, `ProtectedRoute`, `AlertaForm`, `OptimizarRutaForm`, `RutaEstadoBadge`
18. Pruebas E2E Playwright: login, dashboard, mapa, optimizar ruta, registrar alerta

## Definition of Done (por pantalla)

- [ ] Consume endpoint real (o mock MSW en tests)
- [ ] Tipos TypeScript definidos
- [ ] Validación Zod en formularios
- [ ] Estados: loading, error, empty, success
- [ ] Respeta permisos de rol (`RoleRoute` o `PermissionGuard`)
- [ ] Maneja errores del backend con mensajes claros
- [ ] Componentes reutilizables extraídos
- [ ] Tests principales escritos
- [ ] Responsive (mobile-first)
- [ ] Accesible (aria labels, contraste)
- [ ] Sin lógica de backend ni datos quemados
