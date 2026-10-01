package com.phedi.application.party.item;

import static com.phedi.support.PartyTestFixtures.cnpj;
import static com.phedi.support.PartyTestFixtures.document;
import static com.phedi.support.PartyTestFixtures.identifier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.phedi.domain.party.exception.ImmutableAttributeUpdateException;
import com.phedi.domain.party.exception.InvalidIdentificationException;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.IdentityDocumentType;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.domain.party.repository.item.PartyIdentityDocumentRepository;
import com.phedi.domain.party.validation.IdentificationValidationService;

@ExtendWith(MockitoExtension.class)
class PartyIdentityDocumentServiceTest {

    @Mock
    private PartyIdentityDocumentRepository partyIdentityDocumentRepository;

    @Mock
    private IdentificationValidationService validationService;

    private PartyIdentityDocumentService service;

    private static final Identifier PARTY_ID = identifier();

    @BeforeEach
    void setUp() {
        service = new PartyIdentityDocumentService(partyIdentityDocumentRepository, validationService);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "XPTO", // nenhum dígito
            "abc", // lixo alfabético
            "11111111111111", // sequência que a Receita Federal rejeita
            "11222333000182", // formato correto, dígito verificador errado
    })
    void shouldThrowInvalidIdentificationWhenNumberIsRejected(String invalidNumber) {
        // primary = true: se a validação falhasse tarde, demotePrimary() já teria
        // rodado.
        PartyIdentityDocument document = document(IdentityDocumentType.CNPJ, invalidNumber, true);

        when(validationService.validate(IdentityDocumentType.CNPJ.name(), invalidNumber)).thenReturn(false);

        assertThatThrownBy(() -> service.createItem(PARTY_ID, document))
                .isInstanceOf(InvalidIdentificationException.class)
                .hasMessage("Invalid CNPJ: " + invalidNumber);

        // Nenhum efeito colateral: nem demotePrimary(), nem insertFor().
        verifyNoInteractions(partyIdentityDocumentRepository);
    }

    @Test
    void shouldDemoteExistingPrimaryWhenNewDocumentIsPrimary() {
        String validNumber = cnpj();
        PartyIdentityDocument document = document(IdentityDocumentType.CNPJ, validNumber, true);
        PartyIdentityDocument inserted = document(IdentityDocumentType.CNPJ, validNumber, true);

        when(validationService.validate(IdentityDocumentType.CNPJ.name(), validNumber)).thenReturn(true);
        when(partyIdentityDocumentRepository.insertFor(document, PARTY_ID)).thenReturn(inserted);

        assertThat(service.createItem(PARTY_ID, document)).isSameAs(inserted);

        InOrder inOrder = inOrder(partyIdentityDocumentRepository);
        // A ordem é o que importa. demotePrimary() busca por
        // primary=true no party; se rodasse DEPOIS do insert, ele encontraria o
        // documento novo e derrubaria ele também — o party ficaria sem primário.
        inOrder.verify(partyIdentityDocumentRepository).demotePrimary(PARTY_ID);
        inOrder.verify(partyIdentityDocumentRepository).insertFor(document, PARTY_ID);
    }

    @Test
    void shouldNotDemoteWhenNewDocumentIsNotPrimary() {
        String validNumber = cnpj();
        PartyIdentityDocument document = document(IdentityDocumentType.CNPJ, validNumber, false);
        PartyIdentityDocument inserted = document(IdentityDocumentType.CNPJ, validNumber, false);

        when(validationService.validate(IdentityDocumentType.CNPJ.name(), validNumber)).thenReturn(true);
        when(partyIdentityDocumentRepository.insertFor(document, PARTY_ID)).thenReturn(inserted);

        assertThat(service.createItem(PARTY_ID, document)).isSameAs(inserted);

        // Segundário não mexe no primário existente — mas ainda é inserido.
        verify(partyIdentityDocumentRepository, never()).demotePrimary(PARTY_ID);
        verify(partyIdentityDocumentRepository).insertFor(document, PARTY_ID);
    }

    @Test
    void shouldNotDemoteWhenPrimaryIsNull() {
        // primary é Boolean sem @NotNull; Boolean.TRUE.equals(null) resolve para
        // false, então null se comporta como secundário.
        String validNumber = cnpj();
        PartyIdentityDocument document = document(IdentityDocumentType.CNPJ, validNumber, false);
        document.setPrimary(null);

        when(validationService.validate(IdentityDocumentType.CNPJ.name(), validNumber)).thenReturn(true);
        when(partyIdentityDocumentRepository.insertFor(document, PARTY_ID)).thenReturn(document);

        service.createItem(PARTY_ID, document);

        verify(partyIdentityDocumentRepository, never()).demotePrimary(PARTY_ID);
    }

    @Test
    void shouldRejectPromotionToPrimaryOrTypeDocumetOrDocumentNumberOnUpdate() {
        PartyIdentityDocument document = document(IdentityDocumentType.CNPJ, cnpj(), true);

        assertThatThrownBy(() -> service.update(document))
                .isInstanceOf(ImmutableAttributeUpdateException.class)
                .hasMessageContaining(
                        "immutable and must not be informed on update: primary, identityDocumentType, identityDocumentNumber");

        // A rejeição tem que acontecer antes de qualquer escrita.
        verifyNoInteractions(partyIdentityDocumentRepository);
    }

    @Test
    void shouldRejectDemotionOnUpdate() {
        PartyIdentityDocument document = document(IdentityDocumentType.CNPJ, cnpj(), false);

        assertThatThrownBy(() -> service.update(document))
                .isInstanceOf(ImmutableAttributeUpdateException.class)
                .hasMessageContaining("immutable and must not be informed on update");

        verifyNoInteractions(partyIdentityDocumentRepository);
    }

    @Test
    void shouldAllowUpdateWhenIdentificationIsWhollyAbsent() {
        // O caso real do patch parcial: {"expiresAt": "2030-01-01"} e nada mais.
        PartyIdentityDocument document = document(IdentityDocumentType.CNPJ, cnpj(), false);
        document.setPrimary(null);
        document.setIdentityDocumentType(null);
        document.setIdentityDocumentNumber(null);
        document.setExpiresAt(LocalDate.of(2030, 1, 1));

        when(partyIdentityDocumentRepository.update(document)).thenReturn(document);

        assertThat(service.update(document)).isSameAs(document);
    }
}