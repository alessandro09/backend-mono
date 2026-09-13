package br.com.alessandro.backend.registry.entities;

import br.com.alessandro.backend.registry.entities.enums.ContactChannel;
import java.util.Objects;

public class CustomerContact {

    private Long id;

    private ContactChannel channel;

    private String contactValue;

    private String contactPersonName;
    
    private String role;
    
    private boolean isPrimary = false;

    private CustomerMaster customer;

    public CustomerContact() {}

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

    public CustomerMaster getCustomer() { return customer; }
    public void setCustomer(CustomerMaster customer) { this.customer = customer; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerContact that = (CustomerContact) o;
        return Objects.equals(id, that.id);
    }

    public int hashCode() { return Objects.hash(id); }
}
