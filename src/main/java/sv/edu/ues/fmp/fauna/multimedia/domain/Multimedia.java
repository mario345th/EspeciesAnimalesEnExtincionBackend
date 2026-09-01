package sv.edu.ues.fmp.fauna.multimedia.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.comun.EntidadAuditable;
import sv.edu.ues.fmp.fauna.comun.enums.TipoMultimedia;
import sv.edu.ues.fmp.fauna.especie.domain.EspecieAnimal;
import sv.edu.ues.fmp.fauna.geografia.domain.Avistamiento;
import sv.edu.ues.fmp.fauna.historial.domain.EventoHistorico;
import sv.edu.ues.fmp.fauna.seguridad.domain.Usuario;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Archivo asociado a una especie (RF-042 a RF-045, RN-007, RN-008).
 *
 * Decision de diseno: el binario NO se guarda en PostgreSQL (RNF-016).
 * La base almacena la URL o clave del archivo y sus metadatos; el contenido
 * vive en almacenamiento externo (carpeta del servidor, S3, MinIO...).
 *
 * "especie" siempre es obligatoria, de modo que toda foto pertenece al menos
 * a un registro identificable. Los vinculos a avistamiento o evento son
 * opcionales y permiten precisar el contexto sin usar relaciones polimorficas.
 */
@Entity
@Table(name = "multimedia")
@Getter
@Setter
@NoArgsConstructor
public class Multimedia extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieAnimal especie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avistamiento_id")
    private Avistamiento avistamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_historico_id")
    private EventoHistorico eventoHistorico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMultimedia tipo = TipoMultimedia.FOTOGRAFIA;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column(name = "tipo_mime", length = 100)
    private String tipoMime;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(length = 500)
    private String descripcion;

    /** Foto que encabeza la ficha publica (RF-045). Solo una por especie. */
    @Column(nullable = false)
    private Boolean principal = false;

    @Column(name = "fecha_captura")
    private LocalDate fechaCaptura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_carga_id")
    private Usuario usuarioCarga;
}
