# Contrato de I/O público

Atributos, obrigatoriedade, exceções e regras das três operações publicadas: `POST /v1/emprestimos`,
`GET /v1/emprestimos/{loanId}` e `POST /v1/pagamentos`. Os nomes são os do JSON (`@JsonProperty` quando existe; senão, o
nome do componente do record). Obrigatório aqui significa anotação `NotNull`, `NotBlank` ou `NotEmpty`. A tabela também
está nos cartões de `docs/archify/arquitetura-emprestimos-pagamentos/arquitetura-emprestimos-pagamentos.html`.

O cabeçalho `Idempotency-Key` é opcional nos dois POST. O GET não usa idempotência.

## LoanRequest

Pedido de `POST /v1/emprestimos`.

| Atributo JSON         | Obrigatório |
|-----------------------|-------------|
| identificador_cliente | sim         |
| nome                  | sim         |
| email                 | sim         |
| descricao_emprestimo  | sim         |
| valor_solicitado      | sim         |
| data_final_vencimento | sim         |

Além da presença: `nome` tem no mínimo 3 caracteres; `email` precisa ser e-mail; `valor_solicitado` fica entre 100.00 e
1000000.00 inclusive. `data_final_vencimento` aceita `dd/MM/yyyy`, `dd-MM-yyyy` ou `yyyy-MM-dd`.

## LoanResponse

Resposta de `POST /v1/emprestimos` (201) e de `GET /v1/emprestimos/{loanId}` (200). Nenhum campo é obrigatório por bean
validation. Jackson omite nulos.

| Atributo JSON            | Obrigatório |
|--------------------------|-------------|
| descricao_emprestimo     | não         |
| valor_solicitado         | não         |
| data_final_vencimento    | não         |
| identificador_emprestimo | não         |
| identificador_cliente    | não         |
| nome                     | não         |
| email                    | não         |
| status                   | não         |
| atualizado_em            | não         |

Na criação, o caso de uso gera `identificador_emprestimo` (UUID) e preenche `atualizado_em`. Se `status` chega nulo,
grava `pendente`.

## PaymentRequest

Pedido de `POST /v1/pagamentos`.

| Atributo JSON         | Obrigatório |
|-----------------------|-------------|
| identificador_cliente | sim         |
| pagamentos            | sim         |

`pagamentos` precisa ter ao menos um item. Cada item é validado.

## PaymentItemRequest

Item de `pagamentos`.

| Atributo JSON            | Obrigatório |
|--------------------------|-------------|
| identificador_emprestimo | sim         |
| valor_pagamento          | sim         |
| comentario               | não         |

`valor_pagamento` tem mínimo 0.01.

## PaymentResponse

Corpo declarado para o 201 de `POST /v1/pagamentos`. Nenhum campo é obrigatório por bean validation.

| Atributo JSON         | Obrigatório |
|-----------------------|-------------|
| identificador_cliente | não         |
| pagamentos_efetuados  | não         |

O mapper de volta do Mongo não copia campos do documento para o domínio. O 201 pode sair sem esses nomes, porque nulos
são omitidos.

## PaymentItemResponse

Item de `pagamentos_efetuados`.

| Atributo JSON            | Obrigatório |
|--------------------------|-------------|
| identificador_emprestimo | não         |
| identificador_pagamento  | não         |
| opcao_pagamento          | não         |

O mapper para o DTO preenche empréstimo e pagamento e deixa `opcao_pagamento` nulo. Com omissão de nulos, o nome não
aparece no JSON.

## ApiErrorResponse

| Atributo JSON | Obrigatório |
|---------------|-------------|
| error         | não         |

## ErrorBody

Objeto `error`.

| Atributo JSON | Obrigatório |
|---------------|-------------|
| code          | não         |
| message       | não         |
| details       | não         |

## FieldDetail

Item de `error.details`. Em `BUSINESS_RULE`, `field` vai nulo.

| Atributo JSON | Obrigatório |
|---------------|-------------|
| field         | não         |
| message       | não         |

## Excecoes

Códigos que `ApiExceptionHandler` coloca em `error.code`.

| Código                       | HTTP              | Quando                                               |
|------------------------------|-------------------|------------------------------------------------------|
| VALIDATION_ERROR             | 422               | Bean validation do corpo (`@Valid`)                  |
| MALFORMED_REQUEST            | 400               | JSON ilegível, ou outro 400 genérico do Spring       |
| INVALID_IDEMPOTENCY_KEY      | 400               | `Idempotency-Key` fora do padrão                     |
| NOT_FOUND                    | 404               | Empréstimo ausente, ou 404 genérico                  |
| IDEMPOTENCY_IN_PROGRESS      | 409               | Mesma chave ainda em processamento                   |
| BUSINESS_RULE                | 422               | `BusinessException` do domínio                       |
| IDEMPOTENCY_PAYLOAD_MISMATCH | 422               | Mesma chave com outro corpo                          |
| METHOD_NOT_ALLOWED           | 405               | Método HTTP não mapeado                              |
| UNSUPPORTED_MEDIA_TYPE       | 415               | Content-Type não suportado                           |
| REQUEST_ERROR                | o status recebido | Demais falhas tratadas por `handleExceptionInternal` |
| INTERNAL_ERROR               | 500               | Exceção não prevista                                 |

Sucesso: os POST respondem 201 e o GET responde 200.

## Regras

- Chave de idempotência nula ou em branco não consulta o banco: a ação executa de novo. Repetir o POST cria outro
  empréstimo ou outro pagamento.
- Chave preenchida precisa casar `^[A-Za-z0-9._:-]{1,200}$`. Fora disso, 400 `INVALID_IDEMPOTENCY_KEY`.
- A chave é guardada por escopo: `emprestimos` na criação de empréstimo e `pagamentos` no pagamento. O id no Mongo é
  `escopo:chave`, coleção `idempotency_keys`.
- Mesma chave, mesmo hash do corpo e estado `SUCCEEDED`: devolve a primeira resposta, sem executar de novo.
- Mesma chave, mesmo hash e estado ainda não concluído: 409 `IDEMPOTENCY_IN_PROGRESS`.
- Mesma chave e hash diferente: 422 `IDEMPOTENCY_PAYLOAD_MISMATCH`.
- Se a ação lança `RuntimeException`, a chave `IN_PROGRESS` é apagada. A retentativa pode começar de novo.
- O GET não passa pela idempotência.
- Validação de bean no POST vem antes da idempotência. Corpo inválido responde 422 `VALIDATION_ERROR` e não grava.
- `LoanDomain.validate` rejeita com 422 `BUSINESS_RULE` identificador, nome, e-mail ou descrição vazios; nome com menos
  de 3 caracteres; e-mail fora do padrão do domínio; valor nulo, abaixo de 100.00 ou acima de 1000000.00; data final
  nula, no passado ou com prazo menor que 4 meses.
- Se o status do empréstimo é nulo, a gravação usa `pendente`.
- Empréstimo não encontrado no GET: 404 `NOT_FOUND`, mensagem "Empréstimo não encontrado".
- O pagamento não valida o empréstimo nem publica em fila. `CreatePaymentUseCase` recebe o repositório de empréstimo e o
  envio SQS e não chama nenhum dos dois. `LoanSQSSendServiceImpl.sendLoan` existe e está vazio, e `LoanUseCase` também
  não o chama. Essas filas não fazem parte do caminho das três operações.
