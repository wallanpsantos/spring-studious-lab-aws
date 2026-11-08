# Plano: lock otimista

## Overview

Dá para usar lock otimista no Mongo com `@Version` do Spring Data MongoDB. Nesta base ele não entra em nenhuma etapa que existe hoje. Criar empréstimo, criar pagamento e reservar a chave de idempotência gravam um documento novo. A versão só é conferida numa atualização do mesmo documento. A etapa em que ela passa a valer é a primeira que carregar um empréstimo já gravado e gravar de volta saldo, status ou pagamentos.

Nenhuma tarefa deste plano altera código agora. A fase futura só começa quando esse caso de uso de atualização existir.

## O que o código faz hoje

- `LoanUseCase.processLoan` valida, gera um UUID e chama `save`. Cada chamada é um documento novo. `findByLoanId` só lê.
- `CreatePaymentUseCase.process` só chama `save`. `PaymentDomain` não tem identificador. `PaymentsEntity` não tem `@Id` nem `@Version`.
- `IdempotencyStoreImpl.begin` faz `insert` com `_id` `scope:key`. A colisão cai em `DuplicateKeyException`. O comentário da porta já descreve isso: a unicidade do identificador é o que impede as duas tentativas.
- `complete` faz `findById` e depois `save` de um record novo com o mesmo id. `abandon` lê e, se o estado é `IN_PROGRESS`, chama `deleteById`. No `IdempotentCommand`, só quem ganhou o `begin` chega em `run`. O outro recebe 409. `abandon` e `complete` acontecem no mesmo fio, um ou outro.

`LoanEntity` tem `outstandingBalance`, `status`, `payments` e `lastUpdate`, mas nenhum caso de uso altera esses campos depois do insert.

## Architecture Decisions

- Lock otimista aqui é `@Version` num `Long` da entidade Mongo. A documentação do Spring Data MongoDB diz que o campo entra na conferência do `save`, o valor inicial é zero (um se o tipo for primitivo) e sobe a cada atualização. O `save` do `MongoOperations` lança `OptimisticLockingFailureException` quando o documento mudou no meio. Fonte: [Object Mapping](https://docs.spring.io/spring-data/mongodb/reference/mongodb/mapping/mapping.html) e [MongoOperations.save](https://docs.spring.io/spring-data/mongodb/reference/api/java/org/springframework/data/mongodb/core/MongoOperations.html).
- O tipo é `Long`, não `long`. O primitivo já nasce em 1 e não distingue “ainda não gravado”.
- A versão fica no adaptador. `LoanRepository` ganha uma operação de atualização que lê a entidade, aplica a mudança e grava a mesma instância com a versão carregada. `LoanDomain`, os DTOs e o JSON público não ganham campo `version`. O contrato em `docs/archify/arquitetura-emprestimos-pagamentos/contrato-io.md` só muda se o conflito passar a ser uma resposta HTTP nova.
- `processLoan` continua insert. Versão nula nesse caminho é documento novo. Uma atualização que monte `LoanEntity` de novo, com `id` nulo e versão nula, vira outro insert. O `@Id` lido do Mongo tem de voltar no `save`.
- Idempotência não usa `@Version`. O `begin` já é uma reserva atômica por insert. Colocar `@Version` em `IdempotencyEntity` e continuar com `new IdempotencyEntity(...)` sem a versão lida faria o `complete` tentar um insert do `_id` que já existe.
- `deleteById` não olha versão. Isso não cria uma tarefa: no desenho atual não há dois escritores da mesma chave.
- Pagamento em coleção própria fica de fora. O efeito que disputa o empréstimo mora no documento `loan`. A versão desse documento cobre saldo, status e pagamentos embutidos.
- A baixa do saldo e o status saem no mesmo `save` do documento `loan`. Saldo zerado grava `quitado`. Saldo restante grava `pendente`. Duas gravações abririam a janela em que o processo cai no meio.

## Task List

### Fase 1: Agora

Nenhuma mudança de código.

- [x] Conferir os pontos de escrita: empréstimo, pagamento e idempotência
- [x] Decidir que `@Version` não entra nesses fluxos

### Fase 2: Quando existir atualização de empréstimo

Bloqueada. O gatilho é um caso de uso que carregue um empréstimo persistido e grave saldo, status ou pagamentos de volta. Sem esse caso de uso, as tarefas abaixo não começam.

- [ ] Task 1: Operação de atualização na porta, com id e versão só no adaptador
- [ ] Task 2: `@Version Long` em `LoanEntity` e `save` condicional
- [ ] Task 3: Conflito vira erro de domínio e, se sair na HTTP, 409 no contrato
- [ ] Task 4: Teste de duas atualizações com a mesma versão lida

### Checkpoint: Fase 2

- [ ] `.\mvnw.cmd test` passa sem alterar o comportamento de criar empréstimo, criar pagamento e idempotência
- [ ] POST e GET atuais continuam com o mesmo JSON
- [ ] Revisão humana antes de qualquer código

## Risks and Mitigations

| Risco | Impacto | Mitigation |
|------|--------|------------|
| `@Version` no insert de empréstimo ou pagamento, onde não há segunda escrita | Baixo | Não adicionar o campo nesses fluxos |
| `complete` passar a inserir de novo o mesmo `_id` se a versão lida não for copiada | Alto | Não versionar `IdempotencyEntity` neste plano |
| Atualização futura montar `LoanEntity` sem o `@Id` e a versão lidos | Alto | A operação de update do repositório reutiliza a entidade carregada |
| Documento `loan` já gravado no Mongo local, sem campo `version` | Médio | Em laboratório, a primeira atualização desses documentos precisa tratar versão ausente como zero ou recriar a coleção local. Não há migração de produção |
| 409 novo no envelope público sem atualizar `contrato-io.md` | Médio | Task 3 só mexe no contrato se a resposta HTTP mudar |

## Open Questions

- Qual é a primeira atualização do empréstimo: baixa de saldo, troca de status, ou as duas na mesma escrita? A versão é a mesma. A regra de negócio dessa escrita é outro trabalho.
    R: A primeira atualização é a baixa de saldo. O status vai na mesma escrita: saldo zerado fica quitado e saldo restante fica pendente.
- O conflito fica só dentro do servidor, ou o cliente passa a enviar a versão? Este plano deixa a versão no adaptador. Expor a versão no JSON seria outro contrato.
    R: A versão é só para o lock otimista. O cliente não precisa saber dela ficando no servidor. O contrato HTTP só muda se o conflito virar 409.
