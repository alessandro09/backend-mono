package br.com.alessandro.backend.registry.entrypoint.http.customer.dtos;

import br.com.alessandro.backend.registry.entities.enums.ContactChannel;

public record ContactDTO(
    Long id,
    ContactChannel channel,
    String contactValue,
    String contactPersonName,
    String role,
    boolean isPrimary
) {}
