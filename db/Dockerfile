FROM postgres:latest

ENV POSTGRES_USER qwe
ENV POSTGRES_PASSWORD 123
ENV POSTGRES_DB project1

COPY ddl.sql dml.sql /docker-entrypoint-initdb.d/
