package sv.edu.ues.fmp.fauna.fuente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.comun.enums.AmbitoFuente;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;

/**
 * Vinculo especie-fuente indicando QUE informacion respalda cada referencia
 * (RF-047). Una misma fuente puede respaldar taxonomia y conservacion a la vez.
 */
@Entity
@Table(name = "especie_fuente",
       uniqueConstraints = @UniqueConstraint(name = "uk_especie_fuente",
               columnNames = {"especie_id", "fuente_id", "ambito"}))
@Getter
@Setter
@NoArgsConstructor
public class EspecieFuente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fuente_id", nullable = false)
    private Fuente fuente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AmbitoFuente ambito = AmbitoFuente.GENERAL;

    @Column(length = 500)
    private String observaciones;
}
