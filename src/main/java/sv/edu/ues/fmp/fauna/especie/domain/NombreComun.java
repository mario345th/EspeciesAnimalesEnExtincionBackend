package sv.edu.ues.fmp.fauna.especie.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Una especie puede tener varios nombres locales (RF-028, RN-002). */
@Entity
@Table(name = "nombre_comun",
       uniqueConstraints = @UniqueConstraint(name = "uk_nombre_comun",
               columnNames = {"especie_id", "nombre"}))
@Getter
@Setter
@NoArgsConstructor
public class NombreComun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 60)
    private String idioma;

    @Column(length = 120)
    private String region;

    /** Nombre comun que se muestra por defecto en fichas y listados. */
    @Column(nullable = false)
    private Boolean principal = false;
}
