package com.phedi.infrastructure.persistence.party.repository;

import org.springframework.stereotype.Repository;

import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.repository.OrganizationRepository;
import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;
import com.phedi.infrastructure.persistence.party.mapper.PartyPersistenceMapper;
import com.phedi.infrastructure.persistence.party.repository.base.AbstractPartyRepository;
import com.phedi.infrastructure.persistence.party.repository.base.PartyBaseJpaRepository;

@Repository
public class OrganizationRepositoryImpl extends AbstractPartyRepository<Organization, OrganizationEntity> implements OrganizationRepository {

    protected OrganizationRepositoryImpl(
            PartyBaseJpaRepository<OrganizationEntity> jpaRepository,
            PartyPersistenceMapper<Organization, OrganizationEntity> mapper) {
        super(jpaRepository, mapper);
    }
}
