package br.com.alessandro.backend.auth.api.dto;

import java.util.Set;

public record UserResponse(Long id, String username, boolean enabled, Set<String> roles, Set<String> groups) {
}
