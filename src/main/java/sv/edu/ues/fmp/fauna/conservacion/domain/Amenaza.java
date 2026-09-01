package sv.edu.ues.fmp.fauna.conservacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catalogo reutilizable de amenazas (RF-032). */
@Entity
@Table(name = "amenaza")
@Getter
@Setter
@NoArgsConstructor
public class Amenaza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 80)
    private String categoria;

    @Column(nullable = false)
    private Boolean activo = true;
}
