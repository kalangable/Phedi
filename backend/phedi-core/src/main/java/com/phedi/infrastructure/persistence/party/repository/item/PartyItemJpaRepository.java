package com.phedi.infrastructure.persistence.party.repository.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;

import com.phedi.infrastructure.persistence.party.entity.item.PartyItemEntity;
import com.phedi.infrastructure.persistence.party.repository.base.PartyBaseJpaRepository;

@NoRepositoryBean
public interface PartyItemJpaRepository<ENTITY extends PartyItemEntity> extends PartyBaseJpaRepository<ENTITY> {

    List<ENTITY> findByParty_PublicIdAndIsDeletedFalse(String partyPublicId);

    Optional<ENTITY> findByParty_PublicIdAndPrimaryTrueAndIsDeletedFalse(String partyPublicId);

}