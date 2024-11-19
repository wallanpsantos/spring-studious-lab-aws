package br.com.springstudiouslabaws.labdataprovider.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Document(collection = "payment")
public class PaymentEntity {

    @Id
    private String clientId;

    private String clientName;

    private String clientEmail;

    private List<PaymentItemEntity> payments;

    private LocalDateTime lastUpdate;

    public PaymentEntity(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<PaymentItemEntity> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentItemEntity> payments) {
        this.payments = payments;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(clientName, that.clientName) && Objects.equals(clientEmail, that.clientEmail) && Objects.equals(payments, that.payments) && Objects.equals(lastUpdate, that.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientName, clientEmail, payments, lastUpdate);
    }
}
