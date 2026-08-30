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

import br.com.alessandro.backend.auth.api.dto.ClientRequest;
import br.com.alessandro.backend.auth.api.dto.ClientResponse;
import br.com.alessandro.backend.auth.service.ClientAdminService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/clients")
public class ClientManagementController {

	private final ClientAdminService clientAdminService;

	public ClientManagementController(ClientAdminService clientAdminService) {
		this.clientAdminService = clientAdminService;
	}

	@GetMapping
	public List<ClientResponse> findAll() {
		return this.clientAdminService.findAll();
	}

	@GetMapping("/{id}")
	public ClientResponse findById(@PathVariable String id) {
		return this.clientAdminService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ClientResponse create(@RequestBody @Valid ClientRequest request) {
		return this.clientAdminService.create(request);
	}

	@PutMapping("/{id}")
	public ClientResponse update(@PathVariable String id, @RequestBody @Valid ClientRequest request) {
		return this.clientAdminService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String id) {
		this.clientAdminService.delete(id);
	}

}
