-- V005: Schemas optimizacion, prediccion, kpi y auditoria

CREATE TABLE IF NOT EXISTS optimizacion.solicitud_optimizacion (
    solicitud_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id                 UUID NOT NULL,
    distrito_id_externo       UUID NOT NULL,
    fecha_operacion           DATE NOT NULL,
    tipo_solicitud            VARCHAR(30) NOT NULL DEFAULT 'INICIAL',
    ruta_id_externo           UUID,
    alerta_id_externo         UUID,
    estado                    VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    solicitado_por_usuario_id UUID,
    solicitado_en             TIMESTAMPTZ NOT NULL DEFAULT now(),
    resuelto_en               TIMESTAMPTZ,
    tiempo_resolucion_ms      INTEGER,
    mensaje_error             TEXT,
    CONSTRAINT ck_opt_solicitud_tipo CHECK (tipo_solicitud IN ('INICIAL','REOPTIMIZACION')),
    CONSTRAINT ck_opt_solicitud_estado CHECK (estado IN ('PENDIENTE','PROCESANDO','RESUELTA','FALLIDA')),
    CONSTRAINT ck_opt_solicitud_tiempo CHECK (tiempo_resolucion_ms IS NULL OR tiempo_resolucion_ms >= 0)
);

CREATE TABLE IF NOT EXISTS optimizacion.solicitud_unidad (
    solicitud_unidad_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    solicitud_id               UUID NOT NULL,
    unidad_id_externo          UUID NOT NULL,
    capacidad_m3               NUMERIC(8,2) NOT NULL,
    capacidad_kg               NUMERIC(10,2) NOT NULL,
    deposito_inicio_id_externo UUID NOT NULL,
    deposito_fin_id_externo    UUID NOT NULL,
    disponible_desde           TIME NOT NULL,
    disponible_hasta           TIME NOT NULL,
    CONSTRAINT fk_opt_solicitud_unidad_solicitud FOREIGN KEY (solicitud_id) REFERENCES optimizacion.solicitud_optimizacion(solicitud_id),
    CONSTRAINT ck_opt_solicitud_unidad_cap_m3 CHECK (capacidad_m3 > 0),
    CONSTRAINT ck_opt_solicitud_unidad_cap_kg CHECK (capacidad_kg > 0),
    CONSTRAINT ck_opt_solicitud_unidad_hora CHECK (disponible_hasta > disponible_desde)
);

CREATE TABLE IF NOT EXISTS optimizacion.solicitud_zona (
    solicitud_zona_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    solicitud_id         UUID NOT NULL,
    zona_id_externo      UUID NOT NULL,
    ubicacion_referencia GEOMETRY(POINT, 4326) NOT NULL,
    demanda_kg           NUMERIC(10,2) NOT NULL DEFAULT 0,
    demanda_m3           NUMERIC(8,2) NOT NULL DEFAULT 0,
    ventana_inicio       TIME,
    ventana_fin          TIME,
    prioridad            INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT fk_opt_solicitud_zona_solicitud FOREIGN KEY (solicitud_id) REFERENCES optimizacion.solicitud_optimizacion(solicitud_id),
    CONSTRAINT ck_opt_solicitud_zona_demanda_kg CHECK (demanda_kg >= 0),
    CONSTRAINT ck_opt_solicitud_zona_demanda_m3 CHECK (demanda_m3 >= 0),
    CONSTRAINT ck_opt_solicitud_zona_prioridad CHECK (prioridad >= 1)
);

CREATE TABLE IF NOT EXISTS optimizacion.parametro_solver (
    parametro_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    solicitud_id             UUID NOT NULL,
    algoritmo                VARCHAR(80) NOT NULL DEFAULT 'OR_TOOLS_CVRPTW',
    tiempo_limite_s          INTEGER NOT NULL DEFAULT 30,
    objetivo                 VARCHAR(80) NOT NULL DEFAULT 'MIN_DISTANCIA',
    permite_relajar_ventanas BOOLEAN NOT NULL DEFAULT TRUE,
    penalidad_alerta_critica INTEGER NOT NULL DEFAULT 1000,
    creado_en                TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_opt_parametro_solver_solicitud FOREIGN KEY (solicitud_id) REFERENCES optimizacion.solicitud_optimizacion(solicitud_id),
    CONSTRAINT uq_opt_parametro_solver_solicitud UNIQUE (solicitud_id),
    CONSTRAINT ck_opt_parametro_solver_tiempo CHECK (tiempo_limite_s > 0),
    CONSTRAINT ck_opt_parametro_solver_objetivo CHECK (objetivo IN ('MIN_DISTANCIA','MIN_TIEMPO','BALANCEAR_CARGA'))
);

CREATE TABLE IF NOT EXISTS optimizacion.matriz_costo (
    matriz_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    distrito_id_externo UUID NOT NULL,
    fuente              VARCHAR(30) NOT NULL DEFAULT 'OSRM',
    hash_puntos         TEXT NOT NULL,
    creado_en           TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_opt_matriz_hash UNIQUE (tenant_id, distrito_id_externo, hash_puntos),
    CONSTRAINT ck_opt_matriz_fuente CHECK (fuente IN ('OSRM','MANUAL','CACHE'))
);

CREATE TABLE IF NOT EXISTS optimizacion.matriz_costo_detalle (
    matriz_detalle_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    matriz_id         UUID NOT NULL,
    origen_ref        UUID NOT NULL,
    destino_ref       UUID NOT NULL,
    distancia_m       NUMERIC(12,2) NOT NULL,
    duracion_s        INTEGER NOT NULL,
    CONSTRAINT fk_opt_matriz_detalle_matriz FOREIGN KEY (matriz_id) REFERENCES optimizacion.matriz_costo(matriz_id),
    CONSTRAINT uq_opt_matriz_detalle UNIQUE (matriz_id, origen_ref, destino_ref),
    CONSTRAINT ck_opt_matriz_detalle_distancia CHECK (distancia_m >= 0),
    CONSTRAINT ck_opt_matriz_detalle_duracion CHECK (duracion_s >= 0)
);

CREATE TABLE IF NOT EXISTS optimizacion.solucion_optimizacion (
    solucion_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    solicitud_id              UUID NOT NULL,
    estado                    VARCHAR(30) NOT NULL,
    distancia_total_m         NUMERIC(12,2) DEFAULT 0,
    duracion_total_s          INTEGER DEFAULT 0,
    carga_total_kg            NUMERIC(12,2) DEFAULT 0,
    cantidad_unidades_usadas  INTEGER DEFAULT 0,
    mensaje_solver            TEXT,
    creado_en                 TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_opt_solucion_solicitud FOREIGN KEY (solicitud_id) REFERENCES optimizacion.solicitud_optimizacion(solicitud_id),
    CONSTRAINT ck_opt_solucion_estado CHECK (estado IN ('FACTIBLE','NO_FACTIBLE','PARCIAL')),
    CONSTRAINT ck_opt_solucion_distancia CHECK (distancia_total_m >= 0),
    CONSTRAINT ck_opt_solucion_duracion CHECK (duracion_total_s >= 0),
    CONSTRAINT ck_opt_solucion_carga CHECK (carga_total_kg >= 0)
);

CREATE TABLE IF NOT EXISTS optimizacion.solucion_unidad (
    solucion_unidad_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    solucion_id        UUID NOT NULL,
    unidad_id_externo  UUID NOT NULL,
    distancia_m        NUMERIC(12,2) DEFAULT 0,
    duracion_s         INTEGER DEFAULT 0,
    carga_kg           NUMERIC(12,2) DEFAULT 0,
    geometria          GEOMETRY(LINESTRING, 4326),
    CONSTRAINT fk_opt_solucion_unidad_solucion FOREIGN KEY (solucion_id) REFERENCES optimizacion.solucion_optimizacion(solucion_id),
    CONSTRAINT ck_opt_solucion_unidad_distancia CHECK (distancia_m >= 0),
    CONSTRAINT ck_opt_solucion_unidad_duracion CHECK (duracion_s >= 0),
    CONSTRAINT ck_opt_solucion_unidad_carga CHECK (carga_kg >= 0)
);

CREATE TABLE IF NOT EXISTS optimizacion.solucion_parada (
    solucion_parada_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    solucion_unidad_id UUID NOT NULL,
    zona_id_externo    UUID NOT NULL,
    orden              INTEGER NOT NULL,
    eta                TIMESTAMPTZ,
    demanda_kg         NUMERIC(10,2) DEFAULT 0,
    carga_acumulada_kg NUMERIC(10,2) DEFAULT 0,
    ubicacion          GEOMETRY(POINT, 4326) NOT NULL,
    CONSTRAINT fk_opt_solucion_parada_unidad FOREIGN KEY (solucion_unidad_id) REFERENCES optimizacion.solucion_unidad(solucion_unidad_id),
    CONSTRAINT uq_opt_solucion_parada_orden UNIQUE (solucion_unidad_id, orden),
    CONSTRAINT ck_opt_solucion_parada_orden CHECK (orden > 0),
    CONSTRAINT ck_opt_solucion_parada_demanda CHECK (demanda_kg >= 0),
    CONSTRAINT ck_opt_solucion_parada_carga CHECK (carga_acumulada_kg >= 0)
);

-- Indices optimizacion
CREATE INDEX IF NOT EXISTS idx_opt_solicitud_tenant_fecha ON optimizacion.solicitud_optimizacion (tenant_id, distrito_id_externo, fecha_operacion DESC);
CREATE INDEX IF NOT EXISTS idx_opt_solicitud_estado ON optimizacion.solicitud_optimizacion (estado);
CREATE INDEX IF NOT EXISTS idx_opt_solicitud_unidad_solicitud ON optimizacion.solicitud_unidad (solicitud_id);
CREATE INDEX IF NOT EXISTS idx_opt_solicitud_zona_solicitud ON optimizacion.solicitud_zona (solicitud_id);
CREATE INDEX IF NOT EXISTS idx_opt_solicitud_zona_ubicacion_gist ON optimizacion.solicitud_zona USING GIST (ubicacion_referencia);
CREATE INDEX IF NOT EXISTS idx_opt_matriz_detalle_matriz ON optimizacion.matriz_costo_detalle (matriz_id);
CREATE INDEX IF NOT EXISTS idx_opt_solucion_solicitud ON optimizacion.solucion_optimizacion (solicitud_id);
CREATE INDEX IF NOT EXISTS idx_opt_solucion_unidad_solucion ON optimizacion.solucion_unidad (solucion_id);
CREATE INDEX IF NOT EXISTS idx_opt_solucion_unidad_geometria_gist ON optimizacion.solucion_unidad USING GIST (geometria);
CREATE INDEX IF NOT EXISTS idx_opt_solucion_parada_unidad ON optimizacion.solucion_parada (solucion_unidad_id, orden);
CREATE INDEX IF NOT EXISTS idx_opt_solucion_parada_ubicacion_gist ON optimizacion.solucion_parada USING GIST (ubicacion);

-- Schema prediccion

CREATE TABLE IF NOT EXISTS prediccion.modelo_prediccion (
    modelo_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id      UUID NOT NULL,
    nombre         VARCHAR(120) NOT NULL,
    version        VARCHAR(30) NOT NULL,
    metodo         VARCHAR(50) NOT NULL,
    metrica_nombre VARCHAR(50),
    metrica_valor  NUMERIC(10,4),
    ruta_artefacto TEXT,
    estado         VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    entrenado_en   TIMESTAMPTZ,
    creado_en      TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_pred_modelo UNIQUE (tenant_id, nombre, version),
    CONSTRAINT ck_pred_modelo_metodo CHECK (metodo IN ('PROMEDIO_HISTORICO','XGBOOST','PROPHET','HIBRIDO')),
    CONSTRAINT ck_pred_modelo_estado CHECK (estado IN ('ACTIVO','INACTIVO','ARCHIVADO'))
);

CREATE TABLE IF NOT EXISTS prediccion.historico_generacion_zona (
    historico_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL,
    zona_id_externo UUID NOT NULL,
    fecha           DATE NOT NULL,
    turno           VARCHAR(30) NOT NULL,
    kg_recolectados NUMERIC(10,2) NOT NULL,
    fuente          VARCHAR(30) NOT NULL DEFAULT 'MANUAL',
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_pred_hist_zona_fecha_turno UNIQUE (tenant_id, zona_id_externo, fecha, turno),
    CONSTRAINT ck_pred_hist_turno CHECK (turno IN ('MANANA','TARDE','NOCHE')),
    CONSTRAINT ck_pred_hist_kg CHECK (kg_recolectados >= 0),
    CONSTRAINT ck_pred_hist_fuente CHECK (fuente IN ('PESAJE','EXCEL','MANUAL','ETL'))
);

CREATE TABLE IF NOT EXISTS prediccion.prediccion_generacion (
    prediccion_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id      UUID NOT NULL,
    zona_id_externo UUID NOT NULL,
    modelo_id      UUID,
    fecha_predicha DATE NOT NULL,
    turno          VARCHAR(30) NOT NULL,
    kg_estimados   NUMERIC(10,2) NOT NULL,
    metodo         VARCHAR(50) NOT NULL DEFAULT 'PROMEDIO_HISTORICO',
    confianza      NUMERIC(5,2),
    creado_en      TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_pred_prediccion_modelo FOREIGN KEY (modelo_id) REFERENCES prediccion.modelo_prediccion(modelo_id),
    CONSTRAINT uq_pred_prediccion UNIQUE (tenant_id, zona_id_externo, fecha_predicha, turno, metodo),
    CONSTRAINT ck_pred_prediccion_turno CHECK (turno IN ('MANANA','TARDE','NOCHE')),
    CONSTRAINT ck_pred_prediccion_kg CHECK (kg_estimados >= 0),
    CONSTRAINT ck_pred_prediccion_metodo CHECK (metodo IN ('PROMEDIO_HISTORICO','ML')),
    CONSTRAINT ck_pred_prediccion_confianza CHECK (confianza IS NULL OR (confianza >= 0 AND confianza <= 100))
);

CREATE INDEX IF NOT EXISTS idx_pred_hist_zona_fecha ON prediccion.historico_generacion_zona (tenant_id, zona_id_externo, fecha DESC);
CREATE INDEX IF NOT EXISTS idx_pred_prediccion_zona_fecha ON prediccion.prediccion_generacion (tenant_id, zona_id_externo, fecha_predicha DESC);

-- Schema kpi

CREATE TABLE IF NOT EXISTS kpi.resumen_operativo_diario (
    resumen_id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id                     UUID NOT NULL,
    distrito_id_externo           UUID NOT NULL,
    fecha                         DATE NOT NULL,
    km_programados                NUMERIC(12,2) DEFAULT 0,
    km_recorridos                 NUMERIC(12,2) DEFAULT 0,
    toneladas_recolectadas        NUMERIC(12,2) DEFAULT 0,
    cobertura_porcentaje          NUMERIC(5,2) DEFAULT 0,
    alertas_registradas           INTEGER DEFAULT 0,
    alertas_atendidas             INTEGER DEFAULT 0,
    tiempo_respuesta_promedio_min NUMERIC(10,2),
    creado_en                     TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_kpi_resumen_diario UNIQUE (tenant_id, distrito_id_externo, fecha),
    CONSTRAINT ck_kpi_resumen_km_programados CHECK (km_programados >= 0),
    CONSTRAINT ck_kpi_resumen_km_recorridos CHECK (km_recorridos >= 0),
    CONSTRAINT ck_kpi_resumen_ton CHECK (toneladas_recolectadas >= 0),
    CONSTRAINT ck_kpi_resumen_cobertura CHECK (cobertura_porcentaje >= 0 AND cobertura_porcentaje <= 100),
    CONSTRAINT ck_kpi_resumen_alertas CHECK (alertas_registradas >= 0 AND alertas_atendidas >= 0)
);

CREATE TABLE IF NOT EXISTS kpi.kpi_ruta (
    kpi_ruta_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id               UUID NOT NULL,
    ruta_id_externo         UUID NOT NULL,
    fecha                   DATE NOT NULL,
    distancia_planificada_m NUMERIC(12,2) DEFAULT 0,
    distancia_real_m        NUMERIC(12,2) DEFAULT 0,
    duracion_planificada_s  INTEGER DEFAULT 0,
    duracion_real_s         INTEGER DEFAULT 0,
    zonas_programadas       INTEGER DEFAULT 0,
    zonas_atendidas         INTEGER DEFAULT 0,
    cumplimiento_porcentaje NUMERIC(5,2) DEFAULT 0,
    km_por_tonelada         NUMERIC(10,2),
    creado_en               TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_kpi_ruta UNIQUE (tenant_id, ruta_id_externo),
    CONSTRAINT ck_kpi_ruta_cumplimiento CHECK (cumplimiento_porcentaje >= 0 AND cumplimiento_porcentaje <= 100)
);

CREATE TABLE IF NOT EXISTS kpi.kpi_unidad (
    kpi_unidad_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id               UUID NOT NULL,
    unidad_id_externo       UUID NOT NULL,
    fecha                   DATE NOT NULL,
    km_recorridos           NUMERIC(12,2) DEFAULT 0,
    horas_operacion         NUMERIC(10,2) DEFAULT 0,
    toneladas_recolectadas  NUMERIC(12,2) DEFAULT 0,
    consumo_estimado_litros NUMERIC(12,2) DEFAULT 0,
    creado_en               TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_kpi_unidad_fecha UNIQUE (tenant_id, unidad_id_externo, fecha)
);

CREATE TABLE IF NOT EXISTS kpi.kpi_zona (
    kpi_zona_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id            UUID NOT NULL,
    zona_id_externo      UUID NOT NULL,
    fecha                DATE NOT NULL,
    veces_programada     INTEGER DEFAULT 0,
    veces_atendida       INTEGER DEFAULT 0,
    kg_recolectados      NUMERIC(12,2) DEFAULT 0,
    cobertura_porcentaje NUMERIC(5,2) DEFAULT 0,
    creado_en            TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_kpi_zona_fecha UNIQUE (tenant_id, zona_id_externo, fecha),
    CONSTRAINT ck_kpi_zona_cobertura CHECK (cobertura_porcentaje >= 0 AND cobertura_porcentaje <= 100)
);

CREATE TABLE IF NOT EXISTS kpi.kpi_alerta (
    kpi_alerta_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id            UUID NOT NULL,
    alerta_id_externo    UUID NOT NULL,
    zona_id_externo      UUID,
    registrada_en        TIMESTAMPTZ NOT NULL,
    atendida_en          TIMESTAMPTZ,
    tiempo_respuesta_min NUMERIC(10,2),
    fue_critica          BOOLEAN NOT NULL DEFAULT FALSE,
    incluida_en_ruta     BOOLEAN NOT NULL DEFAULT FALSE,
    creado_en            TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_kpi_alerta UNIQUE (tenant_id, alerta_id_externo),
    CONSTRAINT ck_kpi_alerta_tiempo CHECK (tiempo_respuesta_min IS NULL OR tiempo_respuesta_min >= 0)
);

CREATE INDEX IF NOT EXISTS idx_kpi_resumen_fecha ON kpi.resumen_operativo_diario (tenant_id, distrito_id_externo, fecha DESC);
CREATE INDEX IF NOT EXISTS idx_kpi_ruta_fecha ON kpi.kpi_ruta (tenant_id, fecha DESC);
CREATE INDEX IF NOT EXISTS idx_kpi_unidad_fecha ON kpi.kpi_unidad (tenant_id, unidad_id_externo, fecha DESC);
CREATE INDEX IF NOT EXISTS idx_kpi_zona_fecha ON kpi.kpi_zona (tenant_id, zona_id_externo, fecha DESC);
CREATE INDEX IF NOT EXISTS idx_kpi_alerta_zona ON kpi.kpi_alerta (tenant_id, zona_id_externo);

-- Schema auditoria

CREATE TABLE IF NOT EXISTS auditoria.evento_auditoria (
    evento_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id     UUID NOT NULL,
    usuario_id    UUID,
    modulo        VARCHAR(80) NOT NULL,
    accion        VARCHAR(100) NOT NULL,
    entidad       VARCHAR(100) NOT NULL,
    entidad_id    UUID,
    datos_antes   JSONB,
    datos_despues JSONB,
    creado_en     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS auditoria.outbox_event (
    outbox_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id      UUID NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id   UUID NOT NULL,
    event_type     VARCHAR(100) NOT NULL,
    payload        JSONB NOT NULL,
    estado         VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    creado_en      TIMESTAMPTZ NOT NULL DEFAULT now(),
    publicado_en   TIMESTAMPTZ,
    error_mensaje  TEXT,
    CONSTRAINT ck_auditoria_outbox_estado CHECK (estado IN ('PENDIENTE','PUBLICADO','ERROR'))
);

CREATE INDEX IF NOT EXISTS idx_auditoria_evento_tenant_fecha ON auditoria.evento_auditoria (tenant_id, creado_en DESC);
CREATE INDEX IF NOT EXISTS idx_auditoria_evento_entidad ON auditoria.evento_auditoria (entidad, entidad_id);
CREATE INDEX IF NOT EXISTS idx_auditoria_outbox_estado ON auditoria.outbox_event (estado, creado_en);
CREATE INDEX IF NOT EXISTS idx_auditoria_outbox_event_type ON auditoria.outbox_event (event_type, creado_en DESC);
