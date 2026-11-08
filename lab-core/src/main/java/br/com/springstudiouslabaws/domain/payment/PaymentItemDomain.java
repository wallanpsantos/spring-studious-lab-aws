package br.com.springstudiouslabaws.domain.payment;

import java.math.BigDecimal;

public class PaymentItemDomain {

    private String loanId;
    private String paymentId;
    private BigDecimal paymentValue;
    private String paymentStatus;
    private BigDecimal originalAmount;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(BigDecimal paymentValue) {
        this.paymentValue = paymentValue;
    }
}
