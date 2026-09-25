package com.phedi.infrastructure.web.common;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class ResourceLocationBuilder {

    public URI build(Object... pathVariables) {
        String currentPath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        String suffix = Arrays.stream(pathVariables)
                .map(Object::toString)
                .collect(Collectors.joining("/"));
        return URI.create(currentPath + (suffix.isBlank() ? "" : "/" + suffix));
    }

    /**
     * Monta a Location de um recurso "plano" (identificado por public_id global),
     * a partir do context-path — ex.: /api/v1/documents/{public_id}.
     */
    public URI buildFlat(String resourcePath, Object... pathVariables) {
        String suffix = Arrays.stream(pathVariables)
                .map(Object::toString)
                .collect(Collectors.joining("/"));
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(resourcePath)
                .path(suffix.isBlank() ? "" : "/" + suffix)
                .build()
                .toUri();
    }
}