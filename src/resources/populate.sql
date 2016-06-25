CREATE DATABASE onlineshop OWNER onlineshop;
DROP TABLE products CASCADE;
CREATE TABLE products (
  -- AUTOINCREMENT
  id    SERIAL PRIMARY KEY,
  name  VARCHAR(255) NOT NULL,
  price INTEGER      NOT NULL
);
INSERT INTO products (name, price) VALUES ('basic good', 5);
INSERT INTO products (name, price) VALUES ('cheap good', 3);
INSERT INTO products (name, price) VALUES ('perfect good', 15);
INSERT INTO products (name, price) VALUES ('luxury good', 25);
INSERT INTO products (name, price) VALUES ('exotic good', 45);
DROP TABLE purchases;
CREATE TABLE purchases (
  -- AUTOINCREMENT
  id            SERIAL PRIMARY KEY,
  -- FOREIGN KEY
  product       INTEGER NOT NULL REFERENCES products (id),
  quantity      INTEGER NOT NULL,
  purchase_date DATE    NOT NULL
);
INSERT INTO purchases (product, quantity, purchase_date) VALUES (1, 5, '2016-06-05');
INSERT INTO purchases (product, quantity, purchase_date) VALUES (1, 7, '2016-05-13');
INSERT INTO purchases (product, quantity, purchase_date) VALUES (2, 3, '2016-04-17');
INSERT INTO purchases (product, quantity, purchase_date) VALUES (2, 10, '2016-03-16');
INSERT INTO purchases (product, quantity, purchase_date) VALUES (3, 6, '2016-02-01');
INSERT INTO purchases (product, quantity, purchase_date) VALUES (3, 8, '2016-01-09');
INSERT INTO purchases (product, quantity, purchase_date) VALUES (4, 7, '2015-12-29');
INSERT INTO purchases (product, quantity, purchase_date) VALUES (4, 6, '2015-11-14');
INSERT INTO purchases (product, quantity, purchase_date) VALUES (5, 3, '2016-06-06');
INSERT INTO purchases (product, quantity, purchase_date) VALUES (5, 1, '2016-03-21');
