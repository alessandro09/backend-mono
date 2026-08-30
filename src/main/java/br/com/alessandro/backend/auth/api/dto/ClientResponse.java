package br.com.alessandro.backend.auth.api.dto;

import java.time.Duration;
import java.util.Set;

public record ClientResponse(String id, String clientId, String clientName, Set<String> clientAuthenticationMethods,
		Set<String> authorizationGrantTypes, Set<String> redirectUris, Set<String> postLogoutRedirectUris,
		Set<String> scopes, boolean requireProofKey, boolean requireAuthorizationConsent, Duration accessTokenTtl,
		Duration refreshTokenTtl) {
}
