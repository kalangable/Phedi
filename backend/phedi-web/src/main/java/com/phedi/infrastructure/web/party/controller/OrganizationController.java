package com.phedi.infrastructure.web.party.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phedi.application.party.organization.OrganizationService;
import com.phedi.domain.party.model.PartyIdentifier;
import com.phedi.infrastructure.web.common.ResourceLocationBuilder;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.mapper.OrganizationDomainMapper;

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

    @GetMapping("/{partyIdentifier}")
    public ResponseEntity<OrganizationResponse> get(@PathVariable PartyIdentifier partyIdentifier) {
        var result = organizationService.findById(partyIdentifier);
        return ResponseEntity.ok(mapper.toDto(result));
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody CreateOrganizationRequest organizationRequest) {
        var domain = mapper.toDomain(organizationRequest);
        var organizationCreated = organizationService.create(domain);
        var location = locationBuilder.build(organizationCreated.getPartyIdentifier());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{partyIdentifier}")
    public ResponseEntity<Void> delete(@PathVariable PartyIdentifier partyIdentifier){
        organizationService.delete(partyIdentifier);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{partyIdentifier}/activate")
    public ResponseEntity<Void> activate(@PathVariable PartyIdentifier partyIdentifier){
        organizationService.activate(partyIdentifier);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{partyIdentifier}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable PartyIdentifier partyIdentifier){
        organizationService.deactivate(partyIdentifier);
        return ResponseEntity.noContent().build();
    }
}
