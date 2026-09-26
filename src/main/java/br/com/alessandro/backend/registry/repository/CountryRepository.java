package br.com.alessandro.backend.registry.repository;
import java.util.List;
import java.util.Optional;
import br.com.alessandro.backend.registry.entities.CountryEntity;

public interface CountryRepository {
    List<CountryEntity> findAll();

    Optional<CountryEntity> findById(Long countryId);
}
