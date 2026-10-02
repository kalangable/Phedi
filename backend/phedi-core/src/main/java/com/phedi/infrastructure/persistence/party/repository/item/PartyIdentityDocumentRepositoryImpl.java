package com.phedi.infrastructure.persistence.party.repository.item;

import org.springframework.stereotype.Repository;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.domain.party.repository.item.PartyIdentityDocumentRepository;
import com.phedi.infrastructure.persistence.party.entity.item.PartyIdentityDocumentEntity;
import com.phedi.infrastructure.persistence.party.mapper.item.PartyIdentityDocumentPersistenceMapper;
import com.phedi.infrastructure.persistence.party.repository.base.PartyJpaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j 
@Transactional
public class PartyIdentityDocumentRepositoryImpl extends AbstractPartyItemRepository<PartyIdentityDocument, PartyIdentityDocumentEntity> implements PartyIdentityDocumentRepository {

    public PartyIdentityDocumentRepositoryImpl(
            PartyIdentityDocumentJpaRepository jpaRepository,
            PartyJpaRepository partyJpaRepository,
            PartyIdentityDocumentPersistenceMapper mapper) {
        super(jpaRepository, partyJpaRepository, mapper);
    }

    @Override
    public void demotePrimary(Identifier partyIdentifier) {
        itemRepository.findByParty_PublicIdAndPrimaryTrueAndIsDeletedFalse(partyIdentifier.value())
                .ifPresentOrElse(existing -> {
                    existing.setPrimary(false);
                    itemRepository.save(existing);
                }, () -> log.info("Does not exists Party Item available to demote for {}", partyIdentifier));
    }

}