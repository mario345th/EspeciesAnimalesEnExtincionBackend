package sv.edu.ues.fmp.fauna.seguridad.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Accion autorizable evaluada en el servidor, no en el frontend (RNF-007). */
@Entity
@Table(name = "permiso")
@Getter
@Setter
@NoArgsConstructor
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String codigo;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 60)
    private String modulo;
}
