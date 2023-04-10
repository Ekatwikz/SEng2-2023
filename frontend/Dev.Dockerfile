FROM node:18-alpine
WORKDIR /app/frontend
COPY package*.json .
RUN npm ci
COPY . .
ENTRYPOINT ["npm", "run", "dev"]
EXPOSE 3000
