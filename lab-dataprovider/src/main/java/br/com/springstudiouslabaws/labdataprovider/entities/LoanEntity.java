package br.com.springstudiouslabaws.labdataprovider.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "loan")
public class LoanEntity {

    @Id
    private String id;

    private String loanId;
    private String description;
    private BigDecimal amountRequested;
    private BigDecimal outstandingBalance;
    private String status;
    private String dateFinalPayment;
    private PaymentsEntity payments;
    private ClientEntity client;
    private LocalDateTime lastUpdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
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

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateFinalPayment() {
        return dateFinalPayment;
    }

    public void setDateFinalPayment(String dateFinalPayment) {
        this.dateFinalPayment = dateFinalPayment;
    }

    public PaymentsEntity getPayments() {
        return payments;
    }

    public void setPayments(PaymentsEntity payments) {
        this.payments = payments;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
