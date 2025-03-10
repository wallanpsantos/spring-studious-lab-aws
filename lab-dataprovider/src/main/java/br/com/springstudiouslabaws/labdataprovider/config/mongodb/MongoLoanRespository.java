package br.com.springstudiouslabaws.labdataprovider.config.mongodb;

import br.com.springstudiouslabaws.labdataprovider.entities.LoanEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoLoanRespository extends MongoRepository<LoanEntity, String> {

    /**
     * Verifica se existe algum empréstimo para o cliente com o ID fornecido.
     * Utiliza uma query personalizada para MongoDB para verificar apenas o campo client.clientId.
     *
     * @param clientId ID do cliente a ser verificado
     * @return true se existir pelo menos um empréstimo para o cliente, false caso contrário
     */
    @Query(value = "{ 'client.clientId': ?0 }", exists = true)
    boolean existsByClientClientId(String clientId);

    /**
     * Busca um empréstimo pelo seu identificador específico.
     *
     * @param loanId ID do empréstimo a ser buscado
     * @return true se existir um empréstimo com o ID fornecido, false caso contrário
     */
    boolean existsByLoanId(String loanId);
}