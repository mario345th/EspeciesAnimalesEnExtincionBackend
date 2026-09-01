package sv.edu.ues.fmp.fauna.fuente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.comun.EntidadAuditable;

import java.time.LocalDate;

/** Referencia cientifica o documental reutilizable (RF-046, RN-020). */
@Entity
@Table(name = "fuente")
@Getter
@Setter
@NoArgsConstructor
public class Fuente extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String titulo;

    @Column(length = 300)
    private String autores;

    @Column(length = 200)
    private String institucion;

    @Column(length = 500)
    private String referencia;

    @Column(length = 500)
    private String url;

    /** LIBRO, ARTICULO, SITIO_WEB, INFORME, HERBARIO, ENTREVISTA... */
    @Column(length = 40)
    private String tipo;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "fecha_consulta")
    private LocalDate fechaConsulta;

    @Column(nullable = false)
    private Boolean activo = true;
}
