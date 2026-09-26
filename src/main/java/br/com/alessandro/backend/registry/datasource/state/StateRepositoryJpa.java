package br.com.alessandro.backend.registry.datasource.state;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alessandro.backend.registry.datasource.state.model.StateModel;

@Repository 
public interface StateRepositoryJpa extends JpaRepository<StateModel, Long> {
    public List<StateModel> findAllByCountryId(Long countryId);

    public List<StateModel> findAllByCountryAcronym(String countryAcronym);
}
