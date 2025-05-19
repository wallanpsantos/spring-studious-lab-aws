package br.com.springstudiouslabaws.enums;

public enum PaymentItemStatusEnum {
    PARTIAL("parcial"),
    TOTAL("total"),
    OVERMEASURE("excedente");

    private final String value;

    PaymentItemStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentItemStatusEnum fromString(String value) {
        for (PaymentItemStatusEnum status : PaymentItemStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) return status;
        }
        throw new IllegalArgumentException("Status de pagamento inválido: " + value);
    }
}

