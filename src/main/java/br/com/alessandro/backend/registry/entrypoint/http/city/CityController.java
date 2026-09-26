package br.com.alessandro.backend.registry.entrypoint.http.city;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.alessandro.backend.registry.entities.CityEntity;
import java.util.List;

@RequestMapping("/api/v1/cities")
public interface CityController {
    @GetMapping("by-state-id")
    List<CityEntity> findAllByStateId(@RequestParam Long stateId);

    @GetMapping("/{id}")
    CityEntity findById(@PathVariable Long id);
}
