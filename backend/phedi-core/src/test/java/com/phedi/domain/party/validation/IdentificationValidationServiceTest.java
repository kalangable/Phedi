package com.phedi.domain.party.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.phedi.domain.party.model.item.IdentityDocumentType;

@ExtendWith(MockitoExtension.class)
class IdentificationValidationServiceTest {

    @Mock
    private CnpjValidator cnpjValidator;

    @Mock
    private DefaultIdentificationValidator defaultValidator;

    private IdentificationValidationService service;

    @BeforeEach
    void setUp() {
        service = new IdentificationValidationService(List.of(defaultValidator, cnpjValidator),
                defaultValidator);
    }

    @Test
    void shouldDelegateCnpjToItsSpecificValidator() {
        when(cnpjValidator.supports(IdentityDocumentType.CNPJ)).thenReturn(true);
        when(cnpjValidator.isValid("11222333000181")).thenReturn(true);

        boolean valid = service.validate(IdentityDocumentType.CNPJ, "11222333000181");

        assertThat(valid).isTrue();
        verify(cnpjValidator).isValid("11222333000181");
        verifyNoInteractions(defaultValidator);
    }

    @Test
    void shouldFallBackToDefaultWhenNoSpecificValidatorSupports() {
        when(cnpjValidator.supports(IdentityDocumentType.CPF)).thenReturn(false);
        when(defaultValidator.isValid("12345678901")).thenReturn(true);

        boolean valid = service.validate(IdentityDocumentType.CPF, "12345678901");

        assertThat(valid).isTrue();
        verify(defaultValidator).isValid("12345678901");
    }

    @Test
    void shouldFallBackToDefaultForUnregisteredType() {
        when(defaultValidator.isValid("X-123")).thenReturn(true);

        assertThat(service.validate(IdentityDocumentType.OTHER, "X-123")).isTrue();

        verify(defaultValidator).isValid("X-123");
    }

    @Test
    void shouldNeverResolveTheDefaultValidatorAsSpecific() {
        // Mesmo com o default aceitando tudo, ele não pode "roubar" a resolução.
        when(cnpjValidator.supports(IdentityDocumentType.CNPJ)).thenReturn(false);
        when(defaultValidator.isValid("11222333000181")).thenReturn(true);

        assertThat(service.validate(IdentityDocumentType.CNPJ, "11222333000181")).isTrue();

        // O default é filtrado antes de supports() ser sequer chamado.
        verify(defaultValidator, never()).supports(anyString());
        verify(defaultValidator, never()).supports(any(IdentityDocumentType.class));
    }

    @ParameterizedTest
    @ValueSource(strings = { "CNPJ", "cnpj" })
    void shouldResolveByTypeNameAsWell(String type) {
        when(cnpjValidator.supports(type)).thenReturn(true);
        when(cnpjValidator.isValid("11222333000181")).thenReturn(true);

        assertThat(service.validate(type, "11222333000181")).isTrue();

        verify(cnpjValidator).isValid("11222333000181");
    }

    @Test
    void shouldFormatThroughTheSpecificValidator() {
        when(cnpjValidator.supports(IdentityDocumentType.CNPJ)).thenReturn(true);
        when(cnpjValidator.format("11222333000181")).thenReturn("11.222.333/0001-81");

        String formatted = service.format(IdentityDocumentType.CNPJ, "11222333000181");

        assertThat(formatted).isEqualTo("11.222.333/0001-81");
    }

    @Test
    void shouldFormatThroughDefaultWhenNoSpecificValidatorSupports() {
        when(defaultValidator.format("X-123")).thenReturn("X-123");

        assertThat(service.format(IdentityDocumentType.OTHER, "X-123")).isEqualTo("X-123");

        verify(defaultValidator).format("X-123");
    }

    @Test
    void shouldFormatByTypeNameAsWell() {
        when(cnpjValidator.supports("CNPJ")).thenReturn(true);
        when(cnpjValidator.format("11222333000181")).thenReturn("11.222.333/0001-81");

        assertThat(service.format("CNPJ", "11222333000181")).isEqualTo("11.222.333/0001-81");
    }

    @ParameterizedTest
    @EnumSource(value = IdentityDocumentType.class, names = "CNPJ", mode = EnumSource.Mode.EXCLUDE)
    void shouldRouteEveryOtherTypeToDefault(IdentityDocumentType type) {
        IdentificationValidationService service = new IdentificationValidationService(
                List.of(new DefaultIdentificationValidator()), new DefaultIdentificationValidator());

        // Sem validador específico registrado, tudo cai no padrão.
        assertThat(service.validate(type, "qualquer")).isTrue();
    }

    @Test
    void shouldNotTouchValidatorsWhenReceivingNull() {
        // Nenhum dos validadores deve rebentar ao receber tipo nulo: a resolução
        // cai no default e o formato devolve o valor inalterado.
        IdentificationValidationService service = new IdentificationValidationService(
                List.of(new DefaultIdentificationValidator()), new DefaultIdentificationValidator());

        assertThat(service.validate((IdentityDocumentType) null, "123")).isTrue();
    }
}
