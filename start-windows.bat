@echo off
REM Script para iniciar el proyecto en Windows
REM Requiere: Docker Desktop instalado y ejecutándose

echo.
echo ============================================
echo Editorial Microservices Platform - Windows
echo ============================================
echo.

REM Verificar si Docker está instalado
docker --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker no está instalado o no está en el PATH
    echo Por favor instala Docker Desktop desde: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

REM Verificar si docker-compose está disponible
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: docker-compose no está disponible
    pause
    exit /b 1
)

echo [1/4] Construyendo imágenes Docker...
docker-compose build
if errorlevel 1 (
    echo ERROR: Error durante la construcción de las imágenes
    pause
    exit /b 1
)

echo.
echo [2/4] Iniciando servicios...
docker-compose up -d
if errorlevel 1 (
    echo ERROR: Error iniciando los servicios
    pause
    exit /b 1
)

echo.
echo [3/4] Esperando a que los servicios sean saludables (30-60 segundos)...
timeout /t 30 /nobreak

echo.
echo [4/4] Verificando estado...
docker-compose ps

echo.
echo ============================================
echo ¡Servicios iniciados correctamente!
echo ============================================
echo.
echo Acceder a:
echo   - Frontend: http://localhost:3000
echo   - Authors API: http://localhost:8001
echo   - Publications API: http://localhost:8002
echo.
echo Ver logs:
echo   docker-compose logs -f
echo.
echo Detener servicios:
echo   docker-compose down
echo.
pause
