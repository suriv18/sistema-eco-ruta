-- V002: Schema auth

CREATE TABLE IF NOT EXISTS auth.tenant (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre         VARCHAR(150) NOT NULL,
    ruc            VARCHAR(11),
    estado         VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    creado_en      TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por     UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT uq_auth_tenant_ruc UNIQUE (ruc),
    CONSTRAINT ck_auth_tenant_estado CHECK (estado IN ('ACTIVO','INACTIVO'))
);

CREATE TABLE IF NOT EXISTS auth.usuario (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL,
    nombres         VARCHAR(120) NOT NULL,
    apellidos       VARCHAR(120) NOT NULL,
    email           VARCHAR(150) NOT NULL,
    username        VARCHAR(80) NOT NULL,
    password_hash   TEXT NOT NULL,
    telefono        VARCHAR(20),
    estado          VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    ultimo_login_en TIMESTAMPTZ,
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT fk_auth_usuario_tenant FOREIGN KEY (tenant_id) REFERENCES auth.tenant(id),
    CONSTRAINT uq_auth_usuario_email UNIQUE (email),
    CONSTRAINT uq_auth_usuario_username UNIQUE (username),
    CONSTRAINT ck_auth_usuario_estado CHECK (estado IN ('ACTIVO','BLOQUEADO','INACTIVO'))
);

CREATE TABLE IF NOT EXISTS auth.rol (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo          VARCHAR(50) NOT NULL,
    nombre          VARCHAR(100) NOT NULL,
    descripcion     TEXT,
    estado          VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT uq_auth_rol_codigo UNIQUE (codigo),
    CONSTRAINT ck_auth_rol_estado CHECK (estado IN ('ACTIVO','INACTIVO'))
);

CREATE TABLE IF NOT EXISTS auth.permiso (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo          VARCHAR(100) NOT NULL,
    modulo          VARCHAR(80) NOT NULL,
    descripcion     TEXT,
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT uq_auth_permiso_codigo UNIQUE (codigo)
);

CREATE TABLE IF NOT EXISTS auth.usuario_rol (
    usuario_id UUID NOT NULL,
    rol_id     UUID NOT NULL,
    creado_en  TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_auth_usuario_rol_usuario FOREIGN KEY (usuario_id) REFERENCES auth.usuario(id),
    CONSTRAINT fk_auth_usuario_rol_rol FOREIGN KEY (rol_id) REFERENCES auth.rol(id)
);

CREATE TABLE IF NOT EXISTS auth.rol_permiso (
    rol_id     UUID NOT NULL,
    permiso_id UUID NOT NULL,
    creado_en  TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (rol_id, permiso_id),
    CONSTRAINT fk_auth_rol_permiso_rol FOREIGN KEY (rol_id) REFERENCES auth.rol(id),
    CONSTRAINT fk_auth_rol_permiso_permiso FOREIGN KEY (permiso_id) REFERENCES auth.permiso(id)
);

CREATE TABLE IF NOT EXISTS auth.refresh_token (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id      UUID NOT NULL,
    token_hash      TEXT NOT NULL,
    expira_en       TIMESTAMPTZ NOT NULL,
    revocado        BOOLEAN NOT NULL DEFAULT FALSE,
    revocado_en     TIMESTAMPTZ,
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por      UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en  TIMESTAMPTZ,
    actualizado_por UUID,
    CONSTRAINT fk_auth_refresh_usuario FOREIGN KEY (usuario_id) REFERENCES auth.usuario(id)
);

CREATE TABLE IF NOT EXISTS auth.login_auditoria (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id        UUID,
    usuario_id       UUID,
    username_intento VARCHAR(150),
    ip_origen        VARCHAR(45),
    user_agent       TEXT,
    exitoso          BOOLEAN NOT NULL,
    motivo_fallo     TEXT,
    creado_en        TIMESTAMPTZ NOT NULL DEFAULT now(),
    creado_por       UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000001',
    actualizado_en   TIMESTAMPTZ,
    actualizado_por  UUID,
    CONSTRAINT fk_auth_login_usuario FOREIGN KEY (usuario_id) REFERENCES auth.usuario(id)
);

CREATE INDEX IF NOT EXISTS idx_auth_usuario_tenant ON auth.usuario (tenant_id);
CREATE INDEX IF NOT EXISTS idx_auth_usuario_estado ON auth.usuario (estado);
CREATE INDEX IF NOT EXISTS idx_auth_refresh_usuario ON auth.refresh_token (usuario_id);
CREATE INDEX IF NOT EXISTS idx_auth_login_usuario_fecha ON auth.login_auditoria (usuario_id, creado_en DESC);
