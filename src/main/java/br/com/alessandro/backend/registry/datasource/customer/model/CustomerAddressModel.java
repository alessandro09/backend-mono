package br.com.alessandro.backend.registry.datasource.customer.model;

import br.com.alessandro.backend.registry.entities.enums.AddressType;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "customer_addresses", indexes = {
    @Index(name = "idx_cust_addr_customer_id", columnList = "customer_id")
})
public class CustomerAddressModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false, length = 20)
    private AddressType addressType;

    @Column(nullable = false, length = 150)
    private String street;

    @Column(nullable = false, length = 20)
    private String number;
    
    @Column(length = 100)
    private String complement;
    
    @Column(nullable = false, length = 80)
    private String neighborhood;
    
    @Column(nullable = false, length = 100)
    private String city;
    
    @Column(nullable = false, length = 50)
    private String state;
    
    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;
    
    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_address_customer"))
    private CustomerMasterModel customer;

    public CustomerAddressModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AddressType getAddressType() { return addressType; }
    public void setAddressType(AddressType addressType) { this.addressType = addressType; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getComplement() { return complement; }
    public void setComplement(String complement) { this.complement = complement; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public CustomerMasterModel getCustomer() { return customer; }
    public void setCustomer(CustomerMasterModel customer) { this.customer = customer; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAddressModel that = (CustomerAddressModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
