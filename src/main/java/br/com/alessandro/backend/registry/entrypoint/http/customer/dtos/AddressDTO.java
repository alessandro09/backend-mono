package br.com.alessandro.backend.registry.entrypoint.http.customer.dtos;

import br.com.alessandro.backend.registry.entities.enums.AddressType;

public record AddressDTO(
    Long id,
    AddressType addressType,
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    String postalCode,
    String countryCode
) {}