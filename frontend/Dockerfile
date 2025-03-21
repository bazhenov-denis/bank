# Stage 1: Сборка React приложения
FROM node:18 as build
WORKDIR /app
# Копируем файлы зависимостей и устанавливаем их
COPY package.json package-lock.json ./
RUN npm install
# Копируем остальные файлы и собираем приложение
COPY . .
RUN npm run build

# Stage 2: Раздача статичных файлов через Nginx
FROM nginx
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
