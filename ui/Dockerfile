# Nginx web server from the official docker registry
FROM nginx:1.14.0-alpine

EXPOSE 8080

RUN rm -rv /etc/nginx/conf.d
COPY conf /etc/nginx

# The static site is built using npm run build
# the output of build is stored in the dist dir
WORKDIR /usr/share/nginx/html
COPY ./dist/ /usr/share/nginx/html