package sv.edu.ues.fmp.fauna.identificacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Valor permitido de una caracteristica de tipo LISTA (ej. hoja: OVALADA). */
@Entity
@Table(name = "valor_caracteristica",
       uniqueConstraints = @UniqueConstraint(name = "uk_valor_caracteristica",
               columnNames = {"caracteristica_id", "codigo"}))
@Getter
@Setter
@NoArgsConstructor
public class ValorCaracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;

    @Column(nullable = false, length = 60)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Boolean activo = true;
}
