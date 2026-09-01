package sv.edu.ues.fmp.fauna.conservacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;
import sv.edu.ues.fmp.fauna.fuente.domain.Fuente;

import java.time.LocalDate;

/**
 * Relacion N:M con atributos propios. Se modela como entidad (y no con
 * @ManyToMany) porque la asociacion lleva nivel de impacto, descripcion y fuente.
 */
@Entity
@Table(name = "especie_amenaza",
       uniqueConstraints = @UniqueConstraint(name = "uk_especie_amenaza",
               columnNames = {"especie_id", "amenaza_id"}))
@Getter
@Setter
@NoArgsConstructor
public class EspecieAmenaza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "amenaza_id", nullable = false)
    private Amenaza amenaza;

    @Column(name = "nivel_impacto", length = 30)
    private String nivelImpacto;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id")
    private Fuente fuente;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro = LocalDate.now();
}
