package com.phedi.domain.party.exception;

import java.util.Collection;
import java.util.List;

/**
 * Sinaliza que um update tentou informar atributos imutáveis.
 *
 * <p>Existe para que o chamador receba um problema tipado, e não um
 * {@code IllegalStateException} genérico: quem chama (controller, consumer de
 * mensageria, CLI) precisa distinguir "recurso inexistente" de "payload com
 * campo proibido", porque os dois não se tratam igual — um é 404, o outro é
 * 400.
 *
 * <p>A exceção carrega os nomes dos campos recusados, para que a resposta ao
 * cliente possa apontar exatamente o que foi enviado.
 */
public class ImmutableAttributeUpdateException extends RuntimeException {

    private final transient List<String> attributes;

    public ImmutableAttributeUpdateException(Collection<String> attributes) {
        super(buildMessage(attributes));
        this.attributes = List.copyOf(attributes);
    }

    private static String buildMessage(Collection<String> attributes) {
        return String.format("Attributes are immutable and must not be informed on update: %s",
                String.join(", ", attributes));
    }

    /**
     * Atributos recusados, na ordem em que foram detectados.
     */
    public List<String> getAttributes() {
        return attributes;
    }
}