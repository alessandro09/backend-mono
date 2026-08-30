package br.com.alessandro.backend.auth.api.dto;

import java.time.Instant;

public record AuthorizationResponse(String id, String grantType, String scopes, Instant accessTokenIssuedAt,
		Instant accessTokenExpiresAt, Instant refreshTokenIssuedAt, Instant refreshTokenExpiresAt) {
}
