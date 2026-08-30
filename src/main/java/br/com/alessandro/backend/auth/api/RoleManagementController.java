package br.com.alessandro.backend.auth.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.alessandro.backend.auth.api.dto.RoleRequest;
import br.com.alessandro.backend.auth.entity.Role;
import br.com.alessandro.backend.auth.repository.GroupRepository;
import br.com.alessandro.backend.auth.repository.RoleRepository;
import br.com.alessandro.backend.auth.repository.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/roles")
public class RoleManagementController {

	private final RoleRepository roleRepository;

	private final UserRepository userRepository;

	private final GroupRepository groupRepository;

	public RoleManagementController(RoleRepository roleRepository, UserRepository userRepository,
			GroupRepository groupRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
	}

	@GetMapping
	public List<String> findAll() {
		return this.roleRepository.findAll().stream().map(Role::getName).toList();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String create(@RequestBody @Valid RoleRequest request) {
		String name = normalize(request.name());
		if (this.roleRepository.existsById(name)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Role já existe: " + name);
		}
		return this.roleRepository.save(new Role(name)).getName();
	}

	@DeleteMapping("/{name}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void delete(@PathVariable String name) {
		String roleName = normalize(name);
		Role role = this.roleRepository.findById(roleName)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role não encontrada: " + roleName));
		// remove a role de usuários e grupos antes de excluir
		this.userRepository.findAll().forEach((user) -> user.getRoles().remove(role));
		this.groupRepository.findAll().forEach((group) -> group.getRoles().remove(role));
		this.roleRepository.delete(role);
	}

	private static String normalize(String name) {
		return name.startsWith("ROLE_") ? name : "ROLE_" + name;
	}

}
