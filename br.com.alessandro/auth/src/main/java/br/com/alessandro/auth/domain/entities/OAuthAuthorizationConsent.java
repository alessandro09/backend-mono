package br.com.alessandro.auth.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * JPA entity representing an OAuth2 authorization consent, equivalent to the
 * default {@code oauth2_authorization_consent} table used by Spring Authorization Server.
 * Identified by the composite key (registeredClientId, principalName).
 */
@Data
@Entity
@Table(name = "oauth_authorization_consent")
@IdClass(OAuthAuthorizationConsentId.class)
public class OAuthAuthorizationConsent {

    @Id
    @Column(name = "registered_client_id", length = 100)
    private String registeredClientId;

    @Id
    @Column(name = "principal_name", length = 200)
    private String principalName;

    @Column(name = "authorities", columnDefinition = "TEXT", nullable = false)
    private String authorities;
}
