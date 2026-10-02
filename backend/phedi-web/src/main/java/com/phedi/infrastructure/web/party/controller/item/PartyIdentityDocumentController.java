package com.phedi.infrastructure.web.party.controller.item;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phedi.application.party.item.PartyIdentityDocumentService;
import com.phedi.domain.party.model.Identifier;
import com.phedi.infrastructure.web.common.ResourceLocationBuilder;
import com.phedi.infrastructure.web.party.dto.item.IdentityDocumentRequest;
import com.phedi.infrastructure.web.party.dto.item.IdentityDocumentResponse;
import com.phedi.infrastructure.web.party.mapper.IdentityDocumentMapper;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parties")
@Tag(name = "Organization", description = "the Organization Api")
@RequiredArgsConstructor
public class PartyIdentityDocumentController {

    private final PartyIdentityDocumentService identityDocumentService;
    private final IdentityDocumentMapper mapper;
    private final ResourceLocationBuilder locationBuilder;

    @ApiResponse(responseCode = "201", description = "Identity Document created successfully")
    @PostMapping("/{identifier}/documents")
    public ResponseEntity<Void> addIdentityDocument(@PathVariable Identifier identifier, @Valid @RequestBody IdentityDocumentRequest request) {
        var domain = mapper.toDomain(request);
        var created = identityDocumentService.createItem(identifier, domain);
        var location = locationBuilder.buildFlat("/documents", created.getIdentifier().value());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{identifier}/documents")
    @ResponseStatus(HttpStatus.OK)
    public List<IdentityDocumentResponse> get(@PathVariable Identifier identifier) {
        return identityDocumentService.findAllByParty(identifier).stream().map(mapper::toDto).toList();
    }

}
