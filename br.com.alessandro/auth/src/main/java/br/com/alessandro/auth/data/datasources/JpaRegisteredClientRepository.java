package br.com.alessandro.auth.data.datasources;

import br.com.alessandro.auth.config.AuthorizationServerObjectMapperFactory;
import br.com.alessandro.auth.data.repositories.OAuthClientRepository;
import br.com.alessandro.auth.domain.entities.OAuthClient;
import br.com.alessandro.auth.domain.entities.OAuthClientScope;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA-backed implementation of {@link RegisteredClientRepository}. Replaces the
 * default in-memory/JDBC implementations, persisting registered OAuth2 clients
 * through {@link OAuthClientRepository}.
 */
@Slf4j
@Repository
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final OAuthClientRepository oAuthClientRepository;
    private final ObjectMapper authorizationServerObjectMapper;

    public JpaRegisteredClientRepository(OAuthClientRepository oAuthClientRepository) {
        this.oAuthClientRepository = oAuthClientRepository;
        this.authorizationServerObjectMapper = AuthorizationServerObjectMapperFactory.create();
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        OAuthClient entity = oAuthClientRepository.findByClientId(registeredClient.getClientId())
                .orElseGet(OAuthClient::new);
        toEntity(registeredClient, entity);
        oAuthClientRepository.save(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        try {
            UUID uuid = UUID.fromString(id);
            return oAuthClientRepository.findById(uuid).map(this::toObject).orElse(null);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return oAuthClientRepository.findByClientId(clientId).map(this::toObject).orElse(null);
    }

    private RegisteredClient toObject(OAuthClient entity) {
        Set<String> grantTypes = splitToSet(entity.getAuthorizationGrantTypes());
        Set<String> authMethods = splitToSet(entity.getAuthenticationMethods());
        Set<String> scopes = entity.getScopes().stream()
                .map(OAuthClientScope::getScope)
                .collect(Collectors.toSet());
        Set<String> redirectUris = splitToSet(entity.getRedirectUris());
        Set<String> postLogoutRedirectUris = splitToSet(entity.getPostLogoutRedirectUris());

        RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId().toString())
                .clientId(entity.getClientId())
                .clientIdIssuedAt(entity.getClientIdIssuedAt())
                .clientSecret(entity.getClientSecret())
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
                .clientName(entity.getClientName())
                .clientSettings(parseClientSettings(entity.getClientSettings()))
                .tokenSettings(parseTokenSettings(entity.getTokenSettings()));

        grantTypes.forEach(gt -> builder.authorizationGrantType(new AuthorizationGrantType(gt)));
        authMethods.forEach(am -> builder.clientAuthenticationMethod(new ClientAuthenticationMethod(am)));
        redirectUris.forEach(builder::redirectUri);
        postLogoutRedirectUris.forEach(builder::postLogoutRedirectUri);
        scopes.forEach(builder::scope);

        return builder.build();
    }

    private void toEntity(RegisteredClient registeredClient, OAuthClient entity) {
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt() != null
                ? registeredClient.getClientIdIssuedAt() : Instant.now());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setAuthorizationGrantTypes(registeredClient.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.joining(",")));
        entity.setAuthenticationMethods(registeredClient.getClientAuthenticationMethods().stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(Collectors.joining(",")));
        entity.setRedirectUris(String.join(",", registeredClient.getRedirectUris()));
        entity.setPostLogoutRedirectUris(String.join(",", registeredClient.getPostLogoutRedirectUris()));
        entity.setClientSettings(writeJson(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(writeJson(registeredClient.getTokenSettings().getSettings()));

        entity.getScopes().clear();
        for (String scope : registeredClient.getScopes()) {
            OAuthClientScope scopeEntity = new OAuthClientScope();
            scopeEntity.setScope(scope);
            scopeEntity.setClient(entity);
            entity.getScopes().add(scopeEntity);
        }
    }

    private ClientSettings parseClientSettings(String json) {
        Map<String, Object> settings = readJson(json);
        return ClientSettings.withSettings(settings).build();
    }

    private TokenSettings parseTokenSettings(String json) {
        Map<String, Object> settings = readJson(json);
        return TokenSettings.withSettings(settings).build();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> readJson(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            return authorizationServerObjectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse settings JSON", e);
            throw new IllegalArgumentException("Unable to parse settings JSON", e);
        }
    }

    private String writeJson(Map<String, Object> settings) {
        try {
            return authorizationServerObjectMapper.writeValueAsString(settings);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize settings JSON", e);
            throw new IllegalArgumentException("Unable to serialize settings JSON", e);
        }
    }

    private Set<String> splitToSet(String value) {
        if (value == null || value.isBlank()) {
            return new HashSet<>();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(HashSet::new));
    }
}
