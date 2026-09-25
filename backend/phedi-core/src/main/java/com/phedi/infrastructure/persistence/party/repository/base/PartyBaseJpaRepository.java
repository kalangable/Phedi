package com.phedi.infrastructure.persistence.party.repository.base;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.phedi.infrastructure.persistence.party.entity.base.AbstractEntity;

@NoRepositoryBean
public interface PartyBaseJpaRepository<ENTITY extends AbstractEntity> extends JpaRepository<ENTITY, Long> {

    Optional<ENTITY> findByPublicIdAndIsDeletedFalse(String publicId);

    List<ENTITY> findByIsDeletedFalse();
}