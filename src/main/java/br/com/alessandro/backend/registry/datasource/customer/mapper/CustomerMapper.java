package br.com.alessandro.backend.registry.datasource.customer.mapper;

import br.com.alessandro.backend.registry.datasource.customer.model.CustomerAddressModel;
import br.com.alessandro.backend.registry.datasource.customer.model.CustomerContactModel;
import br.com.alessandro.backend.registry.datasource.customer.model.CustomerFinancialsModel;
import br.com.alessandro.backend.registry.datasource.customer.model.CustomerMasterModel;
import br.com.alessandro.backend.registry.entities.CustomerAddress;
import br.com.alessandro.backend.registry.entities.CustomerContact;
import br.com.alessandro.backend.registry.entities.CustomerFinancials;
import br.com.alessandro.backend.registry.entities.CustomerMaster;

public interface CustomerMapper {
    public static CustomerMasterModel toModel(CustomerMaster entity) {
        if (entity == null) {
            return null;
        }

        CustomerMasterModel model = new CustomerMasterModel();
        model.setId(entity.getId());
        model.setPersonType(entity.getPersonType());
        model.setLegalName(entity.getLegalName());
        model.setTradeName(entity.getTradeName());
        model.setTaxId(entity.getTaxId());
        model.setStateTaxId(entity.getStateTaxId());
        model.setMunicipalTaxId(entity.getMunicipalTaxId());
        model.setSuframaCode(entity.getSuframaCode());
        model.setStatus(entity.getStatus());
        model.setSalesOrganizationId(entity.getSalesOrganizationId());
        model.setDistributionChannelId(entity.getDistributionChannelId());
        model.setSalesRepresentativeId(entity.getSalesRepresentativeId());
        model.setMetadata(entity.getMetadata());

        if (entity.getAddresses() != null) {
            entity.getAddresses().stream()
                .map(CustomerMapper::toModel)
                .forEach(model::addAddress);
        }

        if (entity.getContacts() != null) {
            entity.getContacts().stream()
                .map(CustomerMapper::toModel)
                .forEach(model::addContact);
        }

        if (entity.getFinancials() != null) {
            entity.getFinancials().stream()
                .map(CustomerMapper::toModel)
                .forEach(model::addFinancials);
        }
        return model;
    }

    public static CustomerMaster toEntity(CustomerMasterModel model) {
        if (model == null) {
            return null;
        }

        CustomerMaster entity = new CustomerMaster();
        entity.setId(model.getId());
        entity.setPersonType(model.getPersonType());
        entity.setLegalName(model.getLegalName());
        entity.setTradeName(model.getTradeName());
        entity.setTaxId(model.getTaxId());
        entity.setStateTaxId(model.getStateTaxId());
        entity.setMunicipalTaxId(model.getMunicipalTaxId());
        entity.setSuframaCode(model.getSuframaCode());
        entity.setStatus(model.getStatus());
        entity.setSalesOrganizationId(model.getSalesOrganizationId());
        entity.setDistributionChannelId(model.getDistributionChannelId());
        entity.setSalesRepresentativeId(model.getSalesRepresentativeId());
        entity.setMetadata(model.getMetadata());

        if (model.getAddresses() != null) {
            model.getAddresses().stream()
                .map(CustomerMapper::toEntity)
                .forEach(entity::addAddress);
        }

        if (model.getContacts() != null) {
            model.getContacts().stream()
                .map(CustomerMapper::toEntity)
                .forEach(entity::addContact);
        }

        if (model.getFinancials() != null) {
            model.getFinancials().stream()
                .map(CustomerMapper::toEntity)
                .forEach(entity::addFinancials);
        }
        return entity;
    }

    public static CustomerAddressModel toModel(CustomerAddress entity) {
        if (entity == null) {
            return null;
        }

        CustomerAddressModel model = new CustomerAddressModel();
        model.setId(entity.getId());
        model.setAddressType(entity.getAddressType());
        model.setStreet(entity.getStreet());
        model.setNumber(entity.getNumber());
        model.setComplement(entity.getComplement());
        model.setNeighborhood(entity.getNeighborhood());
        model.setCity(entity.getCity());
        model.setState(entity.getState());
        model.setPostalCode(entity.getPostalCode());
        model.setCountryCode(entity.getCountryCode());
        return model;
    }

    public static CustomerAddress toEntity(CustomerAddressModel model) {
        if (model == null) {
            return null;
        }

        CustomerAddress entity = new CustomerAddress();
        entity.setId(model.getId());
        entity.setAddressType(model.getAddressType());
        entity.setStreet(model.getStreet());
        entity.setNumber(model.getNumber());
        entity.setComplement(model.getComplement());
        entity.setNeighborhood(model.getNeighborhood());
        entity.setCity(model.getCity());
        entity.setState(model.getState());
        entity.setPostalCode(model.getPostalCode());
        entity.setCountryCode(model.getCountryCode());
        return entity;
    }

    public static CustomerContactModel toModel(CustomerContact entity) {
        if (entity == null) {
            return null;
        }

        CustomerContactModel model = new CustomerContactModel();
        model.setId(entity.getId());
        model.setChannel(entity.getChannel());
        model.setContactValue(entity.getContactValue());
        model.setContactPersonName(entity.getContactPersonName());
        model.setRole(entity.getRole());
        model.setPrimary(entity.isPrimary());
        return model;
    }

    public static CustomerContact toEntity(CustomerContactModel model) {
        if (model == null) {
            return null;
        }

        CustomerContact entity = new CustomerContact();
        entity.setId(model.getId());
        entity.setChannel(model.getChannel());
        entity.setContactValue(model.getContactValue());
        entity.setContactPersonName(model.getContactPersonName());
        entity.setRole(model.getRole());
        entity.setPrimary(model.isPrimary());
        return entity;
    }

    public static CustomerFinancialsModel toModel(CustomerFinancials entity) {
        if (entity == null) {
            return null;
        }

        CustomerFinancialsModel model = new CustomerFinancialsModel();
        model.setId(entity.getId());
        model.setCreditLimit(entity.getCreditLimit());
        model.setCurrency(entity.getCurrency());
        model.setPaymentTermId(entity.getPaymentTermId());
        model.setPaymentMethodId(entity.getPaymentMethodId());
        model.setBankCode(entity.getBankCode());
        model.setRoutingNumber(entity.getRoutingNumber());
        model.setSwiftBic(entity.getSwiftBic());
        model.setAccountNumber(entity.getAccountNumber());
        model.setAccountType(entity.getAccountType());
        return model;
    }

    public static CustomerFinancials toEntity(CustomerFinancialsModel model) {
        if (model == null) {
            return null;
        }

        CustomerFinancials entity = new CustomerFinancials();
        entity.setId(model.getId());
        entity.setCreditLimit(model.getCreditLimit());
        entity.setCurrency(model.getCurrency());
        entity.setPaymentTermId(model.getPaymentTermId());
        entity.setPaymentMethodId(model.getPaymentMethodId());
        entity.setBankCode(model.getBankCode());
        entity.setRoutingNumber(model.getRoutingNumber());
        entity.setSwiftBic(model.getSwiftBic());
        entity.setAccountNumber(model.getAccountNumber());
        entity.setAccountType(model.getAccountType());
        return entity;
    }
}
