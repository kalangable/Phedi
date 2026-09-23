package com.phedi.infrastructure.persistence.party.repository;

import java.util.List;

import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;

interface OrganizationJpaRepository extends PartySpecializationJpaRepository<OrganizationEntity> {

    // Find by legal name
    List<OrganizationEntity> findByLegalNameAndIsDeletedFalse(String legalName);

    // Find by trade name
    List<OrganizationEntity> findByTradeNameAndIsDeletedFalse(String tradeName);
}
