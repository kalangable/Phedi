package com.phedi.infrastructure.web.party.dto;

import java.time.LocalDate;
import java.util.List;

import com.phedi.infrastructure.web.party.dto.item.AddressRequest;
import com.phedi.infrastructure.web.party.dto.item.ContactRequest;
import com.phedi.infrastructure.web.party.dto.item.IdentityDocumentRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequest {

    @NotBlank
    @Size(max = 200)
    private String legalName;

    @Size(max = 200)
    private String tradeName;

    @Size(max = 200)
    private String brandName;

    private LocalDate foundingDate;

    /**
     * Aceitos pelo payload mas ainda nao persistidos: nao existe repositorio
     * de contatos. O {@code OrganizationMapper} ignora a lista de forma
     * explicita. Enquanto isso, o campo so cumpre papel de documentacao do
     * contrato — e valida-lo e um trabalho jogado fora.
     */
    @Valid
    private List<ContactRequest> contacts;

    /**
     * Aceitos pelo payload mas ainda nao persistidos: nao existe repositorio
     * de enderecos. Ver {@link #contacts}.
     */
    @Valid
    private List<AddressRequest> addresses;

    /**
     * Documentos de identificacao aninhados: sao persistidos junto com a
     * organizacao, cada um com o seu identificador gerado e respeitando a
     * regra de primary. Exigir ao menos um mantem a promessa de que toda
     * organizacao nasce identificada.
     */
    @Valid
    @NotEmpty
    private List<IdentityDocumentRequest> documents;

}
