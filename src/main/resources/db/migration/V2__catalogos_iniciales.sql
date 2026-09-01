-- =====================================================================
-- Datos semilla de catalogos administrables
-- Ajustar con el cliente (Ing. Dagoberto Perez / Depto. de Agronomia)
-- =====================================================================

-- Estados de conservacion (categorias UICN como punto de partida)
INSERT INTO estado_conservacion (codigo, nombre, descripcion, orden_gravedad, autoridad) VALUES
 ('EX','Extinta','No queda ningun individuo vivo.',              100,'UICN'),
 ('EW','Extinta en estado silvestre','Solo sobrevive en cautiverio o cultivo.', 90,'UICN'),
 ('CR','En peligro critico','Riesgo extremadamente alto de extincion.',  80,'UICN'),
 ('EN','En peligro','Riesgo muy alto de extincion.',              70,'UICN'),
 ('VU','Vulnerable','Riesgo alto de extincion.',                  60,'UICN'),
 ('NT','Casi amenazada','Proxima a calificar como amenazada.',    50,'UICN'),
 ('LC','Preocupacion menor','Amplia distribucion y abundante.',   40,'UICN'),
 ('DD','Datos insuficientes','Informacion inadecuada para evaluar.', 20,'UICN'),
 ('NE','No evaluada','Aun no evaluada con los criterios.',        10,'UICN');

-- Tipos de habitat
INSERT INTO tipo_habitat (codigo, nombre) VALUES
 ('BOSQUE_SECO','Bosque seco tropical'),
 ('BOSQUE_HUMEDO','Bosque humedo'),
 ('BOSQUE_NEBLISELVA','Bosque nebuloso'),
 ('MANGLAR','Manglar'),
 ('HUMEDAL','Humedal'),
 ('ZONA_COSTERA','Zona costera'),
 ('RIO_QUEBRADA','Rio o quebrada'),
 ('ZONA_AGRICOLA','Zona agricola'),
 ('ZONA_URBANA','Zona urbana'),
 ('MATORRAL','Matorral');

-- Amenazas
INSERT INTO amenaza (codigo, nombre, categoria) VALUES
 ('DEFORESTACION','Deforestacion','Perdida de habitat'),
 ('CAMBIO_USO_SUELO','Cambio de uso de suelo','Perdida de habitat'),
 ('CONTAMINACION','Contaminacion de agua o suelo','Contaminacion'),
 ('TRAFICO_ILEGAL','Trafico o comercio ilegal','Explotacion'),
 ('CACERIA','Caceria o extraccion','Explotacion'),
 ('INCENDIOS','Incendios forestales','Disturbio'),
 ('ESPECIES_INVASORAS','Especies invasoras','Biologica'),
 ('CAMBIO_CLIMATICO','Cambio climatico','Climatica'),
 ('URBANIZACION','Expansion urbana','Perdida de habitat');

-- Medidas de conservacion
INSERT INTO medida_conservacion (codigo, nombre) VALUES
 ('AREA_PROTEGIDA','Inclusion en area natural protegida'),
 ('PROPAGACION','Programa de propagacion o reproduccion'),
 ('EDUCACION','Educacion ambiental'),
 ('MONITOREO','Monitoreo poblacional'),
 ('RESTAURACION','Restauracion de habitat'),
 ('NORMATIVA','Proteccion legal o normativa');

-- Tipos de evento historico
INSERT INTO tipo_evento (codigo, nombre) VALUES
 ('CAMBIO_CONSERVACION','Cambio de estado de conservacion'),
 ('AVISTAMIENTO','Avistamiento registrado'),
 ('CAMBIO_DISTRIBUCION','Cambio en la distribucion'),
 ('NUEVA_AMENAZA','Nueva amenaza identificada'),
 ('MEDIDA_APLICADA','Medida de conservacion aplicada'),
 ('REVISION_TAXONOMICA','Revision taxonomica'),
 ('PUBLICACION','Publicacion cientifica asociada'),
 ('OTRO','Otro acontecimiento relevante');

-- Roles
INSERT INTO rol (nombre, descripcion) VALUES
 ('ADMINISTRADOR','Gestiona usuarios, roles, permisos y catalogos.'),
 ('ESPECIALISTA','Valida cientificamente la informacion antes de publicarla.'),
 ('INVESTIGADOR','Registra, actualiza y envia informacion a validacion.'),
 ('ESTUDIANTE','Registra observaciones y consulta segun permisos.'),
 ('PUBLICO','Consulta unicamente informacion publicada y no sensible.');

-- Permisos
INSERT INTO permiso (codigo, descripcion, modulo) VALUES
 ('USUARIO_GESTIONAR','Crear, editar y desactivar usuarios','SEGURIDAD'),
 ('ROL_GESTIONAR','Administrar roles y permisos','SEGURIDAD'),
 ('ESPECIE_CREAR','Registrar especies','ESPECIE'),
 ('ESPECIE_EDITAR','Editar especies','ESPECIE'),
 ('ESPECIE_DESACTIVAR','Desactivar especies','ESPECIE'),
 ('ESPECIE_ENVIAR_VALIDACION','Enviar registros a validacion','VALIDACION'),
 ('ESPECIE_VALIDAR','Aprobar o rechazar informacion cientifica','VALIDACION'),
 ('ESPECIE_PUBLICAR','Publicar informacion validada','VALIDACION'),
 ('CATALOGO_GESTIONAR','Administrar catalogos configurables','CATALOGO'),
 ('MULTIMEDIA_CARGAR','Cargar y asociar multimedia','MULTIMEDIA'),
 ('AVISTAMIENTO_REGISTRAR','Registrar avistamientos','GEOGRAFIA'),
 ('UBICACION_SENSIBLE_VER','Ver coordenadas exactas restringidas','GEOGRAFIA'),
 ('REPORTE_GENERAR','Generar reportes internos','REPORTES'),
 ('BITACORA_CONSULTAR','Consultar bitacoras de auditoria','AUDITORIA');

-- Asignacion base de permisos por rol
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id FROM rol r, permiso p
WHERE r.nombre = 'ADMINISTRADOR'
  AND p.codigo IN ('USUARIO_GESTIONAR','ROL_GESTIONAR','CATALOGO_GESTIONAR',
                   'REPORTE_GENERAR','BITACORA_CONSULTAR','ESPECIE_PUBLICAR');

INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id FROM rol r, permiso p
WHERE r.nombre = 'ESPECIALISTA'
  AND p.codigo IN ('ESPECIE_CREAR','ESPECIE_EDITAR','ESPECIE_VALIDAR','ESPECIE_PUBLICAR',
                   'MULTIMEDIA_CARGAR','AVISTAMIENTO_REGISTRAR','UBICACION_SENSIBLE_VER',
                   'REPORTE_GENERAR');

INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id FROM rol r, permiso p
WHERE r.nombre = 'INVESTIGADOR'
  AND p.codigo IN ('ESPECIE_CREAR','ESPECIE_EDITAR','ESPECIE_ENVIAR_VALIDACION',
                   'MULTIMEDIA_CARGAR','AVISTAMIENTO_REGISTRAR','REPORTE_GENERAR');

INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id FROM rol r, permiso p
WHERE r.nombre = 'ESTUDIANTE'
  AND p.codigo IN ('AVISTAMIENTO_REGISTRAR','MULTIMEDIA_CARGAR');

-- Caracteristicas para identificacion asistida de fauna
INSERT INTO caracteristica (codigo, nombre, tipo_dato, grupo) VALUES
 ('TAMANO_APROX','Tamano aproximado','LISTA','CUERPO'),
 ('COBERTURA_CORPORAL','Cobertura corporal','LISTA','CUERPO'),
 ('EXTREMIDADES','Tipo de extremidades','LISTA','CUERPO'),
 ('COLOR_PREDOMINANTE','Color predominante','LISTA','COLOR'),
 ('ACTIVIDAD','Actividad observable','LISTA','COMPORTAMIENTO');

INSERT INTO valor_caracteristica (caracteristica_id, codigo, nombre)
SELECT c.id, v.codigo, v.nombre
FROM caracteristica c
JOIN (VALUES
  ('TAMANO_APROX','MENOR_10CM','Menor a 10 cm'),
  ('TAMANO_APROX','ENTRE_10_50CM','Entre 10 y 50 cm'),
  ('TAMANO_APROX','ENTRE_50_150CM','Entre 50 y 150 cm'),
  ('TAMANO_APROX','MAYOR_150CM','Mayor a 150 cm'),
  ('COBERTURA_CORPORAL','PELO','Pelo'),
  ('COBERTURA_CORPORAL','PLUMAS','Plumas'),
  ('COBERTURA_CORPORAL','ESCAMAS','Escamas'),
  ('COBERTURA_CORPORAL','CAPARAZON','Caparazon'),
  ('COBERTURA_CORPORAL','PIEL_DESNUDA','Piel desnuda'),
  ('EXTREMIDADES','CUATRO_PATAS','Cuatro patas'),
  ('EXTREMIDADES','DOS_PATAS','Dos patas'),
  ('EXTREMIDADES','ALAS','Alas'),
  ('EXTREMIDADES','ALETAS','Aletas'),
  ('EXTREMIDADES','SIN_EXTREMIDADES','Sin extremidades'),
  ('COLOR_PREDOMINANTE','CAFE','Cafe'),
  ('COLOR_PREDOMINANTE','NEGRO','Negro'),
  ('COLOR_PREDOMINANTE','VERDE','Verde'),
  ('COLOR_PREDOMINANTE','GRIS','Gris'),
  ('COLOR_PREDOMINANTE','MULTICOLOR','Multicolor'),
  ('ACTIVIDAD','DIURNA','Diurna'),
  ('ACTIVIDAD','NOCTURNA','Nocturna'),
  ('ACTIVIDAD','CREPUSCULAR','Crepuscular')
) AS v(cod_car, codigo, nombre) ON v.cod_car = c.codigo;
