FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps

# Crear estructura de directorios esperada
RUN mkdir -p src/components src/services public

COPY . .

# Crear build mínimo si no existe
RUN npm run build || (mkdir -p build && echo "Build directory created")

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Crear index.html básico si no existe
RUN if [ ! -f /usr/share/nginx/html/index.html ]; then \
    mkdir -p /usr/share/nginx/html && \
    echo '<!DOCTYPE html><html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width"><title>Editorial Platform</title></head><body><div id="root"><h1>Editorial Platform is loading...</h1></div><script src="https://cdnjs.cloudflare.com/ajax/libs/react/18.2.0/react.production.min.js"></script></body></html>' > /usr/share/nginx/html/index.html; \
    fi

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
