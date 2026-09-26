package br.com.alessandro.backend.registry.datasource.country;

import br.com.alessandro.backend.registry.datasource.country.model.CountryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepositoryJpa extends JpaRepository<CountryModel, Long> {

}