package sv.edu.ues.fmp.fauna.conservacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;
import sv.edu.ues.fmp.fauna.fuente.domain.Fuente;
import sv.edu.ues.fmp.fauna.seguridad.domain.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Cada cambio de estado se agrega como una fila nueva; nunca se sobrescribe
 * (RF-031, RF-035, RN-012). El estado vigente tambien se guarda denormalizado
 * en EspecieAnimal para que los filtros y listados sean rapidos.
 */
@Entity
@Table(name = "historial_conservacion")
@Getter
@Setter
@NoArgsConstructor
public class HistorialConservacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_conservacion_id", nullable = false)
    private EstadoConservacion estadoConservacion;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id")
    private Fuente fuente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(nullable = false)
    private Boolean vigente = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
