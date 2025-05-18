CREATE TABLE client (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    webhook_url VARCHAR(1024) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE notification_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id VARCHAR(100) NOT NULL UNIQUE,
    event_type VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    delivery_date DATE NOT NULL,
    delivery_status VARCHAR(50) NOT NULL,
    client_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES client(id)
);