package br.com.alessandro.backend.registry.entities;

import java.util.List;

public record StateEntity(
    Long id,
    String name,
    String acronym,
    Integer ibge,
    CountryEntity country,
    List<Integer> ddd
) { }
