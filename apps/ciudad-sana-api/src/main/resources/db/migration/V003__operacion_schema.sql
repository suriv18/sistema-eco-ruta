-- V003: Schema operacion

CREATE TABLE IF NOT EXISTS operacion.distrito (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL,
    nombre          VARCHAR(120) NOT NULL,
    ubigeo          VARCHAR(6),
    limite          GEOMETRY(MULTIPOLYGON, 4326),
    estado          VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT uq_operacion_distrito_tenant_nombre UNIQUE (tenant_id, nombre),
    CONSTRAINT ck_operacion_distrito_estado CHECK (estado IN ('ACTIVO','INACTIVO'))
);

CREATE TABLE IF NOT EXISTS operacion.zona (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    distrito_id         UUID NOT NULL,
    codigo              VARCHAR(50) NOT NULL,
    nombre              VARCHAR(150) NOT NULL,
    tipo_zona           VARCHAR(30) NOT NULL,
    poligono            GEOMETRY(POLYGON, 4326) NOT NULL,
    punto_referencia    GEOMETRY(POINT, 4326),
    gen_promedio_kg_dia NUMERIC(10,2) DEFAULT 0,
    frecuencia_semana   INTEGER DEFAULT 0,
    ventana_inicio      TIME,
    ventana_fin         TIME,
    prioridad_base      INTEGER NOT NULL DEFAULT 1,
    estado              VARCHAR(30) NOT NULL DEFAULT 'ACTIVA',
    creado_en           TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por          UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en      TIMESTAMPTZ,
    actualizado_por     UUID,
    CONSTRAINT fk_operacion_zona_distrito FOREIGN KEY (distrito_id) REFERENCES operacion.distrito(id),
    CONSTRAINT uq_operacion_zona_tenant_codigo UNIQUE (tenant_id, codigo),
    CONSTRAINT ck_operacion_zona_tipo CHECK (tipo_zona IN ('RESIDENCIAL','COMERCIAL','MIXTA','MERCADO','INDUSTRIAL','OTRA')),
    CONSTRAINT ck_operacion_zona_estado CHECK (estado IN ('ACTIVA','INACTIVA')),
    CONSTRAINT ck_operacion_zona_gen CHECK (gen_promedio_kg_dia >= 0),
    CONSTRAINT ck_operacion_zona_frec CHECK (frecuencia_semana >= 0 AND frecuencia_semana <= 7),
    CONSTRAINT ck_operacion_zona_prioridad CHECK (prioridad_base >= 1)
);

CREATE TABLE IF NOT EXISTS operacion.deposito (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL,
    distrito_id     UUID NOT NULL,
    nombre          VARCHAR(150) NOT NULL,
    ubicacion       GEOMETRY(POINT, 4326) NOT NULL,
    direccion       TEXT,
    tipo            VARCHAR(40) NOT NULL,
    estado          VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT fk_operacion_deposito_distrito FOREIGN KEY (distrito_id) REFERENCES operacion.distrito(id),
    CONSTRAINT ck_operacion_deposito_tipo CHECK (tipo IN ('BASE','TRANSFERENCIA','RELLENO','OTRO')),
    CONSTRAINT ck_operacion_deposito_estado CHECK (estado IN ('ACTIVO','INACTIVO'))
);

CREATE TABLE IF NOT EXISTS operacion.contenedor (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id         UUID NOT NULL,
    zona_id           UUID NOT NULL,
    codigo            VARCHAR(50) NOT NULL,
    ubicacion         GEOMETRY(POINT, 4326) NOT NULL,
    capacidad_m3      NUMERIC(8,2) NOT NULL,
    estado_contenedor VARCHAR(30) NOT NULL DEFAULT 'VACIO',
    estado            VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    creado_en         TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por        UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en    TIMESTAMPTZ,
    actualizado_por   UUID,
    CONSTRAINT fk_operacion_contenedor_zona FOREIGN KEY (zona_id) REFERENCES operacion.zona(id),
    CONSTRAINT uq_operacion_contenedor_tenant_codigo UNIQUE (tenant_id, codigo),
    CONSTRAINT ck_operacion_contenedor_capacidad CHECK (capacidad_m3 > 0),
    CONSTRAINT ck_operacion_contenedor_estado_contenedor CHECK (estado_contenedor IN ('VACIO','PARCIAL','LLENO','DESBORDADO')),
    CONSTRAINT ck_operacion_contenedor_estado CHECK (estado IN ('ACTIVO','INACTIVO'))
);

CREATE TABLE IF NOT EXISTS operacion.unidad (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id         UUID NOT NULL,
    placa             VARCHAR(15) NOT NULL,
    codigo_interno    VARCHAR(50),
    tipo_unidad       VARCHAR(50) NOT NULL,
    capacidad_m3      NUMERIC(8,2) NOT NULL,
    capacidad_kg      NUMERIC(10,2) NOT NULL,
    consumo_km_litro  NUMERIC(8,2),
    estado_operativo  VARCHAR(30) NOT NULL DEFAULT 'OPERATIVA',
    estado            VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    creado_en         TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por        UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en    TIMESTAMPTZ,
    actualizado_por   UUID,
    CONSTRAINT uq_operacion_unidad_tenant_placa UNIQUE (tenant_id, placa),
    CONSTRAINT ck_operacion_unidad_tipo CHECK (tipo_unidad IN ('COMPACTADOR','BARANDA','VOLQUETE','MOTOFURGON','OTRO')),
    CONSTRAINT ck_operacion_unidad_capacidad_m3 CHECK (capacidad_m3 > 0),
    CONSTRAINT ck_operacion_unidad_capacidad_kg CHECK (capacidad_kg > 0),
    CONSTRAINT ck_operacion_unidad_estado_operativo CHECK (estado_operativo IN ('OPERATIVA','MANTENIMIENTO','INACTIVA','AVERIADA')),
    CONSTRAINT ck_operacion_unidad_estado CHECK (estado IN ('ACTIVO','INACTIVO'))
);

CREATE TABLE IF NOT EXISTS operacion.chofer (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL,
    nombres         VARCHAR(120) NOT NULL,
    apellidos       VARCHAR(120) NOT NULL,
    dni             VARCHAR(8),
    licencia        VARCHAR(30),
    telefono        VARCHAR(20),
    estado          VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT uq_operacion_chofer_tenant_dni UNIQUE (tenant_id, dni),
    CONSTRAINT ck_operacion_chofer_estado CHECK (estado IN ('ACTIVO','INACTIVO','SUSPENDIDO'))
);

CREATE TABLE IF NOT EXISTS operacion.turno (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL,
    unidad_id       UUID NOT NULL,
    chofer_id       UUID NOT NULL,
    distrito_id     UUID NOT NULL,
    fecha           DATE NOT NULL,
    hora_inicio     TIME NOT NULL,
    hora_fin        TIME NOT NULL,
    tipo_turno      VARCHAR(30) NOT NULL,
    estado          VARCHAR(30) NOT NULL DEFAULT 'PROGRAMADO',
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT fk_operacion_turno_unidad FOREIGN KEY (unidad_id) REFERENCES operacion.unidad(id),
    CONSTRAINT fk_operacion_turno_chofer FOREIGN KEY (chofer_id) REFERENCES operacion.chofer(id),
    CONSTRAINT fk_operacion_turno_distrito FOREIGN KEY (distrito_id) REFERENCES operacion.distrito(id),
    CONSTRAINT uq_operacion_turno_unidad_fecha_hora UNIQUE (tenant_id, unidad_id, fecha, hora_inicio),
    CONSTRAINT uq_operacion_turno_chofer_fecha_hora UNIQUE (tenant_id, chofer_id, fecha, hora_inicio),
    CONSTRAINT ck_operacion_turno_tipo CHECK (tipo_turno IN ('MANANA','TARDE','NOCHE')),
    CONSTRAINT ck_operacion_turno_estado CHECK (estado IN ('PROGRAMADO','EN_CURSO','FINALIZADO','CANCELADO')),
    CONSTRAINT ck_operacion_turno_hora CHECK (hora_fin > hora_inicio)
);

CREATE TABLE IF NOT EXISTS operacion.ruta (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    turno_id            UUID NOT NULL,
    distrito_id         UUID NOT NULL,
    deposito_origen_id  UUID NOT NULL,
    deposito_destino_id UUID NOT NULL,
    fecha               DATE NOT NULL,
    tipo_ruta           VARCHAR(30) NOT NULL DEFAULT 'OPTIMIZADA',
    estado              VARCHAR(30) NOT NULL DEFAULT 'BORRADOR',
    distancia_total_m   NUMERIC(12,2) DEFAULT 0,
    duracion_total_s    INTEGER DEFAULT 0,
    carga_total_kg      NUMERIC(12,2) DEFAULT 0,
    geometria           GEOMETRY(LINESTRING, 4326),
    creado_en           TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por          UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en      TIMESTAMPTZ,
    actualizado_por     UUID,
    CONSTRAINT fk_operacion_ruta_turno FOREIGN KEY (turno_id) REFERENCES operacion.turno(id),
    CONSTRAINT fk_operacion_ruta_distrito FOREIGN KEY (distrito_id) REFERENCES operacion.distrito(id),
    CONSTRAINT fk_operacion_ruta_deposito_origen FOREIGN KEY (deposito_origen_id) REFERENCES operacion.deposito(id),
    CONSTRAINT fk_operacion_ruta_deposito_destino FOREIGN KEY (deposito_destino_id) REFERENCES operacion.deposito(id),
    CONSTRAINT ck_operacion_ruta_tipo CHECK (tipo_ruta IN ('HISTORICA','OPTIMIZADA','REOPTIMIZADA')),
    CONSTRAINT ck_operacion_ruta_estado CHECK (estado IN ('BORRADOR','APROBADA','EN_EJECUCION','FINALIZADA','CANCELADA')),
    CONSTRAINT ck_operacion_ruta_distancia CHECK (distancia_total_m >= 0),
    CONSTRAINT ck_operacion_ruta_duracion CHECK (duracion_total_s >= 0),
    CONSTRAINT ck_operacion_ruta_carga CHECK (carga_total_kg >= 0)
);

CREATE TABLE IF NOT EXISTS operacion.ruta_version (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id         UUID NOT NULL,
    ruta_id           UUID NOT NULL,
    version           INTEGER NOT NULL,
    motivo            VARCHAR(100) NOT NULL DEFAULT 'INICIAL',
    alerta_id_externo UUID,
    generado_por      VARCHAR(80) NOT NULL DEFAULT 'SISTEMA',
    distancia_total_m NUMERIC(12,2) DEFAULT 0,
    duracion_total_s  INTEGER DEFAULT 0,
    carga_total_kg    NUMERIC(12,2) DEFAULT 0,
    geometria         GEOMETRY(LINESTRING, 4326),
    creado_en         TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por        UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    CONSTRAINT fk_operacion_ruta_version_ruta FOREIGN KEY (ruta_id) REFERENCES operacion.ruta(id),
    CONSTRAINT uq_operacion_ruta_version UNIQUE (ruta_id, version),
    CONSTRAINT ck_operacion_ruta_version_version CHECK (version > 0),
    CONSTRAINT ck_operacion_ruta_version_motivo CHECK (motivo IN ('INICIAL','ALERTA_CRITICA','DESVIO','MANUAL','RECALCULO')),
    CONSTRAINT ck_operacion_ruta_version_generado_por CHECK (generado_por IN ('USUARIO','SISTEMA','OPTIMIZADOR'))
);

CREATE TABLE IF NOT EXISTS operacion.ruta_parada (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    ruta_version_id     UUID NOT NULL,
    zona_id             UUID NOT NULL,
    contenedor_id       UUID,
    orden               INTEGER NOT NULL,
    eta                 TIMESTAMPTZ,
    hora_llegada_real   TIMESTAMPTZ,
    hora_salida_real    TIMESTAMPTZ,
    demanda_estimada_kg NUMERIC(10,2) DEFAULT 0,
    carga_acumulada_kg  NUMERIC(10,2) DEFAULT 0,
    estado              VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    creado_en           TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por          UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en      TIMESTAMPTZ,
    actualizado_por     UUID,
    CONSTRAINT fk_operacion_ruta_parada_version FOREIGN KEY (ruta_version_id) REFERENCES operacion.ruta_version(id),
    CONSTRAINT fk_operacion_ruta_parada_zona FOREIGN KEY (zona_id) REFERENCES operacion.zona(id),
    CONSTRAINT fk_operacion_ruta_parada_contenedor FOREIGN KEY (contenedor_id) REFERENCES operacion.contenedor(id),
    CONSTRAINT uq_operacion_ruta_parada_orden UNIQUE (ruta_version_id, orden),
    CONSTRAINT ck_operacion_ruta_parada_orden CHECK (orden > 0),
    CONSTRAINT ck_operacion_ruta_parada_demanda CHECK (demanda_estimada_kg >= 0),
    CONSTRAINT ck_operacion_ruta_parada_carga CHECK (carga_acumulada_kg >= 0),
    CONSTRAINT ck_operacion_ruta_parada_estado CHECK (estado IN ('PENDIENTE','ATENDIDA','OMITIDA','EN_ATENCION'))
);

CREATE TABLE IF NOT EXISTS operacion.ruta_evento (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL,
    ruta_id         UUID NOT NULL,
    tipo_evento     VARCHAR(60) NOT NULL,
    descripcion     TEXT,
    posicion        GEOMETRY(POINT, 4326),
    datos           JSONB,
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    CONSTRAINT fk_operacion_ruta_evento_ruta FOREIGN KEY (ruta_id) REFERENCES operacion.ruta(id),
    CONSTRAINT ck_operacion_ruta_evento_tipo CHECK (tipo_evento IN ('INICIO','FIN','DESVIO','PARADA_ATENDIDA','AVERIA','REOPTIMIZACION','OBSERVACION'))
);

CREATE TABLE IF NOT EXISTS operacion.horario_recoleccion (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL,
    zona_id         UUID NOT NULL,
    dia_semana      INTEGER NOT NULL,
    hora_inicio     TIME NOT NULL,
    hora_fin        TIME NOT NULL,
    observacion     TEXT,
    estado          VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT fk_operacion_horario_zona FOREIGN KEY (zona_id) REFERENCES operacion.zona(id),
    CONSTRAINT uq_operacion_horario UNIQUE (tenant_id, zona_id, dia_semana, hora_inicio, hora_fin),
    CONSTRAINT ck_operacion_horario_dia CHECK (dia_semana BETWEEN 1 AND 7),
    CONSTRAINT ck_operacion_horario_estado CHECK (estado IN ('ACTIVO','INACTIVO')),
    CONSTRAINT ck_operacion_horario_hora CHECK (hora_fin > hora_inicio)
);

CREATE INDEX IF NOT EXISTS idx_operacion_distrito_tenant ON operacion.distrito (tenant_id);
CREATE INDEX IF NOT EXISTS idx_operacion_zona_tenant_distrito ON operacion.zona (tenant_id, distrito_id);
CREATE INDEX IF NOT EXISTS idx_operacion_zona_estado ON operacion.zona (estado);
CREATE INDEX IF NOT EXISTS idx_operacion_unidad_tenant_estado ON operacion.unidad (tenant_id, estado_operativo);
CREATE INDEX IF NOT EXISTS idx_operacion_chofer_tenant_estado ON operacion.chofer (tenant_id, estado);
CREATE INDEX IF NOT EXISTS idx_operacion_turno_fecha ON operacion.turno (tenant_id, distrito_id, fecha);
CREATE INDEX IF NOT EXISTS idx_operacion_ruta_fecha_estado ON operacion.ruta (tenant_id, distrito_id, fecha, estado);
CREATE INDEX IF NOT EXISTS idx_operacion_ruta_version_ruta ON operacion.ruta_version (ruta_id, version DESC);
CREATE INDEX IF NOT EXISTS idx_operacion_ruta_parada_version ON operacion.ruta_parada (ruta_version_id, orden);
CREATE INDEX IF NOT EXISTS idx_operacion_horario_zona ON operacion.horario_recoleccion (tenant_id, zona_id, dia_semana);

CREATE INDEX IF NOT EXISTS idx_operacion_distrito_limite_gist ON operacion.distrito USING GIST (limite);
CREATE INDEX IF NOT EXISTS idx_operacion_zona_poligono_gist ON operacion.zona USING GIST (poligono);
CREATE INDEX IF NOT EXISTS idx_operacion_zona_punto_gist ON operacion.zona USING GIST (punto_referencia);
CREATE INDEX IF NOT EXISTS idx_operacion_deposito_ubicacion_gist ON operacion.deposito USING GIST (ubicacion);
CREATE INDEX IF NOT EXISTS idx_operacion_contenedor_ubicacion_gist ON operacion.contenedor USING GIST (ubicacion);
CREATE INDEX IF NOT EXISTS idx_operacion_ruta_geometria_gist ON operacion.ruta USING GIST (geometria);
CREATE INDEX IF NOT EXISTS idx_operacion_ruta_version_geometria_gist ON operacion.ruta_version USING GIST (geometria);
CREATE INDEX IF NOT EXISTS idx_operacion_ruta_evento_posicion_gist ON operacion.ruta_evento USING GIST (posicion);
