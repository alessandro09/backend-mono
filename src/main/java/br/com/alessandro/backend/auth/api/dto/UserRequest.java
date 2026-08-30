package br.com.alessandro.backend.auth.api.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(@NotBlank String username, String password, Boolean enabled, Set<String> roles,
		Set<String> groups) {
}
