-- =====================================================================
-- SISTEMA DE INFORMACION WEB: FAUNA EN PELIGRO DE EXTINCION
-- Universidad de El Salvador - Facultad Multidisciplinaria Paracentral
-- Esquema inicial - PostgreSQL 14+
-- =====================================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =====================================================================
-- 1. SEGURIDAD  (RF-001 a RF-006, RN-004, RN-005)
-- =====================================================================

CREATE TABLE usuario (
    id                    BIGSERIAL PRIMARY KEY,
    uuid                  UUID         NOT NULL DEFAULT gen_random_uuid(),
    nombre_usuario        VARCHAR(60)  NOT NULL,
    correo                VARCHAR(150) NOT NULL,
    contrasena_hash       VARCHAR(255) NOT NULL,
    nombres               VARCHAR(100) NOT NULL,
    apellidos             VARCHAR(100) NOT NULL,
    activo                BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_ultimo_acceso   TIMESTAMP,
    fecha_creacion        TIMESTAMP    NOT NULL DEFAULT now(),
    fecha_actualizacion   TIMESTAMP,
    CONSTRAINT uk_usuario_nombre UNIQUE (nombre_usuario),
    CONSTRAINT uk_usuario_correo UNIQUE (correo),
    CONSTRAINT uk_usuario_uuid   UNIQUE (uuid)
);

CREATE TABLE rol (
    id            BIGSERIAL PRIMARY KEY,
    nombre        VARCHAR(60)  NOT NULL,
    descripcion   VARCHAR(255),
    activo        BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_rol_nombre UNIQUE (nombre)
);

CREATE TABLE permiso (
    id            BIGSERIAL PRIMARY KEY,
    codigo        VARCHAR(80)  NOT NULL,
    descripcion   VARCHAR(255),
    modulo        VARCHAR(60),
    CONSTRAINT uk_permiso_codigo UNIQUE (codigo)
);

CREATE TABLE usuario_rol (
    usuario_id  BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    rol_id      BIGINT NOT NULL REFERENCES rol(id)     ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, rol_id)
);

CREATE TABLE rol_permiso (
    rol_id      BIGINT NOT NULL REFERENCES rol(id)     ON DELETE CASCADE,
    permiso_id  BIGINT NOT NULL REFERENCES permiso(id) ON DELETE CASCADE,
    PRIMARY KEY (rol_id, permiso_id)
);

-- =====================================================================
-- 2. TAXONOMIA  (RF-026 a RF-029, RN-019)
-- Arbol unico auto-referenciado: sirve para flora (division) y fauna (filo)
-- =====================================================================

CREATE TABLE taxon (
    id                   BIGSERIAL PRIMARY KEY,
    nombre               VARCHAR(120) NOT NULL,
    nivel                VARCHAR(20)  NOT NULL,   -- REINO, FILO, DIVISION, CLASE, ORDEN, FAMILIA, GENERO, ESPECIE
    autor                VARCHAR(150),
    descripcion          VARCHAR(500),
    padre_id             BIGINT REFERENCES taxon(id),
    activo               BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion       TIMESTAMP    NOT NULL DEFAULT now(),
    fecha_actualizacion  TIMESTAMP,
    CONSTRAINT ck_taxon_nivel CHECK (nivel IN
        ('REINO','FILO','DIVISION','CLASE','ORDEN','FAMILIA','GENERO','ESPECIE','SUBESPECIE')),
    CONSTRAINT uk_taxon_nombre_nivel_padre UNIQUE (nombre, nivel, padre_id)
);
CREATE INDEX ix_taxon_padre ON taxon(padre_id);
CREATE INDEX ix_taxon_nivel ON taxon(nivel);

-- =====================================================================
-- 3. CATALOGOS ADMINISTRABLES  (RN-013, RN-025)
-- =====================================================================

CREATE TABLE estado_conservacion (
    id              BIGSERIAL PRIMARY KEY,
    codigo          VARCHAR(20)  NOT NULL,       -- EX, EW, CR, EN, VU, NT, LC, DD, NE
    nombre          VARCHAR(80)  NOT NULL,
    descripcion     VARCHAR(500),
    orden_gravedad  INTEGER      NOT NULL DEFAULT 0,
    autoridad       VARCHAR(150),                -- UICN, MARN, etc.
    activo          BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_estado_conservacion_codigo UNIQUE (codigo)
);

CREATE TABLE amenaza (
    id            BIGSERIAL PRIMARY KEY,
    codigo        VARCHAR(40)  NOT NULL,
    nombre        VARCHAR(150) NOT NULL,
    descripcion   VARCHAR(500),
    categoria     VARCHAR(80),
    activo        BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_amenaza_codigo UNIQUE (codigo)
);

CREATE TABLE medida_conservacion (
    id            BIGSERIAL PRIMARY KEY,
    codigo        VARCHAR(40)  NOT NULL,
    nombre        VARCHAR(150) NOT NULL,
    descripcion   VARCHAR(500),
    activo        BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_medida_codigo UNIQUE (codigo)
);

CREATE TABLE tipo_habitat (
    id            BIGSERIAL PRIMARY KEY,
    codigo        VARCHAR(40)  NOT NULL,
    nombre        VARCHAR(150) NOT NULL,
    descripcion   VARCHAR(500),
    activo        BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_tipo_habitat_codigo UNIQUE (codigo)
);

CREATE TABLE tipo_evento (
    id            BIGSERIAL PRIMARY KEY,
    codigo        VARCHAR(40)  NOT NULL,
    nombre        VARCHAR(150) NOT NULL,
    descripcion   VARCHAR(500),
    activo        BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_tipo_evento_codigo UNIQUE (codigo)
);

-- =====================================================================
-- 4. FUENTES CIENTIFICAS  (RF-046, RF-047, RN-020)
-- =====================================================================

CREATE TABLE fuente (
    id                   BIGSERIAL PRIMARY KEY,
    titulo               VARCHAR(300) NOT NULL,
    autores              VARCHAR(300),
    institucion          VARCHAR(200),
    referencia           VARCHAR(500),
    url                  VARCHAR(500),
    tipo                 VARCHAR(40),   -- LIBRO, ARTICULO, SITIO_WEB, INFORME, HERBARIO, ENTREVISTA
    fecha_publicacion    DATE,
    fecha_consulta       DATE,
    activo               BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion       TIMESTAMP    NOT NULL DEFAULT now(),
    fecha_actualizacion  TIMESTAMP
);
CREATE INDEX ix_fuente_titulo ON fuente(lower(titulo));

-- =====================================================================
-- 5. ESPECIE (nucleo comun flora + fauna)  (RN-001, RN-006)
-- =====================================================================

CREATE TABLE especie (
    id                        BIGSERIAL PRIMARY KEY,
    uuid                      UUID         NOT NULL DEFAULT gen_random_uuid(),
    nombre_cientifico         VARCHAR(200) NOT NULL,
    autor_cientifico          VARCHAR(150),
    descripcion_general       TEXT,
    observaciones             TEXT,
    -- Caracteristicas propias de fauna (RF-021 a RF-023)
    tamano                    VARCHAR(150),
    peso                      VARCHAR(150),
    coloracion                TEXT,
    cobertura_corporal        VARCHAR(150),
    rasgos_fisicos            TEXT,
    alimentacion              TEXT,
    reproduccion              TEXT,
    comportamiento            TEXT,
    taxon_id                  BIGINT REFERENCES taxon(id),
    estado_conservacion_id    BIGINT REFERENCES estado_conservacion(id),
    estado_registro           VARCHAR(30)  NOT NULL DEFAULT 'BORRADOR',
    usuario_registro_id       BIGINT REFERENCES usuario(id),
    usuario_validacion_id     BIGINT REFERENCES usuario(id),
    fecha_validacion          TIMESTAMP,
    fecha_publicacion         TIMESTAMP,
    activo                    BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion            TIMESTAMP    NOT NULL DEFAULT now(),
    fecha_actualizacion       TIMESTAMP,
    CONSTRAINT uk_especie_uuid   UNIQUE (uuid),
    CONSTRAINT uk_especie_nombre UNIQUE (nombre_cientifico),
    CONSTRAINT ck_especie_estado CHECK (estado_registro IN
        ('BORRADOR','PENDIENTE_VALIDACION','VALIDADO','PUBLICADO','RECHAZADO','EN_CORRECCION'))
);
CREATE INDEX ix_especie_nombre_cientifico ON especie(lower(nombre_cientifico));
CREATE INDEX ix_especie_estado_registro   ON especie(estado_registro);
CREATE INDEX ix_especie_conservacion      ON especie(estado_conservacion_id);

CREATE TABLE nombre_comun (
    id            BIGSERIAL PRIMARY KEY,
    especie_id    BIGINT       NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    nombre        VARCHAR(150) NOT NULL,
    idioma        VARCHAR(60),
    region        VARCHAR(120),
    principal     BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_nombre_comun UNIQUE (especie_id, nombre)
);
CREATE INDEX ix_nombre_comun_nombre ON nombre_comun(lower(nombre));

-- =====================================================================
-- 6. CONSERVACION  (RF-030 a RF-035, RN-012)
-- =====================================================================

CREATE TABLE historial_conservacion (
    id                        BIGSERIAL PRIMARY KEY,
    especie_id                BIGINT    NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    estado_conservacion_id    BIGINT    NOT NULL REFERENCES estado_conservacion(id),
    fecha_asignacion          DATE      NOT NULL,
    fuente_id                 BIGINT REFERENCES fuente(id),
    usuario_id                BIGINT REFERENCES usuario(id),
    observaciones             TEXT,
    vigente                   BOOLEAN   NOT NULL DEFAULT TRUE,
    fecha_creacion            TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX ix_hist_conservacion_especie ON historial_conservacion(especie_id, fecha_asignacion DESC);

CREATE TABLE especie_amenaza (
    id              BIGSERIAL PRIMARY KEY,
    especie_id      BIGINT NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    amenaza_id      BIGINT NOT NULL REFERENCES amenaza(id),
    nivel_impacto   VARCHAR(30),      -- BAJO, MEDIO, ALTO, CRITICO
    descripcion     TEXT,
    fuente_id       BIGINT REFERENCES fuente(id),
    fecha_registro  DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT uk_especie_amenaza UNIQUE (especie_id, amenaza_id)
);

CREATE TABLE especie_medida (
    id                      BIGSERIAL PRIMARY KEY,
    especie_id              BIGINT NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    medida_conservacion_id  BIGINT NOT NULL REFERENCES medida_conservacion(id),
    estado_implementacion   VARCHAR(40),   -- PROPUESTA, EN_EJECUCION, FINALIZADA
    descripcion             TEXT,
    fuente_id               BIGINT REFERENCES fuente(id),
    fecha_registro          DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT uk_especie_medida UNIQUE (especie_id, medida_conservacion_id)
);

-- =====================================================================
-- 7. GEOGRAFIA: habitat / distribucion / avistamiento  (RF-036 a RF-041, RN-009)
-- =====================================================================

CREATE TABLE especie_habitat (
    id                      BIGSERIAL PRIMARY KEY,
    especie_id              BIGINT NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    tipo_habitat_id         BIGINT NOT NULL REFERENCES tipo_habitat(id),
    descripcion             TEXT,
    altitud_min_msnm        INTEGER,
    altitud_max_msnm        INTEGER,
    CONSTRAINT uk_especie_habitat UNIQUE (especie_id, tipo_habitat_id)
);

CREATE TABLE distribucion (
    id                    BIGSERIAL PRIMARY KEY,
    especie_id            BIGINT       NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    tipo_distribucion     VARCHAR(15)  NOT NULL DEFAULT 'ACTUAL',  -- ACTUAL | HISTORICA
    pais                  VARCHAR(80)  NOT NULL DEFAULT 'El Salvador',
    departamento          VARCHAR(80),
    municipio             VARCHAR(80),
    distrito              VARCHAR(80),
    sitio                 VARCHAR(200),
    latitud               NUMERIC(9,6),
    longitud              NUMERIC(9,6),
    nivel_sensibilidad    VARCHAR(20)  NOT NULL DEFAULT 'PUBLICO', -- PUBLICO | RESTRINGIDO | CONFIDENCIAL
    descripcion           TEXT,
    fuente_id             BIGINT REFERENCES fuente(id),
    fecha_creacion        TIMESTAMP    NOT NULL DEFAULT now(),
    fecha_actualizacion   TIMESTAMP,
    CONSTRAINT ck_distribucion_tipo CHECK (tipo_distribucion IN ('ACTUAL','HISTORICA')),
    CONSTRAINT ck_distribucion_sens CHECK (nivel_sensibilidad IN ('PUBLICO','RESTRINGIDO','CONFIDENCIAL'))
);
CREATE INDEX ix_distribucion_especie ON distribucion(especie_id);
CREATE INDEX ix_distribucion_depto   ON distribucion(departamento);

CREATE TABLE avistamiento (
    id                     BIGSERIAL PRIMARY KEY,
    uuid                   UUID         NOT NULL DEFAULT gen_random_uuid(),
    especie_id             BIGINT       NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    fecha_avistamiento     DATE         NOT NULL,
    hora_avistamiento      TIME,
    departamento           VARCHAR(80),
    municipio              VARCHAR(80),
    distrito               VARCHAR(80),
    sitio                  VARCHAR(200),
    latitud                NUMERIC(9,6),
    longitud               NUMERIC(9,6),
    precision_metros       INTEGER,
    nivel_sensibilidad     VARCHAR(20)  NOT NULL DEFAULT 'PUBLICO',
    cantidad_individuos    INTEGER,
    observador_id          BIGINT REFERENCES usuario(id),
    observaciones          TEXT,
    estado_registro        VARCHAR(30)  NOT NULL DEFAULT 'BORRADOR',
    fecha_creacion         TIMESTAMP    NOT NULL DEFAULT now(),
    fecha_actualizacion    TIMESTAMP,
    CONSTRAINT uk_avistamiento_uuid UNIQUE (uuid),
    CONSTRAINT ck_avistamiento_sens CHECK (nivel_sensibilidad IN ('PUBLICO','RESTRINGIDO','CONFIDENCIAL')),
    CONSTRAINT ck_avistamiento_cant CHECK (cantidad_individuos IS NULL OR cantidad_individuos > 0)
);
CREATE INDEX ix_avistamiento_especie_fecha ON avistamiento(especie_id, fecha_avistamiento DESC);

-- =====================================================================
-- 8. HISTORIAL DE EVENTOS  (RF-053 a RF-056, RN-021)
-- =====================================================================

CREATE TABLE evento_historico (
    id                          BIGSERIAL PRIMARY KEY,
    especie_id                  BIGINT       NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    tipo_evento_id              BIGINT       NOT NULL REFERENCES tipo_evento(id),
    fecha_evento                DATE         NOT NULL,
    titulo                      VARCHAR(200) NOT NULL,
    descripcion                 TEXT,
    usuario_id                  BIGINT REFERENCES usuario(id),
    fuente_id                   BIGINT REFERENCES fuente(id),
    distribucion_id             BIGINT REFERENCES distribucion(id),
    avistamiento_id             BIGINT REFERENCES avistamiento(id),
    historial_conservacion_id   BIGINT REFERENCES historial_conservacion(id),
    fecha_creacion              TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX ix_evento_especie_fecha ON evento_historico(especie_id, fecha_evento DESC);

-- =====================================================================
-- 9. MULTIMEDIA  (RF-042 a RF-045, RN-007, RN-008)
-- =====================================================================

CREATE TABLE multimedia (
    id                    BIGSERIAL PRIMARY KEY,
    uuid                  UUID         NOT NULL DEFAULT gen_random_uuid(),
    especie_id            BIGINT       NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    avistamiento_id       BIGINT REFERENCES avistamiento(id) ON DELETE SET NULL,
    evento_historico_id   BIGINT REFERENCES evento_historico(id) ON DELETE SET NULL,
    tipo                  VARCHAR(20)  NOT NULL DEFAULT 'FOTOGRAFIA',  -- FOTOGRAFIA | VIDEO | DOCUMENTO
    url                   VARCHAR(500) NOT NULL,
    nombre_archivo        VARCHAR(255) NOT NULL,
    tipo_mime             VARCHAR(100),
    tamano_bytes          BIGINT,
    descripcion           VARCHAR(500),
    principal             BOOLEAN      NOT NULL DEFAULT FALSE,
    fecha_captura         DATE,
    usuario_carga_id      BIGINT REFERENCES usuario(id),
    fecha_creacion        TIMESTAMP    NOT NULL DEFAULT now(),
    fecha_actualizacion   TIMESTAMP,
    CONSTRAINT uk_multimedia_uuid UNIQUE (uuid),
    CONSTRAINT ck_multimedia_tipo CHECK (tipo IN ('FOTOGRAFIA','VIDEO','DOCUMENTO'))
);
CREATE INDEX ix_multimedia_especie ON multimedia(especie_id);
-- Solo una fotografia principal por especie (RF-045)
CREATE UNIQUE INDEX uk_multimedia_principal
    ON multimedia(especie_id) WHERE principal = TRUE;

-- =====================================================================
-- 10. VINCULO ESPECIE - FUENTE  (RN-020)
-- =====================================================================

CREATE TABLE especie_fuente (
    id              BIGSERIAL PRIMARY KEY,
    especie_id      BIGINT      NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    fuente_id       BIGINT      NOT NULL REFERENCES fuente(id),
    ambito          VARCHAR(30) NOT NULL DEFAULT 'GENERAL',
        -- GENERAL, TAXONOMIA, MORFOLOGIA, CONSERVACION, NUTRICION, DISTRIBUCION
    observaciones   VARCHAR(500),
    CONSTRAINT uk_especie_fuente UNIQUE (especie_id, fuente_id, ambito)
);

-- =====================================================================
-- 11. VALIDACION CIENTIFICA  (RF-048 a RF-052, RN-003, RN-017)
-- =====================================================================

CREATE TABLE validacion (
    id                  BIGSERIAL PRIMARY KEY,
    entidad             VARCHAR(60)  NOT NULL,   -- ESPECIE, USO_ALIMENTARIO, AVISTAMIENTO...
    entidad_id          BIGINT       NOT NULL,
    especie_id          BIGINT REFERENCES especie(id) ON DELETE CASCADE,
    decision            VARCHAR(20)  NOT NULL,   -- ENVIADO, APROBADO, RECHAZADO, PUBLICADO, DESPUBLICADO
    estado_anterior     VARCHAR(30),
    estado_nuevo        VARCHAR(30)  NOT NULL,
    observacion         TEXT,
    usuario_id          BIGINT       NOT NULL REFERENCES usuario(id),
    fecha_decision      TIMESTAMP    NOT NULL DEFAULT now(),
    version_contenido   VARCHAR(80),
    CONSTRAINT ck_validacion_decision CHECK (decision IN
        ('ENVIADO','APROBADO','RECHAZADO','PUBLICADO','DESPUBLICADO'))
);
CREATE INDEX ix_validacion_entidad ON validacion(entidad, entidad_id);
CREATE INDEX ix_validacion_especie ON validacion(especie_id, fecha_decision DESC);

-- =====================================================================
-- 12. AUDITORIA  (RF-069, RF-070, RN-024)
-- =====================================================================

CREATE TABLE auditoria (
    id                BIGSERIAL PRIMARY KEY,
    entidad           VARCHAR(60)  NOT NULL,
    entidad_id        BIGINT,
    accion            VARCHAR(30)  NOT NULL,  -- CREAR, EDITAR, DESACTIVAR, VALIDAR, PUBLICAR, ACCESO_SENSIBLE...
    usuario_id        BIGINT REFERENCES usuario(id),
    nombre_usuario    VARCHAR(60),
    valor_anterior    TEXT,
    valor_nuevo       TEXT,
    ip_origen         VARCHAR(45),
    fecha             TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX ix_auditoria_entidad ON auditoria(entidad, entidad_id);
CREATE INDEX ix_auditoria_fecha   ON auditoria(fecha DESC);

-- =====================================================================
-- 13. IDENTIFICACION ASISTIDA  (RF-060 a RF-063) - preparado para V3
-- Caracteristicas normalizadas y consultables por filtros
-- =====================================================================

CREATE TABLE caracteristica (
    id            BIGSERIAL PRIMARY KEY,
    codigo        VARCHAR(60)  NOT NULL,
    nombre        VARCHAR(150) NOT NULL,
    tipo_dato     VARCHAR(20)  NOT NULL DEFAULT 'LISTA',  -- LISTA | TEXTO | NUMERO
    grupo         VARCHAR(60),                            -- HOJA, FLOR, TAMANO, COLOR...
    activo        BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_caracteristica_codigo UNIQUE (codigo)
);

CREATE TABLE valor_caracteristica (
    id                  BIGSERIAL PRIMARY KEY,
    caracteristica_id   BIGINT       NOT NULL REFERENCES caracteristica(id) ON DELETE CASCADE,
    codigo              VARCHAR(60)  NOT NULL,
    nombre              VARCHAR(150) NOT NULL,
    activo              BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_valor_caracteristica UNIQUE (caracteristica_id, codigo)
);

CREATE TABLE especie_caracteristica (
    id                        BIGSERIAL PRIMARY KEY,
    especie_id                BIGINT NOT NULL REFERENCES especie(id) ON DELETE CASCADE,
    caracteristica_id         BIGINT NOT NULL REFERENCES caracteristica(id),
    valor_caracteristica_id   BIGINT REFERENCES valor_caracteristica(id),
    valor_texto               VARCHAR(255),
    valor_numerico            NUMERIC(12,4),
    CONSTRAINT uk_especie_caracteristica UNIQUE (especie_id, caracteristica_id, valor_caracteristica_id)
);
CREATE INDEX ix_especie_caracteristica_busqueda
    ON especie_caracteristica(caracteristica_id, valor_caracteristica_id);
