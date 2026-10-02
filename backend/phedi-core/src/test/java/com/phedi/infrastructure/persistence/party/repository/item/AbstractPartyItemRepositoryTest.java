package com.phedi.infrastructure.persistence.party.repository.item;

import static com.phedi.support.PartyTestFixtures.cnpjDocument;
import static com.phedi.support.PartyTestFixtures.cnpjDocumentEntity;
import static com.phedi.support.PartyTestFixtures.organization;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.util.ReflectionTestUtils;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.domain.party.service.PublicIdGenerator;
import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;
import com.phedi.infrastructure.persistence.party.entity.item.PartyIdentityDocumentEntity;
import com.phedi.infrastructure.persistence.party.mapper.item.PartyIdentityDocumentPersistenceMapper;
import com.phedi.infrastructure.persistence.party.repository.base.PartyJpaRepository;

@ExtendWith(MockitoExtension.class)
class AbstractPartyItemRepositoryTest {

    @Mock
    private PartyIdentityDocumentJpaRepository itemRepository;

    @Mock
    private PartyJpaRepository partyJpaRepository;

    @Mock
    private PartyIdentityDocumentPersistenceMapper mapper;

    @Mock
    private PublicIdGenerator publicIdGenerator;

    @Mock
    private AuditorAware<String> auditorAware;

    private PartyIdentityDocumentRepositoryImpl repository;

    private static final String PARTY_PUBLIC_ID = "1000001";

    @BeforeEach
    void setUp() {
        repository = new PartyIdentityDocumentRepositoryImpl(itemRepository, partyJpaRepository, mapper);

        ReflectionTestUtils.setField(repository, "auditorAware", auditorAware);
        ReflectionTestUtils.setField(repository, "publicIdGenerator", publicIdGenerator);
    }

    @Test
    void shouldGenerateIdentifierWhenItemHasNone() {
        PartyIdentityDocument item = cnpjDocument();
        item.setIdentifier(null);

        PartyIdentityDocumentEntity entity = cnpjDocumentEntity();

        when(publicIdGenerator.generate()).thenReturn(new Identifier("1000002"));
        when(partyJpaRepository.findByPublicIdAndIsDeletedFalse(PARTY_PUBLIC_ID)).thenReturn(Optional.of(organization()));
        when(mapper.toEntity(item)).thenReturn(entity);
        when(itemRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(item);

        PartyIdentityDocument result = repository.insertFor(item, new Identifier(PARTY_PUBLIC_ID));

        assertThat(item.getIdentifier()).isEqualTo(new Identifier("1000002"));
        assertThat(result).isSameAs(item);
    }

    @Test
    void shouldPreserveExistingIdentifier() {
        PartyIdentityDocument item = cnpjDocument();
        Identifier original = item.getIdentifier();

        PartyIdentityDocumentEntity entity = cnpjDocumentEntity();

        when(partyJpaRepository.findByPublicIdAndIsDeletedFalse(PARTY_PUBLIC_ID)).thenReturn(Optional.of(organization()));
        when(mapper.toEntity(item)).thenReturn(entity);
        when(itemRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(item);

        repository.insertFor(item, new Identifier(PARTY_PUBLIC_ID));

        assertThat(item.getIdentifier()).isEqualTo(original);
        verify(publicIdGenerator, never()).generate();
    }

    @Test
    void shouldSetPartyOnEntity() {
        PartyIdentityDocument item = cnpjDocument();
        
        OrganizationEntity party = organization();
        
        PartyIdentityDocumentEntity entity = cnpjDocumentEntity();

        when(partyJpaRepository.findByPublicIdAndIsDeletedFalse(PARTY_PUBLIC_ID)).thenReturn(Optional.of(party));
        when(mapper.toEntity(item)).thenReturn(entity);
        when(itemRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(item);

        repository.insertFor(item, new Identifier(PARTY_PUBLIC_ID));

        assertThat(entity.getParty()).isSameAs(party);
        verify(itemRepository).save(entity);
    }

    @Test
    void shouldThrowWhenPartyNotFound() {
        PartyIdentityDocument item = cnpjDocument();

        item.setIdentifier(null);

        when(publicIdGenerator.generate()).thenReturn(new Identifier("1000002"));
        when(partyJpaRepository.findByPublicIdAndIsDeletedFalse(PARTY_PUBLIC_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.insertFor(item, new Identifier(PARTY_PUBLIC_ID)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Party not found: " + PARTY_PUBLIC_ID);

        verify(itemRepository, never()).save(any());
    }


    @Test
    void shouldReturnCurrentAuditor() {
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("alice"));

        assertThat(repository.currentAuditor()).isEqualTo("alice");
    }

    @Test
    void shouldFallbackToSystemWhenNoAuditor() {
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.empty());

        assertThat(repository.currentAuditor()).isEqualTo("SYSTEM");
    }
}
