package com.phedi.application.party.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phedi.application.party.Deletable;
import com.phedi.application.party.Findable;
import com.phedi.application.party.StatusChangeable;
import com.phedi.application.party.Updatable;
import com.phedi.domain.party.exception.ImmutableAttributeUpdateException;
import com.phedi.domain.party.exception.InvalidIdentificationException;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.IdentityDocumentType;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.domain.party.repository.item.PartyIdentityDocumentRepository;
import com.phedi.domain.party.validation.IdentificationValidationService;

import lombok.RequiredArgsConstructor;

@Service 
@Transactional
@RequiredArgsConstructor
public class PartyIdentityDocumentService implements PartyItemCreatable<PartyIdentityDocument>, Findable<PartyIdentityDocument>, PartyItemListable<PartyIdentityDocument>, Updatable<PartyIdentityDocument>, StatusChangeable, Deletable {

    private final PartyIdentityDocumentRepository partyIdentityDocumentRepository;
    private final IdentificationValidationService validationService;

    @Override
    public PartyIdentityDocument createItem(Identifier partyIdentifier, PartyIdentityDocument identityDocument) {

        checkIdentificationDataPresent(identityDocument);
        validateIdentification(identityDocument.getIdentityDocumentType(), identityDocument.getIdentityDocumentNumber());

        if (Boolean.TRUE.equals(identityDocument.getPrimary())) {
            partyIdentityDocumentRepository.demotePrimary(partyIdentifier);
        }

        return partyIdentityDocumentRepository.insertFor(identityDocument, partyIdentifier);
    }

    private void checkIdentificationDataPresent(PartyIdentityDocument identityDocument) {
        if (identityDocument.getIdentityDocumentType() == null) {
            throw new InvalidIdentificationException("Identity Document Type is required");
        }
        if(identityDocument.getIdentityDocumentNumber() == null || identityDocument.getIdentityDocumentNumber().isBlank()){
            throw new InvalidIdentificationException("Identity Document is required");
        }
    }

    private void validateIdentification(IdentityDocumentType identificationType, String identificationNumber) {
        if (!validationService.validate(identificationType.name(), identificationNumber)) {
            throw new InvalidIdentificationException(identificationType.name(), identificationNumber);
        }
    }

    @Override
    public PartyIdentityDocument findByIdentifier(Identifier identifier) {
        return partyIdentityDocumentRepository.findByIdentifier(identifier).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public List<PartyIdentityDocument> findAllByParty(Identifier partyIdentifier) {
        return partyIdentityDocumentRepository.findAllByParty(partyIdentifier);
    }

    @Override
    public PartyIdentityDocument update(PartyIdentityDocument identityDocument) {
        List<String> informed = new ArrayList<>();
        if (identityDocument.getPrimary() != null) {
            informed.add("primary");
        }
        if (identityDocument.getIdentityDocumentType() != null) {
            informed.add("identityDocumentType");
        }
        if (identityDocument.getIdentityDocumentNumber() != null) {
            informed.add("identityDocumentNumber");
        }
        if (!informed.isEmpty()) {
            throw new ImmutableAttributeUpdateException(informed);
        }
        return partyIdentityDocumentRepository.update(identityDocument);
    }

    @Override
    public void delete(Identifier identifier) {
        partyIdentityDocumentRepository.deleteByIdentifier(identifier);
    }

    @Override
    public void activate(Identifier identifier) {
        partyIdentityDocumentRepository.activate(identifier);
    }

    @Override
    public void deactivate(Identifier identifier) {
        partyIdentityDocumentRepository.deactivate(identifier);
    }

}
