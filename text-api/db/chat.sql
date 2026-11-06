-- Схема и таблица для приложения чата (урок 10)
CREATE SCHEMA chat;

CREATE TABLE chat.message (
                              id          BIGSERIAL     PRIMARY KEY,
                              sender_name VARCHAR(100)  NOT NULL,
                              sender_ip   VARCHAR(50),
                              content     VARCHAR(1000) NOT NULL,
                              received_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);