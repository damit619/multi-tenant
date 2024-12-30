CREATE SCHEMA IF NOT EXISTS manipal01;

CREATE TABLE IF NOT EXISTS manipal01.file_data
(
    id    integer primary key,
    device_id   integer      not null,
    device_type varchar(60) not null,
    model varchar(150) not null,
    manufacturer  varchar(150)  not null,
    approval_date   date      not null
);

CREATE  SEQUENCE IF NOT EXISTS manipal01.file_data_id_seq;
