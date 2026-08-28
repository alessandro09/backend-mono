package br.com.alessandro.auth.data.repositories;

import br.com.alessandro.auth.domain.entities.OAuthAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuthAuthorizationRepository extends JpaRepository<OAuthAuthorization, String> {

    Optional<OAuthAuthorization> findByState(String state);

    Optional<OAuthAuthorization> findByAuthorizationCodeValue(String authorizationCodeValue);

    Optional<OAuthAuthorization> findByAccessTokenValue(String accessTokenValue);

    Optional<OAuthAuthorization> findByRefreshTokenValue(String refreshTokenValue);

    Optional<OAuthAuthorization> findByOidcIdTokenValue(String oidcIdTokenValue);

    Optional<OAuthAuthorization> findByUserCodeValue(String userCodeValue);

    Optional<OAuthAuthorization> findByDeviceCodeValue(String deviceCodeValue);

    @Query("""
            select a from OAuthAuthorization a
            where a.state = :token
               or a.authorizationCodeValue = :token
               or a.accessTokenValue = :token
               or a.refreshTokenValue = :token
               or a.oidcIdTokenValue = :token
               or a.userCodeValue = :token
               or a.deviceCodeValue = :token
            """)
    Optional<OAuthAuthorization> findByAnyTokenValue(@Param("token") String token);
}
