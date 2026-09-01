package sv.edu.ues.fmp.fauna.geografia.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catalogo configurable de tipos de habitat (bosque seco, manglar, humedal...). */
@Entity
@Table(name = "tipo_habitat")
@Getter
@Setter
@NoArgsConstructor
public class TipoHabitat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;
}
