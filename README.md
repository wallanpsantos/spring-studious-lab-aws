
<h2 align="center">Em Desenvolvimento! 🐾🚧</h2>

<div style="display: flex; justify-content: space-between; align-items: flex-start; margin-top: 20px;">
    <!-- Left Column: Metrics -->
    <div style="flex: 1; max-width: 30%; min-width: 20%;">
        <img align="right" width="25%" alt="Photo Profile" src="https://github.com/user-attachments/assets/59a4ff14-2ffa-4d57-b5b4-67bfe687cd94">
    </div>


  <div align="left" >
  <h3>Sobre o projeto:</h3>
  <p>
      Este projeto está em desenvolvimento. Novas funcionalidades e melhorias estão sendo implementadas para aprimorar a estrutura e facilitar a reutilização em futuros projetos que utilizem serviços da AWS (LocalStack).
      A estrutura de infraestrutura é construída do zero para integrar e utilizar vários serviços AWS, permitindo flexibilidade e escalabilidade para diferentes cenários de aplicação.
  </p>
  <p>
      Em breve, será adicionada uma documentação detalhada ao README, cobrindo a arquitetura, configurações e instruções de uso para facilitar a compreensão e a implementação do projeto.
  </p>
  <p>
      Se você tem interesse em acompanhar o progresso, fique à vontade para explorar as atualizações e contribuir com sugestões ou melhorias!
  </p>
  </div>
</div>

<hr/>

# Guia de Uso do Projeto Spring Studious Lab AWS

## Requisitos Mínimos
- Java 21 (LTS) ou Java 17 (LTS mínimo)
- Maven 3.8.0 ou superior
- Docker e Docker Compose
- IDE recomendada: IntelliJ IDEA ou Eclipse

## Estrutura do Projeto (Clean Architecture)

O projeto segue a Clean Architecture com a seguinte estrutura:

```
spring-studious-lab-aws/
├── lab-core/               # Camada de domínio e casos de uso
├── lab-dataprovider/      # Camada de infraestrutura e repositórios
└── lab-entrypoint/        # Camada de controllers e APIs REST
```

## Exemplo de Uso

### 1. Iniciando o Ambiente

```bash
# Clone o repositório
git clone https://github.com/wallanpsantos/spring-studious-lab-aws.git

# Entre no diretório
cd spring-studious-lab-aws

# Inicie os containers
docker-compose up -d
```

### 2. Exemplos de Requisições

#### Criação de Empréstimo
```http
POST http://localhost:8082/v1/emprestimos
Content-Type: application/json

{
    "identificador_cliente": "123",
    "nome": "João Silva",
    "email": "joao@email.com",
    "descricao_emprestimo": "Empréstimo pessoal",
    "valor_solicitado": 5000.00,
    "data_final_vencimento": "2025-12-29"
}
```

#### Processamento de Pagamento
```http
PUT http://localhost:8082/v1/pagamentos
Content-Type: application/json

{
    "client_id": "123",
    "payment_items": [
        {
            "payment_id": "PAY001",
            "payment_value": 1000.00,
            "payment_status": "parcial"
        }
    ]
}
```

## Integrações AWS (LocalStack)

O projeto utiliza LocalStack para simular serviços AWS localmente:

- **S3**: Armazenamento de documentos
- **SNS**: Sistema de notificações
- **SQS**: Filas de mensagens

### Configuração dos Serviços AWS

Os serviços são configurados automaticamente através dos scripts em `/localstack`:
- `init-s3-bucket.sh`: Cria buckets S3
- `init-sns-topic.sh`: Configura tópicos SNS
- `init-sqs-queue.sh`: Configura filas SQS
- `subscribe-sqs-to-sns.sh`: Configura assinaturas

## Recomendações de Melhorias

1. **Validação de Dados**
    - Implementar validação customizada usando `@Validated`
    - Adicionar validação de negócios no domínio

2. **Segurança**
    - Implementar autenticação OAuth2/JWT
    - Adicionar rate limiting
    - Implementar CORS adequadamente

3. **Observabilidade**
    - Adicionar Micrometer para métricas
    - Implementar tracing distribuído
    - Melhorar logs estruturados

4. **Testes**
    - Aumentar cobertura de testes
    - Implementar testes de integração
    - Adicionar testes de contrato

5. **Documentação**
    - Melhorar documentação OpenAPI
    - Adicionar exemplos de uso
    - Documentar regras de negócio

## Boas Práticas

1. **Clean Code**
    - Manter métodos pequenos e focados
    - Usar nomes significativos
    - Seguir princípio da responsabilidade única

2. **Clean Architecture**
    - Manter independência de frameworks
    - Inversão de dependência
    - Separação clara de camadas

3. **Tratamento de Erros**
    - Usar exceções específicas do domínio
    - Implementar circuit breaker
    - Logging adequado

## Logs e Monitoramento

```yaml
logging:
  level:
    root: INFO
    br.com.springstudiouslabaws: DEBUG
    org.springframework: INFO
```

## Ambiente de Desenvolvimento

Para desenvolvimento local:

1. Configure o application-local.yml
2. Use o profile "local"
3. Execute o LocalStack
4. Inicie a aplicação

## Troubleshooting

1. **Erro de Conexão com MongoDB**
    - Verifique se o container está rodando
    - Confirme as credenciais no .env

2. **Erro nos Serviços AWS**
    - Verifique o LocalStack
    - Confirme as configurações no .env