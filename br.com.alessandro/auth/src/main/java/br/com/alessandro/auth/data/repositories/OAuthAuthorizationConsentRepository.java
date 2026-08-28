package br.com.alessandro.auth.data.repositories;

import br.com.alessandro.auth.domain.entities.OAuthAuthorizationConsent;
import br.com.alessandro.auth.domain.entities.OAuthAuthorizationConsentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthAuthorizationConsentRepository
        extends JpaRepository<OAuthAuthorizationConsent, OAuthAuthorizationConsentId> {

    Optional<OAuthAuthorizationConsent> findByRegisteredClientIdAndPrincipalName(
            String registeredClientId, String principalName);

    void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}
