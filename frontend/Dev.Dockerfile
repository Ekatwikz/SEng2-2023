FROM node:18-alpine
WORKDIR /app/frontend
COPY package*.json .
RUN npm ci
COPY . .
# sus mounts? sadge. TODO: make this cleaner
CMD ( [ -d node_modules ] || npm install ) && npm run dev
EXPOSE 3000
