package br.com.springstudiouslabaws.labdataprovider.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentsEntity {

    private String paymentId;
    private String comment;
    private BigDecimal paymentValue;
    private String paymentOption;
    private BigDecimal amountRequested;
    private LocalDateTime timestampOperation;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(BigDecimal paymentValue) {
        this.paymentValue = paymentValue;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public BigDecimal getAmountRequested() {
        return amountRequested;
    }

    public void setAmountRequested(BigDecimal amountRequested) {
        this.amountRequested = amountRequested;
    }

    public LocalDateTime getTimestampOperation() {
        return timestampOperation;
    }

    public void setTimestampOperation(LocalDateTime timestampOperation) {
        this.timestampOperation = timestampOperation;
    }
}
