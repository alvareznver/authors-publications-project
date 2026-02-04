# Editorial Microservices Platform

Sistema de gestión de autores y publicaciones basado en microservicios con Spring Boot, PostgreSQL y Docker.

## 📋 Requisitos Previos

- **Docker** 20.10+
- **Docker Compose** 2.0+
- **Java** 17 (para desarrollo local)
- **Maven** 3.9+ (para desarrollo local)
- **Node.js** 16+ (para frontend)

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                     FRONTEND (React/Angular)                 │
│                        Port: 3000                            │
└─────────────────────────────────────────────────────────────┘
           │                              │
           ↓                              ↓
┌──────────────────────┐      ┌──────────────────────┐
│  Authors Service     │      │ Publications Service │
│     Port: 8001       │      │     Port: 8002       │
│  Spring Boot + JPA   │      │  Spring Boot + JPA   │
└──────────────────────┘      └──────────────────────┘
           │                              │
           ↓                              ↓
┌──────────────────────┐      ┌──────────────────────┐
│  PostgreSQL Authors  │      │PostgreSQL Publications│
│  Port: 5432          │      │  Port: 5433          │
└──────────────────────┘      └──────────────────────┘
```

## 🚀 Inicio Rápido

### Opción 1: Con Docker Compose (Recomendado)

```bash
# 1. Clonar o descargar el proyecto
cd authors-publications-project

# 2. Construir e iniciar todos los servicios
docker-compose up --build

# 3. Esperar a que todos los servicios estén saludables (30-60 segundos)
# Verificar logs: docker-compose logs -f

# 4. Acceder a las aplicaciones
# Frontend: http://localhost:3000
# Authors API: http://localhost:8001
# Publications API: http://localhost:8002
```

### Opción 2: Desarrollo Local

#### Paso 1: Iniciar las bases de datos

```bash
docker-compose up db-authors db-publications
```

#### Paso 2: Compilar y ejecutar Authors Service

```bash
cd authors-service
mvn clean install
mvn spring-boot:run
```

#### Paso 3: Compilar y ejecutar Publications Service

```bash
cd publications-service
mvn clean install
# Asegurar que AUTHORS_SERVICE_URL=http://localhost:8001
mvn spring-boot:run
```

#### Paso 4: Frontend

```bash
cd frontend
npm install
npm start
```

## 📚 APIs Disponibles

### Authors Service (Puerto 8001)

#### Crear Autor
```bash
curl -X POST http://localhost:8001/api/v1/authors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan García",
    "email": "juan@example.com",
    "bio": "Escritor y periodista",
    "authorType": "INDIVIDUAL",
    "country": "España",
    "phone": "+34900000000"
  }'
```

#### Obtener Autor
```bash
curl http://localhost:8001/api/v1/authors/1
```

#### Listar Autores
```bash
curl "http://localhost:8001/api/v1/authors?page=0&size=10"
```

#### Buscar Autores
```bash
curl "http://localhost:8001/api/v1/authors/search?keyword=Juan"
```

#### Actualizar Autor
```bash
curl -X PUT http://localhost:8001/api/v1/authors/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Juan García Updated"}'
```

#### Verificar Existencia de Autor
```bash
curl http://localhost:8001/api/v1/authors/1/exists
```

### Publications Service (Puerto 8002)

#### Crear Publicación
```bash
curl -X POST http://localhost:8002/api/v1/publications \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mi Primer Artículo",
    "description": "Descripción breve",
    "content": "Contenido del artículo...",
    "authorId": 1,
    "keywords": "tecnología,innovación",
    "category": "Tecnología",
    "language": "ES"
  }'
```

#### Obtener Publicación
```bash
curl http://localhost:8002/api/v1/publications/1
```

#### Listar Publicaciones
```bash
curl "http://localhost:8002/api/v1/publications?page=0&size=10"
```

#### Publicaciones por Autor
```bash
curl "http://localhost:8002/api/v1/publications/author/1?page=0&size=10"
```

#### Publicaciones por Estado
```bash
curl "http://localhost:8002/api/v1/publications/status/DRAFT?page=0&size=10"
```

#### Cambiar Estado de Publicación
```bash
curl -X PATCH http://localhost:8002/api/v1/publications/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "IN_REVIEW",
    "reviewerNotes": "Por favor revisar ortografía"
  }'
```

#### Estados de Transición Válidos
- `DRAFT` → `IN_REVIEW`, `REJECTED`
- `IN_REVIEW` → `APPROVED`, `REJECTED`, `DRAFT`
- `APPROVED` → `PUBLISHED`, `DRAFT`
- `PUBLISHED` → `ARCHIVED`
- `REJECTED` → `DRAFT`
- `ARCHIVED` → (sin transiciones)

## 🏗️ Estructura de Proyectos

```
authors-publications-project/
├── authors-service/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/editorial/authors/
│       │   ├── domain/
│       │   │   ├── entity/
│       │   │   │   ├── BaseEntity.java (Clase Abstracta)
│       │   │   │   ├── Author.java (Clase Derivada)
│       │   │   │   └── AuthorType.java (Enumeración)
│       │   │   └── repository/
│       │   │       └── AuthorRepository.java (Pattern: Repository)
│       │   ├── application/
│       │   │   ├── service/
│       │   │   │   ├── IAuthorService.java
│       │   │   │   └── impl/
│       │   │   │       ├── AuthorServiceImpl.java
│       │   │   │       └── AuthorValidator.java (Pattern: Strategy)
│       │   │   └── mapper/
│       │   │       └── AuthorMapper.java (MapStruct)
│       │   ├── presentation/
│       │   │   ├── controller/
│       │   │   │   └── AuthorController.java
│       │   │   └── dto/
│       │   │       ├── CreateAuthorDTO.java
│       │   │       └── AuthorResponseDTO.java
│       │   └── infrastructure/
│       │       ├── config/
│       │       └── exception/
│       │           ├── AuthorExceptions.java
│       │           └── GlobalExceptionHandler.java
│       └── resources/
│           └── application.properties
│
├── publications-service/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/editorial/publications/
│       │   ├── domain/
│       │   │   ├── entity/
│       │   │   │   ├── BasePublication.java (Clase Abstracta)
│       │   │   │   ├── Publication.java (Clase Derivada)
│       │   │   │   └── PublicationStatus.java (Enumeración)
│       │   │   └── repository/
│       │   │       └── PublicationRepository.java (Pattern: Repository)
│       │   ├── application/
│       │   │   ├── service/
│       │   │   │   ├── IPublicationService.java
│       │   │   │   └── impl/
│       │   │   │       ├── PublicationServiceImpl.java
│       │   │   │       └── PublicationValidator.java (Pattern: Strategy)
│       │   │   └── mapper/
│       │   │       └── PublicationMapper.java (MapStruct)
│       │   ├── presentation/
│       │   │   ├── controller/
│       │   │   │   └── PublicationController.java
│       │   │   └── dto/
│       │   │       ├── CreatePublicationDTO.java
│       │   │       ├── UpdatePublicationStatusDTO.java
│       │   │       └── PublicationResponseDTO.java
│       │   └── infrastructure/
│       │       ├── config/
│       │       │   └── RestTemplateConfig.java
│       │       ├── client/
│       │       │   └── AuthorServiceClient.java (Pattern: Adapter)
│       │       └── exception/
│       │           ├── PublicationExceptions.java
│       │           └── GlobalExceptionHandler.java
│       └── resources/
│           └── application.properties
│
├── frontend/
│   ├── package.json
│   ├── Dockerfile
│   ├── public/
│   └── src/
│       ├── components/
│       ├── pages/
│       ├── services/
│       └── App.js
│
├── docker-compose.yml
└── README.md
```

## 🎨 Patrones de Diseño Implementados

### 1. **Repository Pattern**
- **Ubicación**: `AuthorRepository.java`, `PublicationRepository.java`
- **Propósito**: Abstrae la lógica de acceso a datos
- **Beneficio**: Desacoplamiento de la persistencia

### 2. **Strategy Pattern**
- **Ubicación**: `AuthorValidator.java`, `PublicationValidator.java`
- **Propósito**: Encapsula diferentes estrategias de validación
- **Beneficio**: Flexibilidad en cambios de reglas de validación

### 3. **Adapter Pattern**
- **Ubicación**: `AuthorServiceClient.java`
- **Propósito**: Adapta la comunicación HTTP con Authors Service
- **Beneficio**: Aislamiento de cambios en APIs externas

### 4. **Mapper Pattern (MapStruct)**
- **Ubicación**: `AuthorMapper.java`, `PublicationMapper.java`
- **Propósito**: Mapeo automático entre entidades y DTOs
- **Beneficio**: Reduce código boilerplate y errores

### 5. **Facade Pattern**
- **Ubicación**: `IAuthorService`, `IPublicationService`
- **Propósito**: Simplifica la interfaz para el cliente
- **Beneficio**: Coordina componentes complejos

## 🔐 Principios SOLID Aplicados

### S - Single Responsibility Principle
- **AuthorValidator**: Solo valida autores
- **PublicationValidator**: Solo valida publicaciones
- **AuthorServiceClient**: Solo comunica con Authors Service

### O - Open/Closed Principle
- **BaseEntity** y **BasePublication**: Extendibles sin modificación
- **PublicationStatus.canTransitionTo()**: Lógica de transición encapsulada

### L - Liskov Substitution Principle
- **Author extends BaseEntity**
- **Publication extends BasePublication**
- Ambas pueden usarse polimórficamente

### I - Interface Segregation Principle
- **IAuthorService** con métodos específicos
- **IPublicationService** independiente
- No hay métodos innecesarios

### D - Dependency Inversion Principle
- DTOs para entrada/salida
- Inyección de dependencias con Spring
- Interfaces para servicios

## 🧪 Pruebas Manuales

### Flujo Completo: Crear Autor → Crear Publicación → Cambiar Estado

```bash
# 1. Crear autor
AUTHOR_ID=$(curl -s -X POST http://localhost:8001/api/v1/authors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Author",
    "email": "test@example.com",
    "authorType": "INDIVIDUAL"
  }' | jq '.id')

echo "Autor creado con ID: $AUTHOR_ID"

# 2. Crear publicación
PUB_ID=$(curl -s -X POST http://localhost:8002/api/v1/publications \
  -H "Content-Type: application/json" \
  -d "{
    \"title\": \"Test Publication\",
    \"content\": \"This is test content\",
    \"authorId\": $AUTHOR_ID
  }" | jq '.id')

echo "Publicación creada con ID: $PUB_ID"

# 3. Cambiar estado a IN_REVIEW
curl -X PATCH http://localhost:8002/api/v1/publications/$PUB_ID/status \
  -H "Content-Type: application/json" \
  -d '{"status": "IN_REVIEW"}'

# 4. Cambiar estado a APPROVED
curl -X PATCH http://localhost:8002/api/v1/publications/$PUB_ID/status \
  -H "Content-Type: application/json" \
  -d '{"status": "APPROVED"}'

# 5. Cambiar estado a PUBLISHED
curl -X PATCH http://localhost:8002/api/v1/publications/$PUB_ID/status \
  -H "Content-Type: application/json" \
  -d '{"status": "PUBLISHED"}'

# 6. Obtener publicación (con datos del autor enriquecidos)
curl http://localhost:8002/api/v1/publications/$PUB_ID
```

## 🐛 Troubleshooting

### Los servicios no inician
```bash
# Ver logs detallados
docker-compose logs -f authors-service
docker-compose logs -f publications-service

# Asegurar que puertos están libres
lsof -i :8001
lsof -i :8002
lsof -i :5432
lsof -i :5433
```

### Base de datos no responde
```bash
# Reiniciar contenedores
docker-compose restart db-authors db-publications

# Esperar healthcheck
docker-compose ps
```

### Publications Service no puede comunicarse con Authors Service
```bash
# Verificar conectividad entre contenedores
docker-compose exec publications-service \
  curl http://authors-service:8001/api/v1/authors/stats/total
```

## 📝 Variables de Entorno

### Authors Service
- `DB_HOST`: Host de base de datos (default: localhost)
- `DB_PORT`: Puerto de base de datos (default: 5432)
- `DB_NAME`: Nombre de base de datos (default: authors_db)
- `DB_USER`: Usuario (default: postgres)
- `DB_PASSWORD`: Contraseña (default: postgres)

### Publications Service
- `DB_HOST`: Host de base de datos (default: localhost)
- `DB_PORT`: Puerto de base de datos (default: 5432)
- `DB_NAME`: Nombre de base de datos (default: publications_db)
- `DB_USER`: Usuario (default: postgres)
- `DB_PASSWORD`: Contraseña (default: postgres)
- `AUTHORS_SERVICE_URL`: URL del servicio de autores

## 🚪 Puertos Utilizados

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| Frontend | 3000 | Aplicación React |
| Authors Service | 8001 | API REST de Autores |
| Publications Service | 8002 | API REST de Publicaciones |
| PostgreSQL Authors | 5432 | Base de datos de Autores |
| PostgreSQL Publications | 5433 | Base de datos de Publicaciones |

## 📊 Estadísticas y Monitoreo

```bash
# Total de autores activos
curl http://localhost:8001/api/v1/authors/stats/total

# Total de publicaciones
curl http://localhost:8002/api/v1/publications/stats/total

# Total de publicaciones por estado
curl http://localhost:8002/api/v1/publications/stats/by-status/PUBLISHED
```

## 🔄 Ciclo de Vida de una Publicación

```
DRAFT
  ├─→ IN_REVIEW
  │    ├─→ APPROVED
  │    │    ├─→ PUBLISHED
  │    │    │    └─→ ARCHIVED
  │    │    └─→ DRAFT (retrabajo)
  │    ├─→ REJECTED
  │    │    └─→ DRAFT (reenvío)
  │    └─→ DRAFT (cambios solicitados)
  └─→ REJECTED
       └─→ DRAFT (reenvío)
```

## 📞 Soporte y Contacto

- **Email**: support@editorial.com
- **Issues**: GitHub Issues
- **Documentation**: Wiki

## 📄 Licencia

MIT License - Incluida en LICENSE.txt

---

**Nota**: Este proyecto está diseñado para ser escalable y mantenible. 
Para cambios en BPMN y procesos más complejos, ver documentación de Camunda en `/bpmn`.
