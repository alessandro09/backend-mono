package br.com.alessandro.backend.registry.datasource.customer.model;

import br.com.alessandro.backend.registry.entities.enums.ClientStatusType;
import br.com.alessandro.backend.registry.entities.enums.PersonType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "customers")
public class CustomerMasterModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "person_type", nullable = false, length = 20)
    private PersonType personType;

    @Column(name = "legal_name", nullable = false, length = 150)
    private String legalName;

    @Column(name = "trade_name", length = 150)
    private String tradeName;

    @Column(name = "tax_id", nullable = false, unique = true, length = 50)
    private String taxId; // Comporta o tamanho de CNPJ/CPF com pontuação ou chaves globais

    @Column(name = "state_tax_id", length = 30)
    private String stateTaxId;

    @Column(name = "municipal_tax_id", length = 30)
    private String municipalTaxId;

    @Column(name = "suframa_code", length = 20)
    private String suframaCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClientStatusType status;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerAddressModel> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerContactModel> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerFinancialsModel> financials = new ArrayList<>();

    @Column(name = "sales_organization_id")
    private Long salesOrganizationId;

    @Column(name = "distribution_channel_id")
    private Long distributionChannelId;

    @Column(name = "sales_representative_id")
    private Long salesRepresentativeId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    public CustomerMasterModel() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addAddress(CustomerAddressModel address) {
        addresses.add(address);
        address.setCustomer(this);
    }

    public void removeAddress(CustomerAddressModel address) {
        addresses.remove(address);
        address.setCustomer(null);
    }

    public void addContact(CustomerContactModel contact) {
        contacts.add(contact);
        contact.setCustomer(this);
    }

    public void removeContact(CustomerContactModel contact) {
        contacts.remove(contact);
        contact.setCustomer(null);
    }

    public void addFinancials(CustomerFinancialsModel financials) {
        this.financials.add(financials);
        financials.setCustomer(this);
    }

    public void removeFinancials(CustomerFinancialsModel financials) {
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

    public List<CustomerAddressModel> getAddresses() { return addresses; }
    public void setAddresses(List<CustomerAddressModel> addresses) { this.addresses = addresses; }

    public List<CustomerContactModel> getContacts() { return contacts; }
    public void setContacts(List<CustomerContactModel> contacts) { this.contacts = contacts; }

    public List<CustomerFinancialsModel> getFinancials() { return financials; }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerMasterModel that = (CustomerMasterModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
