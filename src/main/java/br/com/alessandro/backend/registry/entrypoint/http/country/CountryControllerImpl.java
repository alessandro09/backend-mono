package br.com.alessandro.backend.registry.entrypoint.http.country;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import br.com.alessandro.backend.registry.entities.CountryEntity;
import br.com.alessandro.backend.registry.iteractors.CountryService;

@RestController
public class CountryControllerImpl implements CountryController {
    private final CountryService countryService;

    public CountryControllerImpl(CountryService countryService) {
        this.countryService = countryService;
    }

	@Override
	public List<CountryEntity> findAll() {
		return countryService.findAll();
	}

	@Override
	public CountryEntity findById(Long id) {
		return countryService.findById(id).orElse(null);
	}
    
}
