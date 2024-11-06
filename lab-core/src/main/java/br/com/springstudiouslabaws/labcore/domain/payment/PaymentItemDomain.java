package br.com.springstudiouslabaws.labcore.domain.payment;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentItemDomain {

    private String paymentId;
    private BigDecimal paymentValue;
    private String paymentStatus;
    private BigDecimal originalAmount;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(BigDecimal paymentValue) {
        this.paymentValue = paymentValue;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentItemDomain that = (PaymentItemDomain) o;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(paymentValue, that.paymentValue) && Objects.equals(paymentStatus, that.paymentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, paymentValue, paymentStatus);
    }

    @Override
    public String toString() {
        return "{" +
                "paymentId='" + paymentId + '\'' +
                ", paymentValue=" + paymentValue +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", originalAmount=" + originalAmount +
                '}';
    }
}
