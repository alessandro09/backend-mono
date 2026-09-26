package br.com.alessandro.backend.registry.repository;

import java.util.List;
import java.util.Optional;

import br.com.alessandro.backend.registry.entities.StateEntity;

public interface StateRepository {
    List<StateEntity> findAllByCountryId(Long countryId);

    List<StateEntity> findAllByCountryAcronym(String countryAcronym);

    Optional<StateEntity> findById(Long stateId);
}
