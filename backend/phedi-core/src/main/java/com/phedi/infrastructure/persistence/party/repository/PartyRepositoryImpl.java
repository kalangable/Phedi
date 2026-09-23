package com.phedi.infrastructure.persistence.party.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Party;
import com.phedi.domain.party.repository.PartyRepository;
import com.phedi.domain.party.service.PublicIdGenerator;
import com.phedi.infrastructure.persistence.party.entity.PartyEntity;
import com.phedi.infrastructure.persistence.party.mapper.PartyPersistenceMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public abstract class PartyRepositoryImpl<D extends Party, E extends PartyEntity> implements PartyRepository<D> {

    private final PartyJpaRepository partyJpaRepository;
    private final PartySpecializationJpaRepository<E> specializationJpaRepositoryJpaRepository;
    private final PublicIdGenerator publicIdGenerator;
    protected final PartyPersistenceMapper<D, E> mapper;

    @Override
    public D insert(D domain) {
        if (domain.getIdentifier() == null) {
            domain.setIdentifier(publicIdGenerator.generate());
        }

        E entity = mapper.toEntity(domain);

        E savedEntity = specializationJpaRepositoryJpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public D update(D domain) {
        if (domain.getIdentifier() == null || domain.getIdentifier().value().isBlank()) {
            throw new IllegalArgumentException("Identifier is required for update");
        }

        E existing = specializationJpaRepositoryJpaRepository
                .findByPublicIdAndIsDeletedFalse(domain.getIdentifier().value())
                .orElseThrow(() -> new RuntimeException(
                        String.format("%s not found: %s", domain.getClass().getSimpleName(),
                                domain.getIdentifier().value())));

        mapper.updateEntity(domain, existing);

        return mapper.toDomain(specializationJpaRepositoryJpaRepository.save(existing));
    }

    @Override
    public void deleteByIdentifier(Identifier identifier) {
        partyJpaRepository.softDelete(identifier.value());
    }

    @Override
    public void activate(Identifier identifier) {
        partyJpaRepository.activate(identifier.value());
    }

    @Override
    public void deactivate(Identifier identifier) {
        partyJpaRepository.deactivate(identifier.value());
    }

    @Override
    public List<D> findAll() {
        return specializationJpaRepositoryJpaRepository.findByIsDeletedFalse()
                .stream().map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<D> findByIdentifier(Identifier identifier) {
        return specializationJpaRepositoryJpaRepository.findByPublicIdAndIsDeletedFalse(identifier.value()).map(mapper::toDomain);
    }

}