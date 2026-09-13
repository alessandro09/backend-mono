package br.com.alessandro.backend.registry.iteractors;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.alessandro.backend.registry.entities.CustomerMaster;
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

    public CustomerMaster save(CustomerMaster request) {
        return customerRepository.save(request);
    }

    public Optional<CustomerMaster> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<CustomerMaster> findByTaxId(String taxId) {
        return customerRepository.findByTaxId(taxId);
    }

    public void changeStatus(Long id, ClientStatusType status) {
        customerRepository.changeStatus(id, status);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public @Nullable CustomerMaster update(Long id, CustomerMaster request) {
        return customerRepository.update(id, request);
    }

    public Page<CustomerMaster> search(String searchTerm, ClientStatusType status, Pageable pageable) {
        return customerRepository.search(searchTerm, status, pageable);
    }
    
}