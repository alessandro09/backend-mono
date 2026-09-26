package br.com.alessandro.backend.registry.iteractors;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.alessandro.backend.registry.entities.CustomerMasterEntity;
import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;
import br.com.alessandro.backend.registry.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(
        CustomerRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
    }

    public CustomerMasterEntity save(CustomerMasterEntity request) {
        return customerRepository.save(request);
    }

    public Optional<CustomerMasterEntity> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<CustomerMasterEntity> findByTaxId(String taxId) {
        return customerRepository.findByTaxId(taxId);
    }

    public void changeStatus(Long id, ClientStatusType status) {
        customerRepository.changeStatus(id, status);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public @Nullable CustomerMasterEntity update(Long id, CustomerMasterEntity request) {
        return customerRepository.update(id, request);
    }

    public Page<CustomerMasterEntity> search(String searchTerm, ClientStatusType status, Pageable pageable) {
        return customerRepository.search(searchTerm, status, pageable);
    }
    
}