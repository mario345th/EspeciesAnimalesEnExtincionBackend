package sv.edu.ues.fmp.fauna.geografia.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;

/**
 * HABITAT = el tipo de ambiente donde vive la especie.
 * Es distinto de distribucion y de avistamiento (RF-036, RN-009).
 */
@Entity
@Table(name = "especie_habitat",
       uniqueConstraints = @UniqueConstraint(name = "uk_especie_habitat",
               columnNames = {"especie_id", "tipo_habitat_id"}))
@Getter
@Setter
@NoArgsConstructor
public class EspecieHabitat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_habitat_id", nullable = false)
    private TipoHabitat tipoHabitat;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "altitud_min_msnm")
    private Integer altitudMinMsnm;

    @Column(name = "altitud_max_msnm")
    private Integer altitudMaxMsnm;
}
