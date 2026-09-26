package br.com.alessandro.backend.registry.entities;

import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;
import br.com.alessandro.backend.registry.entities.enums.PersonType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomerMasterEntity {
    private Long id;

    private PersonType personType;

    private String legalName;

    private String tradeName;

    private String taxId;

    private String stateTaxId;

    private String municipalTaxId;

    private String suframaCode;

    private ClientStatusType status;

    private List<CustomerAddressEntity> addresses = new ArrayList<>();

    private List<CustomerContactEntity> contacts = new ArrayList<>();

    private List<CustomerFinancialsEntity> financials = new ArrayList<>();

    private Long salesOrganizationId;

    private Long distributionChannelId;

    private Long salesRepresentativeId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Map<String, Object> metadata;

    public CustomerMasterEntity() {}

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addAddress(CustomerAddressEntity address) {
        addresses.add(address);
        address.setCustomer(this);
    }

    public void removeAddress(CustomerAddressEntity address) {
        addresses.remove(address);
        address.setCustomer(null);
    }

    public void addContact(CustomerContactEntity contact) {
        contacts.add(contact);
        contact.setCustomer(this);
    }

    public void removeContact(CustomerContactEntity contact) {
        contacts.remove(contact);
        contact.setCustomer(null);
    }

    public void addFinancials(CustomerFinancialsEntity financials) {
        this.financials.add(financials);
        financials.setCustomer(this);
    }

    public void removeFinancials(CustomerFinancialsEntity financials) {
        this.financials.remove(financials);
        financials.setCustomer(null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PersonType getPersonType() { return personType; }
    public void setPersonType(PersonType personType) { this.personType = personType; }

    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }

    public String getTradeName() { return tradeName; }
    public void setTradeName(String tradeName) { this.tradeName = tradeName; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public String getStateTaxId() { return stateTaxId; }
    public void setStateTaxId(String stateTaxId) { this.stateTaxId = stateTaxId; }

    public String getMunicipalTaxId() { return municipalTaxId; }
    public void setMunicipalTaxId(String municipalTaxId) { this.municipalTaxId = municipalTaxId; }

    public String getSuframaCode() { return suframaCode; }
    public void setSuframaCode(String suframaCode) { this.suframaCode = suframaCode; }

    public ClientStatusType getStatus() { return status; }
    public void setStatus(ClientStatusType status) { this.status = status; }

    public List<CustomerAddressEntity> getAddresses() { return addresses; }
    public void setAddresses(List<CustomerAddressEntity> addresses) { this.addresses = addresses; }

    public List<CustomerContactEntity> getContacts() { return contacts; }
    public void setContacts(List<CustomerContactEntity> contacts) { this.contacts = contacts; }

    public List<CustomerFinancialsEntity> getFinancials() { return financials; }

    public Long getSalesOrganizationId() { return salesOrganizationId; }
    public void setSalesOrganizationId(Long salesOrganizationId) { this.salesOrganizationId = salesOrganizationId; }

    public Long getDistributionChannelId() { return distributionChannelId; }
    public void setDistributionChannelId(Long distributionChannelId) { this.distributionChannelId = distributionChannelId; }

    public Long getSalesRepresentativeId() { return salesRepresentativeId; }
    public void setSalesRepresentativeId(Long salesRepresentativeId) { this.salesRepresentativeId = salesRepresentativeId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerMasterEntity that = (CustomerMasterEntity) o;
        return Objects.equals(id, that.id);
    }

    public int hashCode() { return Objects.hash(id); }
}
