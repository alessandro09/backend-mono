package br.com.alessandro.backend.registry.datasource.country;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.alessandro.backend.registry.datasource.country.mapper.CountryMapper;
import br.com.alessandro.backend.registry.entities.CountryEntity;
import br.com.alessandro.backend.registry.repository.CountryRepository;

@Repository
public class CountryRepositoryImpl implements CountryRepository {
    private final CountryRepositoryJpa countryRepositoryJpa;

    public CountryRepositoryImpl(CountryRepositoryJpa countryRepositoryJpa) {
        this.countryRepositoryJpa = countryRepositoryJpa;
    }

	@Override
	public List<CountryEntity> findAll() {
		return countryRepositoryJpa.findAll().stream().map(CountryMapper::toEntity).toList();
	}

	@Override
	public Optional<CountryEntity> findById(Long countryId) {
		return countryRepositoryJpa.findById(countryId).map(CountryMapper::toEntity);
	}
}