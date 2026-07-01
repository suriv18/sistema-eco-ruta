# Spec — App Móvil Ciudad Sana

> Especificación nueva. No existía documentación previa de la app móvil.  
> Para el contrato de API ver [`docs/api-contract/`](../api-contract/README.md).

## Repositorio

```
ciudad-sana-mobile/    (repo separado, hermano de service-ciudad-sana)
```

## Stack

| Tecnología | Propósito |
|-----------|-----------|
| React Native 0.76+ | UI nativa iOS y Android |
| Expo SDK 52+ | Toolchain, OTA updates, device APIs |
| Expo Router (file-based) | Navegación |
| TypeScript estricto | Tipado |
| TanStack Query v5 | Server state, cache, refetch |
| React Hook Form + Zod | Formularios y validación |
| `expo-location` | GPS en tiempo real (pings) |
| `expo-camera` / `expo-image-picker` | Fotos de alertas |
| `expo-secure-store` | Almacenamiento seguro de JWT |
| `expo-background-fetch` / `expo-task-manager` | Pings GPS en background |
| `@stomp/stompjs` + `sockjs-client` | WebSocket STOMP (rutas en tiempo real) |
| `expo-network` / `@react-native-community/netinfo` | Detección de conectividad |
| NativeWind (Tailwind para RN) | Estilos |
| Zustand | Estado local UI |

## Perfiles de usuario (dos flujos en una app)

### Perfil 1 — Chofer (rol OPERADOR)
Personal de campo que conduce el camión compactador. Usa la app para:
- Iniciar/finalizar su turno del día
- Ver la ruta asignada y sus paradas ordenadas
- Marcar paradas como atendidas u omitidas
- Registrar eventos (avería, observación)
- Emitir pings GPS automáticamente en background

### Perfil 2 — Ciudadano (sin rol / rol CIUDADANO)
Vecino que reporta acumulación de residuos. Usa la app para:
- Registrar alerta con ubicación GPS automática + foto
- Seguir el estado de sus alertas enviadas

La app detecta el perfil en el login:
- Si el usuario tiene rol `OPERADOR` → flujo chofer
- Si es registro ciudadano o anónimo → flujo ciudadano (endpoint público)

## Variables de entorno

```env
# .env (Expo)
EXPO_PUBLIC_API_BASE_URL=http://192.168.1.x:8080
EXPO_PUBLIC_WS_URL=ws://192.168.1.x:8080/ws
EXPO_PUBLIC_TENANT_ID=11111111-1111-1111-1111-111111111111
```

> En campo la URL debe apuntar a la IP del servidor, no a localhost.

## Estructura de carpetas (Expo Router)

```
ciudad-sana-mobile/
├── app/
│   ├── (auth)/
│   │   └── login.tsx
│   ├── (chofer)/
│   │   ├── _layout.tsx
│   │   ├── index.tsx              ← Dashboard chofer (turno del día)
│   │   ├── turno/
│   │   │   └── [turnoId].tsx      ← Detalle de turno
│   │   └── ruta/
│   │       ├── [rutaId].tsx       ← Paradas de la ruta
│   │       └── parada/
│   │           └── [paradaId].tsx ← Detalle de parada
│   ├── (ciudadano)/
│   │   ├── _layout.tsx
│   │   ├── index.tsx              ← Inicio ciudadano
│   │   ├── nueva-alerta.tsx       ← Formulario de alerta
│   │   └── mis-alertas.tsx        ← Estado de alertas enviadas
│   └── _layout.tsx
│
├── src/
│   ├── shared/
│   │   ├── api/
│   │   │   ├── httpClient.ts
│   │   │   ├── apiResponse.ts
│   │   │   └── queryKeys.ts
│   │   ├── auth/
│   │   │   ├── authStore.ts       (Zustand + expo-secure-store)
│   │   │   └── useAuth.ts
│   │   ├── websocket/
│   │   │   └── websocketClient.ts
│   │   ├── location/
│   │   │   ├── locationService.ts  (expo-location)
│   │   │   └── pingQueue.ts        (cola offline de pings)
│   │   └── components/
│   │       ├── Button.tsx
│   │       ├── Card.tsx
│   │       ├── Badge.tsx
│   │       └── LoadingOverlay.tsx
│   │
│   ├── modules/
│   │   ├── chofer/
│   │   │   ├── api/               (turnosApi.ts, rutasApi.ts, telemetriaApi.ts)
│   │   │   ├── components/        (TurnoCard, RutaParadasList, ParadaCard, EstadoBadge)
│   │   │   ├── hooks/             (useTurnoActivo, useRutaAsignada, usePingsGps)
│   │   │   └── types/
│   │   └── ciudadano/
│   │       ├── api/               (alertasApi.ts)
│   │       ├── components/        (AlertaForm, FotoUploader, AlertaEstadoBadge)
│   │       ├── hooks/             (useRegistrarAlerta, useMisAlertas)
│   │       └── types/
│   │
│   └── test/
│       └── mocks/
│
└── assets/
```

---

## Flujo Chofer

### Pantalla 1 — Login
- Campos: `username`, `password`, `tenantId` (precargado desde env o QR)
- Endpoint: `POST /api/v1/auth/login`
- Almacenar `accessToken` y `refreshToken` en `expo-secure-store`
- Si rol ≠ OPERADOR → mostrar "Tu perfil no es de chofer" + logout

### Pantalla 2 — Dashboard Chofer (Turno del día)
- Mostrar turno asignado para hoy: unidad, distrito, hora inicio/fin
- Endpoint: `GET /api/v1/operacion/turnos?page=0&size=10` (filtrar por fecha=hoy y choferId del token)
- Estado del turno (`PROGRAMADO`, `EN_CURSO`, `FINALIZADO`)
- Botones: **Iniciar turno** (`PATCH /api/v1/operacion/turnos/{id}/iniciar`) y **Finalizar turno** (`PATCH /api/v1/operacion/turnos/{id}/finalizar`)
- Si estado = `EN_CURSO` → mostrar botón "Ver mi ruta"

### Pantalla 3 — Mi Ruta (paradas)
- Endpoint: `GET /api/v1/rutas?distritoId=...&fecha=hoy` (filtrar por turnoId)
- Luego: `GET /api/v1/rutas/{rutaId}/detalle` para paradas ordenadas
- Mostrar lista de paradas con: nombre de zona (resolver zonaId → `/operacion/zonas/{id}`), dirección, hora estimada (eta), demanda en kg, estado
- **Marcar parada**: `PATCH /api/v1/rutas/{rutaId}/paradas/{paradaId}` con `{ nuevoEstado: "EN_ATENCION"|"ATENDIDA"|"OMITIDA", horaLlegada?, horaSalida? }`
- Botón "Registrar evento": tipo (AVERIA, OBSERVACION, ...) + descripción → `POST /api/v1/rutas/{rutaId}/eventos`
- WebSocket: suscribir a `/topic/ruta.actualizada` y `/topic/ruta.estado.cambiado` para recibir reoptimizaciones en tiempo real

### Servicio background — Pings GPS
Tarea background que se activa cuando el turno está `EN_CURSO`:

```typescript
// Cada 30 segundos emitir ping
const ping = {
  dispositivoId: storedDeviceId,
  unidadExternoId: turno.unidadId,
  rutaExternoId: rutaId,
  ts: new Date().toISOString(),
  latitud: location.coords.latitude,
  longitud: location.coords.longitude,
  velocidadKmh: location.coords.speed * 3.6,
  rumboGrados: location.coords.heading,
  precisionM: location.coords.accuracy,
  origen: "APP"
}
// POST /api/v1/telemetria/pings
```

**Offline**: Si no hay red, encolar en SQLite local y reintentar al recuperar conexión. Máximo 500 pings en cola. Si la cola supera el límite, descartar los más viejos.

**Permisos requeridos**: `expo-location` con modo `BACKGROUND` (requiere justificación en las tiendas de apps — "para mostrar tu posición al supervisor mientras recolectas residuos").

---

## Flujo Ciudadano

### Pantalla 1 — Inicio ciudadano
- Dos opciones: **Reportar basura** y **Mis reportes**
- No requiere login (flujo anónimo)
- Opcionalmente puede registrarse (`POST /api/v1/ciudadanos`) para hacer seguimiento

### Pantalla 2 — Nueva alerta
1. **Ubicación**: obtener GPS automático (`expo-location`) o mostrar mapa para seleccionar manualmente
2. **Descripción**: campo de texto breve (`titulo`, `descripcion`)
3. **Foto**: capturar con cámara o elegir de galería (`expo-image-picker`)
4. **Nivel**: selector `NORMAL` / `CRITICA`
5. **Volumen**: `BAJO` / `MEDIO` / `ALTO`
6. Envío: `POST /api/v1/ciudadanos/alertas` con header `X-Tenant-Id: {EXPO_PUBLIC_TENANT_ID}`

**Sin foto en el cuerpo de la alerta**: El backend recibe la alerta primero y luego las fotos se suben por separado con `POST /api/v1/ciudadanos/alertas/{id}/fotos` enviando la URL del archivo ya subido. La app debe subir la imagen a un servicio de almacenamiento (S3 u otro) y enviar la URL al backend.

> 📝 El endpoint de subida de archivo real (`FileStoragePort`) aún no está expuesto como API REST en el backend. Usar URL de imagen de internet o placeholder en MVP. Documentado como deuda técnica.

### Pantalla 3 — Mis alertas
- Si el ciudadano se registró: `GET /api/v1/ciudadanos/alertas` (filtrando por ciudadanoId)
- Si es anónimo: guardar `alertaId` en AsyncStorage para consultar `GET /api/v1/ciudadanos/alertas/{id}`
- Mostrar estado con badge: `REGISTRADA`, `VALIDADA`, `EN_ATENCION`, `ATENDIDA`, `DESCARTADA`

---

## Consideraciones offline-first

| Situación | Comportamiento |
|-----------|---------------|
| Sin red al abrir | Mostrar datos cacheados (TanStack Query `staleTime: Infinity`) |
| Sin red al marcar parada | Encolar en SQLite local, reintentar al reconectar, mostrar "Pendiente de sincronización" |
| Sin red al emitir ping GPS | Encolar en `pingQueue`, reintentar en background |
| Sin red al enviar alerta | Bloquear envío con mensaje claro "Necesitas conexión para enviar el reporte" |
| Reconexión | Vaciar cola de pings y paradas, hacer refetch de la ruta activa |

---

## Permisos de dispositivo

| Permiso | Cuándo pedirlo | Justificación para tienda |
|---------|---------------|--------------------------|
| `LOCATION_FOREGROUND` | Al iniciar turno (chofer) | "Para mostrar tu ubicación en el mapa operativo" |
| `LOCATION_BACKGROUND` | Al iniciar turno (chofer) | "Para registrar tu recorrido mientras recolectas residuos, incluso con la pantalla apagada" |
| `CAMERA` | Al crear alerta (ciudadano) | "Para fotografiar la acumulación de basura que reportas" |
| `MEDIA_LIBRARY` | Al crear alerta (ciudadano) | "Para adjuntar fotos existentes a tu reporte" |

---

## Orden de implementación

1. Setup Expo + TypeScript + NativeWind + ESLint
2. `shared/auth/authStore.ts` con `expo-secure-store`
3. `shared/api/httpClient.ts` — interceptores JWT, refresh automático
4. Pantalla de Login (ambos perfiles)
5. Flujo Chofer: dashboard de turno + iniciar/finalizar
6. Flujo Chofer: lista de paradas de ruta + marcar estado
7. Flujo Chofer: pings GPS en foreground (`expo-location`)
8. Flujo Chofer: pings GPS en background (`expo-task-manager`)
9. Flujo Chofer: cola offline de pings + sincronización
10. Flujo Chofer: WebSocket para reoptimización en tiempo real
11. Flujo Ciudadano: nueva alerta con GPS + selector de mapa
12. Flujo Ciudadano: captura de foto con `expo-camera`
13. Flujo Ciudadano: seguimiento de alertas enviadas
14. Modo offline-first para paradas (SQLite local)
15. Tests unitarios: `AlertaForm`, `ParadaCard`, `usePingsGps`
16. Tests E2E (Detox o Maestro): login chofer, marcar parada, registrar alerta

## Definition of Done (por pantalla)

- [ ] Funciona en Android y iOS
- [ ] Tipos TypeScript definidos
- [ ] Validación Zod en formularios
- [ ] Estados: loading, error, empty, success
- [ ] Respeta perfil de usuario (chofer vs ciudadano)
- [ ] Maneja errores del backend con mensajes claros en español
- [ ] Funciona sin conexión (o muestra mensaje claro de "sin red")
- [ ] Permisos solicitados con justificación clara al usuario
- [ ] Accesible (tamaños de toque ≥ 44×44 pt, contraste adecuado)
- [ ] Probado en dispositivo físico (no solo emulador)

## Deudas técnicas conocidas

1. **Subida de fotos**: El backend no expone un endpoint de almacenamiento de archivos. El MVP puede usar URLs de imágenes externas o placeholder. El `FileStoragePort` existe como interfaz pero el adaptador de almacenamiento (S3, GCS, etc.) está pendiente de implementación.
2. **Registro de ciudadano desde la app**: El endpoint `POST /api/v1/ciudadanos` requiere rol ADMIN. El ciudadano anónimo puede reportar alertas pero no puede auto-registrarse para hacer seguimiento con nombre. Pendiente de revisar permisos del backend.
3. **Filtrado de turnos por chofer**: El endpoint `GET /api/v1/operacion/turnos` no documenta filtro por `choferId`. Verificar con el backend si aplica o si el filtrado se hace del lado del cliente sobre los resultados paginados.
