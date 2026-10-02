package com.phedi.application.party.organization;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phedi.domain.party.exception.ResourceNotFoundException;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationService implements OrganizationCreationService, OrganizationUpdateService, OrganizationDeletionService, OrganizationQueryService, OrganizationStatusChangeService {

    private final OrganizationRepository organizationRepository;

    @Override
    public Organization create(Organization organization) {
        return organizationRepository.insert(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findByIdentifier(Identifier identifier) {
        return organizationRepository.findByIdentifier(identifier)
                .orElseThrow(() -> {
                    var msg = String.format("Organization not found: %s", identifier.value());
                    log.debug(msg);
                    return new ResourceNotFoundException(msg);
                    });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    /**
     * Atualiza uma organização existente.
     *
     * A existência do registro NÃO é verificada aqui: a delegação é pura e o
     * repositório é quem rejeita a operação. {@code AbstractPartyBaseRepository.update}
     * exige o identificador no domínio ({@code IllegalArgumentException} se
     * ausente) e falha com {@link ResourceNotFoundException} quando a organização
     * não existe. O serviço apenas propaga essas exceções.
     */
    @Override
    public Organization update(Organization updatedOrganization) {
        return organizationRepository.update(updatedOrganization);
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
