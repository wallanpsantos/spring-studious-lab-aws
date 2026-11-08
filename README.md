# Spring Studious Lab AWS

Laboratório de empréstimos e pagamentos. A API grava o empréstimo e o pagamento no MongoDB. O ambiente local sobe o banco e o LocalStack com Docker Compose.

## Como rodar

O laboratório pede Java 25, Spring Boot 4.1.2, Maven 3.8 ou superior e Docker Compose v2.

Suba Mongo e LocalStack:

```bash
docker compose up -d
```

A aplicação que sobe é o módulo `lab-entrypoint`, na classe `br.com.springstudiouslabaws.LabEntrypointApplication`, na porta 8082. Não há profile obrigatório do Spring. A configuração padrão aponta para o Mongo e o LocalStack em localhost.

Os testes:

```bash
./mvnw test
.\mvnw.cmd test
```

A cobertura roda o JaCoCo com 80% de linhas por pacote:

```bash
./mvnw verify
.\mvnw.cmd verify
```

## Módulos

O repositório tem quatro módulos.

`lab-core` guarda o domínio, os casos de uso e as portas: repositórios, store de idempotência e interfaces de fila.

`lab-dataprovider` implementa a persistência no MongoDB.

`lab-entrypoint` é a aplicação HTTP: controllers, facades, DTOs e o comando de idempotência.

`lab-config` não é a aplicação que sobe. O `Main` desse módulo fica fora do boot.

A chamada entra pelo controller, passa pela facade e pelo caso de uso, e chega ao banco pela porta definida no core.

## Operações HTTP

Os nomes públicos do JSON estão no contrato. Entre eles: `nome`, `pagamentos` e `identificador_emprestimo`. Campos, obrigatoriedade e códigos de erro estão em `docs/archify/arquitetura-emprestimos-pagamentos/contrato-io.md`.

`POST /v1/emprestimos` cria um empréstimo. `GET /v1/emprestimos/{loanId}` busca pelo identificador gerado na criação. `POST /v1/pagamentos` registra pagamentos.

O cabeçalho `Idempotency-Key` é opcional nos dois POST. Sem a chave, a ação segue. A mesma chave com o mesmo corpo devolve a primeira resposta. Chave inválida responde 400. Chave ainda em processamento responde 409. A mesma chave com outro corpo responde 422. O GET não usa a chave.

O erro usa um envelope com `error.code`, `error.message` e `error.details`.

## O que a criação faz

Criar um empréstimo valida o domínio, atribui um identificador e grava o status `pendente` quando o status vem vazio. Criar um pagamento só salva o pagamento.

## Mongo e LocalStack

O Compose sobe MongoDB 8.0 com a imagem `mongo:8.0` e o LocalStack com a imagem `localstack/localstack:4.14.0`. Os scripts em `localstack/` são montados no `ready.d` do LocalStack. A criação de empréstimo e a de pagamento não gravam arquivo no S3, não publicam no SNS e não enviam mensagem ao SQS.

## Regras para agente

Quando o contrato público de I/O mudar, atualize `docs/archify/arquitetura-emprestimos-pagamentos/contrato-io.md`.

Ao usar o Archify (https://archify.si/guide), não faça commit do JSON que ele gera, nem de recibos ou candidatos. Faça commit do `.html` ou do `.md`. O Markdown do contrato é esse `contrato-io.md`. O HTML de visualização fica em `docs/archify/`. A pasta `.archify/` está no `.gitignore`.
