START TRANSACTION;

DROP TABLE IF EXISTS transfer_type, transfer_status, transfers, users, accounts CASCADE;
DROP SEQUENCE IF EXISTS transfer_type_serial, transfer_status_serial, transfers_serial, users_serial, accounts_serial;

CREATE SEQUENCE users_serial;
CREATE TABLE users (
	user_id int NOT NULL DEFAULT nextval('users_serial'),
	username varchar(100) NOT NULL,
	password_hash varchar(100) NOT NULL,
	
	CONSTRAINT PK_user_id PRIMARY KEY (user_id)
);

CREATE SEQUENCE accounts_serial;
CREATE TABLE accounts (
	account_id int NOT NULL DEFAULT nextval('accounts_serial'),
	balance DECIMAL(15, 2) NOT NULL,
	user_id int NOT NULL,
	
	CONSTRAINT PK_account_id PRIMARY KEY (account_id),
	CONSTRAINT FK_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE SEQUENCE transfer_type_serial;
CREATE TABLE transfer_type (
	type_id int NOT NULL DEFAULT nextval('transfer_type_serial'),
	type_name varchar(65) NOT NULL,
	
	CONSTRAINT PK_type_id PRIMARY KEY (type_id)
);

CREATE SEQUENCE transfer_status_serial;
CREATE TABLE transfer_status (
	status_id int NOT NULL DEFAULT nextval('transfer_status_serial'),
	status_name varchar(65) NOT NULL,
	
	CONSTRAINT PK_status_id PRIMARY KEY (status_id)
);

CREATE SEQUENCE transfers_serial;
CREATE TABLE transfers (
	transfer_id int NOT NULL DEFAULT nextval('transfers_serial'),
	status_id int NOT NULL,
	type_id int NOT NULL,
	amount DECIMAL(15, 2) NOT NULL,
	account_to int NOT NULL,
	account_from int NOT NULL,
	
	CONSTRAINT PK_transfer_id PRIMARY KEY (transfer_id),
	CONSTRAINT FK_status_id FOREIGN KEY (status_id) REFERENCES transfer_status(status_id),
	CONSTRAINT FK_type_id FOREIGN KEY (type_id) REFERENCES transfer_type(type_id),
	CONSTRAINT FK_account_to FOREIGN KEY (account_to) REFERENCES accounts(account_id),
	CONSTRAINT FK_account_from FOREIGN KEY (account_from) REFERENCES accounts(account_id)
	
);

INSERT INTO transfer_type(type_id, type_name) VALUES
(10, 'REQUEST'),
(20, 'SEND');

INSERT INTO transfer_status(status_id, status_name) VALUES
(1, 'PENDING'),
(2, 'APPROVED'),
(3, 'REJECTED');

INSERT INTO users(user_id, username, password_hash) VALUES
(10000, 'frodo_baggins', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC'),
(20000, 'samwise_gamgee','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC');

INSERT INTO accounts(account_id, balance, user_id) VALUES
(1000, 1000.00, 10000),
(2000, 1000.00, 20000);

INSERT INTO transfers(transfer_id, status_id, type_id, amount, account_to, account_from) VALUES
(100000, 1, 10, 50.00, 1000, 2000);

SELECT setval('transfer_type_serial', (SELECT MAX(type_id) + 10 FROM transfer_type));

SELECT setval('transfer_status_serial', (SELECT MAX(status_id) + 1 FROM transfer_status));

SELECT setval('transfers_serial', (SELECT MAX(transfer_id) + 100000 FROM transfers));

SELECT setval('users_serial', (SELECT MAX(user_id) + 10000 FROM users));

SELECT setval('accounts_serial', (SELECT MAX(account_id) + 1000 FROM accounts));

COMMIT;