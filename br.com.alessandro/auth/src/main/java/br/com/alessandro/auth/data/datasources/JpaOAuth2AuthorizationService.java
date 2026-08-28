package br.com.alessandro.auth.data.datasources;

import br.com.alessandro.auth.config.AuthorizationServerObjectMapperFactory;
import br.com.alessandro.auth.data.repositories.OAuthAuthorizationRepository;
import br.com.alessandro.auth.domain.entities.OAuthAuthorization;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * JPA-backed implementation of {@link OAuth2AuthorizationService}. Persists the full
 * lifecycle of an {@link OAuth2Authorization} (authorization code, access token,
 * refresh token, OIDC ID token, device/user codes) through
 * {@link OAuthAuthorizationRepository}, serializing token metadata/attributes as JSON
 * using Jackson (with the Authorization Server Jackson modules registered).
 */
@Slf4j
@Service
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final OAuthAuthorizationRepository oAuthAuthorizationRepository;
    private final RegisteredClientRepository registeredClientRepository;
    private final ObjectMapper authorizationServerObjectMapper;

    public JpaOAuth2AuthorizationService(OAuthAuthorizationRepository oAuthAuthorizationRepository,
                                          RegisteredClientRepository registeredClientRepository) {
        this.oAuthAuthorizationRepository = oAuthAuthorizationRepository;
        this.registeredClientRepository = registeredClientRepository;
        this.authorizationServerObjectMapper = AuthorizationServerObjectMapperFactory.create();
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        OAuthAuthorization entity = toEntity(authorization);
        oAuthAuthorizationRepository.save(entity);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        oAuthAuthorizationRepository.deleteById(authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return oAuthAuthorizationRepository.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        if (tokenType == null) {
            return oAuthAuthorizationRepository.findByAnyTokenValue(token).map(this::toObject).orElse(null);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            return oAuthAuthorizationRepository.findByState(token).map(this::toObject).orElse(null);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            return oAuthAuthorizationRepository.findByAuthorizationCodeValue(token).map(this::toObject).orElse(null);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return oAuthAuthorizationRepository.findByAccessTokenValue(token).map(this::toObject).orElse(null);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            return oAuthAuthorizationRepository.findByOidcIdTokenValue(token).map(this::toObject).orElse(null);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            return oAuthAuthorizationRepository.findByRefreshTokenValue(token).map(this::toObject).orElse(null);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            return oAuthAuthorizationRepository.findByUserCodeValue(token).map(this::toObject).orElse(null);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            return oAuthAuthorizationRepository.findByDeviceCodeValue(token).map(this::toObject).orElse(null);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private OAuth2Authorization toObject(OAuthAuthorization entity) {
        RegisteredClient registeredClient = registeredClientRepository.findById(entity.getRegisteredClientId());
        if (registeredClient == null) {
            throw new DataRetrievalFailureException("The RegisteredClient with id '"
                    + entity.getRegisteredClientId() + "' was not found in the RegisteredClientRepository.");
        }

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(entity.getId())
                .principalName(entity.getPrincipalName())
                .authorizationGrantType(new AuthorizationGrantType(entity.getAuthorizationGrantType()))
                .authorizedScopes(splitToSet(entity.getAuthorizedScopes()))
                .attributes(attrs -> attrs.putAll(readMap(entity.getAttributes())));

        if (StringUtils.hasText(entity.getState())) {
            builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
        }

        if (StringUtils.hasText(entity.getAuthorizationCodeValue())) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
                    entity.getAuthorizationCodeValue(),
                    entity.getAuthorizationCodeIssuedAt(),
                    entity.getAuthorizationCodeExpiresAt());
            builder.token(authorizationCode, metadata -> metadata.putAll(readMap(entity.getAuthorizationCodeMetadata())));
        }

        if (StringUtils.hasText(entity.getAccessTokenValue())) {
            OAuth2AccessToken.TokenType tokenType = null;
            if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(entity.getAccessTokenType())) {
                tokenType = OAuth2AccessToken.TokenType.BEARER;
            } else if (OAuth2AccessToken.TokenType.DPOP.getValue().equalsIgnoreCase(entity.getAccessTokenType())) {
                tokenType = OAuth2AccessToken.TokenType.DPOP;
            }
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    tokenType,
                    entity.getAccessTokenValue(),
                    entity.getAccessTokenIssuedAt(),
                    entity.getAccessTokenExpiresAt(),
                    splitToSet(entity.getAccessTokenScopes()));
            builder.token(accessToken, metadata -> metadata.putAll(readMap(entity.getAccessTokenMetadata())));
        }

        if (StringUtils.hasText(entity.getOidcIdTokenValue())) {
            Map<String, Object> oidcMetadata = readMap(entity.getOidcIdTokenMetadata());
            Map<String, Object> claims = readMap(entity.getOidcIdTokenClaims());
            OidcIdToken oidcIdToken = new OidcIdToken(
                    entity.getOidcIdTokenValue(),
                    entity.getOidcIdTokenIssuedAt(),
                    entity.getOidcIdTokenExpiresAt(),
                    claims);
            builder.token(oidcIdToken, metadata -> metadata.putAll(oidcMetadata));
        }

        if (StringUtils.hasText(entity.getRefreshTokenValue())) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    entity.getRefreshTokenValue(),
                    entity.getRefreshTokenIssuedAt(),
                    entity.getRefreshTokenExpiresAt());
            builder.token(refreshToken, metadata -> metadata.putAll(readMap(entity.getRefreshTokenMetadata())));
        }

        if (StringUtils.hasText(entity.getUserCodeValue())) {
            OAuth2UserCode userCode = new OAuth2UserCode(
                    entity.getUserCodeValue(),
                    entity.getUserCodeIssuedAt(),
                    entity.getUserCodeExpiresAt());
            builder.token(userCode, metadata -> metadata.putAll(readMap(entity.getUserCodeMetadata())));
        }

        if (StringUtils.hasText(entity.getDeviceCodeValue())) {
            OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(
                    entity.getDeviceCodeValue(),
                    entity.getDeviceCodeIssuedAt(),
                    entity.getDeviceCodeExpiresAt());
            builder.token(deviceCode, metadata -> metadata.putAll(readMap(entity.getDeviceCodeMetadata())));
        }

        return builder.build();
    }

    private OAuthAuthorization toEntity(OAuth2Authorization authorization) {
        OAuthAuthorization entity = oAuthAuthorizationRepository.findById(authorization.getId())
                .orElseGet(OAuthAuthorization::new);

        entity.setId(authorization.getId() != null ? authorization.getId() : UUID.randomUUID().toString());
        entity.setRegisteredClientId(authorization.getRegisteredClientId());
        entity.setPrincipalName(authorization.getPrincipalName());
        entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
        entity.setAuthorizedScopes(CollectionUtils.isEmpty(authorization.getAuthorizedScopes()) ? null
                : StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), ","));
        entity.setAttributes(writeMap(authorization.getAttributes()));
        entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            entity.setAuthorizationCodeValue(authorizationCode.getToken().getTokenValue());
            entity.setAuthorizationCodeIssuedAt(authorizationCode.getToken().getIssuedAt());
            entity.setAuthorizationCodeExpiresAt(authorizationCode.getToken().getExpiresAt());
            entity.setAuthorizationCodeMetadata(writeMap(authorizationCode.getMetadata()));
        }

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null) {
            entity.setAccessTokenValue(accessToken.getToken().getTokenValue());
            entity.setAccessTokenIssuedAt(accessToken.getToken().getIssuedAt());
            entity.setAccessTokenExpiresAt(accessToken.getToken().getExpiresAt());
            entity.setAccessTokenMetadata(writeMap(accessToken.getMetadata()));
            entity.setAccessTokenType(accessToken.getToken().getTokenType().getValue());
            entity.setAccessTokenScopes(CollectionUtils.isEmpty(accessToken.getToken().getScopes()) ? null
                    : StringUtils.collectionToDelimitedString(accessToken.getToken().getScopes(), ","));
        }

        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdToken != null) {
            entity.setOidcIdTokenValue(oidcIdToken.getToken().getTokenValue());
            entity.setOidcIdTokenIssuedAt(oidcIdToken.getToken().getIssuedAt());
            entity.setOidcIdTokenExpiresAt(oidcIdToken.getToken().getExpiresAt());
            entity.setOidcIdTokenMetadata(writeMap(oidcIdToken.getMetadata()));
            entity.setOidcIdTokenClaims(writeMap(oidcIdToken.getToken().getClaims()));
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null) {
            entity.setRefreshTokenValue(refreshToken.getToken().getTokenValue());
            entity.setRefreshTokenIssuedAt(refreshToken.getToken().getIssuedAt());
            entity.setRefreshTokenExpiresAt(refreshToken.getToken().getExpiresAt());
            entity.setRefreshTokenMetadata(writeMap(refreshToken.getMetadata()));
        }

        OAuth2Authorization.Token<OAuth2UserCode> userCode = authorization.getToken(OAuth2UserCode.class);
        if (userCode != null) {
            entity.setUserCodeValue(userCode.getToken().getTokenValue());
            entity.setUserCodeIssuedAt(userCode.getToken().getIssuedAt());
            entity.setUserCodeExpiresAt(userCode.getToken().getExpiresAt());
            entity.setUserCodeMetadata(writeMap(userCode.getMetadata()));
        }

        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = authorization.getToken(OAuth2DeviceCode.class);
        if (deviceCode != null) {
            entity.setDeviceCodeValue(deviceCode.getToken().getTokenValue());
            entity.setDeviceCodeIssuedAt(deviceCode.getToken().getIssuedAt());
            entity.setDeviceCodeExpiresAt(deviceCode.getToken().getExpiresAt());
            entity.setDeviceCodeMetadata(writeMap(deviceCode.getMetadata()));
        }

        return entity;
    }

    private Set<String> splitToSet(String value) {
        return StringUtils.hasText(value) ? StringUtils.commaDelimitedListToSet(value) : Collections.emptySet();
    }

    private Map<String, Object> readMap(String data) {
        if (!StringUtils.hasText(data)) {
            return Collections.emptyMap();
        }
        try {
            return authorizationServerObjectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException ex) {
            log.error("Unable to parse authorization JSON payload", ex);
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private String writeMap(Map<String, Object> data) {
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        try {
            return authorizationServerObjectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            log.error("Unable to serialize authorization JSON payload", ex);
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
