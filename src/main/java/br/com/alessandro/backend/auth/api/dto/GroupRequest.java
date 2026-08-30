package br.com.alessandro.backend.auth.api.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record GroupRequest(@NotBlank String name, Set<String> roles) {
}
