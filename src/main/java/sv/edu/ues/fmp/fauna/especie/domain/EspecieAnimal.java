package sv.edu.ues.fmp.fauna.especie.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.comun.EntidadAuditable;
import sv.edu.ues.fmp.fauna.comun.enums.EstadoRegistro;
import sv.edu.ues.fmp.fauna.conservacion.domain.EspecieAmenaza;
import sv.edu.ues.fmp.fauna.conservacion.domain.EspecieMedida;
import sv.edu.ues.fmp.fauna.conservacion.domain.EstadoConservacion;
import sv.edu.ues.fmp.fauna.conservacion.domain.HistorialConservacion;
import sv.edu.ues.fmp.fauna.fuente.domain.EspecieFuente;
import sv.edu.ues.fmp.fauna.geografia.domain.Avistamiento;
import sv.edu.ues.fmp.fauna.geografia.domain.Distribucion;
import sv.edu.ues.fmp.fauna.geografia.domain.EspecieHabitat;
import sv.edu.ues.fmp.fauna.historial.domain.EventoHistorico;
import sv.edu.ues.fmp.fauna.multimedia.domain.Multimedia;
import sv.edu.ues.fmp.fauna.seguridad.domain.Usuario;
import sv.edu.ues.fmp.fauna.taxonomia.domain.Taxon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad central del repositorio de fauna en peligro de extincion.
 *
 * Diferencia con el modelo unificado del documento: como este repositorio solo
 * registra animales, los campos de FaunaDetalle se fusionaron aqui. Ya no hace
 * falta la columna tipo_organismo ni la relacion 1:0..1, y cada consulta se
 * ahorra un JOIN. La tabla conserva el nombre "especie" a proposito, para que
 * un futuro merge con el repositorio de flora no obligue a migrar datos.
 *
 * RN-001: el id interno es estable aunque cambie el nombre cientifico.
 * RN-006: no se elimina fisicamente; se desactiva con la bandera "activo".
 */
@Entity
@Table(name = "especie")
@Getter
@Setter
@NoArgsConstructor
public class EspecieAnimal extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador publico estable, util para URLs y exportaciones (RN-001). */
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "nombre_cientifico", nullable = false, unique = true, length = 200)
    private String nombreCientifico;

    @Column(name = "autor_cientifico", length = 150)
    private String autorCientifico;

    @Column(name = "descripcion_general", columnDefinition = "TEXT")
    private String descripcionGeneral;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // ------------------------------------------------------------------
    // Caracteristicas propias de fauna (RF-021 a RF-023)
    // ------------------------------------------------------------------

    @Column(length = 150)
    private String tamano;

    @Column(length = 150)
    private String peso;

    @Column(columnDefinition = "TEXT")
    private String coloracion;

    @Column(name = "cobertura_corporal", length = 150)
    private String coberturaCorporal;

    @Column(name = "rasgos_fisicos", columnDefinition = "TEXT")
    private String rasgosFisicos;

    @Column(columnDefinition = "TEXT")
    private String alimentacion;

    @Column(columnDefinition = "TEXT")
    private String reproduccion;

    @Column(columnDefinition = "TEXT")
    private String comportamiento;

    // ------------------------------------------------------------------
    // Clasificacion, conservacion y flujo de publicacion
    // ------------------------------------------------------------------

    /** Nodo taxonomico mas especifico conocido (normalmente GENERO o ESPECIE). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxon_id")
    private Taxon taxon;

    /** Estado vigente; el detalle cronologico vive en historialConservacion (RF-035). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_conservacion_id")
    private EstadoConservacion estadoConservacionActual;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro", nullable = false, length = 30)
    private EstadoRegistro estadoRegistro = EstadoRegistro.BORRADOR;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_registro_id")
    private Usuario usuarioRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_validacion_id")
    private Usuario usuarioValidacion;

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Column(nullable = false)
    private Boolean activo = true;

    // ------------------------------------------------------------------
    // Relaciones
    // ------------------------------------------------------------------

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NombreComun> nombresComunes = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Multimedia> multimedia = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EspecieHabitat> habitats = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Distribucion> distribuciones = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avistamiento> avistamientos = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialConservacion> historialConservacion = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EspecieAmenaza> amenazas = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EspecieMedida> medidas = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EspecieFuente> fuentes = new ArrayList<>();

    @OneToMany(mappedBy = "especie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoHistorico> eventosHistoricos = new ArrayList<>();

    // ------------------------------------------------------------------
    // Metodos de conveniencia: mantienen sincronizados ambos lados
    // ------------------------------------------------------------------

    public void agregarNombreComun(NombreComun nombreComun) {
        nombresComunes.add(nombreComun);
        nombreComun.setEspecie(this);
    }

    public void agregarMultimedia(Multimedia archivo) {
        multimedia.add(archivo);
        archivo.setEspecie(this);
    }

    public boolean estaPublicada() {
        return EstadoRegistro.PUBLICADO.equals(estadoRegistro);
    }
}
