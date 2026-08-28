package br.com.alessandro.auth.data.repositories;

import br.com.alessandro.auth.domain.entities.OAuthClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OAuthClientRepository extends JpaRepository<OAuthClient, UUID> {

    Optional<OAuthClient> findByClientId(String clientId);

    boolean existsByClientId(String clientId);
}
