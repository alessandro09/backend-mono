package br.com.alessandro.backend.registry.iteractors;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.alessandro.backend.registry.entities.CountryEntity;
import br.com.alessandro.backend.registry.repository.CountryRepository;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryEntity> findAll() {
        return countryRepository.findAll();
    }

    public Optional<CountryEntity> findById(Long countryId) {
        return countryRepository.findById(countryId);
    }
}
