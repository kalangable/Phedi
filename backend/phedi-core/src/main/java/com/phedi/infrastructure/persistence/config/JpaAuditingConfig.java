package com.phedi.infrastructure.persistence.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {

    /**
     * Usuário responsável pelas operações de escrita, usado para preencher
     * created_by / updated_by / deleted_by.
     *
     * Como o projeto ainda não possui autenticação, retorna o fallback
     * "SYSTEM" para todas as operações. Quando houver segurança, basta ler
     * o principal do SecurityContext (ou do contexto de aplicação/Loki) aqui.
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("SYSTEM");
    }
}