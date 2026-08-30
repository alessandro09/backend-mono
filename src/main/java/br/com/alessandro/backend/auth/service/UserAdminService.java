package br.com.alessandro.backend.auth.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import br.com.alessandro.backend.auth.api.dto.AuthorizationResponse;
import br.com.alessandro.backend.auth.api.dto.UserRequest;
import br.com.alessandro.backend.auth.api.dto.UserResponse;
import br.com.alessandro.backend.auth.entity.Authorization;
import br.com.alessandro.backend.auth.entity.Group;
import br.com.alessandro.backend.auth.entity.Role;
import br.com.alessandro.backend.auth.entity.User;
import br.com.alessandro.backend.auth.repository.AuthorizationRepository;
import br.com.alessandro.backend.auth.repository.GroupRepository;
import br.com.alessandro.backend.auth.repository.RoleRepository;
import br.com.alessandro.backend.auth.repository.UserRepository;

@Service
@Transactional
public class UserAdminService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final GroupRepository groupRepository;

	private final AuthorizationRepository authorizationRepository;

	private final UserAccessService userAccessService;

	private final PasswordEncoder passwordEncoder;

	public UserAdminService(UserRepository userRepository, RoleRepository roleRepository,
			GroupRepository groupRepository, AuthorizationRepository authorizationRepository,
			UserAccessService userAccessService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.groupRepository = groupRepository;
		this.authorizationRepository = authorizationRepository;
		this.userAccessService = userAccessService;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public List<UserResponse> findAll() {
		return this.userRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public UserResponse findById(Long id) {
		return toResponse(getUser(id));
	}

	public UserResponse create(UserRequest request) {
		if (this.userRepository.existsByUsername(request.username())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Usuário já existe: " + request.username());
		}
		if (!StringUtils.hasText(request.password())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória");
		}
		User user = new User(request.username(), this.passwordEncoder.encode(request.password()));
		user.setEnabled(request.enabled() == null || request.enabled());
		user.setRoles(resolveRoles(request.roles()));
		user.setGroups(resolveGroups(request.groups()));
		return toResponse(this.userRepository.save(user));
	}

	public UserResponse update(Long id, UserRequest request) {
		User user = getUser(id);
		if (StringUtils.hasText(request.password())) {
			user.setPassword(this.passwordEncoder.encode(request.password()));
		}
		if (request.roles() != null) {
			user.setRoles(resolveRoles(request.roles()));
		}
		if (request.groups() != null) {
			user.setGroups(resolveGroups(request.groups()));
		}
		User saved = this.userRepository.save(user);
		if (request.enabled() != null) {
			if (request.enabled()) {
				enable(id);
			}
			else {
				disable(id);
			}
		}
		return toResponse(saved);
	}

	public void delete(Long id) {
		User user = getUser(id);
		revokeAccess(user);
		this.userAccessService.unblock(user.getUsername());
		this.userRepository.delete(user);
	}

	/**
	 * Desativa o usuário e revoga o acesso imediatamente: remove todas as
	 * autorizações (refresh tokens, códigos) e bloqueia JWTs em vigor via Redis.
	 */
	public void disable(Long id) {
		User user = getUser(id);
		user.setEnabled(false);
		this.userRepository.save(user);
		revokeAccess(user);
		this.userAccessService.block(user.getUsername());
	}

	public void enable(Long id) {
		User user = getUser(id);
		user.setEnabled(true);
		this.userRepository.save(user);
		this.userAccessService.unblock(user.getUsername());
	}

	@Transactional(readOnly = true)
	public List<AuthorizationResponse> findAuthorizations(Long id) {
		User user = getUser(id);
		return this.authorizationRepository.findAllByPrincipalName(user.getUsername())
			.stream()
			.map(this::toAuthorizationResponse)
			.toList();
	}

	public void revokeAuthorizations(Long id) {
		User user = getUser(id);
		this.authorizationRepository.deleteByPrincipalName(user.getUsername());
	}

	private void revokeAccess(User user) {
		this.authorizationRepository.deleteByPrincipalName(user.getUsername());
	}

	private User getUser(Long id) {
		return this.userRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id));
	}

	private Set<Role> resolveRoles(Set<String> names) {
		Set<Role> roles = new HashSet<>();
		if (names == null) {
			return roles;
		}
		for (String name : names) {
			roles.add(this.roleRepository.findById(name)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role não encontrada: " + name)));
		}
		return roles;
	}

	private Set<Group> resolveGroups(Set<String> names) {
		Set<Group> groups = new HashSet<>();
		if (names == null) {
			return groups;
		}
		for (String name : names) {
			groups.add(this.groupRepository.findById(name)
				.orElseThrow(
						() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Grupo não encontrado: " + name)));
		}
		return groups;
	}

	private UserResponse toResponse(User user) {
		Set<String> roles = new HashSet<>();
		user.getRoles().forEach((role) -> roles.add(role.getName()));
		Set<String> groups = new HashSet<>();
		user.getGroups().forEach((group) -> groups.add(group.getName()));
		return new UserResponse(user.getId(), user.getUsername(), user.isEnabled(), roles, groups);
	}

	private AuthorizationResponse toAuthorizationResponse(Authorization authorization) {
		return new AuthorizationResponse(authorization.getId(), authorization.getAuthorizationGrantType(),
				authorization.getAuthorizedScopes(), authorization.getAccessTokenIssuedAt(),
				authorization.getAccessTokenExpiresAt(), authorization.getRefreshTokenIssuedAt(),
				authorization.getRefreshTokenExpiresAt());
	}

}
