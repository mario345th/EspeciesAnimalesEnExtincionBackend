package sv.edu.ues.fmp.fauna.conservacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;
import sv.edu.ues.fmp.fauna.fuente.domain.Fuente;

import java.time.LocalDate;

/** Medida de conservacion aplicada a una especie concreta (RF-033). */
@Entity
@Table(name = "especie_medida",
       uniqueConstraints = @UniqueConstraint(name = "uk_especie_medida",
               columnNames = {"especie_id", "medida_conservacion_id"}))
@Getter
@Setter
@NoArgsConstructor
public class EspecieMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medida_conservacion_id", nullable = false)
    private MedidaConservacion medidaConservacion;

    @Column(name = "estado_implementacion", length = 40)
    private String estadoImplementacion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id")
    private Fuente fuente;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro = LocalDate.now();
}
