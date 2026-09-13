package br.com.alessandro.backend.registry.entities;

import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;
import br.com.alessandro.backend.registry.entities.enums.PersonType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomerMaster {
    private Long id;

    private PersonType personType;

    private String legalName;

    private String tradeName;

    private String taxId;

    private String stateTaxId;

    private String municipalTaxId;

    private String suframaCode;

    private ClientStatusType status;

    private List<CustomerAddress> addresses = new ArrayList<>();

    private List<CustomerContact> contacts = new ArrayList<>();

    private List<CustomerFinancials> financials = new ArrayList<>();

    private Long salesOrganizationId;

    private Long distributionChannelId;

    private Long salesRepresentativeId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Map<String, Object> metadata;

    public CustomerMaster() {}

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addAddress(CustomerAddress address) {
        addresses.add(address);
        address.setCustomer(this);
    }

    public void removeAddress(CustomerAddress address) {
        addresses.remove(address);
        address.setCustomer(null);
    }

    public void addContact(CustomerContact contact) {
        contacts.add(contact);
        contact.setCustomer(this);
    }

    public void removeContact(CustomerContact contact) {
        contacts.remove(contact);
        contact.setCustomer(null);
    }

    public void addFinancials(CustomerFinancials financials) {
        this.financials.add(financials);
        financials.setCustomer(this);
    }

    public void removeFinancials(CustomerFinancials financials) {
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

    public List<CustomerAddress> getAddresses() { return addresses; }
    public void setAddresses(List<CustomerAddress> addresses) { this.addresses = addresses; }

    public List<CustomerContact> getContacts() { return contacts; }
    public void setContacts(List<CustomerContact> contacts) { this.contacts = contacts; }

    public List<CustomerFinancials> getFinancials() { return financials; }

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
        CustomerMaster that = (CustomerMaster) o;
        return Objects.equals(id, that.id);
    }

    public int hashCode() { return Objects.hash(id); }
}
