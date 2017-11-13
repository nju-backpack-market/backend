CREATE TABLE IF NOT EXISTS orders
(
  oid            BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  state          VARCHAR(40)  NOT NULL,
  time           DATETIME     NOT NULL,
  total_price    DOUBLE       NOT NULL,
  c_name         VARCHAR(40)  NOT NULL,
  c_phone_number VARCHAR(40)  NOT NULL,
  c_email        VARCHAR(40)  NOT NULL,
  c_country      VARCHAR(40)  NOT NULL,
  c_province     VARCHAR(40)  NOT NULL,
  c_city         VARCHAR(40)  NOT NULL,
  c_line1        VARCHAR(110) NOT NULL,
  c_line2        VARCHAR(110) NOT NULL,
  c_postal_code  VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS products
(
  pid         BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  pname       VARCHAR(255) NOT NULL,
  price       DOUBLE       NOT NULL,
  description TEXT         NOT NULL,
  on_sale     BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS product_images (
  pid        BIGINT      NOT NULL,
  image_name VARCHAR(40) NOT NULL,
  PRIMARY KEY (pid, image_name)
);

CREATE TABLE IF NOT EXISTS shopping_items
(
  oid            BIGINT NOT NULL,
  pid            BIGINT NOT NULL,
  count          INT    NOT NULL,
  origin_price   DOUBLE NOT NULL,
  actual_price   DOUBLE NOT NULL,
  subtotal_price DOUBLE NOT NULL,
  PRIMARY KEY (oid, pid)
);

CREATE TABLE IF NOT EXISTS users
(
  username VARCHAR(40) NOT NULL PRIMARY KEY,
  password VARCHAR(40) NOT NULL
);

INSERT INTO users (username, password) VALUES ('123', '123');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code)
VALUES
  (1, 'PAYING', now(), 109.0, 'foo1', '123456', 'foo1@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code)
VALUES
  (2, 'STOCK_OUT', now(), 68.0, 'foo2', '654321', 'foo2@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code)
VALUES
  (3, 'CREATE', now(), 10.0, 'foo3', '567890', 'foo3@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code)
VALUES
  (4, 'DELIVERING', now(), 40.0, 'foo4', '098765', 'foo4@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code)
VALUES
  (5, 'DELIVERING', now(), 69.0, 'foo5', '114514', 'foo5@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001');

INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (1, 1, 1, 10.0, 10.0, 10.0);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (1, 2, 2, 50.0, 50.0, 100.0);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (2, 3, 1, 11.4, 11.4, 11.4);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (2, 4, 3, 19.19, 19.19, 57.57);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (3, 3, 1, 10.0, 10.0, 10.0);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (4, 2, 2, 20.0, 20.0, 40.0);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (5, 1, 1, 30.0, 30.0, 30.0);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (5, 2, 1, 40.0, 40.0, 40.0);
INSERT INTO products (pid, pname, price, description, on_sale) VALUES (1, '红包', 11.4, 'Mock1', TRUE);
INSERT INTO products (pid, pname, price, description, on_sale) VALUES (2, '黄包', 51.4, 'Mock2', TRUE);
INSERT INTO products (pid, pname, price, description, on_sale) VALUES (3, '绿包', 19.19, 'Mock3', FALSE);
INSERT INTO products (pid, pname, price, description, on_sale) VALUES (4, '紫包', 81.0, 'Mock4', TRUE);
INSERT INTO product_images (pid, image_name) VALUES (1, '1');
INSERT INTO product_images (pid, image_name) VALUES (1, '2');
INSERT INTO product_images (pid, image_name) VALUES (1, '3');
INSERT INTO product_images (pid, image_name) VALUES (2, '5');
INSERT INTO product_images (pid, image_name) VALUES (2, '6');
INSERT INTO product_images (pid, image_name) VALUES (3, '2');
INSERT INTO product_images (pid, image_name) VALUES (3, '1');
INSERT INTO product_images (pid, image_name) VALUES (3, '3');
INSERT INTO product_images (pid, image_name) VALUES (3, '7');
INSERT INTO product_images (pid, image_name) VALUES (4, '8');
INSERT INTO product_images (pid, image_name) VALUES (4, '1');
