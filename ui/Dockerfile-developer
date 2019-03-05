FROM node:10 as build

RUN mkdir app
COPY . /app
WORKDIR /app

# Install, lint, test and build
RUN npm i
RUN npm run lint
RUN npm run test
RUN npm run build

# Nginx web server from the official docker registry
FROM nginx:1.14.0-alpine

EXPOSE 8080

RUN rm -rv /etc/nginx/conf.d
COPY conf /etc/nginx

# The static site is built using npm run build
# the output of build is stored in the dist dir
WORKDIR /usr/share/nginx/html
COPY --from=build /app/dist/ /usr/share/nginx/html