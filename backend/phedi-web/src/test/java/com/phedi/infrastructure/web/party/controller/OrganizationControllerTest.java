package com.phedi.infrastructure.web.party.controller;

import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.phedi.application.party.organization.OrganizationService;
import com.phedi.domain.party.exception.ResourceNotFoundException;
import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;
import com.phedi.infrastructure.web.common.DomainExceptionHandler;
import com.phedi.infrastructure.web.common.GlobalExceptionHandler;
import com.phedi.infrastructure.web.common.ResourceLocationBuilder;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.mapper.OrganizationMapper;
import com.phedi.infrastructure.web.problem.ProblemDetailFactory;
import static org.instancio.Select.field;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class OrganizationControllerTest {

        RestTestClient client;

        private final Faker faker = new Faker(Locale.of("pt", "BR"));

        @Mock
        private OrganizationService organizationService;

        @Mock
        private ProblemDetailFactory detailFactory;

        @Mock
        private OrganizationMapper mapper;

        @BeforeEach
        public void setup() {
                detailFactory = new ProblemDetailFactory();
                client = RestTestClient
                        .bindToController(new OrganizationController(organizationService, mapper, new ResourceLocationBuilder()))
                        .configureServer(server -> server.setControllerAdvice(new GlobalExceptionHandler(detailFactory), new DomainExceptionHandler(detailFactory)))
                        .build();
        }

        @Test
        void shouldGetOrganizationResponseByIdentifier() {
                Organization mockOrganization = createMockOrganization();

                OrganizationResponse expected = OrganizationResponse.builder()
                        .identifier(mockOrganization.getIdentifier().value())
                        .legalName(mockOrganization.getLegalName())
                        .brandName(mockOrganization.getBrandName())
                        .isActive(true)
                        .build();

                when(organizationService.findByIdentifier(mockOrganization.getIdentifier())).thenReturn(Mockito.any());
                when(mapper.toDto(mockOrganization)).thenReturn(expected);

                log.info("Test data generated : {}", expected);

                var body = client.get()
                        .uri(uriBuilder -> uriBuilder
                                        .path("/organizations/{identifier}")
                                        .build(mockOrganization.getIdentifier().value()))
                        .header("X-Request-Id", "XXXX")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.identifier").isEqualTo(expected.getIdentifier())
                        .returnResult()
                        .getResponseBody();
                log.info("Response body: {}", new String(body, StandardCharsets.UTF_8));
        }

        private Organization createMockOrganization() {
                return Instancio.of(Organization.class)
                                .set(field(Organization::getLegalName), faker.company().name())
                                .set(field(Organization::getBrandName), faker.company().name())
                                .create();
        }

        @Test
        void shouldReturn404ByInvalidIdentifier() {
                Identifier mockIdentifier = Instancio.create(Identifier.class);

                var msgError = String.format("Organization not found: %s", mockIdentifier.value());
                when(organizationService.findByIdentifier(mockIdentifier)).thenThrow(new ResourceNotFoundException(msgError));
                var body = client.get()
                        .uri(uriBuilder -> uriBuilder
                                        .path("/organizations/{identifier}")
                                        .build(mockIdentifier.value()))
                        .header("X-Request-Id", "XXXX")
                        .exchange()
                        .expectStatus().isNotFound()
                        .expectBody()
                        .jsonPath("$.status").isEqualTo(404)
                        .jsonPath("$.title").isEqualTo("Not Found")
                        .jsonPath("$.detail").isEqualTo(msgError)
                        .jsonPath("$.instance").isEqualTo("/organizations/" + mockIdentifier.value())
                        .returnResult()
                        .getResponseBody();

                log.info("Response body: {}", new String(body, StandardCharsets.UTF_8));
        }

        @Test
        void shouldInsertOrganizationPost() {

                CreateOrganizationRequest mockCreateOrganizationRequest = Instancio.create(CreateOrganizationRequest.class);

                Organization mockOrganization = createMockOrganization();

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

        @Test
        void shouldNotInsertOrganizationPost() {

                CreateOrganizationRequest mockCreateOrganizationRequest = Instancio.create(CreateOrganizationRequest.class);
                mockCreateOrganizationRequest.setLegalName(null);

                log.info("POST Test data generated : {}", mockCreateOrganizationRequest);

                client.post()
                        .uri("/organizations")
                        .body(mockCreateOrganizationRequest)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("$.status").isEqualTo(400)
                        .jsonPath("$.title").isEqualTo("Bad Request")
                        .jsonPath("$.detail").isEqualTo("Validation failed: legalName must not be blank")
                        .jsonPath("$.errors.legalName").isEqualTo("must not be blank");
        }

}