INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code, l_company, l_code)
VALUES
  (1, 'PAYING', now(), 109.0, 'foo1', '123456', 'foo1@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001', NULL, NULL);
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code, l_company, l_code)
VALUES
  (2, 'STOCK_OUT', now(), 68.0, 'foo2', '654321', 'foo2@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001', NULL, NULL);
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code, l_company, l_code)
VALUES
  (3, 'CREATE', now(), 10.0, 'foo3', '567890', 'foo3@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001', NULL, NULL);
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code, l_company, l_code)
VALUES
  (4, 'DELIVERING', now(), 40.0, 'foo4', '098765', 'foo4@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001', '圆通', '123456');
INSERT INTO orders (oid, state, time, total_price, c_name, c_phone_number, c_email, c_country, c_province, c_city, c_line1, c_line2, c_postal_code, l_company, l_code)
VALUES
  (5, 'DELIVERING', now(), 69.0, 'foo5', '114514', 'foo5@software.com', 'China', 'JiangSu', 'Nanjing',
      'XiXia', 'XianLing', '226001', '中通', '234567');

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
