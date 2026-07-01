# Contrato de API — Ciudad Sana

> Fuente de verdad para el panel web y la app móvil.  
> Verificado contra el código fuente del backend (julio 2026).

## Documentos

| Archivo | Contenido |
|---------|-----------|
| [endpoints.md](endpoints.md) | Todos los endpoints REST por módulo, método, path, request, response y roles |
| [auth-y-seguridad.md](auth-y-seguridad.md) | Flujo JWT, roles, header X-Tenant-Id, matriz de permisos |
| [envelopes-y-errores.md](envelopes-y-errores.md) | ApiResponse\<T\>, PageResult\<T\>, paginación, catálogo de errores |
| [websocket.md](websocket.md) | STOMP /ws, topics, payloads y reconexión |
| [maquinas-de-estado.md](maquinas-de-estado.md) | Todos los enums de estado y transiciones válidas |
| [modelo-de-negocio.md](modelo-de-negocio.md) | Agregados por módulo y relaciones cross-módulo |

## Servicios

```
service-ciudad-sana  →  http://localhost:8080  (panel web y app móvil lo consumen)
service-optimization →  http://localhost:8000  (solo lo llama ciudad-sana, server-to-server)
```

Documentación interactiva (cuando el backend está corriendo):
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

---

## Known issues de configuración del backend

Tres inconsistencias detectadas en el análisis. No rompen el backend actual pero **pueden afectar al frontend o al deploy**. Requieren corrección antes de producción.

### KI-01 — SecurityConfig protege ruta incorrecta para KPI
**Problema**: `SecurityConfig.java` tiene la regla:
```java
.requestMatchers("/api/v1/kpi/**").hasAnyRole("ADMIN", "SUPERVISOR", "ANALISTA")
```
Pero el controller expone `/api/v1/kpis/**` (con `s` al final). La regla de seguridad nunca se aplica a los endpoints reales.  
**Impacto**: Los endpoints KPI podrían estar accesibles a roles no autorizados.  
**Fix**: Cambiar `/api/v1/kpi/**` → `/api/v1/kpis/**` en `SecurityConfig.java`.

### KI-02 — Puerto del optimizador desalineado
**Problema**: `application.yml` define `app.optimization.service-url=http://localhost:8000` pero el adapter `FastApiOptimizationClientAdapter.java` usa `@Value("${integracion.optimization.base-url:http://localhost:8001}")`. La property `integracion.optimization.base-url` no está en `application.yml`, así que el adapter usa el default `8001`, mientras el servicio Python escucha en `8000`.  
**Impacto**: La optimización de rutas falla en local salvo que se defina la variable de entorno `integracion.optimization.base-url`.  
**Fix**: Unificar a una sola property. Opción recomendada: cambiar el `@Value` del adapter a `${app.optimization.service-url}`.

### KI-03 — Doble property para OSRM
**Problema**: `application.yml` define `app.osrm.service-url=http://router.project-osrm.org` y el adapter OSRM usa `${integracion.osrm.base-url:http://localhost:5000}`. Dos defaults distintos: uno apunta al OSRM público, el otro al local.  
**Impacto**: En local el adapter usa localhost:5000 (que probablemente no está disponible), en lugar del público.  
**Fix**: Unificar a `app.osrm.service-url` y alinear los defaults entre `application.yml` y el adapter.
