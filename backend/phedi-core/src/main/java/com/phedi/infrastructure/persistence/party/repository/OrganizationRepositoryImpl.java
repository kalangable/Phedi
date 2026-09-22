package com.phedi.infrastructure.persistence.party.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.repository.OrganizationRepository;
import com.phedi.domain.party.service.PartyIdentifierGenerator;
import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;
import com.phedi.infrastructure.persistence.party.mapper.OrganizationPersistenceMapper;

@Repository
public class OrganizationRepositoryImpl extends PartyRepositoryImpl<Organization, OrganizationEntity> implements OrganizationRepository {

    public OrganizationRepositoryImpl(
            PartyJpaRepository partyJpaRepository,
            OrganizationJpaRepository jpaRepository,
            OrganizationPersistenceMapper mapper,
            PartyIdentifierGenerator partyIdentifierGenerator) {

        super(partyJpaRepository, jpaRepository, partyIdentifierGenerator, mapper);
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
        return jpaRepository.existsByIdentificationTypeAndIdentificationNumber(identificationType, identificationNumber);
    }

    @Override
    public Optional<Organization> findByIdentification(String identificationType, String identificationNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByIdentification'");
    }

}