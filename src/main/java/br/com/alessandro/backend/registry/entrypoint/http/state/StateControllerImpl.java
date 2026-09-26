package br.com.alessandro.backend.registry.entrypoint.http.state;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import br.com.alessandro.backend.registry.entities.StateEntity;
import br.com.alessandro.backend.registry.iteractors.StateService;

@RestController
public class StateControllerImpl implements StateController {
    private final StateService stateService;

    public StateControllerImpl(StateService stateService) {
        this.stateService = stateService;
    }

	@Override
	public List<StateEntity> findAllByCountryId(Long countryId) {
		return stateService.findAllByCountryId(countryId);
	}

	@Override
	public StateEntity findById(Long stateId) {
		return stateService.findById(stateId).orElse(null);
	}
    
}
