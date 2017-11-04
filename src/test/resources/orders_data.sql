INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_address)
VALUES (1, 'CREATE', now(), NULL, 'foo1', '123456', 'foo1@software.com', 'NJU');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_address)
VALUES (2, 'STOCK_OUT', now(), NULL, 'foo2', '654321', 'foo2@software.com', 'NJU');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_address)
VALUES (3, 'CREATE', now(), NULL, 'foo3', '567890', 'foo3@software.com', 'NJU');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_address)
VALUES (4, 'DELIVERING', now(), NULL, 'foo4', '098765', 'foo4@software.com', 'NJU');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_address)
VALUES (5, 'DELIVERING', now(), NULL, 'foo5', '114514', 'foo5@software.com', 'NJU');

INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (1, 1, 1, 10, NULL, NULL);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (1, 2, 2, 50, NULL, NULL);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (2, 3, 1, 11.4, NULL, NULL);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (2, 4, 3, 19.19, NULL, NULL);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (3, 3, 1, 10.0, NULL, NULL);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (4, 2, 2, 20.0, NULL, NULL);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (5, 1, 1, 30.0, NULL, NULL);
INSERT INTO shopping_items (oid, pid, count, origin_price, actual_price, subtotal_price)
VALUES (5, 2, 1, 40.0, NULL, NULL);
