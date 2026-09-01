package sv.edu.ues.fmp.fauna.conservacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Catalogo de medidas de proteccion (RF-033). */
@Entity
@Table(name = "medida_conservacion")
@Getter
@Setter
@NoArgsConstructor
public class MedidaConservacion {

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
