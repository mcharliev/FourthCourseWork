-- liquibase formatted sql

--changeset mcharliev:2

CREATE TABLE notification_task
(
    id           SERIAL NOT NULL PRIMARY KEY,
    chat_id      BIGINT,
    notification TEXT,
    date_time    TIMESTAMP
);