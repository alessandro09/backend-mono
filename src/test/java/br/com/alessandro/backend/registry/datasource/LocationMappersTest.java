package br.com.alessandro.backend.registry.datasource;

import br.com.alessandro.backend.registry.datasource.city.mapper.CityMapper;
import br.com.alessandro.backend.registry.datasource.city.model.CityModel;
import br.com.alessandro.backend.registry.datasource.country.mapper.CountryMapper;
import br.com.alessandro.backend.registry.datasource.country.model.CountryModel;
import br.com.alessandro.backend.registry.datasource.state.mapper.StateMapper;
import br.com.alessandro.backend.registry.datasource.state.model.StateModel;
import br.com.alessandro.backend.registry.entities.CityEntity;
import br.com.alessandro.backend.registry.entities.CountryEntity;
import br.com.alessandro.backend.registry.entities.StateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationMappersTest {

    @Test
    @DisplayName("Should map CountryModel to CountryEntity and vice-versa")
    void testCountryMapping() {
        assertNull(CountryMapper.toEntity(null));
        assertNull(CountryMapper.toModel(null));

        var model = new CountryModel();
        model.setId(1L);
        model.setName("Brazil");
        model.setNamePt("Brasil");
        model.setAcronym("BR");
        model.setBacen(1058);
        model.setDdi(55);

        CountryEntity entity = CountryMapper.toEntity(model);
        assertNotNull(entity);
        assertEquals(1L, entity.id());
        assertEquals("Brazil", entity.name());
        assertEquals("Brasil", entity.namePt());
        assertEquals("BR", entity.acronym());
        assertEquals(1058, entity.bacen());
        assertEquals(55, entity.ddi());

        CountryModel roundTrip = CountryMapper.toModel(entity);
        assertNotNull(roundTrip);
        assertEquals(model.getId(), roundTrip.getId());
        assertEquals(model.getName(), roundTrip.getName());
        assertEquals(model.getNamePt(), roundTrip.getNamePt());
        assertEquals(model.getAcronym(), roundTrip.getAcronym());
        assertEquals(model.getBacen(), roundTrip.getBacen());
        assertEquals(model.getDdi(), roundTrip.getDdi());
    }

    @Test
    @DisplayName("Should map StateModel to StateEntity and vice-versa including ibge, ddd and country relationship")
    void testStateMapping() {
        assertNull(StateMapper.toEntity(null));
        assertNull(StateMapper.toModel(null));

        var countryModel = new CountryModel();
        countryModel.setId(1L);
        countryModel.setName("Brazil");
        countryModel.setNamePt("Brasil");
        countryModel.setAcronym("BR");

        var stateModel = new StateModel();
        stateModel.setId(8L);
        stateModel.setName("Espírito Santo");
        stateModel.setAcronym("ES");
        stateModel.setIbge(32);
        stateModel.setCountry(countryModel);
        stateModel.setDdd(List.of(28, 27));

        StateEntity entity = StateMapper.toEntity(stateModel);
        assertNotNull(entity);
        assertEquals(8L, entity.id());
        assertEquals("Espírito Santo", entity.name());
        assertEquals("ES", entity.acronym());
        assertEquals(32, entity.ibge());
        assertNotNull(entity.country());
        assertEquals(1L, entity.country().id());
        assertEquals("BR", entity.country().acronym());
        assertEquals(List.of(28, 27), entity.ddd());

        StateModel roundTrip = StateMapper.toModel(entity);
        assertNotNull(roundTrip);
        assertEquals(stateModel.getId(), roundTrip.getId());
        assertEquals(stateModel.getName(), roundTrip.getName());
        assertEquals(stateModel.getAcronym(), roundTrip.getAcronym());
        assertEquals(stateModel.getIbge(), roundTrip.getIbge());
        assertEquals(List.of(28, 27), roundTrip.getDdd());
        assertNotNull(roundTrip.getCountry());
        assertEquals(1L, roundTrip.getCountry().getId());
    }

    @Test
    @DisplayName("Should map CityModel to CityEntity and vice-versa including coordinates and state relationship")
    void testCityMapping() {
        assertNull(CityMapper.toEntity(null));
        assertNull(CityMapper.toModel(null));

        var stateModel = new StateModel();
        stateModel.setId(8L);
        stateModel.setName("Espírito Santo");
        stateModel.setAcronym("ES");
        stateModel.setIbge(32);

        var cityModel = new CityModel();
        cityModel.setId(1L);
        cityModel.setName("Afonso Cláudio");
        cityModel.setState(stateModel);
        cityModel.setIbge(3200102);
        cityModel.setLatLon("(-20.0778007507324,-41.1260986328125)");
        cityModel.setCodTom((short) 5601);

        CityEntity entity = CityMapper.toEntity(cityModel);
        assertNotNull(entity);
        assertEquals(1L, entity.id());
        assertEquals("Afonso Cláudio", entity.name());
        assertEquals(3200102, entity.ibge());
        assertEquals("(-20.0778007507324,-41.1260986328125)", entity.latLon());
        assertEquals((short) 5601, entity.codTom());
        assertNotNull(entity.state());
        assertEquals(8L, entity.state().id());
        assertEquals("ES", entity.state().acronym());

        CityModel roundTrip = CityMapper.toModel(entity);
        assertNotNull(roundTrip);
        assertEquals(cityModel.getId(), roundTrip.getId());
        assertEquals(cityModel.getName(), roundTrip.getName());
        assertEquals(cityModel.getIbge(), roundTrip.getIbge());
        assertEquals(cityModel.getLatLon(), roundTrip.getLatLon());
        assertEquals(cityModel.getCodTom(), roundTrip.getCodTom());
        assertNotNull(roundTrip.getState());
        assertEquals(8L, roundTrip.getState().getId());
    }
}
