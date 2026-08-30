package br.com.alessandro.backend.auth.initializer;

import java.time.Duration;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.alessandro.backend.auth.entity.Group;
import br.com.alessandro.backend.auth.entity.Role;
import br.com.alessandro.backend.auth.entity.User;
import br.com.alessandro.backend.auth.repository.GroupRepository;
import br.com.alessandro.backend.auth.repository.RoleRepository;
import br.com.alessandro.backend.auth.repository.UserRepository;

@Component
public class AuthInitializer implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(AuthInitializer.class);

	private final RegisteredClientRepository registeredClientRepository;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final GroupRepository groupRepository;

	private final PasswordEncoder passwordEncoder;

	public AuthInitializer(RegisteredClientRepository registeredClientRepository, UserRepository userRepository,
			RoleRepository roleRepository, GroupRepository groupRepository, PasswordEncoder passwordEncoder) {
		this.registeredClientRepository = registeredClientRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.groupRepository = groupRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		initRolesAndGroups();
		initUsers();
		initClients();
	}

	private void initRolesAndGroups() {
		Role adminRole = this.roleRepository.findById("ROLE_ADMIN")
			.orElseGet(() -> this.roleRepository.save(new Role("ROLE_ADMIN")));
		this.roleRepository.findById("ROLE_USER").orElseGet(() -> this.roleRepository.save(new Role("ROLE_USER")));

		this.groupRepository.findById("admin").orElseGet(() -> {
			Group group = new Group("admin");
			group.getRoles().add(adminRole);
			return this.groupRepository.save(group);
		});
	}

	private void initUsers() {
		if (this.userRepository.existsByUsername("admin")) {
			return;
		}
		Role adminRole = this.roleRepository.findById("ROLE_ADMIN").orElseThrow();
		Role userRole = this.roleRepository.findById("ROLE_USER").orElseThrow();
		Group adminGroup = this.groupRepository.findById("admin").orElseThrow();

		User admin = new User("admin", this.passwordEncoder.encode("admin"));
		admin.getRoles().add(adminRole);
		admin.getRoles().add(userRole);
		admin.getGroups().add(adminGroup);
		this.userRepository.save(admin);
		log.info("Usuário 'admin' criado (senha: admin)");
	}

	private void initClients() {
		RegisteredClient existingApp = this.registeredClientRepository.findByClientId("app");
		String appId = (existingApp != null) ? existingApp.getId() : UUID.randomUUID().toString();

		RegisteredClient.Builder appBuilder = RegisteredClient.withId(appId)
			.clientId("app")
			.clientName("App Público (PKCE)")
			.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.scope(OidcScopes.OPENID)
			.scope(OidcScopes.PROFILE)
			.scope("read")
			.clientSettings(ClientSettings.builder()
				.requireProofKey(true)
				.requireAuthorizationConsent(false)
				.build())
			.tokenSettings(TokenSettings.builder()
				.accessTokenTimeToLive(Duration.ofMinutes(30))
				.refreshTokenTimeToLive(Duration.ofDays(1))
				.reuseRefreshTokens(false)
				.build());

		for (int port = 4200; port <= 4210; port++) {
			appBuilder.redirectUri("http://localhost:" + port + "/callback")
				.redirectUri("http://127.0.0.1:" + port + "/callback")
				.postLogoutRedirectUri("http://localhost:" + port)
				.postLogoutRedirectUri("http://localhost:" + port + "/")
				.postLogoutRedirectUri("http://localhost:" + port + "/logout")
				.postLogoutRedirectUri("http://127.0.0.1:" + port)
				.postLogoutRedirectUri("http://127.0.0.1:" + port + "/")
				.postLogoutRedirectUri("http://127.0.0.1:" + port + "/logout");
		}
		appBuilder.postLogoutRedirectUri("http://127.0.0.1:8080/")
			.postLogoutRedirectUri("http://localhost:8080/")
			.postLogoutRedirectUri("http://localhost:8080/login")
			.postLogoutRedirectUri("http://localhost:8080/login?logout")
			.postLogoutRedirectUri("http://127.0.0.1:8080/login")
			.postLogoutRedirectUri("http://127.0.0.1:8080/login?logout");

		this.registeredClientRepository.save(appBuilder.build());
		log.info("Client público 'app' registrado/atualizado (PKCE obrigatório)");

		if (this.registeredClientRepository.findByClientId("service") == null) {
			RegisteredClient service = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("service")
				.clientName("Service Privado")
				.clientSecret(this.passwordEncoder.encode("service"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.scope("read")
				.scope("write")
				.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(30)).build())
				.build();
			this.registeredClientRepository.save(service);
			log.info("Client privado 'service' criado (secret: service)");
		}
	}

}
