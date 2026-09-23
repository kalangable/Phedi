package com.phedi.infrastructure.persistence.party.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.phedi.domain.party.model.PartyType;

@Entity 
@Table (name = "organization")
@PrimaryKeyJoinColumn (name = "id")
@EqualsAndHashCode (callSuper = false)
@Data 
public class OrganizationEntity extends PartyEntity{

    @Column(name = "legal_name", nullable = false, length = 200)
    private String legalName;

    @Column(name = "trade_name", length = 200)
    private String tradeName;

    @Column(name = "brand_name", length = 200)
    private String brandName;

    @Column(name = "founding_date")
    private LocalDate foundingDate;

    // Constructors
    public OrganizationEntity() {
        super(PartyType.ORGANIZATION);
    }

    public OrganizationEntity(String legalName) {
        super(PartyType.ORGANIZATION);
        this.legalName = legalName;
    }

    // Business methods
    public String getDisplayName() {
        return tradeName != null && !tradeName.isBlank() ? tradeName : legalName;
    }
}
