# Como Configurar a Assinatura de Commits com GPG e Autenticação para o GitHub

Este guia abrange desde a geração de uma chave GPG até a configuração de autenticação segura para o GitHub, com passos
detalhados para garantir que seus commits estejam assinados e autenticados corretamente.

---

## ETAPA 1: Gerar uma Chave GPG para Assinatura de Commits

1. **Abra o Terminal** e execute o comando para iniciar a criação da chave GPG:

   ```bash
   gpg --full-generate-key
   ```

2. **Escolha o Tipo de Chave**:
    - Selecione **RSA and RSA** e defina o tamanho como **4096 bits** para melhor segurança.

3. **Defina a Validade da Chave**:
    - Escolha um período como `1y` (um ano) ou `0` para validade indefinida.

4. **Confirme a Criação da Chave**:
    - Digite `y` para confirmar.

5. **Preencha as Informações do Usuário**:
    - Insira o **nome** e o **e-mail** associados à sua conta do GitHub.
    - Para e-mail, você pode utilizar o e-mail gerado pelo GitHub para operações de Git (por exemplo,
      `1233556+username@users.noreply.github.com`). Esse e-mail pode ser encontrado
      em [GitHub Settings > Emails](https://github.com/settings/emails).

6. **Defina uma Senha para a Chave**:
    - Essa senha será solicitada ao assinar commits.

---

## ETAPA 2: Configurar a Chave GPG no GitHub

1. **Obtenha o ID da Chave**:
    - Use o comando abaixo para listar suas chaves e obter o ID (uma sequência de 16 caracteres após `sec`):

      ```bash
      gpg --list-secret-keys --keyid-format=long
      ```

2. **Exportar a Chave Pública**:
    - Substitua `<ID_DA_CHAVE>` pelo ID da chave obtido acima (fica logo após `sec rsa4096/`):

      ```bash
      gpg --armor --export <ID_DA_CHAVE>
      ```

    - Copie o conteúdo da chave pública, incluindo `-----BEGIN PGP PUBLIC KEY BLOCK-----` e
      `-----END PGP PUBLIC KEY BLOCK-----`.

3. **Adicionar a Chave Pública ao GitHub**:
    - No GitHub, vá para **Settings > SSH and GPG keys**.
    - Clique em **New GPG key**, cole a chave pública copiada e clique em **Add GPG key**.

---

## ETAPA 3: Configurar o Git para Assinar Commits Automaticamente

1. **Defina a Chave de Assinatura Padrão**:
    - Substitua `<ID_DA_CHAVE>` pelo ID da chave GPG:

      ```bash
      git config --global user.signingkey <ID_DA_CHAVE>
      ```

2. **Habilitar Assinatura Automática de Commits**:
    - Configure o Git para assinar todos os commits automaticamente:

      ```bash
      git config --global commit.gpgSign true
      ```

3. **Especificar o Programa GPG para o Git (se necessário)**:
    - Configure o Git para usar o GPG padrão:

      ```bash
      git config --global gpg.program gpg
      ```

---

## Configurações Adicionais (Opcional)

1. **Configurar o GPG_TTY para Solicitar Senha**:
    - Adicione ao seu arquivo `~/.bashrc` ou `~/.zshrc`:

      ```bash
      echo 'export GPG_TTY=$(tty)' >> ~/.zshrc
      ```

2. **Configurar Cache de Senha do Agente GPG**:
    - Edite o arquivo `~/.gnupg/gpg-agent.conf`:

      ```bash
      nano ~/.gnupg/gpg-agent.conf
      ```
      
      Cole o código abaixo:

      ```plaintext
      default-cache-ttl 3600
      max-cache-ttl 36000
      ```

    - Explicação:
        - `default-cache-ttl 3600`: Define o tempo padrão para o cache da senha como **3600 segundos** (ou seja, **1
          hora**).
        - `max-cache-ttl 36000`: Define o tempo máximo para o cache da senha como **36000 segundos** (ou seja, **10
          horas**).

    - Reinicie o agente GPG para aplicar as configurações:

      ```bash
      gpgconf --kill gpg-agent
      gpgconf --launch gpg-agent
      ```

3. **Assinando Commits Antigos com o GPG**:

   Caso deseje assinar commits antigos, você pode utilizar o comando `git rebase`.

   > **Atenção!**: Esse comando reescreve o histórico de commits, incluindo datas e autores dos commits.

   O comando a seguir modifica o autor e assina os commits antigos:

   ```bash
   git rebase --exec 'git commit --amend --author="Seu nome <1233556+username@users.noreply.github.com>" --no-edit -S -n' --root
   ```

---

## Problema com SSH ou envio de alteração (PUSH)

1. **Caso esteja pedindo User e Password, impedindo de realizar o PUSH**:

   Isso ocorre porque agora estamos usando o e-mail gerado pelo GitHub para operações de Git, para isso precisa
   atualizar o SSH.

   Gere uma nova chave SSH:

   ```bash
   ssh-keygen -t ed25519 -C "1233556+username@users.noreply.github.com"
   ```

   Copie a chave ssh pública:

   ```bash
   cat ~/.ssh/id_ed25519.pub
   ```

   Defina o valor global para o e-mail privado do Git:

   ```bash
   git config --global user.email "1233556+username@users.noreply.github.com"
   ```

   Para o User pode utilizar o seu username ou Nome:

   ```bash
   git config --global user.name "usernameexemplo"
   ```

   No projeto que está trabalhando se conecte novamente com o repositório remoto:

   ```bash
   git remote set-url origin git@github.com:usernameexemplo/repository.git
   ```

   Agora pode enviar suas alterações normalmente usando o `PUSH`:

   ```bash
   git push
   ```

   Deve aparecer algo como:

   ```bash
   Enumerating objects: 6, done.
   Counting objects: 100% (6/6), done.
   Delta compression using up to 16 threads
   Compressing objects: 100% (4/4), done.
   Writing objects: 100% (4/4), 2.79 KiB | 317.00 KiB/s, done.
   Total 4 (delta 1), reused 0 (delta 0), pack-reused 0
   remote: Resolving deltas: 100% (1/1), completed with 1 local object.
   To github.com:usernameexemplo/repository.git
   ```

---

*Com essas configurações, todos os seus commits estarão assinados e autenticados corretamente no GitHub.* Você estará
usando uma chave GPG para garantir a integridade dos seus commits e autenticação segura com HTTPS ou SSH para interações
com o repositório remoto.
