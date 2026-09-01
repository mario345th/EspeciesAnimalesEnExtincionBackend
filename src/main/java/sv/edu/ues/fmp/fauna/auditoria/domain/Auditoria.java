package sv.edu.ues.fmp.fauna.auditoria.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sv.edu.ues.fmp.fauna.comun.enums.AccionAuditoria;
import sv.edu.ues.fmp.fauna.seguridad.domain.Usuario;

import java.time.LocalDateTime;

/**
 * Bitacora de acciones criticas (RF-069, seccion 18.3).
 * RN-024: solo se inserta; nunca se edita ni se borra desde la aplicacion.
 * Se guarda tambien el nombre de usuario en texto por si la cuenta se elimina.
 */
@Entity
@Table(name = "auditoria")
@Getter
@Setter
@NoArgsConstructor
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String entidad;

    @Column(name = "entidad_id")
    private Long entidadId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccionAuditoria accion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "nombre_usuario", length = 60)
    private String nombreUsuario;

    @Column(name = "valor_anterior", columnDefinition = "TEXT")
    private String valorAnterior;

    @Column(name = "valor_nuevo", columnDefinition = "TEXT")
    private String valorNuevo;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}
