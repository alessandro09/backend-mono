package br.com.alessandro.backend.auth.api.dto;

import java.util.Set;

public record GroupResponse(String name, Set<String> roles) {
}
