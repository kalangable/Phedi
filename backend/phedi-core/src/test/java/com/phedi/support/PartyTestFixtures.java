package com.phedi.support;

import static org.instancio.Instancio.gen;
import static org.instancio.Select.field;

import org.instancio.Instancio;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.PartyType;
import com.phedi.domain.party.model.item.IdentityDocumentType;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;
import com.phedi.infrastructure.persistence.party.entity.PartyEntity;
import com.phedi.infrastructure.persistence.party.entity.item.PartyIdentityDocumentEntity;

public final class PartyTestFixtures {

    private PartyTestFixtures() {
    }

    public static OrganizationEntity organization() {
        return Instancio.of(OrganizationEntity.class)
                .set(field(PartyEntity::getPartyType), PartyType.ORGANIZATION)
                .set(field(PartyEntity::getIsActive), true)
                .set(field(PartyEntity::getIsDeleted), false)
                .create();
    }

    public static PartyIdentityDocumentEntity cnpjDocumentEntity() {
        return documentEntity(IdentityDocumentType.CNPJ, cnpj(), true);
    }

    public static PartyIdentityDocument cnpjDocument() {
        return document(IdentityDocumentType.CNPJ, cnpj(), true);
    }

    public static PartyIdentityDocumentEntity documentEntity(IdentityDocumentType type, String number, boolean primary) {
        return Instancio.of(PartyIdentityDocumentEntity.class)
                .set(field(PartyIdentityDocumentEntity::getParty), (PartyEntity) organization())
                .set(field(PartyIdentityDocumentEntity::getIdentityDocumentType), type)
                .set(field(PartyIdentityDocumentEntity::getIdentityDocumentNumber), number)
                .set(field(PartyIdentityDocumentEntity::getPrimary), primary)
                .set(field(PartyIdentityDocumentEntity::getIsActive), true)
                .set(field(PartyIdentityDocumentEntity::getIsDeleted), false)
                .create();
    }

    public static PartyIdentityDocument document(IdentityDocumentType type, String number, boolean primary) {
        return Instancio.of(PartyIdentityDocument.class)
                .set(field(PartyIdentityDocument::getIdentifier), identifier())
                .set(field(PartyIdentityDocument::getIdentityDocumentType), type)
                .set(field(PartyIdentityDocument::getIdentityDocumentNumber), number)
                .set(field(PartyIdentityDocument::getPrimary), primary)
                .set(field(PartyIdentityDocument::getIsActive), true)
                .create();
    }

    // ------------------------------------------------- Identificadores (BR)

    public static Identifier identifier(){
        return new Identifier(gen().text().uuid().get());
    }

    public static String cnpj() {
        return gen().id().bra().cnpj().formatted().get();
    }
}
