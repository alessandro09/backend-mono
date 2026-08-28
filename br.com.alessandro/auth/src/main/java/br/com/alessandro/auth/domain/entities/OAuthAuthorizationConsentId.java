package br.com.alessandro.auth.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for {@link OAuthAuthorizationConsent}: a consent is uniquely
 * identified by the pair (registeredClientId, principalName).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAuthorizationConsentId implements Serializable {

    private String registeredClientId;

    private String principalName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OAuthAuthorizationConsentId that)) {
            return false;
        }
        return Objects.equals(registeredClientId, that.registeredClientId)
                && Objects.equals(principalName, that.principalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registeredClientId, principalName);
    }
}
