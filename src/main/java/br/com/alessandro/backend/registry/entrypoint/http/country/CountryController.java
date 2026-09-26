package br.com.alessandro.backend.registry.entrypoint.http.country;
import java.util.List;
import br.com.alessandro.backend.registry.entities.CountryEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/countries")
public interface CountryController {
    @GetMapping
    List<CountryEntity> findAll();

    @GetMapping("/{id}")
    CountryEntity findById(@PathVariable Long id);
}
