package br.com.springstudiouslabaws.domain.payment;

import java.util.List;
import java.util.Objects;

public class PaymentDomain {

    private String clientId;

    private List<PaymentItemDomain> paymentItems;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<PaymentItemDomain> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItemDomain> paymentItems) {
        this.paymentItems = paymentItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDomain that = (PaymentDomain) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(paymentItems, that.paymentItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, paymentItems);
    }

    @Override
    public String toString() {
        return "{" +
                "clientId='" + clientId + '\'' +
                ", paymentItems=" + paymentItems +
                '}';
    }
}
