package com.phedi.infrastructure.persistence.party.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phedi.infrastructure.persistence.party.entity.PartyEntity;

interface PartySpecializationJpaRepository<E extends PartyEntity> extends JpaRepository<E, Long> {

    Optional<E> findByPublicIdAndIsDeletedFalse(String publicId);

    // Find all organizations excluding deleted
    List<E> findByIsDeletedFalse();

}
