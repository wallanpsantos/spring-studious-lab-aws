package br.com.springstudiouslabaws.labdataprovider.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentsEntity(
        String paymentId,
        String comment,
        BigDecimal paymentValue,
        String paymentOption,
        BigDecimal amountRequested,
        LocalDateTime timestampOperation
) {

}