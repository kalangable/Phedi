package com.phedi.application.party.item;

import org.springframework.stereotype.Service;

import com.phedi.domain.party.exception.InvalidIdentificationException;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.IdentityDocumentType;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.domain.party.repository.item.PartyIdentityDocumentRepository;
import com.phedi.domain.party.validation.IdentificationValidationService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class PartyIdentityDocumentService implements PartyIdentityDocumentCreationService {

    private final PartyIdentityDocumentRepository partyIdentityDocumentRepository;
    private final IdentificationValidationService validationService;


    @Override
    public PartyIdentityDocument createIdentityDocument(Identifier partyIdentifier, PartyIdentityDocument identityDocument) {

        validateIdentification(identityDocument.getIdentityDocumentType(), identityDocument.getIdentityDocumentNumber());

        // Regra do agregado: só um documento primary por seção.
        // Se o novo documento é primary, o atual é demovido.
        if (Boolean.TRUE.equals(identityDocument.getPrimary())) {
            partyIdentityDocumentRepository.demotePrimary(partyIdentifier);
        }

        return partyIdentityDocumentRepository.insertFor(identityDocument, partyIdentifier);
    }

    private void validateIdentification(IdentityDocumentType identityDocumentType, String identificationNumber) {
        if (!validationService.validate(identityDocumentType.name(), identificationNumber)) {
            throw new InvalidIdentificationException(identityDocumentType.name(), identificationNumber);
        }
    }

}
