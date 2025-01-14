package br.com.springstudiouslabaws.labcore.domain.loan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class LoanDomain {

    String clientId;
    String name;
    String email;
    String description;
    BigDecimal amountRequested;
    LocalDate dateFinalPayment;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmountRequested() {
        return amountRequested;
    }

    public void setAmountRequested(BigDecimal amountRequested) {
        this.amountRequested = amountRequested;
    }

    public LocalDate getDateFinalPayment() {
        return dateFinalPayment;
    }

    public void setDateFinalPayment(LocalDate dateFinalPayment) {
        this.dateFinalPayment = dateFinalPayment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoanDomain that = (LoanDomain) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(description, that.description) && Objects.equals(amountRequested, that.amountRequested) && Objects.equals(dateFinalPayment, that.dateFinalPayment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, name, email, description, amountRequested, dateFinalPayment);
    }

    @Override
    public String toString() {
        return "LoanDomain{" +
                "clientId='" + clientId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", amountRequested=" + amountRequested +
                ", dateFinalPayment=" + dateFinalPayment +
                '}';
    }
}
