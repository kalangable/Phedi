package com.phedi.infrastructure.persistence.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.phedi.infrastructure.persistence.party.entity.PartyEntity;

interface PartyJpaRepository extends JpaRepository<PartyEntity, Long> {

    Boolean existsByPartyNumberAndIsDeletedFalse(String partyNumber);

    @Modifying
    @Query("Update PartyEntity p set p.isActive = false where p.partyNumber = :partyNumber and p.isDeleted = false")
    int deactivate(@Param("partyNumber") String partyNumber);

    @Modifying
    @Query("Update PartyEntity p set p.isActive = true where p.partyNumber = :partyNumber and p.isDeleted = false")
    int activate(@Param("partyNumber") String partyNumber);

    @Modifying
    @Query("Update PartyEntity p set p.isDeleted = true where p.partyNumber = :partyNumber and p.isDeleted = false")
    int softDelete(@Param("partyNumber") String partyNumber);

}
