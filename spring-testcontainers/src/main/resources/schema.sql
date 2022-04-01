DROP TABLE IF EXISTS customers;
create table customers(id BIGINT not null auto_increment primary key, first_name VARCHAR(255), last_name VARCHAR(255));
insert into customers (first_name, last_name) values ('jong', 'lee');
insert into customers (first_name, last_name) values ('hello', 'kim');