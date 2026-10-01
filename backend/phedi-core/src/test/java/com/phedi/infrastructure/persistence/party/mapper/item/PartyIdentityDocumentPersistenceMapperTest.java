package com.phedi.infrastructure.persistence.party.mapper.item;

import static com.phedi.support.PartyTestFixtures.cnpj;
import static com.phedi.support.PartyTestFixtures.document;
import static com.phedi.support.PartyTestFixtures.documentEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.phedi.domain.party.model.item.IdentityDocumentType;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.infrastructure.persistence.party.entity.item.PartyIdentityDocumentEntity;


class PartyIdentityDocumentPersistenceMapperTest {

    private PartyIdentityDocumentPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PartyIdentityDocumentPersistenceMapperImpl();
    }

    @Test
    void shouldNotPromoteWhenDomainIsPrimary() {
        PartyIdentityDocumentEntity entity = documentEntity(IdentityDocumentType.CNPJ, cnpj(), false);
        PartyIdentityDocument domain = document(IdentityDocumentType.CNPJ, cnpj(), true);

        mapper.updateEntity(domain, entity);

        // O domain pediu primary=true; a entidade precisa continuar false.
        assertThat(entity.getPrimary()).isFalse();
    }

    @Test
    void shouldNotDemoteWhenDomainIsNotPrimary() {
        PartyIdentityDocumentEntity entity = documentEntity(IdentityDocumentType.CNPJ, cnpj(), true);
        PartyIdentityDocument domain = document(IdentityDocumentType.CNPJ, cnpj(), false);

        mapper.updateEntity(domain, entity);

        assertThat(entity.getPrimary()).isTrue();
    }

    @Test
    void shouldPreservePrimaryWhenDomainLeavesItAbsent() {
        PartyIdentityDocumentEntity entity = documentEntity(IdentityDocumentType.CNPJ, cnpj(), true);
        PartyIdentityDocument domain = document(IdentityDocumentType.CNPJ, cnpj(), false);
        domain.setPrimary(null);

        mapper.updateEntity(domain, entity);

        assertThat(entity.getPrimary()).isTrue();
    }

    @Test
    void shouldApplyIdentityDocumentTypeWhenItChanges() {
        PartyIdentityDocumentEntity entity = documentEntity(IdentityDocumentType.CNPJ, "11222333000181", false);

        PartyIdentityDocument domain = document(IdentityDocumentType.CPF, "11222333000181", false);

        mapper.updateEntity(domain, entity);

        assertThat(entity.getIdentityDocumentType()).isEqualTo(IdentityDocumentType.CNPJ);
    }

    @Test
    void shouldApplyFieldsPresentInTheDomain() {
        PartyIdentityDocumentEntity entity = documentEntity(IdentityDocumentType.CNPJ, "11222333000181", false);

        PartyIdentityDocument domain = document(IdentityDocumentType.CNPJ, "11222333000182", false);

        domain.setCountryCode("BR");
        domain.setIssuerRegion("SP");
        domain.setIssuingAuthority("Receita Federal");
        domain.setIssuedAt(LocalDate.of(2020, 1, 1));
        domain.setExpiresAt(LocalDate.of(2030, 1, 1));

        mapper.updateEntity(domain, entity);

        assertThat(entity.getIdentityDocumentNumber()).isEqualTo("11222333000181");
        assertThat(entity.getCountryCode()).isEqualTo("BR");
        assertThat(entity.getIssuerRegion()).isEqualTo("SP");
        assertThat(entity.getIssuingAuthority()).isEqualTo("Receita Federal");
        assertThat(entity.getIssuedAt()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(entity.getExpiresAt()).isEqualTo(LocalDate.of(2030, 1, 1));
    }

    @Test
    void shouldPreserveFieldsAbsentFromTheDomain() {

        PartyIdentityDocumentEntity entity = documentEntity(IdentityDocumentType.CNPJ, "11222333000181", false);
        entity.setIssuerRegion("SP");

        PartyIdentityDocument domain = document(IdentityDocumentType.CNPJ, "11222333000182", false);
        domain.setIdentityDocumentNumber(null);
        domain.setIssuerRegion(null);

        mapper.updateEntity(domain, entity);

        assertThat(entity.getIdentityDocumentNumber()).isEqualTo("11222333000181");
        assertThat(entity.getIssuerRegion()).isEqualTo("SP");
    }
}