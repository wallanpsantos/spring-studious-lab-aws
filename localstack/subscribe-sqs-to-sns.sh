#!/bin/bash

###############################################################################
# Script para realizar a subscrição de filas SQS em tópicos SNS no LocalStack
#
# Este script estabelece a conexão entre tópicos SNS e filas SQS, permitindo
# que mensagens publicadas nos tópicos sejam automaticamente enviadas para
# as filas correspondentes.
#
# Variáveis de ambiente necessárias:
# - AWS_SNS_ENDPOINT: Endpoint do serviço SNS
# - AWS_SQS_ENDPOINT: Endpoint do serviço SQS
# - AWS_REGION: Região AWS (ex: us-east-1)
# - SNS_TOPIC_NAME: Nome do tópico SNS
# - SQS_QUEUE_NAME: Nome da fila SQS
#
# Subscrições atuais:
# - payments-topic -> payment-partial-queue
# - payments-topic -> payment-total-queue
# - payments-topic -> payment-overmeasure-queue
###############################################################################

###############################################################################
# Função para subscrever uma fila SQS em um tópico SNS
#
# Argumentos:
#   $1 - Nome do tópico SNS
#   $2 - Nome da fila SQS
#
# Retorno:
#   0 - Sucesso
#   1 - Falha após todas as tentativas
###############################################################################
subscribe_queue_to_topic() {
    local topic_name=$1
    local queue_name=$2
    local max_retries=3          # Número máximo de tentativas
    local retry_count=0
    local retry_delay=2          # Tempo de espera entre tentativas (segundos)

    echo "Subscrevendo fila '$queue_name' ao tópico '$topic_name'"

    # Monta os ARNs para tópico e fila
    local topic_arn="arn:aws:sns:$AWS_REGION:000000000000:$topic_name"
    local queue_arn="arn:aws:sqs:$AWS_REGION:000000000000:$queue_name"

    while [ $retry_count -lt $max_retries ]; do
        if aws --endpoint-url="$AWS_SNS_ENDPOINT" sns subscribe \
            --topic-arn "$topic_arn" \
            --protocol sqs \
            --notification-endpoint "$queue_arn" \
            --region "$AWS_REGION"; then
            echo "✅ Fila '$queue_name' subscrita com sucesso ao tópico '$topic_name'"
            return 0
        fi

        retry_count=$((retry_count + 1))
        if [ $retry_count -lt $max_retries ]; then
            echo "Tentativa $retry_count de $max_retries falhou. Tentando novamente..."
            sleep $retry_delay
        fi
    done

    echo "❌ Erro ao subscrever fila '$queue_name' ao tópico '$topic_name'"
    return 1
}

# Exibe as configurações atuais para debug
echo "Configurações AWS:"
echo "AWS_SNS_ENDPOINT=$AWS_SNS_ENDPOINT"
echo "AWS_SQS_ENDPOINT=$AWS_SQS_ENDPOINT"
echo "AWS_REGION=$AWS_REGION"

###############################################################################
# Criação das subscrições de Pagamento
# Cada fila de pagamento é subscrita no tópico de pagamentos
###############################################################################
echo "======== Criando subscrições de Pagamento ========"
subscribe_queue_to_topic "$SNS_TOPIC_NAME" "$SQS_QUEUE_PARTIAL"
subscribe_queue_to_topic "$SNS_TOPIC_NAME" "$SQS_QUEUE_TOTAL"
subscribe_queue_to_topic "$SNS_TOPIC_NAME" "$SQS_QUEUE_OVERMEASURE"
echo "==============================================="

###############################################################################
# Área para futuras subscrições
#
# Exemplo para subscrições de Empréstimo:
# echo "======== Criando subscrições de Empréstimo ========"
# subscribe_queue_to_topic "$SNS_TOPIC_LOAN" "$SQS_QUEUE_LOAN_REQUEST"
# subscribe_queue_to_topic "$SNS_TOPIC_LOAN" "$SQS_QUEUE_LOAN_APPROVAL"
# echo "================================================="
###############################################################################

echo "Processo de subscrição de filas SQS aos tópicos SNS finalizado!"