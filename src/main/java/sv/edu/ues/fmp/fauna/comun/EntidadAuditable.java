package sv.edu.ues.fmp.fauna.comun;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Campos de trazabilidad tecnica comunes a las entidades editables (RNF-018).
 * Se rellenan automaticamente gracias a @EnableJpaAuditing.
 *
 * Nota de diseno: el "quien" a nivel de dominio (usuario que registra, valida
 * u observa) se modela con llaves foraneas explicitas en cada entidad, porque
 * el documento lo exige como informacion consultable y no solo como metadato.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class EntidadAuditable {

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
