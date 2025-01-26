#!/bin/bash

###############################################################################
# Script para criar filas SQS no LocalStack
#
# Este script é responsável por criar as filas SQS necessárias para o sistema.
# Ele está organizado por domínios (Pagamento, Empréstimo, etc.) para facilitar
# a manutenção e expansão do sistema.
#
# Variáveis de ambiente necessárias:
# - AWS_SQS_ENDPOINT: Endpoint do serviço SQS (ex: http://localstack:4566)
# - AWS_REGION: Região AWS (ex: us-east-1)
#
# Filas de Pagamento:
# - SQS_QUEUE_PARTIAL: Fila para pagamentos parciais
# - SQS_QUEUE_TOTAL: Fila para pagamentos totais
# - SQS_QUEUE_OVERMEASURE: Fila para pagamentos excedentes
#
# Filas de Empréstimo (futuro):
# - SQS_QUEUE_LOAN_REQUEST: Fila para solicitações de empréstimo
# - SQS_QUEUE_LOAN_APPROVAL: Fila para aprovações de empréstimo
###############################################################################

echo "Criando filas SQS no LocalStack..."

###############################################################################
# Função para criar uma fila SQS com retry automático
#
# Argumentos:
#   $1 - Nome da fila a ser criada
#
# Retorno:
#   0 - Sucesso
#   1 - Falha após todas as tentativas
#
# Exemplo de uso:
#   create_queue "my-queue-name"
###############################################################################
create_queue() {
    local queue_name=$1
    local max_retries=3          # Número máximo de tentativas
    local retry_count=0
    local retry_delay=2          # Tempo de espera entre tentativas (segundos)

    echo "Criando fila: $queue_name"

    while [ $retry_count -lt $max_retries ]; do
        # Tenta criar a fila usando AWS CLI
        if aws --endpoint-url="$AWS_SQS_ENDPOINT" sqs create-queue \
            --queue-name "$queue_name" \
            --region "$AWS_REGION"; then
            echo "✅ Fila '$queue_name' criada com sucesso"
            return 0
        fi

        # Incrementa contador e tenta novamente se não atingiu o máximo
        retry_count=$((retry_count + 1))
        if [ $retry_count -lt $max_retries ]; then
            echo "Tentativa $retry_count de $max_retries falhou. Tentando novamente..."
            sleep $retry_delay
        fi
    done

    echo "❌ Erro ao criar fila $queue_name após $max_retries tentativas"
    return 1
}

# Exibe as configurações atuais para debug
echo "Configurações AWS:"
echo "AWS_SQS_ENDPOINT=$AWS_SQS_ENDPOINT"
echo "AWS_REGION=$AWS_REGION"

###############################################################################
# Criação das filas de Pagamento
# Adicione novas filas relacionadas a pagamento nesta seção
###############################################################################
echo "======== Criando filas de Pagamento ========"
create_queue "$SQS_QUEUE_PARTIAL"      # Fila para pagamentos parciais
create_queue "$SQS_QUEUE_TOTAL"        # Fila para pagamentos totais
create_queue "$SQS_QUEUE_OVERMEASURE"  # Fila para pagamentos excedentes
echo "============================================"

###############################################################################
# Criação das filas de Empréstimo (Futuro)
# Descomente e adicione novas filas relacionadas a empréstimo conforme necessário
###############################################################################
# echo "======== Criando filas de Empréstimo ========"
# create_queue "$SQS_QUEUE_LOAN_REQUEST"   # Fila para solicitações de empréstimo
# create_queue "$SQS_QUEUE_LOAN_APPROVAL"  # Fila para aprovações de empréstimo
# echo "============================================"

###############################################################################
# Área para adicionar novos domínios
# Siga o padrão acima para adicionar novas seções de filas
# Exemplo:
#
# echo "======== Criando filas de [Novo Domínio] ========"
# create_queue "$SQS_QUEUE_NOVO_DOMINIO_ACAO"
# echo "============================================"
###############################################################################

echo "Processo de criação de filas finalizado!"