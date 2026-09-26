package br.com.alessandro.backend.registry.entrypoint.http.state;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.alessandro.backend.registry.entities.StateEntity;

@RequestMapping("/api/v1/states")
public interface StateController {
    @GetMapping("/by-country-id/{countryId}")
    List<StateEntity> findAllByCountryId(@PathVariable Long countryId);

    @GetMapping("/{stateId}")
    StateEntity findById(@PathVariable Long stateId);
}
