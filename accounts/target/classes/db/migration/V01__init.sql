create table accounts(
    id serial not null primary key,
    security_number varchar(16) not null unique,
    balance float not null default 0.0
);