package br.com.alessandro.backend.auth.service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import br.com.alessandro.backend.auth.api.dto.ClientRequest;
import br.com.alessandro.backend.auth.api.dto.ClientResponse;
import br.com.alessandro.backend.auth.repository.ClientRepository;

@Service
@Transactional
public class ClientAdminService {

	private static final Duration DEFAULT_ACCESS_TOKEN_TTL = Duration.ofMinutes(30);

	private static final Duration DEFAULT_REFRESH_TOKEN_TTL = Duration.ofHours(24);

	private final ClientRepository clientRepository;

	private final RegisteredClientRepository registeredClientRepository;

	private final PasswordEncoder passwordEncoder;

	public ClientAdminService(ClientRepository clientRepository,
			RegisteredClientRepository registeredClientRepository, PasswordEncoder passwordEncoder) {
		this.clientRepository = clientRepository;
		this.registeredClientRepository = registeredClientRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public List<ClientResponse> findAll() {
		return this.clientRepository.findAll()
			.stream()
			.map((client) -> toResponse(this.registeredClientRepository.findById(client.getId())))
			.toList();
	}

	@Transactional(readOnly = true)
	public ClientResponse findById(String id) {
		return toResponse(getClient(id));
	}

	public ClientResponse create(ClientRequest request) {
		if (this.clientRepository.findByClientId(request.clientId()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Client já existe: " + request.clientId());
		}
		RegisteredClient registeredClient = build(UUID.randomUUID().toString(), request, null);
		this.registeredClientRepository.save(registeredClient);
		return toResponse(registeredClient);
	}

	public ClientResponse update(String id, ClientRequest request) {
		RegisteredClient existing = getClient(id);
		// secret não informado no update mantém o atual
		String encodedSecret = StringUtils.hasText(request.clientSecret())
				? this.passwordEncoder.encode(request.clientSecret()) : existing.getClientSecret();
		RegisteredClient updated = build(existing.getId(), request, encodedSecret);
		this.registeredClientRepository.save(updated);
		return toResponse(updated);
	}

	public void delete(String id) {
		getClient(id);
		this.clientRepository.deleteById(id);
	}

	private RegisteredClient getClient(String id) {
		RegisteredClient registeredClient = this.registeredClientRepository.findById(id);
		if (registeredClient == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client não encontrado: " + id);
		}
		return registeredClient;
	}

	private RegisteredClient build(String id, ClientRequest request, String encodedSecret) {
		boolean confidential = StringUtils.hasText(request.clientSecret()) || encodedSecret != null;

		RegisteredClient.Builder builder = RegisteredClient.withId(id)
			.clientId(request.clientId())
			.clientName(StringUtils.hasText(request.clientName()) ? request.clientName() : request.clientId());

		if (confidential) {
			builder.clientSecret(encodedSecret != null ? encodedSecret
					: this.passwordEncoder.encode(request.clientSecret()))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST);
		}
		else {
			builder.clientAuthenticationMethod(ClientAuthenticationMethod.NONE);
		}

		Set<String> grantTypes = request.grantTypes() == null || request.grantTypes().isEmpty()
				? Set.of("authorization_code", "refresh_token") : request.grantTypes();
		grantTypes.forEach((grantType) -> builder.authorizationGrantType(new AuthorizationGrantType(grantType)));

		if (request.redirectUris() != null) {
			request.redirectUris().forEach(builder::redirectUri);
		}
		if (request.postLogoutRedirectUris() != null) {
			request.postLogoutRedirectUris().forEach(builder::postLogoutRedirectUri);
		}

		Set<String> scopes = request.scopes() == null || request.scopes().isEmpty() ? Set.of("read")
				: request.scopes();
		scopes.forEach(builder::scope);

		boolean requireProofKey = request.requireProofKey() != null ? request.requireProofKey() : !confidential;
		builder.clientSettings(ClientSettings.builder()
			.requireProofKey(requireProofKey)
			.requireAuthorizationConsent(
					request.requireAuthorizationConsent() != null && request.requireAuthorizationConsent())
			.build());

		builder.tokenSettings(TokenSettings.builder()
			.accessTokenTimeToLive(request.accessTokenTtlMinutes() != null
					? Duration.ofMinutes(request.accessTokenTtlMinutes()) : DEFAULT_ACCESS_TOKEN_TTL)
			.refreshTokenTimeToLive(request.refreshTokenTtlHours() != null
					? Duration.ofHours(request.refreshTokenTtlHours()) : DEFAULT_REFRESH_TOKEN_TTL)
			.build());

		return builder.build();
	}

	private ClientResponse toResponse(RegisteredClient client) {
		return new ClientResponse(client.getId(), client.getClientId(), client.getClientName(),
				client.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue)
					.collect(java.util.stream.Collectors.toSet()),
				client.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue)
					.collect(java.util.stream.Collectors.toSet()),
				client.getRedirectUris(), client.getPostLogoutRedirectUris(), client.getScopes(),
				client.getClientSettings().isRequireProofKey(),
				client.getClientSettings().isRequireAuthorizationConsent(),
				client.getTokenSettings().getAccessTokenTimeToLive(),
				client.getTokenSettings().getRefreshTokenTimeToLive());
	}

}
