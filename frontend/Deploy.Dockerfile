FROM node:18-alpine AS build
WORKDIR /app/frontend
COPY package*.json .
RUN npm ci
COPY . .
RUN npm run build

FROM node:18-alpine
LABEL "author"="Emmanuel K"
WORKDIR /app/frontend
COPY package*.json .
RUN npm ci --omit=dev
COPY --from=build /app/frontend/.next .next
ENTRYPOINT ["npm", "start"]
EXPOSE 3000

