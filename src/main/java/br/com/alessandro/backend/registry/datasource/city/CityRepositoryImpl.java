package br.com.alessandro.backend.registry.datasource.city;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.alessandro.backend.registry.datasource.city.mapper.CityMapper;
import br.com.alessandro.backend.registry.entities.CityEntity;
import br.com.alessandro.backend.registry.repository.CityRepository;

@Repository
public class CityRepositoryImpl implements CityRepository {
    private final CityRepositoryJpa cityRepositoryJpa;

    public CityRepositoryImpl(CityRepositoryJpa cityRepositoryJpa) {
        this.cityRepositoryJpa = cityRepositoryJpa;
    }

	@Override
	public List<CityEntity> findAllByStateId(Long stateId) {
		return cityRepositoryJpa.findAllByStateId(stateId).stream().map(CityMapper::toEntity).toList();
	}

	@Override
	public List<CityEntity> findAllByStateAcronym(String stateAcronym) {
		return cityRepositoryJpa.findAllByStateAcronym(stateAcronym).stream().map(CityMapper::toEntity).toList();
	}

	@Override
	public Optional<CityEntity> findById(Long cityId) {
		return cityRepositoryJpa.findById(cityId).map(CityMapper::toEntity);
	}
}