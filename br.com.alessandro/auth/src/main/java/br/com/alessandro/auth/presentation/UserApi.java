package br.com.alessandro.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * REST mapping contract for user-related endpoints. Implementations live under
 * {@code presentation.impl} so the HTTP layer stays decoupled from its contract,
 * following the hexagonal architecture "port" convention.
 */
@RequestMapping("/api/users")
public interface UserApi {

    @GetMapping("/me")
    ResponseEntity<UserProfileResponse> me(Authentication authentication);
}
