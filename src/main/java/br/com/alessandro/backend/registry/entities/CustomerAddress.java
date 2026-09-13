package br.com.alessandro.backend.registry.entities;

import br.com.alessandro.backend.registry.entities.enums.AddressType;
import java.util.Objects;

public class CustomerAddress {
    private Long id;

    private AddressType addressType;

    private String street;

    private String number;
    
    private String complement;
    
    private String neighborhood;
    
    private String city;
    
    private String state;
    
    private String postalCode;
    
    private String countryCode;

    private CustomerMaster customer;

    public CustomerAddress() {}

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

    public CustomerMaster getCustomer() { return customer; }
    public void setCustomer(CustomerMaster customer) { this.customer = customer; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAddress that = (CustomerAddress) o;
        return Objects.equals(id, that.id);
    }

    public int hashCode() { return Objects.hash(id); }
}
