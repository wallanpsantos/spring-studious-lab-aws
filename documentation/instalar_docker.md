# 📜 Tutorial Completo: Instalando e Configurando Docker e Docker Compose no WSL (Windows Subsystem for Linux)

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

## Conclusão

Após seguir estes passos, o Docker e o Docker Compose devem estar instalados e funcionando no seu ambiente WSL. Este
guia cobre as configurações iniciais e resolução de erros comuns para garantir que você possa utilizar o Docker no WSL
sem problemas.


---
<br/>

# 📜 Tutorial Docker e Docker Compose com exemplos de Comandos Essenciais

Este guia cobre os principais comandos para utilizar Docker e Docker Compose, que agora é integrado ao Docker CLI nas
versões mais recentes.

## Seção 1: Comandos Básicos do Docker

Esses comandos ajudam a gerenciar contêineres, imagens, redes e volumes no Docker.

### 1. `docker pull`

Baixa uma imagem do Docker Hub ou de um repositório remoto.

   ```bash
   docker pull nginx
   ```

Esse comando baixa a última versão da imagem do Nginx.

### 2. `docker run`

Cria e inicia um contêiner a partir de uma imagem.

   ```bash
   docker run -d -p 8080:80 --name meu_nginx nginx
   ```

Este comando cria e executa um contêiner Nginx em modo **detached** (`-d`), mapeando a porta 80 do contêiner para a
porta 8080 do host, e nomeia o contêiner como `meu_nginx`.

### 3. `docker ps`

Lista todos os contêineres em execução.

   ```bash
   docker ps
   ```

Para listar todos os contêineres, incluindo os que estão parados, use:

   ```bash
   docker ps -a
   ```

### 4. `docker stop`

Para um contêiner em execução.

   ```bash
   docker stop meu_nginx
   ```

Esse comando para o contêiner chamado `meu_nginx`.

### 5. `docker start`

Inicia um contêiner parado.

   ```bash
   docker start meu_nginx
   ```

Inicia o contêiner `meu_nginx` que foi parado.

### 6. `docker rm`

Remove um contêiner parado.

   ```bash
   docker rm meu_nginx
   ```

Para remover todos os contêineres parados de uma vez:

   ```bash
   docker rm $(docker ps -a -q)
   ```

### 7. `docker rmi`

Remove uma imagem do Docker.

   ```bash
   docker rmi nginx
   ```

Para remover todas as imagens não utilizadas:

   ```bash
   docker rmi $(docker images -q)
   ```

### 8. `docker exec`

Executa um comando dentro de um contêiner em execução.

   ```bash
   docker exec -it meu_nginx /bin/bash
   ```

Este comando abre um terminal interativo (`-it`) dentro do contêiner `meu_nginx`.

### 9. `docker logs`

Exibe os logs de um contêiner.

   ```bash
   docker logs meu_nginx
   ```

Para acompanhar os logs em tempo real, use o parâmetro `-f`:

   ```bash
   docker logs -f meu_nginx
   ```

### 10. `docker inspect`

Exibe informações detalhadas sobre um contêiner ou imagem.

   ```bash
   docker inspect meu_nginx
   ```

### 11. `docker images`

Lista todas as imagens locais.

   ```bash
   docker images
   ```

### 12. `docker build`

Cria uma imagem a partir de um `Dockerfile`.

   ```bash
   docker build -t minha_imagem:latest .
   ```

Este comando cria uma imagem chamada `minha_imagem` usando o `Dockerfile` no diretório atual.

### 13. `docker network ls`

Lista todas as redes Docker.

   ```bash
   docker network ls
   ```

### 14. `docker network create`

Cria uma nova rede Docker.

   ```bash
   docker network create minha_rede
   ```

### 15. `docker network connect`

Conecta um contêiner a uma rede.

   ```bash
   docker network connect minha_rede meu_nginx
   ```

---

## Seção 2: Comandos do Docker Compose (Nova Versão: `docker compose`)

Esses comandos são específicos para Docker Compose, usado para orquestrar múltiplos contêineres.

### 1. `docker compose up`

Inicia todos os serviços definidos no `docker-compose.yml`.

   ```bash
   docker compose up
   ```

Para executar em segundo plano (modo detached), use `-d`:

   ```bash
   docker compose up -d
   ```

### 2. `docker compose down`

Para e remove contêineres, redes e volumes criados pelo `docker compose up`.

   ```bash
   docker compose down
   ```

Para remover volumes também:

   ```bash
   docker compose down -v
   ```

### 3. `docker compose ps`

Lista todos os contêineres gerenciados pelo Docker Compose.

   ```bash
   docker compose ps
   ```

### 4. `docker compose build`

Constrói (ou reconstrói) imagens dos serviços definidos no `docker-compose.yml`.

   ```bash
   docker compose build
   ```

Para reconstruir uma imagem específica:

   ```bash
   docker compose build nome_do_servico
   ```

### 5. `docker compose stop`

Para todos os contêineres gerenciados pelo Docker Compose.

   ```bash
   docker compose stop
   ```

### 6. `docker compose start`

Inicia todos os contêineres parados gerenciados pelo Docker Compose.

   ```bash
   docker compose start
   ```

### 7. `docker compose restart`

Reinicia os contêineres.

   ```bash
   docker compose restart
   ```

### 8. `docker compose logs`

Exibe os logs de todos os serviços.

   ```bash
   docker compose logs
   ```

Para ver logs de um serviço específico:

   ```bash
   docker compose logs nome_do_servico
   ```

Para acompanhar os logs em tempo real:

   ```bash
   docker compose logs -f
   ```

### 9. `docker compose exec`

Executa um comando em um contêiner gerenciado pelo Docker Compose.

   ```bash
   docker compose exec nome_do_servico comando
   ```

Exemplo: Acessar o terminal de um contêiner específico:

   ```bash
   docker compose exec nome_do_servico /bin/bash
   ```

### 10. `docker compose config`

Valida e exibe o `docker-compose.yml`.

   ```bash
   docker compose config
   ```

Esse comando ajuda a verificar se o arquivo `docker-compose.yml` está correto antes de rodar o `up`.

## Conclusão

Esses comandos cobrem as operações mais comuns e essenciais para o gerenciamento de contêineres e orquestração de
serviços com Docker e Docker Compose.
<br/>
Eles permitem desde a criação de contêineres simples até a configuração de ambientes complexos com múltiplos serviços.

---
<br/>

# 📜 Tutorial: Configurando Docker para Rodar sem `sudo` e Executar `docker compose` sem Manter o Daemon Sempre Ativo

Este tutorial guia você para configurar o Docker e o Docker Compose para rodarem sem a necessidade de `sudo`.
<br/>
<b>(Windows com WSL) ➡️</b> Criar um script que verifica se o Docker Daemon (`dockerd`) está ativo,
iniciando-o apenas quando
necessário e desligando-o automaticamente após um período de inatividade.

## Passo 1: Permitir que Docker Rode sem `sudo`

Primeiro, adicione seu usuário ao grupo `docker`. Isso permitirá que você execute comandos Docker e Docker Compose sem a
necessidade de `sudo`.

1. Execute o comando abaixo para adicionar seu usuário ao grupo `docker`, caso não tenha feito:

   ```bash
   sudo usermod -aG docker $USER
   ```

2. **Reinicie o terminal ou faça logout e login novamente** para aplicar as permissões de grupo.

3. Verifique se a permissão foi aplicada executando:

   ```bash
   groups
   ```

   Certifique-se de que `docker` está listado como um dos grupos do seu usuário.

---

## **Passo 2: Criar um Script para Verificar e Iniciar o `dockerd` Automaticamente**

Para evitar a necessidade de executar `sudo dockerd` sempre que precisar do Docker, vamos criar um script que inicia o
`dockerd` automaticamente apenas quando necessário. Esse script também desligará o `dockerd` após **8 horas** de
inatividade.

1. **Crie o Diretório para o Script**

   Crie um diretório chamado `bin` em sua pasta inicial (ou use outro local de sua preferência):

   ```bash
   mkdir -p ~/bin
   ```

2. **Crie o diretório para os logs:**

   ```bash
   mkdir -p ~/logs/dockerd
   ```

3. **Crie o Script `docker-check.sh`**

   Crie um arquivo chamado `docker-check.sh` no diretório `~/bin`:

   ```bash
   nano ~/bin/docker-check.sh
   ```

4. **Escreva o Código do Script**

   Insira o seguinte conteúdo no `docker-check.sh`:

   ```bash
   #!/bin/bash
   
   # Esse script faz o seguinte:
   # 1. Verifica se o dockerd está ativo. Se não estiver, ele o inicia em segundo plano.
   # 2. Usa sudo para iniciar o daemon com permissões adequadas.
   # 3. Monitora o dockerd por algumas horas e o desliga se não houver contêineres em execução.
   
   LOG_DIR="$HOME/logs/dockerd"
   LOG_FILE="${LOG_DIR}/dockerd_$(date +%Y%m%d_%H%M%S).log"
   
   # Certifique-se de que o diretório de logs existe
   mkdir -p "$LOG_DIR"
   
   # Verifica se o Docker daemon está em execução
   if ! pgrep -x "dockerd" > /dev/null; then
       echo "Docker daemon não está em execução. Tentando iniciar com sudo..."
       sudo nohup dockerd > "$LOG_FILE" 2>&1 &
       sleep 5  # Aguarda alguns segundos para garantir que o dockerd tenha iniciado
   
       # Verifica novamente se o dockerd foi iniciado
       if ! pgrep -x "dockerd" > /dev/null; then
           echo "Erro: Não foi possível iniciar o Docker daemon. Verifique os logs em $LOG_FILE."
           exit 1
       fi
   fi
   
   echo "Docker daemon está em execução."
   echo "Logs do Docker estão sendo salvos em $LOG_FILE"
   echo "Use 'tail -f $LOG_FILE' para monitorar os logs."
   
   # Verificar se ainda há contêineres em execução
   END_TIME=$((SECONDS + 2 * 3600))  # Tempo final: 2 horas em segundos
   
   while [ $SECONDS -lt $END_TIME ]; do
       if [ -n "$(docker ps -q)" ]; then
           # Se houver contêineres em execução, aguarde mais 10 minuto antes de verificar novamente
           sleep 600
       else
           echo "Nenhum contêiner em execução. Continuando a monitorar."
           echo "Caso feche o terminal WSL, precisará rodar o script novamente."
           sleep 600
       fi
   done
   
   # Verificar novamente e desliga o dockerd se necessário
   if [ -z "$(docker ps -q)" ]; then
       echo "Nenhum contêiner em execução. Parando o Docker daemon."
       sudo pkill dockerd
   else
       echo "Contêineres ainda estão em execução. O Docker daemon continuará ativo."
   fi
   ```

5. **Torne o Script Executável**

   Para garantir que o script funcione, torne-o executável:

   ```bash
   chmod +x ~/bin/docker-check.sh
   ```

---

#### **Passo 3: Criar um Alias para Facilitar o Uso**

Para tornar o uso do script mais fácil, vamos criar um alias para iniciar o `dockerd`.

1. Abra seu arquivo `.bashrc` ou `.zshrc` (dependendo do shell que você usa):

   ```bash
   nano ~/.bashrc
   ```
   ou

   ```bash
   nano ~/.zshrc
   ```

2. Adicione o seguinte alias ao final do arquivo:

   ```bash
   alias init-docker="~/bin/docker-check.sh"
   ```

3. Salve e feche o arquivo, depois atualize o shell:

   ```bash
   source ~/.bashrc
   ```
   ou

   ```bash
   source ~/.zshrc
   ```

#### IMPORANTE ➡️ *Sempre que quiser iniciar o Docker Daemon e monitorar sua execução, rode o alias criado anteriormente:*.

   ```bash
   init-docker
   ```

ou

   ```bash
   ~/bin/docker-check.sh
   ```

### Conclusão

Com essas configurações, você poderá:

- Iniciar o `dockerd` automaticamente apenas quando necessário.
- Desligar o `dockerd` após um tempo se não houver contêineres em execução.
- Evitar que o `dockerd` fique ativo desnecessariamente, economizando recursos.

---
