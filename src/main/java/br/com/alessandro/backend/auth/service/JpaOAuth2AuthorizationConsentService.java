package br.com.alessandro.backend.auth.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import br.com.alessandro.backend.auth.entity.AuthorizationConsent;
import br.com.alessandro.backend.auth.repository.AuthorizationConsentRepository;

@Service
@Transactional
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	private final AuthorizationConsentRepository authorizationConsentRepository;

	public JpaOAuth2AuthorizationConsentService(AuthorizationConsentRepository authorizationConsentRepository) {
		this.authorizationConsentRepository = authorizationConsentRepository;
	}

	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		this.authorizationConsentRepository.save(toEntity(authorizationConsent));
	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		this.authorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
				authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}

	@Override
	@Transactional(readOnly = true)
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		return this.authorizationConsentRepository
			.findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName)
			.map(this::toObject)
			.orElse(null);
	}

	private OAuth2AuthorizationConsent toObject(AuthorizationConsent authorizationConsent) {
		OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent
			.withId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
		StringUtils.commaDelimitedListToSet(authorizationConsent.getAuthorities())
			.forEach((authority) -> builder.authority(new SimpleGrantedAuthority(authority)));
		return builder.build();
	}

	private AuthorizationConsent toEntity(OAuth2AuthorizationConsent authorizationConsent) {
		AuthorizationConsent entity = new AuthorizationConsent();
		entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
		entity.setPrincipalName(authorizationConsent.getPrincipalName());
		entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorizationConsent.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.toList()));
		return entity;
	}

}
