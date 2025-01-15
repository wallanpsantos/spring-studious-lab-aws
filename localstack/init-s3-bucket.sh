#!/bin/bash

# Utilizar o comando no terminal: chmod +x init-s3-bucket.sh

# Carrega variáveis do .env
#export "$(grep -v '^#' .env | xargs)"

# Verificar endpoints
echo "AWS_SNS_ENDPOINT=$AWS_SNS_ENDPOINT"
echo "AWS_SQS_ENDPOINT=$AWS_SQS_ENDPOINT"
echo "AWS_REGION=$AWS_REGION"

# Nome do bucket do .env
BUCKET_NAME="$S3_BUCKET_NAME"

# Criação do bucket
awslocal s3api create-bucket --bucket "$BUCKET_NAME"

# Mensagem de sucesso
echo "Bucket '$BUCKET_NAME' criado com ✅ sucesso no LocalStack."
