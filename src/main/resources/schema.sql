CREATE DATABASE IF NOT EXISTS nju_backpack_market;

use nju_backpack_market;

CREATE TABLE products (
    pid bigint NOT NULL AUTO_INCREMENT,
    pname varchar(255) NOT NULL,
    price double NOT NULL,
    description text NOT NULL,
    PRIMARY KEY(pid)
);

CREATE TABLE orders(
    oid bigint NOT NULL AUTO_INCREMENT,
    state varchar(40) NOT NULL,
    time datetime NOT NULL,
    c_name varchar(40) NOT NULL,
    c_phone_number varchar(40) NOT NULL,
    c_email varchar(40) NOT NULL,
    c_address varchar(255) NOT NULL,
    PRIMARY KEY(oid)
);

CREATE TABLE shopping_items(
    oid bigint NOT NULL,
    pid bigint NOT NULL,
    count int NOT NULL,
    origin_price double NOT NULL,
    actual_price double,
    subtotal_price double NOT NULL,
    PRIMARY KEY(oid, pid)
);

CREATE TABLE users(
    username varchar(40) NOT NULL,
    password varchar(40) NOT NULL,
    PRIMARY KEY(username)
);