package br.com.alessandro.backend.registry.datasource.country.mapper;

import br.com.alessandro.backend.registry.datasource.country.model.CountryModel;
import br.com.alessandro.backend.registry.entities.CountryEntity;

public interface CountryMapper {
    static CountryEntity toEntity(CountryModel model) {
        if (model == null) {
            return null;
        }

        return new CountryEntity(
            model.getId(),
            model.getName(),
            model.getNamePt(),
            model.getAcronym(),
            model.getBacen(),
            model.getDdi()
        );
    }

    static CountryModel toModel(CountryEntity entity) {
        if (entity == null) {
            return null;
        }

        var model = new CountryModel();
        model.setId(entity.id());
        model.setName(entity.name());
        model.setNamePt(entity.namePt());
        model.setAcronym(entity.acronym());
        model.setBacen(entity.bacen());
        model.setDdi(entity.ddi());

        return model;
    }
}
