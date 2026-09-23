package com.phedi.infrastructure.persistence.party.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.repository.OrganizationRepository;
import com.phedi.domain.party.service.PublicIdGenerator;
import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;
import com.phedi.infrastructure.persistence.party.mapper.OrganizationPersistenceMapper;

@Repository
public class OrganizationRepositoryImpl extends PartyRepositoryImpl<Organization, OrganizationEntity> implements OrganizationRepository {

    public OrganizationRepositoryImpl(
            PartyJpaRepository partyJpaRepository,
            OrganizationJpaRepository jpaRepository,
            OrganizationPersistenceMapper mapper,
            PublicIdGenerator publicIdGenerator) {

        super(partyJpaRepository, jpaRepository, publicIdGenerator, mapper);
        this.jpaRepository = jpaRepository;
    }

    private final OrganizationJpaRepository jpaRepository;

    @Override
    public List<Organization> findByTradeName(String tradeName) {
        return jpaRepository.findByTradeNameAndIsDeletedFalse(tradeName)
                .stream().map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public List<Organization> findByLegalName(String legalName) {
        return jpaRepository.findByLegalNameAndIsDeletedFalse(legalName)
                .stream().map(mapper::toDomain)
                .toList();
    }

    @Override
    public Boolean existsByIdentificationTypeAndIdentificationNumber(String identificationType, String identificationNumber) {
        return null;
    }

}
