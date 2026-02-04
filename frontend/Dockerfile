# Stage 1: Build
FROM node:18-alpine AS build

WORKDIR /app

# Copiar archivos de package
COPY package*.json ./

# Limpiar caché de npm y instalar dependencias
RUN npm cache clean --force && \
    npm install --legacy-peer-deps

# Copiar el resto del código
COPY . .

# Variables de entorno para el build
ENV NODE_ENV=production
ENV GENERATE_SOURCEMAP=false
ENV CI=false

# Ejecutar build
RUN npm run build

# Stage 2: Serve
FROM nginx:alpine

# Copiar archivos compilados
COPY --from=build /app/build /usr/share/nginx/html

# Copiar configuración de nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Crear un script de inicio para verificar archivos
RUN echo '#!/bin/sh' > /docker-entrypoint.sh && \
    echo 'echo "Checking build files..."' >> /docker-entrypoint.sh && \
    echo 'ls -la /usr/share/nginx/html/' >> /docker-entrypoint.sh && \
    echo 'if [ ! -f /usr/share/nginx/html/index.html ]; then' >> /docker-entrypoint.sh && \
    echo '  echo "ERROR: index.html not found!"' >> /docker-entrypoint.sh && \
    echo '  exit 1' >> /docker-entrypoint.sh && \
    echo 'fi' >> /docker-entrypoint.sh && \
    echo 'exec nginx -g "daemon off;"' >> /docker-entrypoint.sh && \
    chmod +x /docker-entrypoint.sh

EXPOSE 80

CMD ["/docker-entrypoint.sh"]
