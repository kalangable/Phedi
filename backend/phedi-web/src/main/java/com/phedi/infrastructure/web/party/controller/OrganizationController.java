package com.phedi.infrastructure.web.party.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phedi.application.party.organization.OrganizationService;
import com.phedi.domain.party.model.Identifier;
import com.phedi.infrastructure.web.common.ResourceLocationBuilder;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.dto.UpdateOrganizationRequest;
import com.phedi.infrastructure.web.party.mapper.OrganizationDomainMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationDomainMapper mapper;
    private final ResourceLocationBuilder locationBuilder;

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> get() {
        var result = organizationService.findAll().stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<OrganizationResponse> get(@PathVariable Identifier identifier) {
        var result = organizationService.findByIdentifier(identifier);
        return ResponseEntity.ok(mapper.toDto(result));
    }

    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody CreateOrganizationRequest organizationRequest) {
        var domain = mapper.toDomain(organizationRequest);
        var organizationCreated = organizationService.create(domain);
        var location = locationBuilder.build(organizationCreated.getIdentifier());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{identifier}")
    public ResponseEntity<Void> update(@PathVariable Identifier identifier, @Valid @RequestBody UpdateOrganizationRequest updatedOrganization){
        var domain = mapper.toDomain(identifier.value(), updatedOrganization);
        organizationService.update(domain);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> delete(@PathVariable Identifier identifier){
        organizationService.delete(identifier);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{identifier}/activate")
    public ResponseEntity<Void> activate(@PathVariable Identifier identifier){
        organizationService.activate(identifier);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{identifier}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Identifier identifier){
        organizationService.deactivate(identifier);
        return ResponseEntity.noContent().build();
    }
}
