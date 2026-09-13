package br.com.alessandro.backend.registry.entrypoint.http.customer;

import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;
import br.com.alessandro.backend.registry.entrypoint.http.customer.dtos.CustomerRequestDTO;
import br.com.alessandro.backend.registry.entrypoint.http.customer.dtos.CustomerResponseDTO;
import br.com.alessandro.backend.registry.entrypoint.http.customer.mappers.CustomerMapper;
import br.com.alessandro.backend.registry.iteractors.CustomerService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import static br.com.alessandro.backend.registry.entrypoint.http.customer.mappers.CustomerMapper.*;

@RestController
public class CustomerControllerImpl implements CustomerController {
    private final CustomerService customerService;

    public CustomerControllerImpl(
        CustomerService customerService
    ) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<CustomerResponseDTO> create(CustomerRequestDTO request) {
        var response = customerService.save(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(response));
    }

    @Override
    public ResponseEntity<CustomerResponseDTO> findById(Long id) {
        var customer = customerService.findById(id).map(CustomerMapper::toDTO).orElse(null);
        
        return ResponseEntity.ok(customer);
    }

    @Override
    public ResponseEntity<CustomerResponseDTO> findByTaxId(String taxId) {
        var customer = customerService.findByTaxId(taxId).map(CustomerMapper::toDTO).orElse(null);

        return ResponseEntity.ok(customer);
    }

    @Override
    public ResponseEntity<Page<CustomerResponseDTO>> list(String searchTerm, ClientStatusType status, Pageable pageable) {
        var customers = customerService.search(searchTerm, status, pageable);
        var customerDTOs = customers.map(CustomerMapper::toDTO);

        return ResponseEntity.ok(customerDTOs);
    }

    @Override
    public ResponseEntity<CustomerResponseDTO> update(Long id, CustomerRequestDTO request) {
        var updatedCustomer = customerService.update(id, toEntity(request));
        return ResponseEntity.ok(toDTO(updatedCustomer));
    }

    @Override
    public ResponseEntity<Void> updateStatus(Long id, ClientStatusType status) {
        customerService.changeStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
