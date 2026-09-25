package com.phedi.application.party.item;

import org.springframework.stereotype.Service;

import com.phedi.domain.party.exception.InvalidIdentificationException;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.DocumentType;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.domain.party.repository.item.PartyIdentityDocumentRepository;
import com.phedi.domain.party.validation.IdentificationValidationService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class PartyDocumentService implements PartyDocumentCreationService {

    private final PartyIdentityDocumentRepository partyIdentityDocumentRepository;
    private final IdentificationValidationService validationService;


    @Override
    public PartyIdentityDocument createDocument(Identifier partyIdentifier, PartyIdentityDocument document) {

        validateIdentification(document.getDocumentType(), document.getDocumentNumber());

        // Regra do agregado: só um documento primary por seção.
        // Se o novo documento é primary, o atual é demovido.
        if (Boolean.TRUE.equals(document.getPrimary())) {
            partyIdentityDocumentRepository.demotePrimary(partyIdentifier);
        }

        return partyIdentityDocumentRepository.insertFor(document, partyIdentifier);
    }

    private void validateIdentification(DocumentType documentType, String identificationNumber) {
        if (!validationService.validate(documentType.name(), identificationNumber)) {
            throw new InvalidIdentificationException(documentType.name(), identificationNumber);
        }
    }

}
