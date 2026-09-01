package sv.edu.ues.fmp.fauna.validacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.comun.enums.DecisionValidacion;
import sv.edu.ues.fmp.fauna.comun.enums.EstadoRegistro;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;
import sv.edu.ues.fmp.fauna.seguridad.domain.Usuario;

import java.time.LocalDateTime;

/**
 * Bitacora del flujo de validacion cientifica (RF-050, RF-051, RN-017).
 * Guarda quien envio, quien decidio, cuando, con que observacion y que
 * transicion de estado se genero (seccion "Auditoria de validacion").
 */
@Entity
@Table(name = "validacion")
@Getter
@Setter
@NoArgsConstructor
public class Validacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre logico de la entidad revisada: ESPECIE, USO_ALIMENTARIO, AVISTAMIENTO... */
    @Column(nullable = false, length = 60)
    private String entidad;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    /** Referencia directa a la especie para consultar el historial completo. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especie_id")
    private EspecieAnimal especie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DecisionValidacion decision;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 30)
    private EstadoRegistro estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 30)
    private EstadoRegistro estadoNuevo;

    /** Obligatoria cuando la decision es RECHAZADO (RF-051). */
    @Column(columnDefinition = "TEXT")
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_decision", nullable = false)
    private LocalDateTime fechaDecision = LocalDateTime.now();

    @Column(name = "version_contenido", length = 80)
    private String versionContenido;
}
