package br.com.alessandro.backend.registry.entrypoint.http.customer.mappers;

import br.com.alessandro.backend.registry.entities.CustomerAddressEntity;
import br.com.alessandro.backend.registry.entities.CustomerContactEntity;
import br.com.alessandro.backend.registry.entities.CustomerFinancialsEntity;
import br.com.alessandro.backend.registry.entities.CustomerMasterEntity;
import br.com.alessandro.backend.registry.entrypoint.http.customer.dtos.AddressDTO;
import br.com.alessandro.backend.registry.entrypoint.http.customer.dtos.ContactDTO;
import br.com.alessandro.backend.registry.entrypoint.http.customer.dtos.CustomerRequestDTO;
import br.com.alessandro.backend.registry.entrypoint.http.customer.dtos.CustomerResponseDTO;
import br.com.alessandro.backend.registry.entrypoint.http.customer.dtos.FinancialsDTO;

public interface CustomerMapper {

    public static CustomerResponseDTO toDTO(CustomerMasterEntity customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerResponseDTO(
            customer.getId(),
            customer.getPersonType(),
            customer.getLegalName(),
            customer.getTradeName(),
            customer.getTaxId(),
            customer.getStateTaxId(),
            customer.getMunicipalTaxId(),
            customer.getSuframaCode(),
            customer.getStatus(),
            customer.getAddresses().stream().map(CustomerMapper::toDTO).toList(),
            customer.getContacts().stream().map(CustomerMapper::toDTO).toList(),
            customer.getFinancials().stream().map(CustomerMapper::toDTO).toList(),
            customer.getSalesOrganizationId(),
            customer.getDistributionChannelId(),
            customer.getSalesRepresentativeId(),
            customer.getCreatedAt(),
            customer.getUpdatedAt(),
            customer.getMetadata()
        );
    }

    public static CustomerMasterEntity toEntity(CustomerRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        CustomerMasterEntity customer = new CustomerMasterEntity();
        customer.setPersonType(dto.personType());
        customer.setLegalName(dto.legalName());
        customer.setTradeName(dto.tradeName());
        customer.setTaxId(dto.taxId());
        customer.setStateTaxId(dto.stateTaxId());
        customer.setMunicipalTaxId(dto.municipalTaxId());
        customer.setSuframaCode(dto.suframaCode());
        customer.setStatus(dto.status());
        customer.setSalesOrganizationId(dto.salesOrganizationId());
        customer.setDistributionChannelId(dto.distributionChannelId());
        customer.setSalesRepresentativeId(dto.salesRepresentativeId());
        customer.setMetadata(dto.metadata());

        if (dto.addresses() != null) {
            dto.addresses().stream().map(CustomerMapper::toEntity).forEach(customer::addAddress);
        }

        if (dto.contacts() != null) {
            dto.contacts().stream().map(CustomerMapper::toEntity).forEach(customer::addContact);
        }

        if (dto.financials() != null) {
            dto.financials().stream()
                .map(CustomerMapper::toEntity)
                .forEach(customer::addFinancials);
        }

        return customer;
    }

    private static AddressDTO toDTO(CustomerAddressEntity address) {
        if (address == null) {
            return null;
        }

        return new AddressDTO(
            address.getId(),
            address.getAddressType(),
            address.getStreet(),
            address.getNumber(),
            address.getComplement(),
            address.getNeighborhood(),
            address.getCity(),
            address.getState(),
            address.getPostalCode(),
            address.getCountryCode()
        );
    }

    private static CustomerAddressEntity toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }

        CustomerAddressEntity address = new CustomerAddressEntity();
        address.setId(dto.id());
        address.setAddressType(dto.addressType());
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());
        address.setNeighborhood(dto.neighborhood());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setPostalCode(dto.postalCode());
        address.setCountryCode(dto.countryCode());
        return address;
    }

    private static ContactDTO toDTO(CustomerContactEntity contact) {
        if (contact == null) {
            return null;
        }

        return new ContactDTO(
            contact.getId(),
            contact.getChannel(),
            contact.getContactValue(),
            contact.getContactPersonName(),
            contact.getRole(),
            contact.isPrimary()
        );
    }

    private static CustomerContactEntity toEntity(ContactDTO dto) {
        if (dto == null) {
            return null;
        }

        CustomerContactEntity contact = new CustomerContactEntity();
        contact.setId(dto.id());
        contact.setChannel(dto.channel());
        contact.setContactValue(dto.contactValue());
        contact.setContactPersonName(dto.contactPersonName());
        contact.setRole(dto.role());
        contact.setPrimary(dto.isPrimary());
        return contact;
    }

    private static FinancialsDTO toDTO(CustomerFinancialsEntity financials) {
        if (financials == null) {
            return null;
        }

        return new FinancialsDTO(
            financials.getId(),
            financials.getCreditLimit(),
            financials.getCurrency(),
            financials.getPaymentTermId(),
            financials.getPaymentMethodId(),
            financials.getBankCode(),
            financials.getRoutingNumber(),
            financials.getSwiftBic(),
            financials.getAccountNumber(),
            financials.getAccountType()
        );
    }

    private static CustomerFinancialsEntity toEntity(FinancialsDTO dto) {
        if (dto == null) {
            return null;
        }

        CustomerFinancialsEntity financials = new CustomerFinancialsEntity();
        financials.setId(dto.id());
        financials.setCreditLimit(dto.creditLimit());
        financials.setCurrency(dto.currency());
        financials.setPaymentTermId(dto.paymentTermId());
        financials.setPaymentMethodId(dto.paymentMethodId());
        financials.setBankCode(dto.bankCode());
        financials.setRoutingNumber(dto.routingNumber());
        financials.setSwiftBic(dto.swiftBic());
        financials.setAccountNumber(dto.accountNumber());
        financials.setAccountType(dto.accountType());
        return financials;
    }
}
