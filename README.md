# EspeciesAnimalesEnExtincion — Backend núcleo

Universidad de El Salvador · Facultad Multidisciplinaria Paracentral
Departamento de Informática · Cátedra de Ingeniería de Software

Modelo de datos y entidades JPA del repositorio de fauna. **28 tablas, 26 entidades, 8 enums.**

## Qué cambió respecto al modelo unificado del documento

| Cambio | Razón |
|---|---|
| `Especie` → **`EspecieAnimal`** | Este repositorio solo registra animales; el nombre genérico mentía. |
| **`FaunaDetalle` se fusionó** dentro de `EspecieAnimal` | La relación 1:0..1 con `@MapsId` existía para que una tabla sirviera a flora y fauna. Sin esa necesidad, es un JOIN gratis en cada consulta. |
| **`TipoOrganismo` eliminado** | Toda fila es fauna. Una columna con un solo valor posible no informa nada. |
| `NivelTaxonomico` **sin `DIVISION`** | Fauna usa *filo*. |
| **`Caracteristica` sin campo `ambito`** | Ya no hay que distinguir si aplica a flora o fauna. |
| `UsoAlimentario` e `InformacionNutricional` **no existen aquí** | Pertenecen al repositorio de plantas. |

La tabla sigue llamándose `especie` (no `especie_animal`) **a propósito**: si más adelante deciden fusionar los dos sistemas, las tablas comunes coinciden columna por columna y la migración es una unión, no una reescritura.

## Entidades

**Seguridad** — Usuario, Rol, Permiso
**Núcleo** — EspecieAnimal, NombreComun, Taxon
**Conservación** — EstadoConservacion, HistorialConservacion, Amenaza, EspecieAmenaza, MedidaConservacion, EspecieMedida
**Geografía** — TipoHabitat, EspecieHabitat, Distribucion, Avistamiento
**Soporte** — Multimedia, Fuente, EspecieFuente, TipoEvento, EventoHistorico, Validacion, Auditoria
**Identificación asistida** — Caracteristica, ValorCaracteristica, EspecieCaracteristica

## Arranque en IntelliJ IDEA

```sql
CREATE DATABASE fauna_extincion;
```

1. `File → Open` sobre la carpeta (detecta el `pom.xml`).
2. `Settings → Build → Compiler → Annotation Processors → Enable annotation processing` (Lombok).
3. Ajustar usuario y contraseña en `application.yml`.
4. Ejecutar `FaunaApplication`. Corre en el puerto **8080**. Flyway aplica `V1` y `V2` solo.

Si falla con `Schema-validation: missing column…`, una entidad y el SQL no coinciden. Ese error es útil: no lo silencien cambiando a `ddl-auto: update`.

## Reglas de equipo

- **Rango Flyway del núcleo: `V1`–`V99`.** El equipo de services usa `V100` en adelante.
- **Nunca editar un script ya ejecutado.** Flyway guarda checksum; los cambios van en un archivo nuevo.
- Los paquetes `seguridad/`, `especie/`, `taxonomia/` y `multimedia/` son del núcleo. Los demás equipos piden cambios por issue.
- **Los nombres de tabla, columnas, permisos y estados deben ser idénticos a los del repositorio de plantas.** Es lo único que mantiene abierta la puerta a unificar después.

## Pendiente de confirmar con el cliente

- ¿Este repositorio cubre **toda** la fauna o solo la amenazada? Cambia si `estado_conservacion` es el eje del sistema o un atributo más.
- Catálogo oficial de estados de conservación (UICN, MARN o listado propio).
- Si los avistamientos entran en la versión 1.
- Política de precisión geográfica publicable.
