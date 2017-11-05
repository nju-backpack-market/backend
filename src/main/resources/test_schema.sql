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
  c_address      VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS products
(
  pid         BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  pname       VARCHAR(255) NOT NULL,
  price       DOUBLE       NOT NULL,
  description TEXT         NOT NULL
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

