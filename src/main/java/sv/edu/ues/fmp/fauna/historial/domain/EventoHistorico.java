package sv.edu.ues.fmp.fauna.historial.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.conservacion.domain.HistorialConservacion;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;
import sv.edu.ues.fmp.fauna.fuente.domain.Fuente;
import sv.edu.ues.fmp.fauna.geografia.domain.Avistamiento;
import sv.edu.ues.fmp.fauna.geografia.domain.Distribucion;
import sv.edu.ues.fmp.fauna.seguridad.domain.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Acontecimiento cronologico de una especie (RF-053, RF-054, RN-021).
 * Es la fuente de la linea de tiempo (RF-055, CU-23). Los eventos se agregan,
 * nunca se sobrescriben, para no perder el pasado.
 */
@Entity
@Table(name = "evento_historico")
@Getter
@Setter
@NoArgsConstructor
public class EventoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_evento_id", nullable = false)
    private TipoEvento tipoEvento;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDate fechaEvento;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id")
    private Fuente fuente;

    // Vinculos opcionales al hecho concreto que origino el evento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distribucion_id")
    private Distribucion distribucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avistamiento_id")
    private Avistamiento avistamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historial_conservacion_id")
    private HistorialConservacion historialConservacion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
