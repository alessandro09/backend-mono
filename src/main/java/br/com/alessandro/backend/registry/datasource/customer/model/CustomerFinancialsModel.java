package br.com.alessandro.backend.registry.datasource.customer.model;

import br.com.alessandro.backend.registry.entities.enums.AccountType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "customer_financials")
public class CustomerFinancialsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;

    @Column(length = 3)
    private String currency;
    
    @Column(name = "payment_term_id")
    private Long paymentTermId;
    
    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "bank_code", length = 10)
    private String bankCode;
    
    @Column(name = "routing_number", length = 30)
    private String routingNumber;
    
    @Column(name = "swift_bic", length = 20)
    private String swiftBic;
    
    @Column(name = "account_number", length = 30)
    private String accountNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", length = 20)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_financials_customer"))
    private CustomerMasterModel customer;

    public CustomerFinancialsModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Long getPaymentTermId() { return paymentTermId; }
    public void setPaymentTermId(Long paymentTermId) { this.paymentTermId = paymentTermId; }

    public Long getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(Long paymentMethodId) { this.paymentMethodId = paymentMethodId; }

    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }

    public String getRoutingNumber() { return routingNumber; }
    public void setRoutingNumber(String routingNumber) { this.routingNumber = routingNumber; }

    public String getSwiftBic() { return swiftBic; }
    public void setSwiftBic(String swiftBic) { this.swiftBic = swiftBic; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public CustomerMasterModel getCustomer() { return customer; }
    public void setCustomer(CustomerMasterModel customer) { this.customer = customer; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerFinancialsModel that = (CustomerFinancialsModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
