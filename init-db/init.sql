DROP TABLE IF EXISTS bids;
DROP TABLE IF EXISTS auctions;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    balance NUMERIC(19, 2),
    version BIGINT
);

CREATE TABLE vehicles (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(255),
    model VARCHAR(255),
    year INTEGER,
    fuel_type VARCHAR(255),
    engine_capacity INTEGER,
    description TEXT,
    vin VARCHAR(255),
    image VARCHAR(255)
);

CREATE TABLE auctions (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT UNIQUE NOT NULL,
    start_price NUMERIC(19, 2),
    current_price NUMERIC(19, 2),
    min_increment NUMERIC(19, 2),
    end_time TIMESTAMP,
    version BIGINT,
    status VARCHAR(50),

    CONSTRAINT fk_auctions_vehicle
        FOREIGN KEY (vehicle_id)
        REFERENCES vehicles (id)
        ON DELETE CASCADE
);

CREATE TABLE bids (
    id BIGSERIAL PRIMARY KEY,
    auction_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount NUMERIC(19, 2),
    created_at TIMESTAMP,

    CONSTRAINT fk_bids_auction
        FOREIGN KEY (auction_id)
        REFERENCES auctions (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_bids_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);
