DROP TABLE IF EXISTS AccountOwner;

CREATE TABLE AccountOwner (id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
 name VARCHAR(30) NOT NULL,
 lastName VARCHAR(30) NOT NULL);

INSERT INTO AccountOwner (name, lastName) VALUES ('test1','test111');
INSERT INTO AccountOwner (name, lastName) VALUES ('test2','test222');
INSERT INTO AccountOwner (name, lastName) VALUES ('test3','test333');
INSERT INTO AccountOwner (name, lastName) VALUES ('test4','test444');

DROP TABLE IF EXISTS Account;

CREATE TABLE Account (id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
ownerId INTEGER,
amount DECIMAL(19,2),
currencyCode VARCHAR(30)
);

INSERT INTO Account (ownerId,amount,currencyCode) VALUES (1,100.00,'USD');
INSERT INTO Account (ownerId,amount,currencyCode) VALUES (2,200.00,'USD');
INSERT INTO Account (ownerId,amount,currencyCode) VALUES (3,300.00,'EUR');
INSERT INTO Account (ownerId,amount,currencyCode) VALUES (4,400.00,'RUB');
