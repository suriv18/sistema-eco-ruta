# Autenticación y Seguridad

## Mecanismo

JWT Bearer + Spring Security stateless (sin sesión de servidor).

## Flujo de autenticación

```
1. POST /api/v1/auth/login  { username, password, tenantId }
        ↓
   Respuesta: { accessToken, refreshToken, expiresIn, roles, ... }

2. Cada request protegido:
   Authorization: Bearer <accessToken>

3. Si accessToken expira (1 hora):
   POST /api/v1/auth/refresh  { refreshToken }
        ↓
   Respuesta: nuevo { accessToken, refreshToken }

4. Cerrar sesión:
   POST /api/v1/auth/logout  { refreshToken }
   → invalida el refreshToken en BD
```

## Endpoints de autenticación

| Método | Path | Body | Respuesta | Notas |
|--------|------|------|-----------|-------|
| POST | `/api/v1/auth/login` | `LoginRequest` | `ApiResponse<LoginResponseDto>` | **Público** |
| POST | `/api/v1/auth/refresh` | `RefreshTokenRequest` | `ApiResponse<LoginResponseDto>` | **Público** |
| POST | `/api/v1/auth/logout` | `RefreshTokenRequest` | 204 No Content | Autenticado |
| GET | `/api/v1/auth/me` | — | `ApiResponse<UsuarioResponseDto>` | Autenticado |

### LoginRequest
```json
{
  "username": "string (requerido)",
  "password": "string (requerido)",
  "tenantId": "UUID (requerido)"
}
```

### LoginResponseDto
```json
{
  "accessToken": "string",
  "refreshToken": "string",
  "tokenType": "Bearer",
  "expiresIn": 3600000,
  "usuarioId": "UUID",
  "username": "string",
  "roles": ["ADMIN"]
}
```

## Cabeceras HTTP

| Header | Cuándo | Valor de ejemplo |
|--------|--------|-----------------|
| `Authorization` | Todos los endpoints privados | `Bearer eyJhbGc...` |
| `X-Tenant-Id` | Solo `POST /api/v1/ciudadanos/alertas` (público) | `11111111-1111-1111-1111-111111111111` |
| `Content-Type` | POST / PUT / PATCH con body | `application/json` |

## Claims del JWT

```json
{
  "sub": "UUID-del-usuario",
  "tenantId": "UUID-del-tenant",
  "roles": ["SUPERVISOR"],
  "iat": 1751000000,
  "exp": 1751003600
}
```

- **Duración access token**: 1 hora (configurable `JWT_EXPIRATION_MS`)
- **Duración refresh token**: 7 días (configurable `JWT_REFRESH_EXPIRATION_MS`)

## Roles y permisos

| Rol | Descripción | Nivel de acceso |
|-----|-------------|----------------|
| `ADMIN` | Administrador del sistema | Total: usuarios, roles, catálogos, auditoría |
| `SUPERVISOR` | Supervisor municipal | Rutas, alertas, operación, KPIs, mapa |
| `OPERADOR` | Operador de campo / chofer | Consulta rutas asignadas, telemetría, marcar paradas |
| `ANALISTA` | Analista de datos | Solo lectura: KPIs, rutas, alertas, mapa |
| `CIUDADANO` | Ciudadano (app móvil) | Solo registrar alertas (endpoint público) |

## Matriz de acceso por módulo

| Módulo / Ruta | ADMIN | SUPERVISOR | OPERADOR | ANALISTA | Público |
|---------------|-------|-----------|---------|---------|---------|
| `POST /auth/login` | ✓ | ✓ | ✓ | ✓ | ✓ |
| `POST /auth/refresh` | ✓ | ✓ | ✓ | ✓ | ✓ |
| `/api/v1/usuarios/**` | ✓ | — | — | — | — |
| `/api/v1/roles/**` | ✓ | — | — | — | — |
| `/api/v1/permisos/**` | ✓ | — | — | — | — |
| `GET /operacion/**` | ✓ | ✓ | ✓ | ✓ | — |
| `POST/PUT/PATCH /operacion/**` | ✓ | ✓ | — | — | — |
| `GET /rutas/**` | ✓ | ✓ | ✓ | ✓ | — |
| `POST /rutas/**` (crear/optimizar) | ✓ | ✓ | — | — | — |
| `/telemetria/**` | ✓ | ✓ | ✓ | ✓ | — |
| `POST /ciudadanos/alertas` | ✓ | ✓ | ✓ | — | ✓ (con X-Tenant-Id) |
| Resto `/ciudadanos/**` | ✓ | ✓ | ✓ | — | — |
| `/api/v1/kpis/**` | ✓ | ✓ | — | ✓ | — |
| `/api/v1/auditoria/**` | ✓ | — | — | — | — |

> **Nota (KI-01)**: La regla de `SecurityConfig` usa `/api/v1/kpi/**` (sin `s`) pero el controller expone `/api/v1/kpis/**`. Ver [known issues](README.md#known-issues).

## Guía de implementación para el cliente

### Almacenamiento de tokens

**Web**: `localStorage` para accessToken y refreshToken (o `httpOnly cookie` si se gestiona desde el backend).  
**Móvil**: `expo-secure-store` para ambos tokens (nunca `AsyncStorage` sin cifrar para credenciales).

### Interceptor de refresh automático (Axios / fetch)

```typescript
// En caso de 401, intentar refresh antes de redirigir al login
onResponseError(error) {
  if (error.response?.status === 401 && !isRefreshRequest) {
    const newTokens = await refresh(storedRefreshToken)
    if (newTokens) {
      // reintentar request original con nuevo accessToken
    } else {
      // redirigir a /login
    }
  }
}
```

### Multi-tenant

El `tenantId` viaja en el JWT para todos los endpoints privados — el backend lo extrae del token automáticamente. Solo el endpoint público de alertas ciudadanas requiere enviarlo explícitamente en el header `X-Tenant-Id`.
