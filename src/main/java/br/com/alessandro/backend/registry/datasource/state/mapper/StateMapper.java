package br.com.alessandro.backend.registry.datasource.state.mapper;

import br.com.alessandro.backend.registry.datasource.country.mapper.CountryMapper;
import br.com.alessandro.backend.registry.datasource.state.model.StateModel;
import br.com.alessandro.backend.registry.entities.StateEntity;

public class StateMapper {
    public static StateEntity toEntity(StateModel model) {
        if (model == null) {
            return null;
        }
        
        return new StateEntity(
            model.getId(),
            model.getName(),
            model.getAcronym(),
            model.getIbge(),
            CountryMapper.toEntity(model.getCountry()),
            model.getDdd()
        );
    }

    public static StateModel toModel(StateEntity entity) {
        if (entity == null) {
            return null;
        }

        var model = new StateModel();
        model.setId(entity.id());
        model.setName(entity.name());
        model.setAcronym(entity.acronym());
        model.setIbge(entity.ibge());
        model.setCountry(CountryMapper.toModel(entity.country()));
        model.setDdd(entity.ddd());
        
        return model;
    }
}
