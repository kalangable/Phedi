package com.phedi.infrastructure.web.common;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class ResourceLocationBuilder {

    public URI build(Object... pathVariables) {
        String path = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(suffixOf(pathVariables))
                .build()
                .getPath();

        return URI.create(path);
    }

    public URI buildFlat(String resourcePath, Object... pathVariables) {
        String path = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(resourcePath)
                .path(suffixOf(pathVariables))
                .build()
                .getPath();

        return URI.create(path);
    }

    private static String suffixOf(Object... pathVariables) {
        String suffix = Arrays.stream(pathVariables)
                .map(Object::toString)
                .collect(Collectors.joining("/"));

        return suffix.isBlank() ? "" : "/" + suffix;
    }

}
