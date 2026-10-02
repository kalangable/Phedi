package com.phedi.application.party.organization;

import static com.phedi.support.PartyTestFixtures.organizationDomain;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.phedi.domain.party.exception.ResourceNotFoundException;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.repository.OrganizationRepository;
import com.phedi.support.PartyTestFixtures;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    private OrganizationService service;

    private static Identifier PUBLIC_ID = PartyTestFixtures.identifier();

    @BeforeEach
    void setUp() {
        service = new OrganizationService(organizationRepository);
    }

    @Test
    void shouldReturnOrganizationWhenFound() {
        Organization expected = organizationDomain();

        when(organizationRepository.findByIdentifier(PUBLIC_ID)).thenReturn(Optional.of(expected));

        Organization result = service.findByIdentifier(PUBLIC_ID);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void shouldThrowResourceNotFoundWhenNotFound() {
        when(organizationRepository.findByIdentifier(PUBLIC_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByIdentifier(PUBLIC_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Organization not found: " + PUBLIC_ID.value());
    }

    @Test
    void shouldReturnUpdatedOrganization() {
        Organization domain = organizationDomain();
        domain.setIdentifier(PUBLIC_ID);
        Organization updated = organizationDomain();

        when(organizationRepository.update(domain)).thenReturn(updated);

        assertThat(service.update(domain)).isSameAs(updated);
    }

    @Test
    void shouldPropagateResourceNotFoundWhenOrganizationNotFound() {
        Organization domain = organizationDomain();
        domain.setIdentifier(PUBLIC_ID);

        when(organizationRepository.update(domain))
                .thenThrow(new ResourceNotFoundException("Organization not found: " + PUBLIC_ID.value()));

        assertThatThrownBy(() -> service.update(domain))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Organization not found: " + PUBLIC_ID.value());
    }
}
