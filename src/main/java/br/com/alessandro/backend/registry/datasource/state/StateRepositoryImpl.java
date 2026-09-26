package br.com.alessandro.backend.registry.datasource.state;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.alessandro.backend.registry.datasource.state.mapper.StateMapper;
import br.com.alessandro.backend.registry.entities.StateEntity;
import br.com.alessandro.backend.registry.repository.StateRepository;

@Repository
public class StateRepositoryImpl implements StateRepository {
    private final StateRepositoryJpa stateRepositoryJpa;

    public StateRepositoryImpl(StateRepositoryJpa stateRepositoryJpa) {
        this.stateRepositoryJpa = stateRepositoryJpa;
    }

	@Override
	public List<StateEntity> findAllByCountryId(Long countryId) {
        var models = stateRepositoryJpa.findAllByCountryId(countryId);
		
        return models.stream().map(StateMapper::toEntity).toList();
	}

	@Override
	public List<StateEntity> findAllByCountryAcronym(String countryAcronym) {
		var models = stateRepositoryJpa.findAllByCountryAcronym(countryAcronym);

        return models.stream().map(StateMapper::toEntity).toList();
	}

	@Override
	public Optional<StateEntity> findById(Long stateId) {
		var model = stateRepositoryJpa.findById(stateId);

        return model.map(StateMapper::toEntity);
	}
}
