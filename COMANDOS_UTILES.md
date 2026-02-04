# COMANDOS ÚTILES PARA DESARROLLO

## Docker Compose

### Iniciar todos los servicios
docker-compose up -d

### Detener todos los servicios
docker-compose down

### Ver logs
docker-compose logs -f                          # Todos los servicios
docker-compose logs -f authors-service          # Solo Authors Service
docker-compose logs -f publications-service     # Solo Publications Service
docker-compose logs -f frontend                 # Solo Frontend

### Ejecutar comando en contenedor
docker-compose exec authors-service bash
docker-compose exec publications-service bash
docker-compose exec db-authors psql -U postgres -d authors_db

### Reconstruir imágenes
docker-compose build
docker-compose up -d --build

### Eliminar todo (incluido datos)
docker-compose down -v

---

## Authors Service (Puerto 8001)

### Health Check
curl http://localhost:8001/api/v1/authors/stats/total

### Crear Autor
curl -X POST http://localhost:8001/api/v1/authors \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@test.com","authorType":"INDIVIDUAL"}'

### Listar Autores
curl http://localhost:8001/api/v1/authors?page=0&size=10

### Obtener Autor
curl http://localhost:8001/api/v1/authors/1

### Buscar Autores
curl "http://localhost:8001/api/v1/authors/search?keyword=test"

### Actualizar Autor
curl -X PUT http://localhost:8001/api/v1/authors/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated","email":"updated@test.com"}'

### Eliminar Autor
curl -X DELETE http://localhost:8001/api/v1/authors/1

---

## Publications Service (Puerto 8002)

### Health Check
curl http://localhost:8002/api/v1/publications/stats/total

### Crear Publicación
curl -X POST http://localhost:8002/api/v1/publications \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","content":"Content","authorId":1}'

### Listar Publicaciones
curl http://localhost:8002/api/v1/publications?page=0&size=10

### Obtener Publicación
curl http://localhost:8002/api/v1/publications/1

### Publicaciones por Autor
curl http://localhost:8002/api/v1/publications/author/1

### Publicaciones por Estado
curl http://localhost:8002/api/v1/publications/status/DRAFT

### Buscar Publicaciones
curl "http://localhost:8002/api/v1/publications/search?keyword=test"

### Cambiar Estado
curl -X PATCH http://localhost:8002/api/v1/publications/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"IN_REVIEW"}'

### Eliminar Publicación
curl -X DELETE http://localhost:8002/api/v1/publications/1

---

## Base de Datos - Authors (Puerto 5432)

### Conectar a PostgreSQL Authors
docker-compose exec db-authors psql -U postgres -d authors_db

### Queries útiles en PostgreSQL
SELECT * FROM authors;
SELECT * FROM authors WHERE is_active = true;
SELECT COUNT(*) FROM authors;
SELECT * FROM authors ORDER BY created_at DESC LIMIT 10;

---

## Base de Datos - Publications (Puerto 5433)

### Conectar a PostgreSQL Publications
docker-compose exec db-publications psql -U postgres -d publications_db

### Queries útiles en PostgreSQL
SELECT * FROM publications;
SELECT * FROM publications WHERE status = 'PUBLISHED';
SELECT COUNT(*) FROM publications GROUP BY status;
SELECT * FROM publications WHERE author_id = 1;

---

## Frontend (Puerto 3000)

### Acceder a la aplicación
http://localhost:3000

### Instalar dependencias
npm install

### Ejecutar en desarrollo
npm start

### Build para producción
npm run build

---

## Maven - Desarrollo Local

### Authors Service
cd authors-service
mvn clean install
mvn spring-boot:run

### Publications Service
cd publications-service
mvn clean install
mvn spring-boot:run

---

## Troubleshooting

### Ver estado de todos los contenedores
docker-compose ps

### Ver estadísticas de recursos
docker stats

### Ejecutar comando en contenedor
docker-compose exec [service] [comando]

### Revisar logs de error
docker-compose logs [service] --tail=100

### Limpiar espacio en disco
docker system prune -a --volumes
