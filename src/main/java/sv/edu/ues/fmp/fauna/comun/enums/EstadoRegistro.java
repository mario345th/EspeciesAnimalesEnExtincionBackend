package sv.edu.ues.fmp.fauna.comun.enums;

/**
 * Estados del flujo de validacion y publicacion (RF-048, Figura 3 del documento).
 * BORRADOR -> PENDIENTE_VALIDACION -> VALIDADO -> PUBLICADO
 *                     |-> RECHAZADO -> EN_CORRECCION -> PENDIENTE_VALIDACION
 */
public enum EstadoRegistro {
    BORRADOR,
    PENDIENTE_VALIDACION,
    VALIDADO,
    PUBLICADO,
    RECHAZADO,
    EN_CORRECCION
}
