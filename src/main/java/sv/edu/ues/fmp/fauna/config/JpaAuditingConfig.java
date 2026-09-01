package sv.edu.ues.fmp.fauna.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Habilita el llenado automatico de fechaCreacion / fechaActualizacion. */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
