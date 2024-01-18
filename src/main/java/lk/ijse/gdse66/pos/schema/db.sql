DROP DATABASE IF EXISTS javaee_customer;
CREATE DATABASE IF NOT EXISTS javaee_customer;
USE javaee_customer;

CREATE TABLE Customer
(
    id      VARCHAR(8),
    name    VARCHAR(30),
    address VARCHAR(30),
    salary  double,
    CONSTRAINT PRIMARY KEY (id)
);

INSERT INTO Customer
VALUES ('C001', 'Thushal', 'Galle', 95000),
       ('C002', 'Kamal', 'Matara', 55000),
       ('C003', 'Nimal', 'Colombo', 45000),
       ('C004', 'Sunil', 'Negambo', 75000),
       ('C005', 'Jagath', 'Kegalle', 45000);