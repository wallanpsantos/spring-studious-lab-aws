package br.com.springstudiouslabaws.labcore.domain.payment;

import br.com.springstudiouslabaws.labcore.enums.PaymentItemStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class StartDebitItemDomain {

    private String paymentId;
    private String paymentStatus;
    private final LocalDate dateFinalPayment;
    private final BigDecimal originalAmount;

    public StartDebitItemDomain(BigDecimal originalAmount, LocalDate dateFinalPayment) {
        this.originalAmount = originalAmount;
        this.dateFinalPayment = dateFinalPayment;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDate getDateFinalPayment() {
        return dateFinalPayment;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = UUID.randomUUID().toString();
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = PaymentItemStatusEnum.NEW.getValue();
    }

}