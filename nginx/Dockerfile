FROM nginx:1.19.6-alpine

# remove o arquivo default.conf do nginx
RUN rm /etc/nginx/conf.d/default.conf

# copia o arquivo de configuração do nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

