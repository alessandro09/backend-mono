package br.com.alessandro.backend.auth.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.alessandro.backend.auth.api.dto.GroupRequest;
import br.com.alessandro.backend.auth.api.dto.GroupResponse;
import br.com.alessandro.backend.auth.entity.Group;
import br.com.alessandro.backend.auth.entity.Role;
import br.com.alessandro.backend.auth.repository.GroupRepository;
import br.com.alessandro.backend.auth.repository.RoleRepository;
import br.com.alessandro.backend.auth.repository.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/groups")
public class GroupManagementController {

	private final GroupRepository groupRepository;

	private final RoleRepository roleRepository;

	private final UserRepository userRepository;

	public GroupManagementController(GroupRepository groupRepository, RoleRepository roleRepository,
			UserRepository userRepository) {
		this.groupRepository = groupRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	@GetMapping
	public List<GroupResponse> findAll() {
		return this.groupRepository.findAll().stream().map(this::toResponse).toList();
	}

	@GetMapping("/{name}")
	public GroupResponse findById(@PathVariable String name) {
		return toResponse(getGroup(name));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public GroupResponse create(@RequestBody @Valid GroupRequest request) {
		if (this.groupRepository.existsById(request.name())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Grupo já existe: " + request.name());
		}
		Group group = new Group(request.name());
		group.setRoles(resolveRoles(request.roles()));
		return toResponse(this.groupRepository.save(group));
	}

	@PutMapping("/{name}")
	@Transactional
	public GroupResponse update(@PathVariable String name, @RequestBody @Valid GroupRequest request) {
		Group group = getGroup(name);
		if (request.roles() != null) {
			group.setRoles(resolveRoles(request.roles()));
		}
		return toResponse(this.groupRepository.save(group));
	}

	@DeleteMapping("/{name}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void delete(@PathVariable String name) {
		Group group = getGroup(name);
		// remove o grupo dos usuários antes de excluir
		this.userRepository.findAll().forEach((user) -> user.getGroups().remove(group));
		this.groupRepository.delete(group);
	}

	private Group getGroup(String name) {
		return this.groupRepository.findById(name)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo não encontrado: " + name));
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

	private GroupResponse toResponse(Group group) {
		Set<String> roles = new HashSet<>();
		group.getRoles().forEach((role) -> roles.add(role.getName()));
		return new GroupResponse(group.getName(), roles);
	}

}
