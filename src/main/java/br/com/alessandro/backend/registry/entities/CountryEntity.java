package br.com.alessandro.backend.registry.entities;

public record CountryEntity(
    Long id,
    String name,
    String namePt,
    String acronym,
    Integer bacen,
    Integer ddi
) { }
