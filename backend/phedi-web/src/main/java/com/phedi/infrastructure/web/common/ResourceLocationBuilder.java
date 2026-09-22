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
}