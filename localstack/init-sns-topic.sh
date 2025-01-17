#!/bin/bash

###############################################################################
# Script para criar tópicos SNS no LocalStack
#
# Este script é responsável por criar os tópicos SNS necessários para o sistema.
# Os tópicos SNS são usados para publicação e assinatura de mensagens,
# permitindo comunicação assíncrona entre diferentes componentes do sistema.
#
# Variáveis de ambiente necessárias:
# - AWS_SNS_ENDPOINT: Endpoint do serviço SNS (ex: http://localstack:4566)
# - AWS_REGION: Região AWS (ex: us-east-1)
# - SNS_TOPIC_NAME: Nome do tópico a ser criado
#
# Tópicos atuais:
# - payments-topic: Tópico para notificações relacionadas a pagamentos
#
# Tópicos futuros:
# - loan-topic: Tópico para notificações de empréstimos
###############################################################################

###############################################################################
# Função para criar um tópico SNS com retry automático
#
# Argumentos:
#   $1 - Nome do tópico a ser criado
#
# Retorno:
#   0 - Sucesso
#   1 - Falha após todas as tentativas
###############################################################################
create_topic() {
    local topic_name=$1
    local max_retries=3          # Número máximo de tentativas
    local retry_count=0
    local retry_delay=2          # Tempo de espera entre tentativas (segundos)

    echo "Criando tópico SNS: $topic_name"

    while [ $retry_count -lt $max_retries ]; do
        if aws --endpoint-url="$AWS_SNS_ENDPOINT" sns create-topic \
            --name "$topic_name" \
            --region "$AWS_REGION"; then
            echo "✅ Tópico SNS '$topic_name' criado com sucesso"
            return 0
        fi

        retry_count=$((retry_count + 1))
        if [ $retry_count -lt $max_retries ]; then
            echo "Tentativa $retry_count de $max_retries falhou. Tentando novamente..."
            sleep $retry_delay
        fi
    done

    echo "❌ Erro ao criar tópico $topic_name após $max_retries tentativas"
    return 1
}

# Exibe as configurações atuais para debug
echo "Configurações AWS:"
echo "AWS_SNS_ENDPOINT=$AWS_SNS_ENDPOINT"
echo "AWS_REGION=$AWS_REGION"

###############################################################################
# Criação dos tópicos SNS
# Para adicionar novos tópicos:
# 1. Adicione a variável de ambiente no .env
# 2. Adicione a criação do tópico aqui seguindo o padrão
###############################################################################

echo "======== Criando tópicos SNS ========"
create_topic "$SNS_TOPIC_NAME"
echo "===================================="

# Exemplo de como adicionar novos tópicos:
# echo "======== Criando tópicos de Empréstimo ========"
# create_topic "$SNS_TOPIC_LOAN"
# echo "=============================================="

echo "Processo de criação de tópicos SNS finalizado!"