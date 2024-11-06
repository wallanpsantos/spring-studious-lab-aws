package br.com.springstudiouslabaws.labdataprovider.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Document(collection = "payment")
public class PaymentEntity {

    @Id
    private String clientId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(payments, that.payments) && Objects.equals(lastUpdate, that.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, payments, lastUpdate);
    }
}
