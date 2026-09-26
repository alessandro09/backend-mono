package br.com.alessandro.backend.registry.entities;

public record CityEntity(
    Long id,
    String name,
    StateEntity state,
    Integer ibge,
    String latLon,
    Short codTom
) { }
