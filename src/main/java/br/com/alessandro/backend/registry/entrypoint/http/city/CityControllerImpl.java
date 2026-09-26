package br.com.alessandro.backend.registry.entrypoint.http.city;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import br.com.alessandro.backend.registry.entities.CityEntity;
import br.com.alessandro.backend.registry.iteractors.CityService;

@RestController
public class CityControllerImpl implements CityController {
    private final CityService cityService;

    public CityControllerImpl(CityService cityService) {
        this.cityService = cityService;
    }

	@Override
	public List<CityEntity> findAllByStateId(Long stateId) {
		return cityService.findAllByStateId(stateId);
	}

	@Override
	public CityEntity findById(Long id) {
		return cityService.findById(id).orElse(null);
	}
    
}
