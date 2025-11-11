# ETAPA 1: Construcción (Build Stage)
FROM node:20-alpine AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar archivos de configuración de npm
# (package.json y package-lock.json)
COPY package*.json ./

# Instalar todas las dependencias del proyecto
# Esto instala node_modules/ dentro del contenedor
RUN npm install

# Copiar TODO el código fuente al contenedor
COPY . .

# Ejecutar el build de producción de Angular
# Esto genera la carpeta dist/ con los archivos optimizados
RUN npm run build -- --configuration production

# ETAPA 2: Servidor Web (Production Stage)
# Usamos Nginx para servir los archivos estáticos
FROM nginx:alpine

# Crear directorio para logs
RUN mkdir -p /var/log/nginx

COPY --from=build /app/dist/tienda-online-ui /usr/share/nginx/html

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer el puerto 80 (puerto estándar de HTTP)
EXPOSE 80

# Comando para iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]
