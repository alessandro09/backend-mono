package br.com.alessandro.backend.registry.entrypoint.http.customer.dtos;

import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;
import br.com.alessandro.backend.registry.entities.enums.PersonType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record CustomerResponseDTO(
    Long id,
    PersonType personType,
    String legalName,
    String tradeName,
    String taxId,
    String stateTaxId,
    String municipalTaxId,
    String suframaCode,
    ClientStatusType status,
    List<AddressDTO> addresses,
    List<ContactDTO> contacts,
    List<FinancialsDTO> financials,
    Long salesOrganizationId,
    Long distributionChannelId,
    Long salesRepresentativeId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Map<String, Object> metadata
) {}