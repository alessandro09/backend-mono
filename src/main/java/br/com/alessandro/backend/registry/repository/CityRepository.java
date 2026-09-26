package br.com.alessandro.backend.registry.repository;

import java.util.Optional;
import java.util.List;

import br.com.alessandro.backend.registry.entities.CityEntity;

public interface CityRepository {
    List<CityEntity> findAllByStateId(Long stateId);

    List<CityEntity> findAllByStateAcronym(String stateAcronym);

    Optional<CityEntity> findById(Long cityId);
}
