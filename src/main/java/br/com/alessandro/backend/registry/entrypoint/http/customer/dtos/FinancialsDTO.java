package br.com.alessandro.backend.registry.entrypoint.http.customer.dtos;

import br.com.alessandro.backend.registry.entities.enums.AccountType;
import java.math.BigDecimal;

public record FinancialsDTO(
    Long id,
    BigDecimal creditLimit,
    String currency,
    Long paymentTermId,
    Long paymentMethodId,
    String bankCode,
    String routingNumber,
    String swiftBic,
    String accountNumber,
    AccountType accountType
) {}