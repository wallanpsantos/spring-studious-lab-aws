#!/bin/sh
set -e

echo "Initializing Scripts..."

# Criar diretório se não existir
mkdir -p /etc/localstack/init/ready.d

# Copiar scripts e dar permissões
cp ./localstack/*.sh /etc/localstack/init/ready.d
chmod +x /etc/localstack/init/ready.d/*.sh

# Iniciar LocalStack
exec docker-entrypoint.sh "$@"