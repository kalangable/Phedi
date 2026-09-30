package com.phedi.domain.party.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.phedi.domain.party.model.item.IdentityDocumentType;

class CnpjValidatorTest {

    private final CnpjValidator validator = new CnpjValidator();

    private static final String VALID = "11222333000181";
    private static final String VALID_FORMATTED = "11.222.333/0001-81";

    @ParameterizedTest
    @ValueSource(strings = { VALID, VALID_FORMATTED })
    void shouldAcceptValidCnpj(String cnpj) {
        assertThat(validator.isValid(cnpj)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            // dígito verificador 1 errado
            "11222333000191",
            // dígito verificador 2 errado
            "11222333000182",
            // os dois dígitos trocados
            "11222333000118",
            // sequência que a Receita Federal rejeita
            "11111111111111",
            "00000000000000",
    })
    void shouldRejectWrongCheckDigits(String cnpj) {
        assertThat(validator.isValid(cnpj)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1122233300018",   // 13 dígitos
            "112223330001811", // 15 dígitos
            "",                // vazio
            "   ",             // só espaços
            "abc",             // sem dígitos
    })
    void shouldRejectMalformedLength(String cnpj) {
        assertThat(validator.isValid(cnpj)).isFalse();
    }

    @Test
    void shouldRejectNull() {
        assertThat(validator.isValid(null)).isFalse();
    }

    @Test
    void shouldFormatValidCnpj() {
        assertThat(validator.format(VALID)).isEqualTo(VALID_FORMATTED);
    }

    @Test
    void shouldKeepValueWhenLengthIsNotFourteen() {
        assertThat(validator.format("123")).isEqualTo("123");
    }

    @Test
    void shouldReturnNullWhenFormattingNull() {
        assertThat(validator.format(null)).isNull();
    }

    @Test
    void shouldSupportCnpj() {
        assertThat(validator.supports(IdentityDocumentType.CNPJ)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = IdentityDocumentType.class, names = "CNPJ", mode = EnumSource.Mode.EXCLUDE)
    void shouldNotSupportOtherTypes(IdentityDocumentType type) {
        assertThat(validator.supports(type)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({ "CNPJ", "cnpj", "CnPj" })
    void shouldSupportCnpjIgnoringCase(String type) {
        assertThat(validator.supports(type)).isTrue();
    }

    @Test
    void shouldNotSupportNull() {
        assertThat(validator.supports((IdentityDocumentType) null)).isFalse();
    }
}
