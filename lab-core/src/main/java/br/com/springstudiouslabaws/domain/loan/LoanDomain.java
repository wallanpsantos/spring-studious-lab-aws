package br.com.springstudiouslabaws.domain.loan;

import br.com.springstudiouslabaws.exceptions.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

public class LoanDomain {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final int MINIMUM_LOAN_TERM_MONTHS = 4;
    private static final BigDecimal MINIMUM_LOAN_AMOUNT = BigDecimal.valueOf(100.00);
    private static final BigDecimal MAXIMUM_LOAN_AMOUNT = BigDecimal.valueOf(1000000.00);
    public static final int MINIMUM_CHARACTER_NAME = 3;

    String clientId;
    String name;
    String email;
    String description;
    BigDecimal amountRequested;
    LocalDate dateFinalPayment;
    String loanId;
    LocalDateTime lastUpdate;
    String status;

    // Validação completa do domínio
    public void validate() {
        validateClientId();
        validateName();
        validateEmail();
        validateDescription();
        validateAmountRequested();
        validateDateFinalPayment();
    }

    private void validateClientId() {
        if (clientId == null || clientId.isBlank()) {
            throw new BusinessException("Identificador do cliente não pode ser vazio");
        }
    }

    private void validateName() {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Nome do cliente não pode ser vazio");
        }
        if (name.length() < MINIMUM_CHARACTER_NAME) {
            throw new BusinessException(String.format("Nome do cliente deve ter no mínimo %s caracteres", MINIMUM_CHARACTER_NAME));
        }
    }

    private void validateEmail() {
        if (email == null || email.isBlank()) {
            throw new BusinessException("E-mail não pode ser vazio");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException("E-mail inválido");
        }
    }

    private void validateDescription() {
        if (description == null || description.isBlank()) {
            throw new BusinessException("Descrição do empréstimo não pode ser vazia");
        }
    }

    private void validateAmountRequested() {
        if (amountRequested == null) {
            throw new BusinessException("Valor do empréstimo não pode ser nulo");
        }
        if (amountRequested.compareTo(MINIMUM_LOAN_AMOUNT) < 0) {
            throw new BusinessException(String.format("Valor do empréstimo deve ser maior que R$ %,.2f", MINIMUM_LOAN_AMOUNT));
        }
        if (amountRequested.compareTo(MAXIMUM_LOAN_AMOUNT) > 0) {
            throw new BusinessException(String.format("Valor do empréstimo não pode exceder R$ %,.2f", MAXIMUM_LOAN_AMOUNT));
        }
    }

    private void validateDateFinalPayment() {
        if (dateFinalPayment == null) {
            throw new BusinessException("Data final de pagamento não pode ser nula");
        }
        if (dateFinalPayment.isBefore(LocalDate.now())) {
            throw new BusinessException("Data final de pagamento não pode ser no passado");
        }

        long loanTermMonths = ChronoUnit.MONTHS.between(LocalDate.now(), dateFinalPayment);
        if (loanTermMonths < MINIMUM_LOAN_TERM_MONTHS) {
            throw new BusinessException(String.format("Prazo mínimo do empréstimo é de %d meses", MINIMUM_LOAN_TERM_MONTHS));
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmountRequested() {
        return amountRequested;
    }

    public void setAmountRequested(BigDecimal amountRequested) {
        this.amountRequested = amountRequested;
    }

    public LocalDate getDateFinalPayment() {
        return dateFinalPayment;
    }

    public void setDateFinalPayment(LocalDate dateFinalPayment) {
        this.dateFinalPayment = dateFinalPayment;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoanDomain{" +
                "amountRequested=" + amountRequested +
                ", clientId='" + clientId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", dateFinalPayment=" + dateFinalPayment +
                ", loanId='" + loanId + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", status='" + status + '\'' +
                '}';
    }
}
