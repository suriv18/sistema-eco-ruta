-- V004: Schemas telemetria y ciudadano

CREATE TABLE IF NOT EXISTS telemetria.dispositivo_gps (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id         UUID NOT NULL,
    unidad_externo_id UUID NOT NULL,
    imei              VARCHAR(50),
    proveedor         VARCHAR(80),
    estado            VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    ultimo_ping_en    TIMESTAMPTZ,
    creado_en         TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por        UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en    TIMESTAMPTZ,
    actualizado_por   UUID,
    CONSTRAINT uq_telemetria_dispositivo_unidad UNIQUE (tenant_id, unidad_externo_id),
    CONSTRAINT uq_telemetria_dispositivo_imei UNIQUE (imei),
    CONSTRAINT ck_telemetria_dispositivo_estado CHECK (estado IN ('ACTIVO','INACTIVO','FALLA'))
);

CREATE TABLE IF NOT EXISTS telemetria.telemetria_gps (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id         UUID NOT NULL,
    dispositivo_id    UUID NOT NULL,
    unidad_externo_id UUID NOT NULL,
    ruta_externo_id   UUID,
    ts                TIMESTAMPTZ NOT NULL,
    latitud           NUMERIC(10,7),
    longitud          NUMERIC(10,7),
    posicion          GEOMETRY(POINT, 4326),
    velocidad_kmh     NUMERIC(8,2),
    rumbo_grados      NUMERIC(8,2),
    precision_m       NUMERIC(8,2),
    origen            VARCHAR(30) NOT NULL DEFAULT 'GPS_REAL',
    recibido_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_en         TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por        UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    CONSTRAINT fk_telemetria_gps_dispositivo FOREIGN KEY (dispositivo_id) REFERENCES telemetria.dispositivo_gps(id),
    CONSTRAINT ck_telemetria_gps_velocidad CHECK (velocidad_kmh IS NULL OR velocidad_kmh >= 0),
    CONSTRAINT ck_telemetria_gps_rumbo CHECK (rumbo_grados IS NULL OR (rumbo_grados >= 0 AND rumbo_grados <= 360)),
    CONSTRAINT ck_telemetria_gps_origen CHECK (origen IN ('GPS_REAL','SIMULADOR','APP'))
);

CREATE TABLE IF NOT EXISTS telemetria.estado_unidad_actual (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id            UUID NOT NULL,
    unidad_externo_id    UUID NOT NULL,
    ruta_externo_id      UUID,
    latitud              NUMERIC(10,7),
    longitud             NUMERIC(10,7),
    ultima_posicion      GEOMETRY(POINT, 4326),
    ultima_velocidad_kmh NUMERIC(8,2),
    ultimo_ping_en       TIMESTAMPTZ,
    estado_movimiento    VARCHAR(30) NOT NULL DEFAULT 'SIN_SENAL',
    actualizado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_telemetria_estado_unidad UNIQUE (tenant_id, unidad_externo_id),
    CONSTRAINT ck_telemetria_estado_movimiento CHECK (estado_movimiento IN ('EN_RUTA','DETENIDA','SIN_SENAL','DESCARGANDO','FUERA_DE_RUTA'))
);

CREATE TABLE IF NOT EXISTS telemetria.evento_desvio_ruta (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id          UUID NOT NULL,
    unidad_externo_id  UUID NOT NULL,
    ruta_externo_id    UUID NOT NULL,
    latitud            NUMERIC(10,7),
    longitud           NUMERIC(10,7),
    posicion           GEOMETRY(POINT, 4326),
    distancia_desvio_m NUMERIC(10,2) NOT NULL,
    severidad          VARCHAR(30) NOT NULL,
    estado             VARCHAR(30) NOT NULL DEFAULT 'ABIERTO',
    detectado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    atendido_en        TIMESTAMPTZ,
    creado_en          TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por         UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en     TIMESTAMPTZ,
    actualizado_por    UUID,
    CONSTRAINT ck_telemetria_desvio_distancia CHECK (distancia_desvio_m >= 0),
    CONSTRAINT ck_telemetria_desvio_severidad CHECK (severidad IN ('BAJA','MEDIA','ALTA')),
    CONSTRAINT ck_telemetria_desvio_estado CHECK (estado IN ('ABIERTO','ATENDIDO','DESCARTADO'))
);

CREATE TABLE IF NOT EXISTS telemetria.evento_conectividad (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id         UUID NOT NULL,
    unidad_externo_id UUID NOT NULL,
    dispositivo_id    UUID,
    tipo_evento       VARCHAR(40) NOT NULL,
    detalle           TEXT,
    detectado_en      TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_en         TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por        UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en    TIMESTAMPTZ,
    actualizado_por   UUID,
    CONSTRAINT fk_telemetria_evento_conectividad_dispositivo FOREIGN KEY (dispositivo_id) REFERENCES telemetria.dispositivo_gps(id),
    CONSTRAINT ck_telemetria_evento_conectividad_tipo CHECK (tipo_evento IN ('SIN_SENAL','SENAL_RECUPERADA','GPS_FALLA','BATERIA_BAJA'))
);

CREATE INDEX IF NOT EXISTS idx_telemetria_dispositivo_unidad ON telemetria.dispositivo_gps (tenant_id, unidad_externo_id);
CREATE INDEX IF NOT EXISTS idx_telemetria_gps_unidad_ts ON telemetria.telemetria_gps (tenant_id, unidad_externo_id, ts DESC);
CREATE INDEX IF NOT EXISTS idx_telemetria_gps_ruta_ts ON telemetria.telemetria_gps (tenant_id, ruta_externo_id, ts DESC);
CREATE INDEX IF NOT EXISTS idx_telemetria_gps_posicion_gist ON telemetria.telemetria_gps USING GIST (posicion);
CREATE INDEX IF NOT EXISTS idx_telemetria_estado_tenant ON telemetria.estado_unidad_actual (tenant_id, estado_movimiento);
CREATE INDEX IF NOT EXISTS idx_telemetria_estado_posicion_gist ON telemetria.estado_unidad_actual USING GIST (ultima_posicion);
CREATE INDEX IF NOT EXISTS idx_telemetria_desvio_ruta_estado ON telemetria.evento_desvio_ruta (tenant_id, ruta_externo_id, estado);
CREATE INDEX IF NOT EXISTS idx_telemetria_desvio_posicion_gist ON telemetria.evento_desvio_ruta USING GIST (posicion);

-- Schema ciudadano

CREATE TABLE IF NOT EXISTS ciudadano.ciudadano (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL,
    nombres         VARCHAR(120),
    apellidos       VARCHAR(120),
    telefono        VARCHAR(20),
    email           VARCHAR(150),
    documento       VARCHAR(20),
    estado          VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT ck_ciudadano_estado CHECK (estado IN ('ACTIVO','BLOQUEADO','INACTIVO'))
);

CREATE TABLE IF NOT EXISTS ciudadano.alerta_ciudadana (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    ciudadano_id        UUID,
    distrito_id_externo UUID NOT NULL,
    zona_id_externo     UUID,
    titulo              VARCHAR(150) NOT NULL,
    descripcion         TEXT,
    latitud             NUMERIC(10,7),
    longitud            NUMERIC(10,7),
    ubicacion           GEOMETRY(POINT, 4326),
    volumen_estimado    VARCHAR(30) NOT NULL DEFAULT 'MEDIO',
    nivel_criticidad    VARCHAR(30) NOT NULL DEFAULT 'NORMAL',
    fuente              VARCHAR(30) NOT NULL DEFAULT 'WEB',
    estado              VARCHAR(30) NOT NULL DEFAULT 'REGISTRADA',
    creado_en           TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por          UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en      TIMESTAMPTZ,
    actualizado_por     UUID,
    CONSTRAINT fk_ciudadano_alerta_ciudadano FOREIGN KEY (ciudadano_id) REFERENCES ciudadano.ciudadano(id),
    CONSTRAINT ck_ciudadano_alerta_volumen CHECK (volumen_estimado IN ('BAJO','MEDIO','ALTO')),
    CONSTRAINT ck_ciudadano_alerta_criticidad CHECK (nivel_criticidad IN ('NORMAL','CRITICA')),
    CONSTRAINT ck_ciudadano_alerta_fuente CHECK (fuente IN ('APP','WEB','OEFA','OPERADOR')),
    CONSTRAINT ck_ciudadano_alerta_estado CHECK (estado IN ('REGISTRADA','VALIDADA','EN_ATENCION','ATENDIDA','DESCARTADA','DUPLICADA'))
);

CREATE TABLE IF NOT EXISTS ciudadano.alerta_foto (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    alerta_id      UUID NOT NULL,
    url_archivo    TEXT NOT NULL,
    tipo_mime      VARCHAR(50) NOT NULL,
    tamanio_bytes  BIGINT,
    creado_en      TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por     UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    CONSTRAINT fk_ciudadano_alerta_foto_alerta FOREIGN KEY (alerta_id) REFERENCES ciudadano.alerta_ciudadana(id),
    CONSTRAINT ck_ciudadano_alerta_foto_tamanio CHECK (tamanio_bytes IS NULL OR tamanio_bytes >= 0)
);

CREATE TABLE IF NOT EXISTS ciudadano.alerta_estado_historial (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id               UUID NOT NULL,
    alerta_id               UUID NOT NULL,
    estado_anterior         VARCHAR(30),
    estado_nuevo            VARCHAR(30) NOT NULL,
    comentario              TEXT,
    cambiado_por_usuario_id UUID,
    cambiado_en             TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_en               TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por              UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    CONSTRAINT fk_ciudadano_alerta_historial_alerta FOREIGN KEY (alerta_id) REFERENCES ciudadano.alerta_ciudadana(id)
);

CREATE TABLE IF NOT EXISTS ciudadano.validacion_alerta (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id          UUID NOT NULL,
    alerta_id          UUID NOT NULL,
    es_duplicada       BOOLEAN NOT NULL DEFAULT FALSE,
    alerta_original_id UUID,
    dentro_geocerca    BOOLEAN NOT NULL DEFAULT TRUE,
    score_spam         NUMERIC(5,2) NOT NULL DEFAULT 0,
    resultado          VARCHAR(30) NOT NULL DEFAULT 'REVISION',
    validada_en        TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_en          TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por         UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    CONSTRAINT fk_ciudadano_validacion_alerta FOREIGN KEY (alerta_id) REFERENCES ciudadano.alerta_ciudadana(id),
    CONSTRAINT fk_ciudadano_validacion_alerta_original FOREIGN KEY (alerta_original_id) REFERENCES ciudadano.alerta_ciudadana(id),
    CONSTRAINT uq_ciudadano_validacion_alerta UNIQUE (alerta_id),
    CONSTRAINT ck_ciudadano_validacion_score CHECK (score_spam >= 0 AND score_spam <= 100),
    CONSTRAINT ck_ciudadano_validacion_resultado CHECK (resultado IN ('APROBADA','RECHAZADA','REVISION'))
);

CREATE TABLE IF NOT EXISTS ciudadano.integracion_oefa (
    id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id             UUID NOT NULL,
    alerta_id             UUID,
    id_externo_oefa       VARCHAR(100),
    payload_original      JSONB,
    estado_sincronizacion VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    ultimo_intento_en     TIMESTAMPTZ,
    creado_en             TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por            UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    CONSTRAINT fk_ciudadano_integracion_oefa_alerta FOREIGN KEY (alerta_id) REFERENCES ciudadano.alerta_ciudadana(id),
    CONSTRAINT ck_ciudadano_integracion_oefa_estado CHECK (estado_sincronizacion IN ('PENDIENTE','ENVIADO','RECIBIDO','ERROR'))
);

CREATE INDEX IF NOT EXISTS idx_ciudadano_alerta_tenant_estado ON ciudadano.alerta_ciudadana (tenant_id, estado, creado_en DESC);
CREATE INDEX IF NOT EXISTS idx_ciudadano_alerta_zona ON ciudadano.alerta_ciudadana (tenant_id, zona_id_externo);
CREATE INDEX IF NOT EXISTS idx_ciudadano_alerta_criticidad ON ciudadano.alerta_ciudadana (tenant_id, nivel_criticidad, estado);
CREATE INDEX IF NOT EXISTS idx_ciudadano_alerta_ubicacion_gist ON ciudadano.alerta_ciudadana USING GIST (ubicacion);
CREATE INDEX IF NOT EXISTS idx_ciudadano_alerta_foto_alerta ON ciudadano.alerta_foto (alerta_id);
CREATE INDEX IF NOT EXISTS idx_ciudadano_alerta_historial_alerta ON ciudadano.alerta_estado_historial (alerta_id, cambiado_en DESC);
CREATE INDEX IF NOT EXISTS idx_ciudadano_validacion_alerta ON ciudadano.validacion_alerta (alerta_id);
