package sv.edu.ues.fmp.fauna.historial.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Catalogo de tipos de acontecimiento de la linea de tiempo.
 * Es catalogo y no enum porque el documento deja pendiente confirmar
 * la lista exacta de eventos con el cliente (seccion 21).
 */
@Entity
@Table(name = "tipo_evento")
@Getter
@Setter
@NoArgsConstructor
public class TipoEvento {

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
