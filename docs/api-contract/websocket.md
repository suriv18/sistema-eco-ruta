# WebSocket — Eventos en tiempo real

## Conexión STOMP

El backend expone WebSocket usando el protocolo **STOMP sobre SockJS**.

```
Endpoint:  ws://localhost:8080/ws
Protocolo: STOMP + SockJS (fallback HTTP long-polling automático)
```

### Cabecera de autenticación

Enviar el JWT en la cabecera STOMP al conectar:

```typescript
const headers = {
  Authorization: `Bearer ${accessToken}`,
};
client.activate({ connectHeaders: headers });
```

### Configuración de destinos

| Tipo | Prefijo | Ejemplo |
|------|---------|---------|
| Topics broadcast (servidor → todos los clientes) | `/topic/` | `/topic/ruta.estado.cambiado` |
| Mensajes al servidor (cliente → servidor) | `/app/` | `/app/ping` (no implementado aún) |

Todos los mensajes actuales son **broadcast** — no hay suscripciones por usuario individual.

---

## Topics disponibles

### 1. `unidad.posicion.actualizada`
Se emite cada vez que llega un ping GPS desde una unidad en campo.

**Suscripción**: `/topic/unidad.posicion.actualizada`

**Payload**:
```json
{
  "unidadExternoId": "UUID",
  "latitud": -12.1179,
  "longitud": -77.0330,
  "tenantId": "UUID"
}
```

**Uso en UI**: Actualizar el marcador de la unidad en el mapa. Si la unidad tenía estado `SIN_SENAL`, reactivar su icono.

---

### 2. `unidad.sin.senal`
Se emite cuando se detecta un desvío de ruta. En la versión actual usa el payload del evento `DesvioRutaDetectadoEvent`.

**Suscripción**: `/topic/unidad.sin.senal`

**Payload**:
```json
{
  "agregadoId": "UUID",
  "unidadExternoId": "UUID",
  "rutaExternoId": "UUID",
  "distanciaDesvioM": 450.0,
  "tenantId": "UUID"
}
```

**Uso en UI**: Cambiar el icono de la unidad a estado `FUERA_DE_RUTA` o `SIN_SENAL`, mostrar alerta visual al supervisor, registrar en panel de desvíos activos.

---

### 3. `alerta.critica.recibida`
Se emite cuando se registra una nueva alerta ciudadana.

**Suscripción**: `/topic/alerta.critica.recibida`

**Payload**:
```json
{
  "alertaId": "UUID",
  "distritoExternoId": "UUID",
  "titulo": "Acumulación de basura en Av. Larco",
  "tenantId": "UUID"
}
```

**Uso en UI**: Mostrar toast/badge de notificación, colocar marcador de alerta en el mapa, incrementar contador de alertas en el dashboard. Si `nivelCriticidad == "CRITICA"`, destacar con color rojo.

---

### 4. `ruta.actualizada`
Se emite cuando se agrega una nueva versión a una ruta (optimización inicial o reoptimización).

**Suscripción**: `/topic/ruta.actualizada`

**Payload**:
```json
{
  "rutaId": "UUID",
  "version": 2,
  "motivo": "ALERTA_CRITICA",
  "tenantId": "UUID"
}
```

**Uso en UI**: Invalidar el query de detalle de la ruta (`/api/v1/rutas/{rutaId}/detalle`), mostrar notificación "Ruta actualizada — versión 2", actualizar la línea de ruta en el mapa.

---

### 5. `ruta.estado.cambiado`
Se emite cuando la ruta cambia de estado (aprobada, en ejecución, finalizada, cancelada).

**Suscripción**: `/topic/ruta.estado.cambiado`

**Payload**:
```json
{
  "rutaId": "UUID",
  "estadoAnterior": "APROBADA",
  "estadoNuevo": "EN_EJECUCION",
  "tenantId": "UUID"
}
```

**Uso en UI**: Actualizar badge de estado en la tabla de rutas, invalidar query de la ruta, cambiar color de la línea en el mapa según el nuevo estado.

---

## Variables de entorno

```env
VITE_WS_URL=ws://localhost:8080/ws
```

## Implementación recomendada (React)

```typescript
// shared/websocket/websocketClient.ts
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export function createWebSocketClient(token: string): Client {
  return new Client({
    webSocketFactory: () => new SockJS(import.meta.env.VITE_WS_URL),
    connectHeaders: { Authorization: `Bearer ${token}` },
    reconnectDelay: 5000,        // reconexión automática cada 5 s
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  });
}
```

```typescript
// shared/websocket/useWebSocketSubscription.ts
export function useWebSocketSubscription<T>(
  topic: string,
  handler: (payload: T) => void,
) {
  const { client } = useWebSocketContext();
  useEffect(() => {
    if (!client?.connected) return;
    const sub = client.subscribe(topic, (msg) => {
      handler(JSON.parse(msg.body) as T);
    });
    return () => sub.unsubscribe();
  }, [client?.connected, topic]);
}
```

## Reglas

- Si el WebSocket se desconecta, mostrar indicador visual pero **no bloquear la app** — seguir mostrando el último estado conocido.
- Al reconectar, re-suscribir a todos los topics y hacer `refetch` de los recursos activos (rutas en ejecución, estado de unidades).
- Filtrar mensajes por `tenantId` en el cliente si el tenant del usuario no coincide (defensa en profundidad, aunque hoy los topics no filtran por tenant en el broker).
- Liberar suscripciones al desmontar el componente.
