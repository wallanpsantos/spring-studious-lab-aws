package br.com.springstudiouslabaws.labdataprovider.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class PaymentItemEntity {

    private String paymentId;
    private BigDecimal paymentValue;
    private String paymentStatus;
    private LocalDateTime paymentDateTime;
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

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
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
        PaymentItemEntity that = (PaymentItemEntity) o;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(paymentValue, that.paymentValue) && Objects.equals(paymentStatus, that.paymentStatus) && Objects.equals(paymentDateTime, that.paymentDateTime) && Objects.equals(originalAmount, that.originalAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, paymentValue, paymentStatus, paymentDateTime, originalAmount);
    }
}

