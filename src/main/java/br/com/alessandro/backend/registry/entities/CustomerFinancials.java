package br.com.alessandro.backend.registry.entities;

import br.com.alessandro.backend.registry.entities.enums.AccountType;
import java.math.BigDecimal;
import java.util.Objects;

public class CustomerFinancials {
    private Long id;

    private BigDecimal creditLimit;

    private String currency;
    
    private Long paymentTermId;
    
    private Long paymentMethodId;

    private String bankCode;
    
    private String routingNumber;
    
    private String swiftBic;
    
    private String accountNumber;
    
    private AccountType accountType;

    private CustomerMaster customer;

    public CustomerFinancials() {}

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

    public CustomerMaster getCustomer() { return customer; }
    public void setCustomer(CustomerMaster customer) { this.customer = customer; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerFinancials that = (CustomerFinancials) o;
        return Objects.equals(id, that.id);
    }

    public int hashCode() { return Objects.hash(id); }
}
