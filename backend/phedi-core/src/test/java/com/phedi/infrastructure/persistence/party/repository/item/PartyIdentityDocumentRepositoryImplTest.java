package com.phedi.infrastructure.persistence.party.repository.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.phedi.domain.party.model.Identifier;
import com.phedi.infrastructure.persistence.party.entity.item.PartyIdentityDocumentEntity;
import com.phedi.infrastructure.persistence.party.mapper.item.PartyIdentityDocumentPersistenceMapper;
import com.phedi.infrastructure.persistence.party.repository.base.PartyJpaRepository;

@ExtendWith(MockitoExtension.class)
class PartyIdentityDocumentRepositoryImplTest {

    @Mock
    private PartyIdentityDocumentJpaRepository itemRepository;

    @Mock
    private PartyJpaRepository partyJpaRepository;

    @Mock
    private PartyIdentityDocumentPersistenceMapper mapper;

    private PartyIdentityDocumentRepositoryImpl repository;

    private static final String PARTY_PUBLIC_ID = "1000001";

    @BeforeEach
    void setUp() {
        repository = new PartyIdentityDocumentRepositoryImpl(itemRepository, partyJpaRepository, mapper);
    }

    @Test
    void shouldDemoteExistingPrimaryDocument() {
        PartyIdentityDocumentEntity current = new PartyIdentityDocumentEntity();
        current.setPrimary(true);

        when(itemRepository.findByParty_PublicIdAndPrimaryTrueAndIsDeletedFalse(PARTY_PUBLIC_ID))
                .thenReturn(Optional.of(current));

        repository.demotePrimary(new Identifier(PARTY_PUBLIC_ID));

        assertThat(current.getPrimary()).isFalse();
    }

    @Test
    void shouldSaveDemotedEntity() {
        PartyIdentityDocumentEntity current = new PartyIdentityDocumentEntity();
        current.setPrimary(true);

        when(itemRepository.findByParty_PublicIdAndPrimaryTrueAndIsDeletedFalse(PARTY_PUBLIC_ID))
                .thenReturn(Optional.of(current));

        repository.demotePrimary(new Identifier(PARTY_PUBLIC_ID));

        verify(itemRepository, times(1)).save(current);
    }

    @Test
    void shouldNotSaveWhenNoPrimaryDocument() {
        when(itemRepository.findByParty_PublicIdAndPrimaryTrueAndIsDeletedFalse(PARTY_PUBLIC_ID))
                .thenReturn(Optional.empty());

        repository.demotePrimary(new Identifier(PARTY_PUBLIC_ID));

        verify(itemRepository, never()).save(any(PartyIdentityDocumentEntity.class));
    }

}
