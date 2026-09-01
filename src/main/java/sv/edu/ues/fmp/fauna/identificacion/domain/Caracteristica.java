package sv.edu.ues.fmp.fauna.identificacion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Caracteristica observable usada por la identificacion asistida (RF-060, RF-061).
 *
 * Por que existe si la ficha ya tiene campos descriptivos: esos campos son texto
 * libre, util para leer la ficha pero imposible de filtrar. Aqui se guarda la
 * version normalizada (forma de hoja = LANCEOLADA) que si permite comparar
 * especies con consultas SQL, sin necesidad de IA (limitacion del documento).
 */
@Entity
@Table(name = "caracteristica")
@Getter
@Setter
@NoArgsConstructor
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    /** LISTA, TEXTO o NUMERO. Define como se captura el valor. */
    @Column(name = "tipo_dato", nullable = false, length = 20)
    private String tipoDato = "LISTA";

    /** Agrupador para la interfaz: HOJA, FLOR, TAMANO, COLOR... */
    @Column(length = 60)
    private String grupo;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "caracteristica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ValorCaracteristica> valores = new ArrayList<>();
}
