# 🏗️ ARQUITECTURA Y PATRONES DE DISEÑO

## 📊 Arquitectura General

```
┌─────────────────────────────────────────────────────────────┐
│                   FRONTEND (React)                           │
│                  Puerto: 3000 / Nginx                        │
└──────────────────────┬──────────────────────────────────────┘
                       │
       ┌───────────────┼───────────────┐
       ↓               ↓               ↓
┌─────────────┐  ┌─────────────┐  ┌──────────────┐
│  Authors    │  │Publications │  │   Caché      │
│  Service    │  │   Service   │  │  (Opcional)  │
│ (8001)      │  │   (8002)    │  │              │
└──────┬──────┘  └──────┬──────┘  └──────────────┘
       │                │
       │ HTTP(S)        │ HTTP(S)
       │                │
   ┌───┴──────┐     ┌───┴──────┐
   │          │────→│          │
   │PostgreSQL│     │PostgreSQL│
   │  Authors │     │ Publicat │
   │   DB     │     │   DB     │
   │  (5432)  │     │  (5433)  │
   └──────────┘     └──────────┘
```

## 🎯 Patrones de Diseño Implementados

### 1. **REPOSITORY PATTERN** ✅
**Ubicación**: `AuthorRepository.java`, `PublicationRepository.java`

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByEmail(String email);
    Page<Author> findAllActive(Pageable pageable);
    List<Author> findAllByIds(List<Long> ids);
}
```

**Beneficios**:
- Abstrae la lógica de persistencia
- Facilita cambios en la fuente de datos
- Mejora testabilidad

---

### 2. **STRATEGY PATTERN** ✅
**Ubicación**: `AuthorValidator.java`, `PublicationValidator.java`

```java
@Component
public class AuthorValidator {
    public void validateCreateAuthor(CreateAuthorDTO dto) { }
    public void validateUpdateAuthor(CreateAuthorDTO dto, Long id) { }
}
```

**Beneficios**:
- Encapsula diferentes estrategias de validación
- Separa lógica de validación del servicio
- Fácil de extender con nuevas validaciones

---

### 3. **ADAPTER PATTERN** ✅
**Ubicación**: `AuthorServiceClient.java`

```java
@Component
public class AuthorServiceClient {
    public AuthorInfo getAuthorById(Long authorId) {
        // Adapta llamadas HTTP a estructura interna
    }
    
    public boolean authorExists(Long authorId) {
        // Abstrae detalles de comunicación
    }
}
```

**Beneficios**:
- Aísla cambios en APIs externas
- Facilita manejo de errores centralizado
- Permite mock para testing

---

### 4. **MAPPER PATTERN (MapStruct)** ✅
**Ubicación**: `AuthorMapper.java`, `PublicationMapper.java`

```java
@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorResponseDTO toResponseDTO(Author author);
    Author toEntity(CreateAuthorDTO dto);
    void updateEntityFromDTO(CreateAuthorDTO dto, @MappingTarget Author author);
}
```

**Beneficios**:
- Mapeo automático de objetos
- Reduce código boilerplate
- Evita errores manuales

---

### 5. **FACADE PATTERN** ✅
**Ubicación**: `IAuthorService`, `IPublicationService`

```java
public interface IAuthorService {
    AuthorResponseDTO createAuthor(CreateAuthorDTO dto);
    AuthorResponseDTO getAuthorById(Long id);
    Page<AuthorResponseDTO> getAllAuthors(Pageable pageable);
    // ... más métodos
}
```

**Beneficios**:
- Simplifica interfaz para el cliente
- Coordina múltiples componentes
- Encapsula complejidad

---

### 6. **DTO (Data Transfer Object) PATTERN** ✅
**Ubicación**: `*DTO.java` en presentation/dto/

```java
public class CreateAuthorDTO {
    @NotBlank String name;
    @Email String email;
    @NotNull AuthorType authorType;
}

public class AuthorResponseDTO {
    Long id;
    String name;
    String email;
    LocalDateTime createdAt;
}
```

**Beneficios**:
- Desacopla entidades de presentación
- Validación en entrada/salida
- Control de versiones de APIs

---

## 🔐 Principios SOLID Implementados

### S - Single Responsibility Principle ✅
Cada clase tiene una única responsabilidad:

- **AuthorValidator**: Solo valida autores
- **AuthorServiceClient**: Solo comunica con Authors Service
- **AuthorRepository**: Solo accede a datos
- **AuthorController**: Solo maneja HTTP

### O - Open/Closed Principle ✅
Clases abiertas a extensión, cerradas a modificación:

```java
@MappedSuperclass
public abstract class BaseEntity {
    abstract String getDisplayName();
}

public class Author extends BaseEntity {
    @Override
    public String getDisplayName() { ... }
}
```

### L - Liskov Substitution Principle ✅
Subclases pueden reemplazar a superclases:

```java
public class Author extends BaseEntity { }
public class Publication extends BasePublication { }

// Pueden usarse polimórficamente
BaseEntity entity = new Author();
entity.getDisplayName();
```

### I - Interface Segregation Principle ✅
Interfaces específicas en lugar de generales:

```java
public interface IAuthorService {
    // Solo métodos relacionados a autores
}

public interface IPublicationService {
    // Solo métodos relacionados a publicaciones
}
```

### D - Dependency Inversion Principle ✅
Depender de abstracciones, no de implementaciones:

```java
@Service
public class AuthorServiceImpl implements IAuthorService {
    private final AuthorRepository repository;  // Inyectado
    private final AuthorMapper mapper;         // Inyectado
    
    public AuthorServiceImpl(AuthorRepository repo, AuthorMapper mp) {
        this.repository = repo;
        this.mapper = mp;
    }
}
```

---

## 🛢️ Estructura de Capas

```
┌─────────────────────────────────────────┐
│   PRESENTATION LAYER (Controllers/DTOs)  │
│   - AuthorController                     │
│   - PublicationController                │
│   - CreateAuthorDTO / AuthorResponseDTO  │
└──────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│   APPLICATION LAYER (Services)           │
│   - IAuthorService / AuthorServiceImpl    │
│   - IPublicationService / PublicatImpl    │
│   - Mappers (MapStruct)                  │
│   - Validators (Strategy)                │
└──────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│   DOMAIN LAYER (Entities & Repositories) │
│   - Author / BaseEntity                  │
│   - Publication / BasePublication        │
│   - AuthorRepository                     │
│   - PublicationRepository                │
└──────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│   INFRASTRUCTURE LAYER                   │
│   - Database (JPA/Hibernate)             │
│   - HTTP Client (RestTemplate)           │
│   - Configuration                        │
│   - Exception Handling                   │
└──────────────────────────────────────────┘
```

---

## 🔄 Flujo de una Solicitud

```
1. HTTP Request
        ↓
2. Controller recibe y valida
        ↓
3. Service ejecuta lógica de negocio
        ↓
4. Validator verifica datos
        ↓
5. Repository accede a base de datos
        ↓
6. Mapper transforma entidad a DTO
        ↓
7. Response HTTP con datos
```

Ejemplo:
```
POST /api/v1/publications
  ↓
PublicationController.createPublication()
  ↓
PublicationServiceImpl.createPublication()
  ├→ PublicationValidator.validateCreatePublication()
  ├→ AuthorServiceClient.authorExists(authorId)
  ├→ PublicationRepository.save()
  ↓
PublicationMapper.toResponseDTO()
  ↓
HTTP 201 Created + PublicationResponseDTO
```

---

## 🧪 Clases Abstractas y Derivadas

### Authors Service
```
BaseEntity (Abstract)
    ↓
Author (Concrete)
    - Implementa getDisplayName()
    - Tiene métodos de negocio
```

### Publications Service
```
BasePublication (Abstract)
    ↓
Publication (Concrete)
    - Implementa getSummary()
    - Maneja transiciones de estado
```

---

## 🔌 Comunicación Entre Microservicios

```
Publications Service → Authors Service
        ↓
PublicationServiceImpl.createPublication(authorId)
        ↓
AuthorServiceClient.authorExists(authorId)
        ↓
RestTemplate.getForEntity(AUTHORS_URL + "/api/v1/authors/{id}/exists")
        ↓
Authors Service Response: { id: 1, exists: true }
```

**Características**:
- Comunicación síncrona vía HTTP REST
- Timeout de 5 segundos
- Manejo de errores robusto
- Puede enriquecerse con datos del autor

---

## 📝 Validaciones en Capas

### Capa de Presentación (DTOs)
```java
@NotBlank(message = "Author name is required")
private String name;

@Email(message = "Email should be valid")
private String email;
```

### Capa de Aplicación (Validador)
```java
public void validateCreateAuthor(CreateAuthorDTO dto) {
    if (authorRepository.findByEmail(dto.getEmail()).isPresent()) {
        throw new EmailAlreadyExistsException(...);
    }
}
```

### Capa de Dominio (Entidad)
```java
public void updateStatus(PublicationStatus newStatus) {
    if (!this.status.canTransitionTo(newStatus)) {
        throw new IllegalStateException(...);
    }
}
```

---

## 🚀 Características Adicionales

✅ **Manejo de Excepciones Global**
- `GlobalExceptionHandler` con `@RestControllerAdvice`

✅ **Logging Detallado**
- SLF4J con Logback
- Logs a nivel DEBUG para desarrollo

✅ **Paginación**
- Spring Data Pagination
- Sort por campos configurables

✅ **Transaccionalidad**
- `@Transactional` en servicios
- Rollback automático en errores

✅ **CORS Habilitado**
- `@CrossOrigin(origins = "*")`

✅ **Health Checks**
- Endpoints de estadísticas
- Verificación de disponibilidad

---

## 📦 Dependencias Clave

- **Spring Boot 3.2.0**: Framework principal
- **Spring Data JPA**: ORM
- **PostgreSQL**: Base de datos
- **MapStruct**: Mapeo de objetos
- **Lombok**: Reducción de boilerplate
- **Jackson**: JSON processing
- **Maven**: Build tool
- **Docker**: Containerización

---

## 🎓 Mejores Prácticas Implementadas

✅ Separación de responsabilidades
✅ Inyección de dependencias
✅ Uso de interfaces
✅ DTOs para entrada/salida
✅ Validación de datos
✅ Manejo de excepciones
✅ Logging estructurado
✅ Transaccionalidad
✅ Paginación
✅ Documentación de código
✅ Versionamiento de APIs (/api/v1)
✅ Códigos HTTP apropiados
