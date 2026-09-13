package br.com.alessandro.backend.registry.datasource.customer;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static br.com.alessandro.backend.registry.datasource.customer.mapper.CustomerMapper.*;

import br.com.alessandro.backend.registry.datasource.customer.mapper.CustomerMapper;
import br.com.alessandro.backend.registry.entities.CustomerMaster;
import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;
import br.com.alessandro.backend.registry.repository.CustomerRepository;

@Service
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerRepositoryJpa repository;

    public CustomerRepositoryImpl(
        CustomerRepositoryJpa repository
    ) {
        this.repository = repository;
    }

    @Override
    public CustomerMaster save(CustomerMaster request) {
        return toEntity(repository.save(toModel(request)));
    }

    @Override
    public Optional<CustomerMaster> findById(Long id) {
        return repository.findById(id).map(CustomerMapper::toEntity);
    }

    @Override
    public void changeStatus(Long id, ClientStatusType status) {
        repository.updateStatusById(id, status);
    }

    @Override
    public Optional<CustomerMaster> findByTaxId(String taxId) {
        return repository.findByTaxId(taxId).map(CustomerMapper::toEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public @Nullable CustomerMaster update(Long id, CustomerMaster request) {
        return repository.findById(id).map(existing -> {
            CustomerMaster updated = toEntity(toModel(request));
            updated.setId(existing.getId());
            return toEntity(repository.save(toModel(updated)));
        }).orElse(null);
    }

    @Override
    public Page<CustomerMaster> search(String searchTerm, ClientStatusType status, Pageable pageable) {
        return repository.search(searchTerm, status, pageable).map(CustomerMapper::toEntity);
    }

}