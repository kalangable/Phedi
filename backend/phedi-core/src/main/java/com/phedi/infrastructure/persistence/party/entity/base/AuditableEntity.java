package com.phedi.infrastructure.persistence.party.entity.base;

import java.time.LocalDateTime;

/**
 * Contrato de auditoria e soft-delete das entidades de persistência.
 *
 * Os campos de data/hora são preenchidos pelo Spring Data JPA Auditing
 * ({@code @CreatedDate}/{@code @LastModifiedDate}) e os campos de usuário
 * responsável por um {@code AuditorAware}.
 *
 * O soft-delete marca o registro como excluído sem removê-lo fisicamente;
 * as consultas do repositório devem filtrar {@code isDeleted = false}.
 */
public interface AuditableEntity {

    LocalDateTime getCreatedAt();

    void setCreatedAt(LocalDateTime createdAt);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    LocalDateTime getUpdatedAt();

    void setUpdatedAt(LocalDateTime updatedAt);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);

    Boolean getIsDeleted();

    void setIsDeleted(Boolean isDeleted);

    LocalDateTime getDeletedAt();

    void setDeletedAt(LocalDateTime deletedAt);

    String getDeletedBy();

    void setDeletedBy(String deletedBy);

    /**
     * Marca o registro como excluído logicamente.
     *
     * O usuário responsável ({@code deletedBy}) deve ser preenchido pelo
     * chamador (por exemplo, a implementação do repositório, usando o
     * {@code AuditorAware} corrente).
     */
    default void softDelete() {
        setIsDeleted(true);
        setDeletedAt(LocalDateTime.now());
    }

    /**
     * Restaura um registro anteriormente excluído logicamente.
     */
    default void restore() {
        setIsDeleted(false);
        setDeletedAt(null);
    }
}