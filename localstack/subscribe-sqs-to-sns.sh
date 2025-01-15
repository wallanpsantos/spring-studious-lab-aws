#!/bin/bash

# Utilizar o comando no terminal: chmod +x subscribe-sqs-to-sns.sh

# Carrega variáveis do .env
#export $(grep -v '^#' .env | xargs)

# Verificar endpoints e região
echo "AWS_SNS_ENDPOINT=$AWS_SNS_ENDPOINT"
echo "AWS_SQS_ENDPOINT=$AWS_SQS_ENDPOINT"
echo "AWS_REGION=$AWS_REGION"

# Realiza a assinatura da fila ao tópico
awslocal sns subscribe \
  --topic-arn "arn:aws:sns:$AWS_REGION:000000000000:$SNS_TOPIC_NAME" \
  --protocol sqs \
  --notification-endpoint "arn:aws:sqs:$AWS_REGION:000000000000:$SQS_QUEUE_NAME"

# Mensagem de sucesso
echo "Subscribed SQS queue '$SQS_QUEUE_NAME' to SNS topic '$SNS_TOPIC_NAME' com ✅ sucesso no LocalStack."
