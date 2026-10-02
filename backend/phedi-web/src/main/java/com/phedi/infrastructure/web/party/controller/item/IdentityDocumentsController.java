package com.phedi.infrastructure.web.party.controller.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phedi.application.party.item.PartyIdentityDocumentService;
import com.phedi.domain.party.model.Identifier;
import com.phedi.infrastructure.web.party.dto.item.IdentityDocumentResponse;
import com.phedi.infrastructure.web.party.dto.item.UpdateIdentityDocumentRequest;
import com.phedi.infrastructure.web.party.mapper.IdentityDocumentMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class IdentityDocumentsController {

    private final PartyIdentityDocumentService identityDocumentService;
    private final IdentityDocumentMapper mapper;

    @GetMapping("/{identifier}")
    @ResponseStatus(HttpStatus.OK)
    public IdentityDocumentResponse get(@PathVariable Identifier identifier) {
        var result = identityDocumentService.findByIdentifier(identifier);
        return mapper.toDto(result);
    }

    @PatchMapping("/{identifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Identifier identifier,  @Valid @RequestBody UpdateIdentityDocumentRequest documentRequest) {
        var domain = mapper.toDomain(identifier.value(), documentRequest);
        identityDocumentService.update(domain);
    }

    @DeleteMapping("/{identifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Identifier identifier){
        identityDocumentService.delete(identifier);
    }

    @PostMapping("/{identifier}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Identifier identifier){
        identityDocumentService.activate(identifier);
    }

    @PostMapping("/{identifier}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Identifier identifier){
        identityDocumentService.deactivate(identifier);
    }
}
