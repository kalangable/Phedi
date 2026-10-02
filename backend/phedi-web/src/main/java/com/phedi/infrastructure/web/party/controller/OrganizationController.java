package com.phedi.infrastructure.web.party.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phedi.application.party.organization.OrganizationService;
import com.phedi.domain.party.model.Identifier;
import com.phedi.infrastructure.web.common.ResourceLocationBuilder;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.dto.UpdateOrganizationRequest;
import com.phedi.infrastructure.web.party.mapper.OrganizationMapper;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationMapper mapper;
    private final ResourceLocationBuilder locationBuilder;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrganizationResponse> get() {
        return organizationService.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{identifier}")
    @ResponseStatus(HttpStatus.OK)
    public OrganizationResponse get(@PathVariable Identifier identifier) {
        var result = organizationService.findByIdentifier(identifier);
        return mapper.toDto(result);
    }

    @ApiResponse(responseCode = "201", description = "Organization created successfully")
    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody CreateOrganizationRequest organizationRequest) {
        var domain = mapper.toDomain(organizationRequest);
        var organizationCreated = organizationService.create(domain);
        var location = locationBuilder.build(organizationCreated.getIdentifier());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{identifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Identifier identifier,
            @Valid @RequestBody UpdateOrganizationRequest updatedOrganization) {
        var domain = mapper.toDomain(identifier.value(), updatedOrganization);
        organizationService.update(domain);
    }

    @DeleteMapping("/{identifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Identifier identifier) {
        organizationService.delete(identifier);
    }

    @PostMapping("/{identifier}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Identifier identifier) {
        organizationService.activate(identifier);
    }

    @PostMapping("/{identifier}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Identifier identifier) {
        organizationService.deactivate(identifier);
    }
}
