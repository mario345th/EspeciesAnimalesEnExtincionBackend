package sv.edu.ues.fmp.fauna.geografia.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.comun.EntidadAuditable;
import sv.edu.ues.fmp.fauna.comun.enums.NivelSensibilidad;
import sv.edu.ues.fmp.fauna.comun.enums.TipoDistribucion;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;
import sv.edu.ues.fmp.fauna.fuente.domain.Fuente;

import java.math.BigDecimal;

/**
 * DISTRIBUCION = area donde la especie esta o estuvo presente (RF-037).
 * Se guarda jerarquia administrativa de El Salvador y coordenadas opcionales.
 */
@Entity
@Table(name = "distribucion")
@Getter
@Setter
@NoArgsConstructor
public class Distribucion extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_distribucion", nullable = false, length = 15)
    private TipoDistribucion tipoDistribucion = TipoDistribucion.ACTUAL;

    @Column(nullable = false, length = 80)
    private String pais = "El Salvador";

    @Column(length = 80)
    private String departamento;

    @Column(length = 80)
    private String municipio;

    @Column(length = 80)
    private String distrito;

    @Column(length = 200)
    private String sitio;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitud;

    /** RF-040 / RN-011: controla si las coordenadas exactas pueden mostrarse. */
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_sensibilidad", nullable = false, length = 20)
    private NivelSensibilidad nivelSensibilidad = NivelSensibilidad.PUBLICO;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id")
    private Fuente fuente;
}
