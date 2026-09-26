package br.com.alessandro.backend.registry.datasource.city.mapper;

import br.com.alessandro.backend.registry.datasource.city.model.CityModel;
import br.com.alessandro.backend.registry.datasource.state.mapper.StateMapper;
import br.com.alessandro.backend.registry.entities.CityEntity;

public interface CityMapper {
    public static CityEntity toEntity(CityModel model) {
        if (model == null) {
            return null;
        }

        return new CityEntity(
            model.getId(),
            model.getName(),
            StateMapper.toEntity(model.getState()),
            model.getIbge(),
            model.getLatLon(),
            model.getCodTom()
        );
    }

    public static CityModel toModel(CityEntity entity) {
        if (entity == null) {
            return null;
        }

        var model = new CityModel();
        model.setId(entity.id());
        model.setName(entity.name());
        model.setState(StateMapper.toModel(entity.state()));
        model.setIbge(entity.ibge());
        model.setLatLon(entity.latLon());
        model.setCodTom(entity.codTom());

        return model;
    }
}
