package sv.edu.ues.fmp.fauna.geografia.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.comun.EntidadAuditable;
import sv.edu.ues.fmp.fauna.comun.enums.EstadoRegistro;
import sv.edu.ues.fmp.fauna.comun.enums.NivelSensibilidad;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;
import sv.edu.ues.fmp.fauna.seguridad.domain.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * AVISTAMIENTO = observacion puntual en una fecha y lugar determinados
 * (CU-21, RF-038, RF-039). Alimenta linea de tiempo y mapas segun permisos.
 */
@Entity
@Table(name = "avistamiento")
@Getter
@Setter
@NoArgsConstructor
public class Avistamiento extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @Column(name = "fecha_avistamiento", nullable = false)
    private LocalDate fechaAvistamiento;

    @Column(name = "hora_avistamiento")
    private LocalTime horaAvistamiento;

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

    @Column(name = "precision_metros")
    private Integer precisionMetros;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_sensibilidad", nullable = false, length = 20)
    private NivelSensibilidad nivelSensibilidad = NivelSensibilidad.PUBLICO;

    @Column(name = "cantidad_individuos")
    private Integer cantidadIndividuos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "observador_id")
    private Usuario observador;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro", nullable = false, length = 30)
    private EstadoRegistro estadoRegistro = EstadoRegistro.BORRADOR;
}
