package com.phedi.infrastructure.persistence.party.repository.base;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.PartyBase;
import com.phedi.domain.party.repository.PartyBaseRepository;
import com.phedi.domain.party.service.PublicIdGenerator;
import com.phedi.infrastructure.persistence.party.entity.base.AbstractEntity;
import com.phedi.infrastructure.persistence.party.mapper.PersistenceMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractPartyBaseRepository<DOMAIN extends PartyBase, ENTITY extends AbstractEntity> implements PartyBaseRepository<DOMAIN> {

    protected final PartyBaseJpaRepository<ENTITY> jpaRepository;
    protected final PersistenceMapper<DOMAIN, ENTITY> mapper;

    @Autowired
    protected PublicIdGenerator publicIdGenerator;

    @Override
    public Optional<DOMAIN> findByIdentifier(Identifier identifier) {
        return jpaRepository.findByPublicIdAndIsDeletedFalse(identifier.value())
                .map(mapper::toDomain);
    }

    @Override
    public DOMAIN update(DOMAIN domain) {
        if (domain.getIdentifier() == null || domain.getIdentifier().value().isBlank()) {
            throw new IllegalArgumentException("Identifier is required for update");
        }

        ENTITY existing = jpaRepository.findByPublicIdAndIsDeletedFalse(domain.getIdentifier().value())
                .orElseThrow(() -> new RuntimeException(
                        String.format("%s not found: %s", domain.getClass().getSimpleName(),
                                domain.getIdentifier().value())));

        mapper.updateEntity(domain, existing);
        existing.setUpdatedBy(currentAuditor());

        return mapper.toDomain(jpaRepository.save(existing));
    }

    @Override
    public void deleteByIdentifier(Identifier identifier) {
        jpaRepository.findByPublicIdAndIsDeletedFalse(identifier.value())
                .ifPresent(entity -> {
                    entity.softDelete();
                    entity.setDeletedBy(currentAuditor());
                    jpaRepository.save(entity);
                });
    }

    @Override
    public void activate(Identifier identifier) {
        jpaRepository.findByPublicIdAndIsDeletedFalse(identifier.value())
                .ifPresent(entity -> {
                    entity.activate();
                    entity.setUpdatedBy(currentAuditor());
                    jpaRepository.save(entity);
                });
    }

    @Override
    public void deactivate(Identifier identifier) {
        jpaRepository.findByPublicIdAndIsDeletedFalse(identifier.value())
                .ifPresent(entity -> {
                    entity.deactivate();
                    entity.setUpdatedBy(currentAuditor());
                    jpaRepository.save(entity);
                });
    }

    protected abstract String currentAuditor();
}