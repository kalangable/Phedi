package com.phedi.infrastructure.persistence.party.entity.base;

/**
 * Contrato de ativação/desativação lógica das entidades.
 *
 * O campo {@code isActive} é um {@link Boolean} (e não {@code boolean})
 * para permitir valores nulos quando necessário, seguindo o padrão de
 * tipos dos demais campos do modelo.
 */
public interface ActivableEntity {

    Boolean getIsActive();

    void setIsActive(Boolean isActive);

    default void activate() {
        setIsActive(true);
    }

    default void deactivate() {
        setIsActive(false);
    }
}