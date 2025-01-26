#!/bin/bash

###############################################################################
# Script para criar buckets S3 no LocalStack
#
# Este script é responsável por criar os buckets S3 necessários para o sistema.
# Os buckets S3 são usados para armazenamento de arquivos e documentos,
# como contratos, comprovantes e outros documentos relacionados.
#
# Variáveis de ambiente necessárias:
# - AWS_REGION: Região AWS (ex: us-east-1)
# - S3_BUCKET_NAME: Nome do bucket principal
#
# Buckets atuais:
# - bucket-studios-lab-aws: Bucket principal para armazenamento de documentos
#
# Buckets futuros (exemplos):
# - bucket-loan-documents: Para documentos de empréstimos
# - bucket-audit-files: Para arquivos de auditoria
###############################################################################

###############################################################################
# Função para criar um bucket S3 com retry automático
#
# Argumentos:
#   $1 - Nome do bucket a ser criado
#
# Retorno:
#   0 - Sucesso
#   1 - Falha após todas as tentativas
#
# Exemplo de uso:
#   create_bucket "my-bucket-name"
###############################################################################
create_bucket() {
    local bucket_name=$1
    local max_retries=3          # Número máximo de tentativas
    local retry_count=0
    local retry_delay=2          # Tempo de espera entre tentativas (segundos)

    echo "Criando bucket S3: $bucket_name"

    while [ $retry_count -lt $max_retries ]; do
        if aws --endpoint-url="http://localhost:4566" s3api create-bucket \
            --bucket "$bucket_name" \
            --region "$AWS_REGION"; then
            echo "✅ Bucket '$bucket_name' criado com sucesso"
            return 0
        fi

        retry_count=$((retry_count + 1))
        if [ $retry_count -lt $max_retries ]; then
            echo "Tentativa $retry_count de $max_retries falhou. Tentando novamente..."
            sleep $retry_delay
        fi
    done

    echo "❌ Erro ao criar bucket $bucket_name após $max_retries tentativas"
    return 1
}

# Exibe as configurações atuais para debug
echo "Configurações AWS:"
echo "AWS_REGION=$AWS_REGION"
echo "S3_BUCKET_NAME=$S3_BUCKET_NAME"

###############################################################################
# Criação dos buckets S3
# Para adicionar novos buckets:
# 1. Adicione a variável de ambiente no .env
# 2. Adicione a criação do bucket aqui seguindo o padrão
###############################################################################

echo "======== Criando buckets S3 ========"
create_bucket "$S3_BUCKET_NAME"
echo "==================================="

# Exemplo de como adicionar novos buckets:
# echo "======== Criando buckets para Empréstimos ========"
# create_bucket "$S3_BUCKET_LOAN_DOCUMENTS"
# echo "==============================================="

###############################################################################
# Área para configurações adicionais dos buckets
# Aqui você pode adicionar configurações como:
# - Políticas de acesso
# - Lifecycle rules
# - Versionamento
# - Criptografia
#
# Exemplo:
# aws --endpoint-url="http://localhost:4566" s3api put-bucket-versioning \
#     --bucket "$S3_BUCKET_NAME" \
#     --versioning-configuration Status=Enabled
###############################################################################

echo "Processo de criação de buckets S3 finalizado!"