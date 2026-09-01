package sv.edu.ues.fmp.fauna.taxonomia.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.comun.EntidadAuditable;
import sv.edu.ues.fmp.fauna.comun.enums.NivelTaxonomico;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del arbol taxonomico (RF-026, RF-027).
 *
 * Decision de diseno: una sola tabla auto-referenciada en lugar de siete tablas
 * (reino, filo, clase, orden, familia, genero, especie). Asi flora (division) y
 * fauna (filo) comparten estructura, se pueden agregar niveles sin tocar el
 * modelo (RNF-019) y las consultas por cualquier nivel son uniformes.
 */
@Entity
@Table(name = "taxon")
@Getter
@Setter
@NoArgsConstructor
public class Taxon extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelTaxonomico nivel;

    @Column(length = 150)
    private String autor;

    @Column(length = 500)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    private Taxon padre;

    @OneToMany(mappedBy = "padre")
    private List<Taxon> hijos = new ArrayList<>();

    @Column(nullable = false)
    private Boolean activo = true;

    /** Devuelve la ruta completa, por ejemplo: Plantae > Magnoliophyta > ... */
    public String getRutaCompleta() {
        return (padre == null) ? nombre : padre.getRutaCompleta() + " > " + nombre;
    }
}
