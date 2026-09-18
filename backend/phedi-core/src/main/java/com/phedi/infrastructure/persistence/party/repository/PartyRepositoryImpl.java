package com.phedi.infrastructure.persistence.party.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.phedi.domain.party.model.Party;
import com.phedi.domain.party.model.PartyIdentifier;
import com.phedi.domain.party.repository.PartyRepository;
import com.phedi.infrastructure.persistence.party.entity.PartyEntity;
import com.phedi.infrastructure.persistence.party.mapper.PartyPersistenceMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public abstract class PartyRepositoryImpl<D extends Party, E extends PartyEntity> implements PartyRepository<D> {

    private final PartyJpaRepository partyJpaRepository;
    private final PartySpecializationJpaRepository<E> specializationJpaRepositoryJpaRepository;
    protected final PartyPersistenceMapper<D, E> mapper;

    @Override
    public D save(D domain) {
        E entity = mapper.toEntity(domain);

        E savedEntity = specializationJpaRepositoryJpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    };

    @Override
    public void deleteByPartyIdentifier(PartyIdentifier partyIdentifier) {
        partyJpaRepository.softDelete(partyIdentifier.value());
    }

    @Override
    public void activate(String partyNumber) {
        partyJpaRepository.activate(partyNumber);
    }

    @Override
    public void deactivate(String partyNumber) {
        partyJpaRepository.deactivate(partyNumber);
    }


    @Override
    public List<D> findAll() {
        return specializationJpaRepositoryJpaRepository.findByIsDeletedFalse()
                .stream().map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<D> findByPartyIdentifier(PartyIdentifier partyIdentifier) {
        return specializationJpaRepositoryJpaRepository.findByPartyNumberAndIsDeletedFalse(partyIdentifier.value()).map(mapper::toDomain);
    }

}
