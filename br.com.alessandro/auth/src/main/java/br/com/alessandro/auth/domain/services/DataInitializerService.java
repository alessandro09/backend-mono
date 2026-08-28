package br.com.alessandro.auth.domain.services;

import br.com.alessandro.auth.data.repositories.AuthUserRepository;
import br.com.alessandro.auth.domain.entities.AuthAuthority;
import br.com.alessandro.auth.domain.entities.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

/**
 * Seeds the database on application startup with:
 * <ul>
 *     <li>A public client configured for the Authorization Code + PKCE flow
 *     (redirecting to {@code http://localhost:3000/callback}).</li>
 *     <li>A private/confidential client configured for the Client Credentials flow
 *     authenticated via {@code client_secret_basic}.</li>
 *     <li>A default {@code admin} user with role {@code ADMIN}.</li>
 * </ul>
 * All operations are idempotent: existing records are left untouched on subsequent runs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataInitializerService implements ApplicationRunner {

    private static final String PUBLIC_CLIENT_ID = "public-client";
    private static final String PRIVATE_CLIENT_ID = "private-client";
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private final RegisteredClientRepository registeredClientRepository;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        registerPublicClient();
        registerPrivateClient();
        registerDefaultAdminUser();
    }

    private void registerPublicClient() {
        if (registeredClientRepository.findByClientId(PUBLIC_CLIENT_ID) != null) {
            log.info("Public OAuth2 client '{}' already registered, skipping.", PUBLIC_CLIENT_ID);
            return;
        }

        RegisteredClient publicClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(PUBLIC_CLIENT_ID)
                .clientName("Public SPA Client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:3000/callback")
                .postLogoutRedirectUri("http://localhost:3000/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .requireProofKey(true)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofDays(30))
                        .reuseRefreshTokens(false)
                        .build())
                .build();

        registeredClientRepository.save(publicClient);
        log.info("Registered public OAuth2 client '{}' (authorization_code + PKCE).", PUBLIC_CLIENT_ID);
    }

    private void registerPrivateClient() {
        if (registeredClientRepository.findByClientId(PRIVATE_CLIENT_ID) != null) {
            log.info("Private OAuth2 client '{}' already registered, skipping.", PRIVATE_CLIENT_ID);
            return;
        }

        RegisteredClient privateClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(PRIVATE_CLIENT_ID)
                .clientSecret(passwordEncoder.encode("private-secret"))
                .clientName("Private Service Client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("api.read")
                .scope("api.write")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(60))
                        .build())
                .build();

        registeredClientRepository.save(privateClient);
        log.info("Registered private OAuth2 client '{}' (client_credentials + client_secret_basic).", PRIVATE_CLIENT_ID);
    }

    private void registerDefaultAdminUser() {
        if (authUserRepository.existsByUsername(DEFAULT_ADMIN_USERNAME)) {
            log.info("Default user '{}' already exists, skipping.", DEFAULT_ADMIN_USERNAME);
            return;
        }

        AuthUser adminUser = new AuthUser();
        adminUser.setUsername(DEFAULT_ADMIN_USERNAME);
        adminUser.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
        adminUser.setEmail("admin@alessandro.com.br");
        adminUser.setEnabled(true);

        AuthAuthority adminAuthority = new AuthAuthority();
        adminAuthority.setAuthority("ROLE_ADMIN");
        adminAuthority.setUser(adminUser);
        adminUser.getAuthorities().add(adminAuthority);

        authUserRepository.save(adminUser);
        log.info("Registered default admin user '{}' with role ADMIN.", DEFAULT_ADMIN_USERNAME);
    }
}
