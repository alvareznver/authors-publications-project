#!/bin/bash

# Script para iniciar el proyecto en Linux/Mac
# Requiere: Docker y Docker Compose instalados

set -e

echo ""
echo "============================================"
echo "Editorial Microservices Platform - Linux/Mac"
echo "============================================"
echo ""

# Verificar si Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "ERROR: Docker no está instalado"
    echo "Por favor instala Docker desde: https://www.docker.com/get-docker"
    exit 1
fi

# Verificar si docker-compose está disponible
if ! command -v docker-compose &> /dev/null; then
    echo "ERROR: docker-compose no está disponible"
    exit 1
fi

echo "[1/4] Construyendo imágenes Docker..."
docker-compose build

echo ""
echo "[2/4] Iniciando servicios..."
docker-compose up -d

echo ""
echo "[3/4] Esperando a que los servicios sean saludables (30-60 segundos)..."
sleep 30

echo ""
echo "[4/4] Verificando estado..."
docker-compose ps

echo ""
echo "============================================"
echo "¡Servicios iniciados correctamente!"
echo "============================================"
echo ""
echo "Acceder a:"
echo "  - Frontend: http://localhost:3000"
echo "  - Authors API: http://localhost:8001"
echo "  - Publications API: http://localhost:8002"
echo ""
echo "Ver logs:"
echo "  docker-compose logs -f"
echo ""
echo "Detener servicios:"
echo "  docker-compose down"
echo ""
