package com.phedi.infrastructure.persistence.party.repository.base;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.phedi.domain.party.exception.ResourceNotFoundException;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.PartyBase;
import com.phedi.domain.party.repository.PartyBaseRepository;
import com.phedi.domain.party.service.PublicIdGenerator;
import com.phedi.infrastructure.persistence.party.entity.base.AbstractEntity;
import com.phedi.infrastructure.persistence.party.mapper.PersistenceMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j 
@RequiredArgsConstructor
public abstract class AbstractPartyBaseRepository<DOMAIN extends PartyBase, ENTITY extends AbstractEntity> implements PartyBaseRepository<DOMAIN> {

    protected final PartyBaseJpaRepository<ENTITY> jpaRepository;
    protected final PersistenceMapper<DOMAIN, ENTITY> mapper;

    @Autowired
    protected PublicIdGenerator publicIdGenerator;

    @Override
    public Optional<DOMAIN> findByIdentifier(Identifier identifier) {
        return getEntityByIdentifier(identifier)
                .map(mapper::toDomain);
    }

    @Override
    public DOMAIN update(DOMAIN domain) {
        checkIdentifierExist(domain);
        ENTITY existing = getExistingEntity(domain);

        mapper.updateEntity(domain, existing);
        existing.setUpdatedBy(currentAuditor());

        return mapper.toDomain(jpaRepository.save(existing));
    }

    protected void checkIdentifierExist(DOMAIN domain) {
        if (domain.getIdentifier() == null || domain.getIdentifier().value().isBlank()) {
            log.debug("Domain does't content Identifier {}", domain);
            throw new IllegalArgumentException("Identifier is required for update");
        }
    }

    protected ENTITY getExistingEntity(DOMAIN domain) {
        return getEntityByIdentifier(domain.getIdentifier())
                .orElseThrow(() -> {
                    var msg = String.format("Domain %s not found: %s", domain.getClass().getSimpleName(),
                                domain.getIdentifier().value());
                    log.debug(msg);
                    return new ResourceNotFoundException(msg);
                });
    }

    @Override
    public void deleteByIdentifier(Identifier identifier) {
        getEntityByIdentifier(identifier)
                .ifPresentOrElse(entity -> {
                    entity.softDelete();
                    entity.setDeletedBy(currentAuditor());
                    jpaRepository.save(entity);
                }, () -> log.info("Does not exists entity available to delete for {}", identifier));
    }

    @Override
    public void activate(Identifier identifier) {
        getEntityByIdentifier(identifier)
                .ifPresentOrElse(entity -> {
                    entity.activate();
                    entity.setUpdatedBy(currentAuditor());
                    jpaRepository.save(entity);
                }, () -> log.info("Does not exists entity available to activate for {}", identifier));
    }

    @Override
    public void deactivate(Identifier identifier) {
        getEntityByIdentifier(identifier)
                .ifPresentOrElse(entity -> {
                    entity.deactivate();
                    entity.setUpdatedBy(currentAuditor());
                    jpaRepository.save(entity);
                }, () -> log.info("Does not exists entity available to deactivate for {}", identifier));
    }

    protected Optional<ENTITY> getEntityByIdentifier(Identifier identifier) {
        return jpaRepository.findByPublicIdAndIsDeletedFalse(identifier.value());
    }

    protected abstract String currentAuditor();
}