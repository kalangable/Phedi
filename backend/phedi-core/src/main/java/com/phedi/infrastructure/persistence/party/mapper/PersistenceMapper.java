package com.phedi.infrastructure.persistence.party.mapper;

public interface PersistenceMapper<DOMAIN, ENTITY> {

    DOMAIN toDomain(ENTITY entity);

    ENTITY toEntity(DOMAIN domain);

    void updateEntity(DOMAIN domain, ENTITY entity);
}

