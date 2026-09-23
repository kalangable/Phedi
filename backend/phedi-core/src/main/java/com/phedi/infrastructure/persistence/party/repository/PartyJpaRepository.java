package com.phedi.infrastructure.persistence.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.phedi.infrastructure.persistence.party.entity.PartyEntity;

interface PartyJpaRepository extends JpaRepository<PartyEntity, Long> {

    Boolean existsByPublicIdAndIsDeletedFalse(String publicId);

    @Modifying
    @Query("Update PartyEntity p set p.isActive = false, p.updatedAt = CURRENT_TIMESTAMP where p.publicId = :publicId and p.isActive = true and p.isDeleted = false")
    int deactivate(@Param("publicId") String publicId);

    @Modifying
    @Query("Update PartyEntity p set p.isActive = true, p.updatedAt = CURRENT_TIMESTAMP where p.publicId = :publicId and p.isActive = false and p.isDeleted = false")
    int activate(@Param("publicId") String publicId);

    @Modifying
    @Query("Update PartyEntity p set p.isDeleted = true , p.deletedAt = CURRENT_TIMESTAMP where p.publicId = :publicId and p.isDeleted = false")
    int softDelete(@Param("publicId") String publicId);

}
