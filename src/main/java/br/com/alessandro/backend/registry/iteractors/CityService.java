package br.com.alessandro.backend.registry.iteractors;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.alessandro.backend.registry.entities.CityEntity;
import br.com.alessandro.backend.registry.repository.CityRepository;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<CityEntity> findAllByStateId(Long stateId) {
        return cityRepository.findAllByStateId(stateId);
    }

    public List<CityEntity> findAllByStateAcronym(String stateAcronym) {
        return cityRepository.findAllByStateAcronym(stateAcronym);
    }

    public Optional<CityEntity> findById(Long cityId) {
        return cityRepository.findById(cityId);
    }
}
