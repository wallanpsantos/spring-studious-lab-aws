package br.com.springstudiouslabaws.enums;

public enum LoanStatusEnum {

    PENDING("pendente"),
    PAID("quitado"),
    OVERDUE("vencido"),
    OVERMEASURE("excedente");

    private final String value;

    LoanStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LoanStatusEnum fromString(String value) {
        for (LoanStatusEnum status : LoanStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) return status;
        }
        throw new IllegalArgumentException("Status do empréstimo inválido: " + value);
    }

}
