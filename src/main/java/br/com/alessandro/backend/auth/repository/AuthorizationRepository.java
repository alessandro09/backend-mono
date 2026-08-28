package br.com.alessandro.backend.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alessandro.backend.auth.entity.Authorization;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, String> {

	Optional<Authorization> findByState(String token);

	Optional<Authorization> findByAuthorizationCodeValue(String token);

	Optional<Authorization> findByAccessTokenValue(String token);

	Optional<Authorization> findByRefreshTokenValue(String token);

	Optional<Authorization> findByOidcIdTokenValue(String token);

	Optional<Authorization> findByUserCodeValue(String token);

	Optional<Authorization> findByDeviceCodeValue(String token);

	@Query("select a from Authorization a where a.state = :token"
			+ " or a.authorizationCodeValue = :token"
			+ " or a.accessTokenValue = :token"
			+ " or a.refreshTokenValue = :token"
			+ " or a.oidcIdTokenValue = :token"
			+ " or a.userCodeValue = :token"
			+ " or a.deviceCodeValue = :token")
	Optional<Authorization> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
			@Param("token") String token);

}
