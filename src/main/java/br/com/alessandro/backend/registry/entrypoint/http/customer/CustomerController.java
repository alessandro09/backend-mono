package br.com.alessandro.backend.registry.entrypoint.http.customer;

import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;
import br.com.alessandro.backend.registry.entrypoint.http.customer.dtos.CustomerRequestDTO;
import br.com.alessandro.backend.registry.entrypoint.http.customer.dtos.CustomerResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/customers")
public interface CustomerController {

    @PostMapping
    ResponseEntity<CustomerResponseDTO> create(@RequestBody CustomerRequestDTO request);

    @GetMapping("/{id}")
    ResponseEntity<CustomerResponseDTO> findById(@PathVariable Long id);

    @GetMapping("/tax-id/{taxId}")
    ResponseEntity<CustomerResponseDTO> findByTaxId(@PathVariable String taxId);

    @GetMapping
    ResponseEntity<Page<CustomerResponseDTO>> list(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) ClientStatusType status,
            Pageable pageable
    );

    @PutMapping("/{id}")
    ResponseEntity<CustomerResponseDTO> update(@PathVariable Long id, @RequestBody CustomerRequestDTO request);

    @PatchMapping("/{id}/status")
    ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam ClientStatusType status);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
