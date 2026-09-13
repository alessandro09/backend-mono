package br.com.alessandro.backend.registry.datasource.customer.model;

import br.com.alessandro.backend.registry.entities.enums.ContactChannel;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "customer_contacts", indexes = {
    @Index(name = "idx_cust_cont_customer_id", columnList = "customer_id")
})
public class CustomerContactModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContactChannel channel;

    @Column(name = "contact_value", nullable = false, length = 150)
    private String contactValue;

    @Column(name = "contact_person_name", length = 100)
    private String contactPersonName;
    
    @Column(length = 50)
    private String role;
    
    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contact_customer"))
    private CustomerMasterModel customer;

    public CustomerContactModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ContactChannel getChannel() { return channel; }
    public void setChannel(ContactChannel channel) { this.channel = channel; }

    public String getContactValue() { return contactValue; }
    public void setContactValue(String contactValue) { this.contactValue = contactValue; }

    public String getContactPersonName() { return contactPersonName; }
    public void setContactPersonName(String contactPersonName) { this.contactPersonName = contactPersonName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }

    public CustomerMasterModel getCustomer() { return customer; }
    public void setCustomer(CustomerMasterModel customer) { this.customer = customer; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerContactModel that = (CustomerContactModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
