# Tutorial Completo: Instalando e Configurando Docker e Docker Compose no WSL (Windows Subsystem for Linux)

Este tutorial guia você pela instalação do Docker e Docker Compose no WSL, abordando erros comuns e suas soluções.

## Passo 1: Atualize o Repositório de Pacotes do Debian/Ubuntu no WSL

Primeiro, certifique-se de que seu repositório de pacotes está atualizado:

   ```bash
   sudo apt-get update
   ```

## Passo 2: Instale Pré-requisitos Necessários

Instale alguns pacotes essenciais para a instalação do Docker:

   ```bash
   sudo apt-get install -y \
       apt-transport-https \
       ca-certificates \
       curl \
       gnupg2 \
       software-properties-common
   ```

## Passo 3: Adicione a Chave GPG Oficial do Docker

Adicione a chave GPG para verificar a autenticidade dos pacotes do Docker:

   ```bash
   curl -fsSL https://download.docker.com/linux/debian/gpg | sudo apt-key add -
   ```

## Passo 4: Adicione o Repositório Docker ao APT

Adicione o repositório Docker às fontes de pacotes do sistema:

   ```bash
   sudo add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/debian \
       $(lsb_release -cs) \
       stable"
   ```

**Nota**: Se estiver usando Ubuntu no WSL, substitua `debian` por `ubuntu` no comando acima.

## Passo 5: Instale o Docker

Instale o Docker Engine e suas dependências:

   ```bash
   sudo apt-get install -y docker-ce docker-ce-cli containerd.io
   ```

## Passo 6: Verifique a Instalação do Docker

Para verificar se o Docker foi instalado corretamente, execute o comando:

   ```bash
   sudo docker run hello-world
   ```

Se você vir uma mensagem de sucesso do Docker, significa que a instalação do Docker foi concluída.

## Passo 7: Instale o Docker Compose

Para instalar a versão mais recente do Docker Compose, execute o comando abaixo para obter a versão mais recente da API
do GitHub e instalar:

   ```bash
   LATEST_COMPOSE_VERSION=$(curl -s https://api.github.com/repos/docker/compose/releases/latest | grep -o -E 'v[0-9]+\.[0-9]+\.[0-9]+' | head -n 1)
   sudo curl -L "https://github.com/docker/compose/releases/download/${LATEST_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
   sudo chmod +x /usr/local/bin/docker-compose
   ```

## Passo 8: Verifique a Instalação do Docker Compose

Após a instalação, verifique a versão do Docker Compose com o comando:

   ```bash
   docker-compose --version
   ```

## Passo 9: Configure o Acesso ao Docker sem `sudo`

Para permitir que o Docker seja executado sem `sudo`, adicione seu usuário ao grupo `docker`:

   ```bash
   sudo usermod -aG docker $USER
   ```

Após adicionar o usuário, faça logout e login novamente (ou reinicie o terminal) para aplicar as permissões de grupo.

### **Observação**: Reiniciar o WSL

Se o Docker ainda pedir permissão de `sudo`, pode ser necessário reiniciar o WSL com o comando:

   ```bash
   wsl --shutdown
   ```

Depois, abra o WSL novamente.

## Soluções para Erros Comuns no WSL

### Erro 1: Docker Daemon Não Está em Execução

Se você receber o erro `Cannot connect to the Docker daemon at unix:///var/run/docker.sock`, isso indica que o Docker
Daemon não está em execução.

#### Solução:

Inicie o Docker Daemon manualmente no WSL com:

   ```bash
   sudo dockerd
   ```

Se preferir, execute esse comando em uma aba separada do terminal para deixar o daemon rodando enquanto usa o Docker em
outra aba.

### Erro 2: “System has not been booted with systemd as init system (PID 1)”

Esse erro ocorre porque o WSL não usa o `systemd` como sistema de inicialização.

#### Solução:

Não é possível usar `systemctl` no WSL. Em vez disso, inicie o Docker Daemon diretamente com:

   ```bash
   sudo dockerd
   ```

Ou configure o Docker Desktop no Windows para integrar com o WSL.

### Erro 3: “Permission Denied” ao Acessar `/var/run/docker.sock`

Se você receber um erro de "permission denied" ao tentar usar o Docker:

#### Solução:

1. Verifique se você está no grupo `docker`:

   ```bash
   groups $USER
   ```

   Certifique-se de que `docker` está listado.

2. Se `docker` não estiver listado, adicione seu usuário ao grupo `docker`:

   ```bash
   sudo usermod -aG docker $USER
   ```

   Faça logout e login novamente ou reinicie o WSL com:

   ```bash
   wsl --shutdown
   ```

3. Verifique as permissões do socket Docker:

   ```bash
   ls -la /var/run/docker.sock
   ```

   Certifique-se de que o socket pertence ao grupo `docker`. Se necessário, ajuste as permissões com:

   ```bash
   sudo chown root:docker /var/run/docker.sock
   sudo chmod 660 /var/run/docker.sock
   ```

### Configuração Alternativa: Usar Docker Desktop com WSL

Se preferir, você pode configurar o Docker Desktop para gerenciar o Docker Daemon no WSL:

1. Abra o Docker Desktop no Windows.
2. Vá em **Settings** > **Resources** > **WSL Integration**.
3. Selecione a distribuição WSL que você está usando e habilite a integração.

Isso permite que o Docker Desktop no Windows gerencie o daemon e o `docker.sock`, facilitando o uso do Docker no WSL sem
necessidade de permissões adicionais.

---

## Conclusão

Após seguir estes passos, o Docker e o Docker Compose devem estar instalados e funcionando no seu ambiente WSL. Este
guia cobre as configurações iniciais e resolução de erros comuns para garantir que você possa utilizar o Docker no WSL
sem problemas.
