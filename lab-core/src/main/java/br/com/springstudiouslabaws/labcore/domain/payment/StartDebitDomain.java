package br.com.springstudiouslabaws.labcore.domain.payment;

import java.util.List;

public class StartDebitDomain {

    private final String clientName;

    private final String clientEmail;

    private final List<StartDebitItemDomain> payments;

    public StartDebitDomain(String clientName, String clientEmail, List<StartDebitItemDomain> payments) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.payments = payments;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public List<StartDebitItemDomain> getPayments() {
        return payments;
    }


}
