package br.com.springstudiouslabaws.labdataprovider.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa um empréstimo no banco de dados MongoDB.
 * Implementada como um record para garantir imutabilidade, consistência de dados
 * e facilitar a manutenção do código.
 * <p>
 * A abordagem com records traz diversos benefícios:
 * <ul>
 *     <li>Imutabilidade intrínseca: evita estados inconsistentes e torna o código mais seguro</li>
 *     <li>Redução significativa de código boilerplate: sem necessidade de getters/setters tradicionais</li>
 *     <li>Implementações automáticas de equals(), hashCode() e toString(): evitando bugs comuns</li>
 *     <li>API clara e concisa: a declaração dos campos revela explicitamente a estrutura da entidade</li>
 *     <li>Thread-safety: objetos imutáveis são naturalmente seguros em ambientes concorrentes</li>
 * </ul>
 * <p>
 * Para trabalhar com a natureza imutável dos records, toda operação de atualização
 * deve retornar uma nova instância com os valores modificados, usando os métodos with*.
 */
@Document(collection = "loan")
public record LoanEntity(
        @Id
        String id,

        String loanId,
        String description,
        BigDecimal amountRequested,
        BigDecimal outstandingBalance,
        String status,
        LocalDate dateFinalPayment,
        PaymentsEntity payments,
        ClientEntity client,
        LocalDateTime lastUpdate
) {
}