package com.phedi.domain.party.exception;

/**
 * Sinaliza que um recurso do domínio foi solicitado por {@code Identifier}
 * mas não existe no repositório.
 *
 * Vive no domínio (e não na infraestrutura) porque a condição é detectada
 * durante o desenvolvimento, antes de qualquer translate de banco: a camada
 * web mapeia esta exceção para HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}