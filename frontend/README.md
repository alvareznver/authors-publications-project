# Frontend - Editorial Platform

## Descripción

Frontend React 18 para la plataforma de gestión de autores y publicaciones.

## Estructura

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── AuthorsComponent.js
│   │   └── PublicationsComponent.js
│   ├── services/
│   │   └── apiService.js
│   ├── App.js
│   ├── App.css
│   └── index.js
├── package.json
├── Dockerfile
├── nginx.conf
├── .babelrc
└── .browserslistrc
```

## Configuración Necesaria

### En desarrollo local

```bash
# Instalar dependencias
npm install

# Ejecutar en desarrollo
npm start

# Compilar para producción
npm run build
```

### En Docker

El Dockerfile está configurado para:
1. Instalar dependencias con npm install
2. Compilar con npm run build
3. Servir con Nginx

## Componentes

### AuthorsComponent
- Crear, listar, editar y eliminar autores
- Búsqueda de autores
- Paginación

### PublicationsComponent
- Crear publicaciones
- Listar publicaciones
- Cambiar estado editorial
- Filtrar por estado
- Paginación

## APIs Usadas

### Authors Service
- Base URL: http://localhost:8001/api/v1
- Endpoints:
  - GET /authors
  - POST /authors
  - GET /authors/{id}
  - PUT /authors/{id}
  - DELETE /authors/{id}
  - GET /authors/search

### Publications Service
- Base URL: http://localhost:8002/api/v1
- Endpoints:
  - GET /publications
  - POST /publications
  - GET /publications/{id}
  - PATCH /publications/{id}/status
  - DELETE /publications/{id}
  - GET /publications/status/{status}
  - GET /publications/author/{authorId}

## Troubleshooting

### npm install falla

```bash
# Usar flag --legacy-peer-deps
npm install --legacy-peer-deps
```

### npm run build falla

```bash
# Limpiar cache
rm -rf node_modules package-lock.json
npm install --legacy-peer-deps
npm run build
```

### Port 3000 ya está en uso

```bash
# En Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# En Linux/Mac
lsof -i :3000
kill -9 <PID>
```

## Notas Importantes

- El archivo `.babelrc` configura Babel para compilación
- El archivo `.browserslistrc` especifica compatibilidad de navegadores
- Nginx sirve los archivos estáticos en el puerto 80
- CORS está habilitado en los servicios backend
