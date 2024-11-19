package br.com.springstudiouslabaws.labcore.enums;

import java.util.stream.Stream;

public enum PaymentItemStatusEnum {
    PARTIAL("parcial"),
    TOTAL("total"),
    OVERMEASURE("excedente"),
    NEW("novo");

    private final String value;

    PaymentItemStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentItemStatusEnum verifyStatusUpdatePayment(final String value) {
        return Stream.of(PARTIAL, TOTAL, OVERMEASURE)
                .filter(e -> e.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status de pagamento inválido: " + value));
    }

}

