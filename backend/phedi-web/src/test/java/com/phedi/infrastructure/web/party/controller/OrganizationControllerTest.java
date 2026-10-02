package com.phedi.infrastructure.web.party.controller;

import static org.mockito.Mockito.when;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.phedi.application.party.organization.OrganizationCreationService;
import com.phedi.application.party.organization.OrganizationService;
import com.phedi.domain.party.exception.ResourceNotFoundException;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;
import com.phedi.infrastructure.web.common.GlobalExceptionHandler;
import com.phedi.infrastructure.web.common.ResourceLocationBuilder;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.mapper.OrganizationMapper;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class OrganizationControllerTest {

    RestTestClient client;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private OrganizationMapper mapper;

    @BeforeEach
    public void setup() {
        client = RestTestClient
                .bindToController(
                        new OrganizationController(organizationService, mapper, new ResourceLocationBuilder()))
                .configureServer(server -> server.setControllerAdvice(new GlobalExceptionHandler()))
                .build();
    }

    @Test
    void shouldGetOrganizationResponseByIdentifier() {
        Organization mockOrganization = Instancio.create(Organization.class);

        OrganizationResponse expected = OrganizationResponse.builder()
                .identifier(mockOrganization.getIdentifier().value())
                .legalName(mockOrganization.getLegalName())
                .brandName(mockOrganization.getBrandName())
                .isActive(true)
                .build();

        when(organizationService.findByIdentifier(mockOrganization.getIdentifier())).thenReturn(Mockito.any());
        when(mapper.toDto(mockOrganization)).thenReturn(expected);

        log.info("Test data generated : {}", expected);

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/organizations/{identifier}")
                        .build(mockOrganization.getIdentifier().value()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(expected.getIdentifier());
    }

    @Test
    void shouldReturn404ByInvalidIdentifier() {
        Identifier mockIdentifier = Instancio.create(Identifier.class);

        var msgError = String.format("Organization not found: %s", mockIdentifier.value());
        when(organizationService.findByIdentifier(mockIdentifier))
                .thenThrow(new ResourceNotFoundException(msgError));

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/organizations/{identifier}")
                        .build(mockIdentifier.value()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.title").isEqualTo("Not Found")
                .jsonPath("$.detail").isEqualTo(msgError)
                .jsonPath("$.instance").isEqualTo("/organizations/" + mockIdentifier.value());
    }

    @Test
    void shouldInsertOrganizationPost() {

        CreateOrganizationRequest mockCreateOrganizationRequest = Instancio.create(CreateOrganizationRequest.class);

        Organization mockOrganization = Instancio.create(Organization.class);

        when(mapper.toDomain(mockCreateOrganizationRequest)).thenReturn(mockOrganization);
        when(organizationService.create(mockOrganization)).thenReturn(mockOrganization);

        log.info("POST Test data generated : {} - {}", mockCreateOrganizationRequest, mockOrganization);

        client.post()
                .uri("/organizations")
                .body(mockCreateOrganizationRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                .location("/organizations/" + mockOrganization.getIdentifier().value())
                .expectBody().isEmpty();
    }
}