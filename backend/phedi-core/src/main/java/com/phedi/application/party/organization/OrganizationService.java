package com.phedi.application.party.organization;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phedi.application.party.item.PartyIdentityDocumentService;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.domain.party.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationService implements OrganizationCreationService, OrganizationUpdateService,
        OrganizationDeletionService, OrganizationQueryService, OrganizationStatusChangeService {

    private final OrganizationRepository organizationRepository;
    private final PartyIdentityDocumentService partyIdentityDocumentService;

    @Override
    public Organization create(Organization organization) {
        Organization created = organizationRepository.insert(organization);

        // O insert devolve o objeto remontado a partir da entidade, que nao tem
        // colecoes. Os documentos vem do objeto de entrada; o identificador
        // gerado, do retorno.
        Identifier partyIdentifier = created.getIdentifier();

        for (PartyIdentityDocument document : organization.getDocuments()) {
            partyIdentityDocumentService.createItem(partyIdentifier, document);
        }

        // createItem escreve o identificador no objeto que recebeu, ou seja no
        // mesmo objeto de entrada. Devolve-lo no agregado criado evita responder
        // "criei uma organizacao sem documentos" logo depois de ter criado tres.
        created.setDocuments(organization.getDocuments());

        return created;
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findByIdentifier(Identifier identifier) {
        return organizationRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Organization not found: %s", identifier)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization update(Organization updatedOrganization) {

        Organization existingOrganization = findByIdentifier(updatedOrganization.getIdentifier());

        // Validate new identification if changed
        checkUpdateRoles(updatedOrganization, existingOrganization);

        return organizationRepository.update(updatedOrganization);
    }

    protected void checkUpdateRoles(Organization updatedOrganization, Organization existingOrganization) {

        log.info("Compare Organizations [{}] [{}]", updatedOrganization, existingOrganization);
    }

    @Override
    public void delete(Identifier identifier) {
        organizationRepository.deleteByIdentifier(identifier);
    }

    @Override
    public void activate(Identifier identifier) {
        organizationRepository.activate(identifier);
    }

    @Override
    public void deactivate(Identifier identifier) {
        organizationRepository.deactivate(identifier);
    }

}
