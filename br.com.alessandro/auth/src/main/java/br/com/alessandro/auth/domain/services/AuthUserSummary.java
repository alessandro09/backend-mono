package br.com.alessandro.auth.domain.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Lightweight, cache/serialization-friendly projection of {@link br.com.alessandro.auth.domain.entities.AuthUser}.
 * Returned by {@link AuthUserService} instead of the raw JPA entity so that Redis
 * caching does not need to serialize Hibernate-managed collection proxies. Implemented
 * as a plain (non-final) class rather than a record because
 * {@link org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer}'s
 * default typing does not embed {@code @class} metadata for final classes, which would
 * otherwise break deserialization of the top-level cached value.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserSummary {

    private String username;
    private String email;
    private boolean enabled;
    private Set<String> authorities;
}
