# spring-studious-lab-aws

Laboratório de empréstimos e pagamentos. Java 25, Spring Boot 4.1.2, Maven 3.8+, MongoDB 8.0 e LocalStack 4.14.0.
Documentação, nomes públicos da API e comentários novos ficam em português.

## Comandos

- Subir Mongo e LocalStack: `docker compose up -d`
- Testes: `.\mvnw.cmd test` (PowerShell) ou `./mvnw test`
- Cobertura: `.\mvnw.cmd verify`. JaCoCo exige 80% de linhas por pacote.
- App: módulo `lab-entrypoint`, classe `br.com.springstudiouslabaws.LabEntrypointApplication`, porta 8082. Não há
  profile obrigatório. Os padrões de `application.yml` apontam para localhost.

## Módulos

- `lab-core`: domínio, casos de uso e portas (`repositories`, `IdempotencyStore`, interfaces SQS). Não depende dos
  outros módulos.
- `lab-dataprovider`: implementações Mongo e SQS. Depende só de `lab-core`.
- `lab-entrypoint`: sobe o Spring Boot. Controllers, facades, DTOs, mappers e `IdempotentCommand`. Depende de `lab-core`
  e de `lab-dataprovider`.
- `lab-config`: `Main` de exemplo. Não entra no boot. Não ligue o serviço a esse módulo.

A chamada HTTP entra por controller e facade, passa pelo caso de uso e sai pelas portas. O adaptador implementa a porta.
Não faça o core importar entrypoint nem dataprovider.

## Contrato HTTP

- `POST /v1/emprestimos`, `GET /v1/emprestimos/{loanId}`, `POST /v1/pagamentos`.
- Nomes públicos do JSON são os `@JsonProperty` dos records (`nome`, `pagamentos`, `identificador_emprestimo`). Em
  record, a anotação vale no accessor. `ContractFieldNames` lê esse accessor.
- `Idempotency-Key` é opcional nos POST. Sem chave, a ação segue. Chave inválida é 400, em andamento é 409, outro corpo
  é 422. O GET não usa a chave.
- Erros usam um envelope: `error.code`, `error.message`, `error.details`.
- Se o contrato público mudar, atualize `docs/archify/arquitetura-emprestimos-pagamentos/contrato-io.md` no mesmo
  trabalho.

## O que o código faz hoje

- Criar empréstimo valida `LoanDomain`, gera UUID e grava `pendente` quando o status vem vazio.
- Criar pagamento só salva. O caso de uso não chama `validate` nem envia SQS.
- `PaymentSQSSendServiceImpl` sabe enviar. Ninguém chama. `LoanSQSSendServiceImpl.sendLoan` está vazio. Os listeners só
  escrevem log.
- O POM pai declara starters de S3 e SNS. Não há classe usando esses serviços. Não desenhe nem documente esse uso como
  se existisse.

## Skills

- As skills padrão deste repositório são [addyosmani/agent-skills](https://github.com/addyosmani/agent-skills)
  e [tt-a1i/archify](https://github.com/tt-a1i/archify).
- Use agent-skills no fluxo de engenharia: especificação, implementação, testes, revisão e git.
- Use o Archify para diagramas e páginas explicativas. A entrega segue a seção abaixo.

## Commits

- A mensagem é em português e descreve só o diff deste commit.
- Assunto: `<emoji> <tipo>(<escopo opcional>): <imperativo>`, com cerca de 72 caracteres e sem ponto final.
- Corpo com até 10 tópicos curtos. Um assunto por commit. Não invente escopo nem ticket.
- Emojis: ✨ feat, ⚡ perf, ♻️ refactor, ⏪ revert, 📝 docs.
- Sem emoji fixo: fix, test, build, style, chore, ci, raw, cleanup, remove, security.
- Exemplo: `✨ feat(auth): adiciona autenticação por refresh token`
- Entregue a mensagem em texto puro, pronta para colar no IntelliJ, sem bloco de código.

## Archify e git

- Ao criar um diagrama com Archify, o HTML de visualização fica em `docs/archify/`.
- Não faça commit do JSON que o Archify gera, nem de recibos e candidatos. A pasta `.archify/` está no `.gitignore`.
- Entra no commit o `.html` ou o `.md`. O Markdown entra quando o Archify gera o contrato, como
  `docs/archify/arquitetura-emprestimos-pagamentos/contrato-io.md`.
- Se o contrato público de I/O mudar, atualize esse `contrato-io.md` no mesmo trabalho.
- Não copie senha, URI com credencial nem chave padrão para documentação nova. Os defaults locais já estão no
  `application.yml` e no Compose.
- Não edite `target/`.
