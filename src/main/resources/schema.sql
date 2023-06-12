---- Created by Vertabelo (http://vertabelo.com)
---- Last modification date: 2020-02-10 11:20:27.273
--
-- tables
-- Table: authorities
CREATE TABLE authorities (
    username varchar(25) NOT NULL,
    authority varchar(10) NOT NULL
);

-- Table: cards
CREATE TABLE cards (
    card_id int(5) NOT NULL AUTO_INCREMENT,
    wallet_id int(5) NOT NULL,
    number varchar(16) NOT NULL,
    cardholder varchar(40) NOT NULL,
    expire_date varchar(5) NOT NULL,
    deleted bit NOT NULL DEFAULT false,
    csv int NOT NULL,
    CONSTRAINT cards_pk PRIMARY KEY (card_id)
);

-- Table: transactions
CREATE TABLE transactions (
    transaction_id int(5) NOT NULL AUTO_INCREMENT,
    sender_id int(5) NOT NULL,
    receiver_id int(5) NOT NULL,
    card_id int(5),
-- to be check
    amount numeric(10,2) NOT NULL,
    currency varchar(50) DEFAULT 'USD',
    description text,
    date timestamp NOT NULL,
    verified bit NOT NULL DEFAULT false,
    idempotency_key varchar(50),
    CONSTRAINT transactions_pk PRIMARY KEY (transaction_id)
);

-- Table: transactions_verification
CREATE TABLE transactions_verification (
    verification_id int(5) NOT NULL AUTO_INCREMENT,
    user_id int(5) NOT NULL,
    transaction_id int(5) NOT NULL,
    code varchar(50) NOT NULL,
    expires timestamp NOT NULL,
    verified bit NOT NULL DEFAULT false,
    UNIQUE INDEX transactions_verification_ak_1 (code),
    CONSTRAINT transactions_verification_pk PRIMARY KEY (verification_id)
);

-- Table: users
CREATE TABLE users (
    user_id int(5) NOT NULL AUTO_INCREMENT,
    username varchar(25) NOT NULL,
    password varchar(68) NOT NULL,
    email varchar(50),
    phone_number varchar(13),
    first_name varchar(15),
    last_name varchar(25),
    profile_picture varchar(256) NULL,
    identity_picture varchar(256) NULL,
    enabled bit NOT NULL DEFAULT true,
    identified bit NOT NULL DEFAULT false,
    blocked bit NOT NULL DEFAULT true,
    UNIQUE INDEX users_unique_keys (username,email),
    CONSTRAINT users_pk PRIMARY KEY (user_id)
);


--
-- Table: users_verification
CREATE TABLE users_verification (
    verification_id int(5) NOT NULL AUTO_INCREMENT,
    user_id int(5) NOT NULL,
    code varchar(50) NOT NULL,
    expires timestamp NOT NULL,
    verified bit NOT NULL DEFAULT false,
    UNIQUE INDEX users_verification_ak_1 (code,user_id),
    CONSTRAINT users_verification_pk PRIMARY KEY (verification_id)
);
--
-- Table: wallets
CREATE TABLE wallets (
    wallet_id int(5) NOT NULL AUTO_INCREMENT,
    user_id int(5) NOT NULL,
-- to be check
    amount numeric(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT wallets_pk PRIMARY KEY (wallet_id)
);

-- foreign keys
-- Reference: authorities_users (table: authorities)
ALTER TABLE authorities ADD CONSTRAINT authorities_users FOREIGN KEY (username)
    REFERENCES users (username);

-- Reference: cards_wallets (table: cards)
ALTER TABLE cards ADD CONSTRAINT cards_wallets FOREIGN KEY (wallet_id)
    REFERENCES wallets (wallet_id);

-- Reference: transactions_cards (table: transactions)
ALTER TABLE transactions ADD CONSTRAINT transactions_cards FOREIGN KEY (card_id)
    REFERENCES cards (card_id);

-- Reference: transactions_receiver_users (table: transactions)
ALTER TABLE transactions ADD CONSTRAINT transactions_receiver_users FOREIGN KEY (receiver_id)
    REFERENCES users (user_id);

-- Reference: transactions_sender_users (table: transactions)
ALTER TABLE transactions ADD CONSTRAINT transactions_sender_users FOREIGN KEY (sender_id)
    REFERENCES users (user_id);

-- Reference: transactions_verification_transactions (table: transactions_verification)
ALTER TABLE transactions_verification ADD CONSTRAINT transactions_verification_transactions FOREIGN KEY (transaction_id)
    REFERENCES transactions (transaction_id);

-- Reference: transactions_verification_users (table: transactions_verification)
ALTER TABLE transactions_verification ADD CONSTRAINT transactions_verification_users FOREIGN KEY (user_id)
    REFERENCES users (user_id);

-- Reference: users_verification_users (table: users_verification)
ALTER TABLE users_verification ADD CONSTRAINT users_verification_users FOREIGN KEY (user_id)
    REFERENCES users (user_id);

-- Reference: wallets_users (table: wallets)
ALTER TABLE wallets ADD CONSTRAINT wallets_users FOREIGN KEY (user_id)
    REFERENCES users (user_id);

-- End of file.
