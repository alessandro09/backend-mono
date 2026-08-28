package br.com.alessandro.auth.presentation;

import java.util.List;

/**
 * Response payload describing the currently authenticated principal.
 */
public record UserProfileResponse(String username, String email, List<String> authorities) {
}
