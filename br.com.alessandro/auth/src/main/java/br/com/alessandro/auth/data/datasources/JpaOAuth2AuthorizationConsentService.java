package br.com.alessandro.auth.data.datasources;

import br.com.alessandro.auth.data.repositories.OAuthAuthorizationConsentRepository;
import br.com.alessandro.auth.domain.entities.OAuthAuthorizationConsent;
import br.com.alessandro.auth.domain.entities.OAuthAuthorizationConsentId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * JPA-backed implementation of {@link OAuth2AuthorizationConsentService}. Persists the
 * consent granted by a resource owner (principal) for a given registered client through
 * {@link OAuthAuthorizationConsentRepository}.
 */
@Service
@RequiredArgsConstructor
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final OAuthAuthorizationConsentRepository oAuthAuthorizationConsentRepository;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        OAuthAuthorizationConsent entity = oAuthAuthorizationConsentRepository
                .findByRegisteredClientIdAndPrincipalName(
                        authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName())
                .orElseGet(OAuthAuthorizationConsent::new);
        entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
        entity.setPrincipalName(authorizationConsent.getPrincipalName());
        entity.setAuthorities(authorizationConsent.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((a, b) -> a + "," + b)
                .orElse(""));
        oAuthAuthorizationConsentRepository.save(entity);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        oAuthAuthorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
                authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return oAuthAuthorizationConsentRepository
                .findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName)
                .map(this::toObject)
                .orElse(null);
    }

    private OAuth2AuthorizationConsent toObject(OAuthAuthorizationConsent entity) {
        OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(
                entity.getRegisteredClientId(), entity.getPrincipalName());
        if (StringUtils.hasText(entity.getAuthorities())) {
            for (String authority : entity.getAuthorities().split(",")) {
                builder.authority(new SimpleGrantedAuthority(authority));
            }
        }
        return builder.build();
    }
}
