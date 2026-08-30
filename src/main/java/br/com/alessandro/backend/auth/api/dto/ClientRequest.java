package br.com.alessandro.backend.auth.api.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record ClientRequest(@NotBlank String clientId, String clientSecret, String clientName, Set<String> grantTypes,
		Set<String> redirectUris, Set<String> postLogoutRedirectUris, Set<String> scopes, Boolean requireProofKey,
		Boolean requireAuthorizationConsent, Long accessTokenTtlMinutes, Long refreshTokenTtlHours) {
}
