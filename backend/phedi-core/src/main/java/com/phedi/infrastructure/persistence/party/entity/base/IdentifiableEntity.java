package com.phedi.infrastructure.persistence.party.entity.base;

/**
 * Contrato de identidade das entidades de persistência.
 *
 * Separa a identidade técnica ({@code id}) da identidade de negócio
 * ({@code publicId}), exposta externamente em URLs e APIs.
 */
public interface IdentifiableEntity {

    Long getId();

    void setId(Long id);

    String getPublicId();

    void setPublicId(String publicId);
}