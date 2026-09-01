package sv.edu.ues.fmp.fauna.identificacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;

import java.math.BigDecimal;

/**
 * Valor concreto que una especie tiene para una caracteristica.
 * Sobre esta tabla se ejecuta el conteo de coincidencias de la
 * identificacion asistida (CU-19 / CU-20).
 */
@Entity
@Table(name = "especie_caracteristica",
       uniqueConstraints = @UniqueConstraint(name = "uk_especie_caracteristica",
               columnNames = {"especie_id", "caracteristica_id", "valor_caracteristica_id"}))
@Getter
@Setter
@NoArgsConstructor
public class EspecieCaracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "valor_caracteristica_id")
    private ValorCaracteristica valorCaracteristica;

    @Column(name = "valor_texto", length = 255)
    private String valorTexto;

    @Column(name = "valor_numerico", precision = 12, scale = 4)
    private BigDecimal valorNumerico;
}
