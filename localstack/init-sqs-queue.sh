#!/bin/bash

# Carrega variáveis do .env
#export "$(grep -v '^#' .env | xargs)"

# Verificar endpoints
echo "AWS_SQS_ENDPOINT=$AWS_SQS_ENDPOINT"
echo "AWS_REGION=$AWS_REGION"

echo "Criando filas SQS no LocalStack..."

# Cria a fila para pagamento parcial
aws --endpoint-url="$AWS_SQS_ENDPOINT" sqs create-queue --queue-name "$SQS_QUEUE_PARTIAL" --region "$AWS_REGION"
echo "SQS queue '$SQS_QUEUE_PARTIAL' criada com sucesso no LocalStack."

# Cria a fila para pagamento total
aws --endpoint-url="$AWS_SQS_ENDPOINT" sqs create-queue --queue-name "$SQS_QUEUE_TOTAL" --region "$AWS_REGION"
echo "SQS queue '$SQS_QUEUE_TOTAL' criada com sucesso no LocalStack."

# Cria a fila para pagamento em excesso
aws --endpoint-url="$AWS_SQS_ENDPOINT" sqs create-queue --queue-name "$SQS_QUEUE_OVERMEASURE" --region "$AWS_REGION"
echo "SQS queue '$SQS_QUEUE_OVERMEASURE' criada com sucesso no LocalStack."

echo "Filas criadas!"
