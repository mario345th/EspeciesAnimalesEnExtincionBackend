package sv.edu.ues.fmp.fauna.comun.enums;

/** Control de precision geografica publicable (RF-040, RN-011, RNF-017). */
public enum NivelSensibilidad {
    /** Coordenadas visibles para cualquier usuario autorizado o publico. */
    PUBLICO,
    /** Solo investigadores o especialistas autorizados ven las coordenadas exactas. */
    RESTRINGIDO,
    /** Coordenadas nunca se exponen; solo se muestra la zona general. */
    CONFIDENCIAL
}
