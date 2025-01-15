#!/bin/bash

# Utilizar o comando no terminal: chmod +x init-sns-topic.sh

# Carrega variáveis do .env
#export "$(grep -v '^#' .env | xargs)"

# Verificar endpoints
echo "AWS_SNS_ENDPOINT=$AWS_SNS_ENDPOINT"
echo "AWS_REGION=$AWS_REGION"

# Nome do tópico do .env
TOPIC_NAME="$SNS_TOPIC_NAME"

# Criação do tópico
awslocal sns create-topic --name "$TOPIC_NAME"

# Mensagem de sucesso
echo "SNS topic '$TOPIC_NAME' criado com ✅ sucesso no LocalStack."
