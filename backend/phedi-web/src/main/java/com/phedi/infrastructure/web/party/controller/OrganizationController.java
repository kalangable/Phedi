package com.phedi.infrastructure.web.party.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phedi.application.party.organization.OrganizationService;
import com.phedi.domain.party.model.PartyIdentifier;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.mapper.OrganizationDomainMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationDomainMapper mapper;

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> listAll() {
        var result = organizationService.findAll().stream().map(mapper::toDto).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{partyIdentifier}")
    public ResponseEntity<OrganizationResponse> getOrganization(@PathVariable PartyIdentifier partyIdentifier) {
        var result = organizationService.findById(partyIdentifier);
        return ResponseEntity.ok(mapper.toDto(result));
    }
}
