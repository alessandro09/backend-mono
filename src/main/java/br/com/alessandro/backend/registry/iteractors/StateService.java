package br.com.alessandro.backend.registry.iteractors;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.alessandro.backend.registry.entities.StateEntity;
import br.com.alessandro.backend.registry.repository.StateRepository;

@Service
public class StateService {
    private final StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public List<StateEntity> findAllByCountryId(Long countryId) {
        return stateRepository.findAllByCountryId(countryId);
    }

    public Optional<StateEntity> findById(Long stateId) {
        return stateRepository.findById(stateId);
    }
}
