package br.com.alessandro.auth.domain.services;

import br.com.alessandro.auth.data.repositories.AuthUserRepository;
import br.com.alessandro.auth.domain.entities.AuthAuthority;
import br.com.alessandro.auth.domain.entities.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Business logic for querying application users. Backed by {@link AuthUserRepository}
 * and cached (via Redis) since user lookups are a frequent, low-churn operation. Returns
 * a plain {@link AuthUserSummary} rather than the JPA entity so the cached payload is a
 * simple, easily (de)serializable value rather than a Hibernate-managed proxy.
 */
@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;

    @Cacheable(cacheNames = "authUsers", key = "#username", unless = "#result == null")
    public AuthUserSummary findByUsername(String username) {
        return authUserRepository.findByUsername(username)
                .map(this::toSummary)
                .orElse(null);
    }

    private AuthUserSummary toSummary(AuthUser authUser) {
        Set<String> authorities = new LinkedHashSet<>();
        for (AuthAuthority authority : authUser.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        return new AuthUserSummary(authUser.getUsername(), authUser.getEmail(), authUser.isEnabled(), authorities);
    }
}
