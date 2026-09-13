package br.com.alessandro.backend.registry.repository;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alessandro.backend.registry.entities.CustomerMaster;
import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;

public interface CustomerRepository {

    public CustomerMaster save(CustomerMaster request);

    public Optional<CustomerMaster> findById(Long id);

    public void changeStatus(Long id, ClientStatusType status);

    public Optional<CustomerMaster> findByTaxId(String taxId);

    public void deleteById(Long id);

    public @Nullable CustomerMaster update(Long id, CustomerMaster request);

    public Page<CustomerMaster> search(String searchTerm, ClientStatusType status, Pageable pageable);
    
}
