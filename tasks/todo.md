# Lock otimista

Decisão registrada em `tasks/plan.md`. Não há implementação na fase atual.

Gatilho da fase 2: a baixa do saldo e o status saem no mesmo `save`. Saldo zerado grava `quitado`. Saldo restante grava `pendente`.

## Task 1: Operação de atualização na porta

**Description:** A porta `LoanRepository` ganha uma operação que altera um empréstimo existente. A implementação carrega o `LoanEntity`, aplica a mudança e grava essa mesma entidade. O `id` do Mongo e a versão não entram em `LoanDomain` nem no JSON. `processLoan` continua só inserindo.

**Acceptance criteria:**
- [ ] A atualização usa o documento carregado por `loanId`, com o mesmo `@Id`
- [ ] Criar empréstimo não passa a exigir versão
- [ ] Nenhum DTO de request ou response ganha campo de versão

**Verification:**
- [ ] Tests pass: `.\mvnw.cmd test` no módulo que receber o teste do repositório
- [ ] Build succeeds: `.\mvnw.cmd test`
- [ ] Manual check: um empréstimo criado pelo POST atual ainda é lido pelo GET com o mesmo corpo

**Dependencies:** Gatilho da fase 2. Sem o caso de uso de atualização, não começa.

**Files likely touched:**
- `lab-core/src/main/java/br/com/springstudiouslabaws/repositories/LoanRepository.java`
- `lab-dataprovider/src/main/java/br/com/springstudiouslabaws/repositories/LoanRepositoryImpl.java`

**Estimated scope:** Small: 1-2 files

## Task 2: `@Version` no documento de empréstimo

**Description:** `LoanEntity` ganha `Long version` com `@Version`. O `save` da atualização devolve `OptimisticLockingFailureException` quando a versão gravada não é a que foi lida. Insert novo permanece com versão nula. Documentos locais antigos, sem o campo, entram na regra escrita no plano: tratar ausência como zero ou recriar a coleção.

**Acceptance criteria:**
- [ ] O campo é `Long`, anotado com `@Version`
- [ ] Duas atualizações feitas a partir da mesma leitura: a primeira grava, a segunda falha com `OptimisticLockingFailureException`
- [ ] O insert de `processLoan` continua gravando o empréstimo

**Verification:**
- [ ] Tests pass: `.\mvnw.cmd -pl lab-dataprovider test` com Mongo de teste, ou o teste de repositório que o módulo já usar
- [ ] Build succeeds: `.\mvnw.cmd test`
- [ ] Manual check: o documento novo em `loan` nasce com `version` 0

**Dependencies:** Task 1

**Files likely touched:**
- `lab-dataprovider/src/main/java/br/com/springstudiouslabaws/entities/LoanEntity.java`
- `lab-dataprovider/src/main/java/br/com/springstudiouslabaws/repositories/LoanRepositoryImpl.java`
- teste novo ao lado do repositório, no módulo que tiver suporte a Mongo

**Estimated scope:** Medium: 3-5 files

## Task 3: Conflito para o caso de uso

**Description:** A falha de versão vira uma exceção de domínio no `lab-core`. O entrypoint só muda se essa falha passar a ser resposta HTTP. Nesse caso o envelope segue `error.code`, `error.message`, `error.details`, o status é 409, e `docs/archify/arquitetura-emprestimos-pagamentos/contrato-io.md` é atualizado no mesmo trabalho.

**Acceptance criteria:**
- [ ] O caso de uso não depende de `OptimisticLockingFailureException`
- [ ] Se a HTTP não mudar, `contrato-io.md` permanece intocado
- [ ] Se a HTTP mudar, o 409 entra no contrato e no teste de contrato já existente

**Verification:**
- [ ] Tests pass: `.\mvnw.cmd test`
- [ ] Build succeeds: `.\mvnw.cmd test`
- [ ] Manual check: POST `/v1/emprestimos` e POST `/v1/pagamentos` mantêm os status atuais

**Dependencies:** Task 2

**Files likely touched:**
- `lab-core/src/main/java/br/com/springstudiouslabaws/exceptions/` (exceção nova)
- `lab-dataprovider/src/main/java/br/com/springstudiouslabaws/repositories/LoanRepositoryImpl.java`
- `lab-entrypoint/src/main/java/br/com/springstudiouslabaws/exceptions/` somente se houver resposta HTTP
- `docs/archify/arquitetura-emprestimos-pagamentos/contrato-io.md` somente se a HTTP mudar

**Estimated scope:** Small: 1-2 files, ou Medium se o contrato HTTP entrar

## Task 4: Teste das duas escritas

**Description:** Um teste cobre o interleaving que o `@Version` existe para pegar. As duas operações leem a mesma versão. Uma grava. A outra falha. Uma terceira, depois de ler de novo, grava.

**Acceptance criteria:**
- [ ] A segunda escrita com a versão antiga falha
- [ ] A escrita depois de uma leitura nova passa
- [ ] Criar empréstimo, criar pagamento e a idempotência não entram nesse teste

**Verification:**
- [ ] Tests pass: `.\mvnw.cmd test`
- [ ] Build succeeds: `.\mvnw.cmd test`
- [ ] Manual check: nenhuma alteração em `IdempotencyStoreImpl`, `CreatePaymentUseCase` ou no JSON público

**Dependencies:** Task 2. Task 3 se o teste atravessar o entrypoint.

**Files likely touched:**
- teste da operação de atualização no módulo onde a Task 2 colocou o Mongo

**Estimated scope:** Small: 1-2 files

## Checkpoint: Depois das Tasks 1-4

- [ ] `.\mvnw.cmd test` verde
- [ ] POST `/v1/emprestimos`, GET `/v1/emprestimos/{loanId}` e POST `/v1/pagamentos` com o mesmo contrato
- [ ] Revisão humana antes de seguir para outra etapa
