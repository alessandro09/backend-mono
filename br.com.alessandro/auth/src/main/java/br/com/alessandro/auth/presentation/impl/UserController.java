package br.com.alessandro.auth.presentation.impl;

import br.com.alessandro.auth.domain.services.AuthUserService;
import br.com.alessandro.auth.domain.services.AuthUserSummary;
import br.com.alessandro.auth.presentation.UserApi;
import br.com.alessandro.auth.presentation.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Implementation of {@link UserApi}, exposing the currently authenticated principal.
 * Every endpoint here requires authentication, as enforced by
 * {@code br.com.alessandro.auth.config.SecurityConfig}. Delegates the profile lookup to
 * {@link AuthUserService} (Redis-cached) to enrich the response with data from the
 * {@code auth_user} table.
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final AuthUserService authUserService;

    @Override
    public ResponseEntity<UserProfileResponse> me(Authentication authentication) {
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        AuthUserSummary authUser = authUserService.findByUsername(authentication.getName());
        if (authUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return ResponseEntity.ok(new UserProfileResponse(authUser.getUsername(), authUser.getEmail(), authorities));
    }
}

