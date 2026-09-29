package com.phedi.infrastructure.persistence.party.repository.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.PartyItem;
import com.phedi.domain.party.repository.item.PartyItemRepository;
import com.phedi.infrastructure.persistence.party.entity.PartyEntity;
import com.phedi.infrastructure.persistence.party.entity.item.PartyItemEntity;
import com.phedi.infrastructure.persistence.party.mapper.PersistenceMapper;
import com.phedi.infrastructure.persistence.party.repository.base.AbstractPartyBaseRepository;
import com.phedi.infrastructure.persistence.party.repository.base.PartyJpaRepository;

public abstract class AbstractPartyItemRepository<DOMAIN extends PartyItem, ENTITY extends PartyItemEntity> extends AbstractPartyBaseRepository<DOMAIN, ENTITY> implements PartyItemRepository<DOMAIN> {

    protected final PartyItemJpaRepository<ENTITY> itemRepository;

    protected final PartyJpaRepository partyJpaRepository;

    @Autowired
    protected AuditorAware<String> auditorAware;

    protected AbstractPartyItemRepository(
            PartyItemJpaRepository<ENTITY> itemRepository,
            PartyJpaRepository partyJpaRepository,
            PersistenceMapper<DOMAIN, ENTITY> mapper) {
        super(itemRepository, mapper);
        this.itemRepository = itemRepository;
        this.partyJpaRepository = partyJpaRepository;
    }

    @Override
    public DOMAIN insertFor(DOMAIN item, Identifier partyIdentifier) {
        if (item.getIdentifier() == null) {
            item.setIdentifier(publicIdGenerator.generate());
        }

        PartyEntity party = partyJpaRepository.findByPublicIdAndIsDeletedFalse(partyIdentifier.value())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Party not found: %s", partyIdentifier.value())));

        ENTITY entity = mapper.toEntity(item);
        entity.setParty(party);

        return mapper.toDomain(itemRepository.save(entity));
    }

    @Override
    public List<DOMAIN> findAllByParty(Identifier partyIdentifier) {
        return itemRepository.findByParty_PublicIdAndIsDeletedFalse(partyIdentifier.value())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    protected String currentAuditor() {
        return auditorAware.getCurrentAuditor().orElse("SYSTEM");
    }
}