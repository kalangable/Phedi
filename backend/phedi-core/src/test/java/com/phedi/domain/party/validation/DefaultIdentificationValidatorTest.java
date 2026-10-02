package com.phedi.domain.party.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.phedi.domain.party.model.item.IdentityDocumentType;

class DefaultIdentificationValidatorTest {

    private final DefaultIdentificationValidator validator = new DefaultIdentificationValidator();

    @ParameterizedTest
    @EnumSource(IdentityDocumentType.class)
    void shouldSupportEveryType(IdentityDocumentType type) {
        assertThat(validator.supports(type)).isTrue();
    }

    @Test
    void shouldSupportAnyString() {
        assertThat(validator.supports("ANYTHING")).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " ", "\t", "\n" })
    void shouldRejectNullOrBlank(String number) {
        assertThat(validator.isValid(number)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = { "X", "123", "qualquer-coisa" })
    void shouldAcceptAnyNonBlankValue(String number) {
        // O padrão não valida formato: só garante que há algo informado.
        assertThat(validator.isValid(number)).isTrue();
    }

    @Test
    void shouldReturnValueUnchangedWhenFormatting() {
        assertThat(validator.format("qualquer-coisa")).isEqualTo("qualquer-coisa");
    }

    @Test
    void shouldReturnNullWhenFormattingNull() {
        assertThat(validator.format(null)).isNull();
    }
}
