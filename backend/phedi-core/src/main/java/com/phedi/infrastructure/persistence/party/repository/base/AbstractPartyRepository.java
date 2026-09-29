package com.phedi.infrastructure.persistence.party.repository.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;

import com.phedi.domain.party.model.Party;
import com.phedi.domain.party.repository.PartyRepository;
import com.phedi.infrastructure.persistence.party.entity.PartyEntity;
import com.phedi.infrastructure.persistence.party.mapper.PartyPersistenceMapper;

@Repository
public abstract class AbstractPartyRepository<DOMAIN extends Party, ENTITY extends PartyEntity> extends AbstractPartyBaseRepository<DOMAIN, ENTITY> implements PartyRepository<DOMAIN> {

    protected AbstractPartyRepository(
            PartyBaseJpaRepository<ENTITY> jpaRepository,
            PartyPersistenceMapper<DOMAIN, ENTITY> mapper) {
        super(jpaRepository, mapper);
    }

    @Autowired
    protected AuditorAware<String> auditorAware;

    @Override
    public DOMAIN insert(DOMAIN domain) {
        if (domain.getIdentifier() == null) {
            domain.setIdentifier(publicIdGenerator.generate());
        }

        ENTITY entity = mapper.toEntity(domain);

        ENTITY savedEntity = jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<DOMAIN> findAll() {
        return jpaRepository.findByIsDeletedFalse()
                .stream().map(mapper::toDomain)
                .toList();
    }

    protected String currentAuditor(){
        return auditorAware.getCurrentAuditor().orElse("SYSTEM");
    }
}