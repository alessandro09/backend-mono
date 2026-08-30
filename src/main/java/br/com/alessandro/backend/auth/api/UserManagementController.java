package br.com.alessandro.backend.auth.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alessandro.backend.auth.api.dto.AuthorizationResponse;
import br.com.alessandro.backend.auth.api.dto.UserRequest;
import br.com.alessandro.backend.auth.api.dto.UserResponse;
import br.com.alessandro.backend.auth.service.UserAdminService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/users")
public class UserManagementController {

	private final UserAdminService userAdminService;

	public UserManagementController(UserAdminService userAdminService) {
		this.userAdminService = userAdminService;
	}

	@GetMapping
	public List<UserResponse> findAll() {
		return this.userAdminService.findAll();
	}

	@GetMapping("/{id}")
	public UserResponse findById(@PathVariable Long id) {
		return this.userAdminService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse create(@RequestBody @Valid UserRequest request) {
		return this.userAdminService.create(request);
	}

	@PutMapping("/{id}")
	public UserResponse update(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
		return this.userAdminService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		this.userAdminService.delete(id);
	}

	@PostMapping("/{id}/disable")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void disable(@PathVariable Long id) {
		this.userAdminService.disable(id);
	}

	@PostMapping("/{id}/enable")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void enable(@PathVariable Long id) {
		this.userAdminService.enable(id);
	}

	@GetMapping("/{id}/authorizations")
	public List<AuthorizationResponse> findAuthorizations(@PathVariable Long id) {
		return this.userAdminService.findAuthorizations(id);
	}

	@DeleteMapping("/{id}/authorizations")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void revokeAuthorizations(@PathVariable Long id) {
		this.userAdminService.revokeAuthorizations(id);
	}

}
