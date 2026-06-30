-- V006: Seed data base

INSERT INTO auth.tenant (id, nombre, estado)
VALUES (
    '00000000-0000-0000-0000-000000000000',
    'Sistema Interno',
    'ACTIVO'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO auth.tenant (id, nombre, estado)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Municipalidad Lima',
    'ACTIVO'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO auth.usuario (id, tenant_id, nombres, apellidos, email, username, password_hash, estado, creado_por)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    '00000000-0000-0000-0000-000000000000',
    'Sistema',
    'Sistema',
    'sistema@ciudadsana.internal',
    'sistema',
    'NO_LOGIN',
    'ACTIVO',
    '00000000-0000-0000-0000-000000000001'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO auth.usuario (id, tenant_id, nombres, apellidos, email, username, password_hash, estado, creado_por)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111',
    'Admin',
    'Municipal',
    'admin@ciudadsana.pe',
    'admin',
    '$2b$10$IA5nLHkIpZRsKZMDq12GuuWrfP//0qy3.jz630dldXOnk0Lr28wmS',
    'ACTIVO',
    '00000000-0000-0000-0000-000000000001'
) ON CONFLICT (id) DO NOTHING;

INSERT INTO auth.rol (codigo, nombre, descripcion, creado_por)
VALUES
    ('ADMIN',      'Administrador',       'Gestiona usuarios, roles, catálogos y configuración del sistema.', '00000000-0000-0000-0000-000000000001'),
    ('SUPERVISOR', 'Supervisor de zona',  'Planifica, aprueba, monitorea y reprograma rutas.',               '00000000-0000-0000-0000-000000000001'),
    ('OPERADOR',   'Operador de flota',   'Consulta y ejecuta la ruta asignada.',                            '00000000-0000-0000-0000-000000000001'),
    ('ANALISTA',   'Analista municipal',  'Consulta KPIs, históricos y mapas de calor.',                     '00000000-0000-0000-0000-000000000001'),
    ('CIUDADANO',  'Ciudadano',           'Registra alertas y consulta horarios de recolección.',            '00000000-0000-0000-0000-000000000001')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO auth.permiso (codigo, modulo, descripcion, creado_por)
VALUES
    ('USUARIO_GESTIONAR',  'AUTH',       'Crear, actualizar y desactivar usuarios.',        '00000000-0000-0000-0000-000000000001'),
    ('ROL_GESTIONAR',      'AUTH',       'Gestionar roles y permisos.',                     '00000000-0000-0000-0000-000000000001'),
    ('CATALOGO_GESTIONAR', 'OPERACION',  'Gestionar distritos, zonas, unidades, choferes, turnos y contenedores.', '00000000-0000-0000-0000-000000000001'),
    ('RUTA_OPTIMIZAR',     'OPERACION',  'Solicitar optimización de rutas.',                '00000000-0000-0000-0000-000000000001'),
    ('RUTA_APROBAR',       'OPERACION',  'Aprobar rutas generadas.',                        '00000000-0000-0000-0000-000000000001'),
    ('RUTA_MONITOREAR',    'OPERACION',  'Monitorear rutas en ejecución.',                  '00000000-0000-0000-0000-000000000001'),
    ('TELEMETRIA_VER',     'TELEMETRIA', 'Ver ubicación GPS de unidades.',                  '00000000-0000-0000-0000-000000000001'),
    ('ALERTA_REGISTRAR',   'CIUDADANO',  'Registrar alerta ciudadana.',                     '00000000-0000-0000-0000-000000000001'),
    ('ALERTA_VALIDAR',     'CIUDADANO',  'Validar o descartar alertas ciudadanas.',         '00000000-0000-0000-0000-000000000001'),
    ('KPI_VER',            'KPI',        'Consultar KPIs operativos.',                      '00000000-0000-0000-0000-000000000001')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN (
    'USUARIO_GESTIONAR','ROL_GESTIONAR','CATALOGO_GESTIONAR','RUTA_OPTIMIZAR',
    'RUTA_APROBAR','RUTA_MONITOREAR','TELEMETRIA_VER','ALERTA_VALIDAR','KPI_VER'
)
WHERE r.codigo = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN ('CATALOGO_GESTIONAR','RUTA_OPTIMIZAR','RUTA_APROBAR','RUTA_MONITOREAR','TELEMETRIA_VER','ALERTA_VALIDAR','KPI_VER')
WHERE r.codigo = 'SUPERVISOR'
ON CONFLICT DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN ('RUTA_MONITOREAR','TELEMETRIA_VER')
WHERE r.codigo = 'OPERADOR'
ON CONFLICT DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN ('KPI_VER','TELEMETRIA_VER','RUTA_MONITOREAR')
WHERE r.codigo = 'ANALISTA'
ON CONFLICT DO NOTHING;

INSERT INTO auth.rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM auth.rol r
JOIN auth.permiso p ON p.codigo IN ('ALERTA_REGISTRAR')
WHERE r.codigo = 'CIUDADANO'
ON CONFLICT DO NOTHING;

INSERT INTO auth.usuario_rol (usuario_id, rol_id)
SELECT '22222222-2222-2222-2222-222222222222', id FROM auth.rol WHERE codigo = 'ADMIN'
ON CONFLICT DO NOTHING;
