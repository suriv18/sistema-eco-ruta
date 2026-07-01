# Envelopes de respuesta y manejo de errores

## ApiResponse\<T\>

Todos los endpoints de `service-ciudad-sana` envuelven su respuesta en `ApiResponse<T>`.

```json
{
  "success": true,
  "data": { ... },
  "message": null,
  "error": null
}
```

### Respuesta exitosa — recurso creado (201)
```json
{
  "success": true,
  "data": { "id": "UUID", ... },
  "message": null,
  "error": null
}
```

### Respuesta exitosa — sin cuerpo (204)
Sin body. Típico de PATCH de estado (activar, desactivar, logout).

### Respuesta de error
```json
{
  "success": false,
  "data": null,
  "message": null,
  "error": {
    "code": "VALIDACION_ERROR",
    "message": "Error de validación en los datos de entrada",
    "errors": [
      { "field": "username", "message": "must not be blank" },
      { "field": "tenantId", "message": "must not be null" }
    ]
  }
}
```

El campo `errors` solo aparece en errores de validación de campos (400).

---

## PageResult\<T\> — respuestas paginadas

Los endpoints de listado devuelven `ApiResponse<PageResult<T>>`.

```json
{
  "success": true,
  "data": {
    "content": [ ... ],
    "page": 0,
    "size": 20,
    "totalElements": 143,
    "totalPages": 8,
    "first": true,
    "last": false
  },
  "message": null,
  "error": null
}
```

### Parámetros de paginación (query params)

| Param | Tipo | Default | Descripción |
|-------|------|---------|-------------|
| `page` | int | 0 | Número de página (base 0) |
| `size` | int | 20 | Elementos por página |

Ejemplo: `GET /api/v1/operacion/choferes?page=0&size=20`

---

## Catálogo de códigos de error

### Errores del GlobalExceptionHandler

| Código | HTTP | Causa |
|--------|------|-------|
| `VALIDACION_ERROR` | 400 | Bean validation fallida (`@Valid`) — incluye array `errors` con campos |
| `ARGUMENTO_INVALIDO` | 400 | `IllegalArgumentException` del dominio (ej. valor fuera de rango) |
| `ERROR_INTERNO` | 500 | Excepción no manejada |

### Errores de negocio (esperados del backend)

Estos códigos vienen en `error.code` cuando el backend devuelve un `Result.failure(...)`.

**Auth**
| Código | HTTP | Significado |
|--------|------|-------------|
| `CREDENCIALES_INVALIDAS` | 401 | Usuario/contraseña incorrectos |
| `USUARIO_BLOQUEADO` | 403 | Cuenta bloqueada por el admin |
| `USUARIO_INACTIVO` | 403 | Cuenta inactiva |
| `USUARIO_SIN_PERMISO` | 403 | Token válido pero sin rol requerido |
| `TOKEN_INVALIDO` | 401 | JWT expirado o firma inválida |

**Operación**
| Código | HTTP | Significado |
|--------|------|-------------|
| `UNIDAD_NO_DISPONIBLE` | 409 | La unidad no está en estado OPERATIVA |
| `UNIDAD_EN_MANTENIMIENTO` | 409 | Unidad en mantenimiento |
| `CHOFER_NO_DISPONIBLE` | 409 | Chofer no disponible (suspendido o inactivo) |
| `TURNO_SUPERPUESTO` | 409 | El turno se superpone con otro del mismo chofer/unidad |
| `TURNO_INVALIDO` | 400 | Horas inválidas (fin ≤ inicio) |
| `ZONA_NO_EXISTE` | 404 | ZonaId no encontrado |
| `ZONA_FUERA_DE_DISTRITO` | 400 | La zona no pertenece al distrito indicado |
| `CAPACIDAD_EXCEDIDA` | 400 | Demanda supera capacidad de la unidad |

**Rutas**
| Código | HTTP | Significado |
|--------|------|-------------|
| `RUTA_NO_FACTIBLE` | 422 | La ruta no puede ser ejecutada (restricciones imposibles) |
| `RUTA_FINALIZADA_NO_MODIFICABLE` | 409 | La ruta está FINALIZADA y no puede modificarse |
| `OPTIMIZADOR_NO_DISPONIBLE` | 503 | El servicio `service-optimization` no responde |
| `SOLUCION_NO_FACTIBLE` | 422 | OR-Tools no encontró solución (demanda excede capacidad) |
| `GEOMETRIA_INVALIDA` | 400 | Coordenadas GPS fuera de rango |

**Alertas ciudadanas**
| Código | HTTP | Significado |
|--------|------|-------------|
| `ALERTA_DUPLICADA` | 409 | La alerta ya fue reportada (según validación) |
| `ALERTA_FUERA_DE_GEOCERCA` | 400 | Coordenadas fuera del área de cobertura del distrito |
| `ALERTA_NO_CRITICA` | 400 | Se intentó operar una alerta como crítica pero su nivel es NORMAL |

---

## Guía de manejo en el cliente

```typescript
async function handleApiCall<T>(promise: Promise<ApiResponse<T>>): Promise<T> {
  const res = await promise;
  if (!res.success) {
    const code = res.error?.code;
    switch (code) {
      case 'CREDENCIALES_INVALIDAS':
        throw new AuthError('Usuario o contraseña incorrectos');
      case 'USUARIO_BLOQUEADO':
        throw new AuthError('Tu cuenta está bloqueada. Contacta al administrador.');
      case 'TOKEN_INVALIDO':
        await refreshToken(); // intentar refresh
        break;
      case 'OPTIMIZADOR_NO_DISPONIBLE':
        throw new ServiceError('El optimizador no está disponible. Intenta más tarde.');
      case 'RUTA_NO_FACTIBLE':
      case 'SOLUCION_NO_FACTIBLE':
        throw new BusinessError(res.error.message);
      case 'VALIDACION_ERROR':
        throw new ValidationError(res.error.errors); // mapear a campos del formulario
      default:
        throw new UnexpectedError(res.error?.message ?? 'Error desconocido');
    }
  }
  return res.data;
}
```

**Regla**: nunca mostrar `error.message` crudo al usuario. Mapear cada `code` a un mensaje en español claro y amigable.
