
--user entity
CREATE TABLE IF NOT EXISTS users(
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
username VARCHAR(100) NOT NULL,
password VARCHAR(255) NOT NULL,
role VARCHAR(100) NOT NULL,
is_account_locked BOOLEAN NOT NULL
);

--transaction entity
CREATE TABLE IF NOT EXISTS transactions_history(
id SERIAL PRIMARY KEY,
amount BIGINT NOT NULL,
number VARCHAR(100) NOT NULL,
ip VARCHAR(255) NOT NULL,
region VARCHAR(50) NOT NULL,
created_at TIMESTAMP NOT NULL,
result VARCHAR(255) NOT NULL,
feedback VARCHAR(255) NOT NULL
);

--suspiciousIp entity
CREATE TABLE IF NOT EXISTS suspicious_ip(
id SERIAL PRIMARY KEY,
ip VARCHAR(20) NOT NULL
);

--cardLimit entity
CREATE TABLE IF NOT EXISTS cards_limit(
id SERIAL PRIMARY KEY,
number VARCHAR(100) NOT NULL,
allowed BIGINT NOT NULL,
manual BIGINT NOT NULL
);

--stolenCard entity
CREATE TABLE IF NOT EXISTS stolen_cards(
id SERIAL PRIMARY KEY,
number VARCHAR(100) NOT NULL
);