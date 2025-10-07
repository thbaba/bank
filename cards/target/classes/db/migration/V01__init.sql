create table accounts(
    id int primary key,
    security_number varchar(16) not null,
    total_limit float not null default 0.0
);

create table cards(
    id serial primary key ,
    account_id int not null references accounts(id),
    card_limit float not null,
    balance float not null default 0.0
);

create table events (
    id serial primary key,
    topic varchar(128) not null,
    key int not null,
    payload text not null,
    status varchar(8) not null default 'PENDING'
);