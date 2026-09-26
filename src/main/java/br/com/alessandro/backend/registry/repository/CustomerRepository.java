package br.com.alessandro.backend.registry.repository;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alessandro.backend.registry.entities.CustomerMasterEntity;
import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;

public interface CustomerRepository {

    public CustomerMasterEntity save(CustomerMasterEntity request);

    public Optional<CustomerMasterEntity> findById(Long id);

    public void changeStatus(Long id, ClientStatusType status);

    public Optional<CustomerMasterEntity> findByTaxId(String taxId);

    public void deleteById(Long id);

    public @Nullable CustomerMasterEntity update(Long id, CustomerMasterEntity request);

    public Page<CustomerMasterEntity> search(String searchTerm, ClientStatusType status, Pageable pageable);
    
}
