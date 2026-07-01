# Ciudad Sana — Documentación del Proyecto

Sistema Inteligente de Gestión y Optimización de Rutas de Recolección de Residuos Sólidos Municipales.

## Estructura de la documentación

```
docs/
├── README.md                         ← Este archivo (índice raíz)
│
├── promp-arquitectura-estructura/
│   ├── promp-backend.txt.txt         ← Spec original del backend (referencia)
│   └── promp-fronend.txt.txt         ← Spec original del frontend (superada por spec-web.md)
│
├── db/
│   └── ddl_ciudad_limpia_lima.sql    ← DDL completo PostgreSQL + PostGIS
│
├── api-contract/                     ← ★ CONTRATO DE API (fuente de verdad para apps cliente)
│   ├── README.md                     ← Índice del contrato
│   ├── endpoints.md                  ← Todos los endpoints por módulo (rutas REALES)
│   ├── auth-y-seguridad.md           ← JWT, roles, flujo de autenticación
│   ├── envelopes-y-errores.md        ← ApiResponse<T>, PageResult<T>, códigos de error
│   ├── websocket.md                  ← STOMP /ws, topics y payloads
│   ├── maquinas-de-estado.md         ← Enums de estado y transiciones válidas
│   └── modelo-de-negocio.md          ← Agregados, campos y relaciones cross-módulo
│
├── frontend-web/
│   └── spec-web.md                   ← ★ Spec del panel web React + Vite (actualizada)
│
└── mobile/
    └── spec-mobile.md                ← ★ Spec de la app móvil React Native / Expo (nueva)
```

## Backends (ya implementados y probados)

| Servicio | Stack | Puerto | Repo |
|----------|-------|--------|------|
| `service-ciudad-sana` | Java 25 / Spring Boot 4.1 / PostgreSQL | 8080 | `suriv18/sistema-eco-ruta` |
| `service-optimization` | Python 3.12 / FastAPI / OR-Tools | 8000 | `suriv18/service-optimization` |

> **Nota**: El web/móvil consumen exclusivamente `service-ciudad-sana`. El servicio de optimización es server-to-server.

## Apps cliente (pendientes de implementar)

| App | Repo futuro | Stack | Usuarios |
|-----|------------|-------|---------|
| Panel Web | `ciudad-sana-frontend-web/` | React + Vite + TypeScript | ADMIN, SUPERVISOR, OPERADOR, ANALISTA |
| App Móvil | `ciudad-sana-mobile/` | React Native + Expo | Choferes (OPERADOR), Ciudadanos |

## Sprints de implementación

- [Sprint panel web](.superpowers/sdd/frontend/progress.md)
- [Sprint app móvil](.superpowers/sdd/mobile/progress.md)

## Documentación técnica de referencia

- [Contrato de API](api-contract/README.md) — Empieza aquí antes de implementar cualquier pantalla.
- [Máquinas de estado](api-contract/maquinas-de-estado.md) — Estados, badges y flujos de UI.
- [WebSocket](api-contract/websocket.md) — Eventos en tiempo real (mapa, alertas, rutas).
- [Known issues de configuración](api-contract/README.md#known-issues) — 3 inconsistencias del backend a corregir.
