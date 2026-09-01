package sv.edu.ues.fmp.fauna.conservacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Catalogo administrable de estados (RN-013). No se codifica de forma rigida
 * en un enum para que el Departamento pueda ajustar categorias y autoridad
 * (UICN, MARN u otra) sin recompilar (RNF-019).
 */
@Entity
@Table(name = "estado_conservacion")
@Getter
@Setter
@NoArgsConstructor
public class EstadoConservacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    /** Permite ordenar reportes de mayor a menor riesgo. */
    @Column(name = "orden_gravedad", nullable = false)
    private Integer ordenGravedad = 0;

    @Column(length = 150)
    private String autoridad;

    @Column(nullable = false)
    private Boolean activo = true;
}
