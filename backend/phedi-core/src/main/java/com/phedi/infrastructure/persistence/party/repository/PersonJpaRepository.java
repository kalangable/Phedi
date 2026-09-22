package com.phedi.infrastructure.persistence.party.repository;

import java.util.List;

import com.phedi.infrastructure.persistence.party.entity.PersonEntity;

interface PersonJpaRepository extends PartySpecializationJpaRepository<PersonEntity> {

    List<PersonEntity> findByFirstNameAndIsDeletedFalse(String firstName);

    List<PersonEntity> findByLastNameAndIsDeletedFalse(String lastName);
}
