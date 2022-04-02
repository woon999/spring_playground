DROP TABLE IF EXISTS customers;
create table customers(id BIGINT not null primary key, first_name VARCHAR(255), last_name VARCHAR(255));
insert into customers (id, first_name, last_name) values (1, 'lee', 'test');
insert into customers (id, first_name, last_name) values (2, 'kim', 'test');
insert into customers (id, first_name, last_name) values (3, 'park', 'test2');