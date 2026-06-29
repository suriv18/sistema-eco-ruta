-- V006: Seed data base

INSERT INTO auth.rol (codigo, nombre, descripcion)
VALUES
    ('ADMIN',      'Administrador',       'Gestiona usuarios, roles, catálogos y configuración del sistema.'),
    ('SUPERVISOR', 'Supervisor de zona',  'Planifica, aprueba, monitorea y reprograma rutas.'),
    ('OPERADOR',   'Operador de flota',   'Consulta y ejecuta la ruta asignada.'),
    ('ANALISTA',   'Analista municipal',  'Consulta KPIs, históricos y mapas de calor.'),
    ('CIUDADANO',  'Ciudadano',           'Registra alertas y consulta horarios de recolección.')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO auth.permiso (codigo, modulo, descripcion)
VALUES
    ('USUARIO_GESTIONAR',  'AUTH',       'Crear, actualizar y desactivar usuarios.'),
    ('ROL_GESTIONAR',      'AUTH',       'Gestionar roles y permisos.'),
    ('CATALOGO_GESTIONAR', 'OPERACION',  'Gestionar distritos, zonas, unidades, choferes, turnos y contenedores.'),
    ('RUTA_OPTIMIZAR',     'OPERACION',  'Solicitar optimización de rutas.'),
    ('RUTA_APROBAR',       'OPERACION',  'Aprobar rutas generadas.'),
    ('RUTA_MONITOREAR',    'OPERACION',  'Monitorear rutas en ejecución.'),
    ('TELEMETRIA_VER',     'TELEMETRIA', 'Ver ubicación GPS de unidades.'),
    ('ALERTA_REGISTRAR',   'CIUDADANO',  'Registrar alerta ciudadana.'),
    ('ALERTA_VALIDAR',     'CIUDADANO',  'Validar o descartar alertas ciudadanas.'),
    ('KPI_VER',            'KPI',        'Consultar KPIs operativos.')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.rol_id, p.permiso_id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN (
    'USUARIO_GESTIONAR','ROL_GESTIONAR','CATALOGO_GESTIONAR','RUTA_OPTIMIZAR',
    'RUTA_APROBAR','RUTA_MONITOREAR','TELEMETRIA_VER','ALERTA_VALIDAR','KPI_VER'
)
WHERE r.codigo = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.rol_id, p.permiso_id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN ('CATALOGO_GESTIONAR','RUTA_OPTIMIZAR','RUTA_APROBAR','RUTA_MONITOREAR','TELEMETRIA_VER','ALERTA_VALIDAR','KPI_VER')
WHERE r.codigo = 'SUPERVISOR'
ON CONFLICT DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.rol_id, p.permiso_id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN ('RUTA_MONITOREAR','TELEMETRIA_VER')
WHERE r.codigo = 'OPERADOR'
ON CONFLICT DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.rol_id, p.permiso_id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN ('KPI_VER','TELEMETRIA_VER','RUTA_MONITOREAR')
WHERE r.codigo = 'ANALISTA'
ON CONFLICT DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.rol_id, p.permiso_id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN ('ALERTA_REGISTRAR')
WHERE r.codigo = 'CIUDADANO'
ON CONFLICT DO NOTHING;
