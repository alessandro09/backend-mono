package br.com.alessandro.backend.registry.datasource.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alessandro.backend.registry.datasource.city.model.CityModel;

@Repository 
public interface CityRepositoryJpa extends JpaRepository<CityModel, Long> {
    public List<CityModel> findAllByStateId(Long stateId);

    public List<CityModel> findAllByStateAcronym(String stateAcronym);
}
