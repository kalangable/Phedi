package com.phedi.infrastructure.web.party.controller.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phedi.application.party.item.PartyDocumentService;
import com.phedi.domain.party.model.Identifier;
import com.phedi.infrastructure.web.common.ResourceLocationBuilder;
import com.phedi.infrastructure.web.party.dto.item.DocumentRequest;
import com.phedi.infrastructure.web.party.mapper.OrganizationDomainMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationDocumentController {

    private final PartyDocumentService documentCreationService;
    private final OrganizationDomainMapper mapper;
    private final ResourceLocationBuilder locationBuilder;

    @PostMapping("/{identifier}/documents")
    public ResponseEntity<Void> addDocument(@PathVariable Identifier identifier,
            @Valid @RequestBody DocumentRequest request) {
        var domain = mapper.toDomain(request);
        var created = documentCreationService.createDocument(identifier, domain);
        var location = locationBuilder.buildFlat("/documents", created.getIdentifier().value());
        return ResponseEntity.created(location).build();
    }
}
