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
    id serial not null primary key,
--     id serial not null,
    name varchar(32) not null,
    topic varchar(128) not null,
    key int not null,
    payload text not null,
    status varchar(8) not null default 'PENDING',
    created_at timestamp without time zone not null default now()
) ;
--     partition by hash (key);

-- create table events0 partition of events for values with (modulus 4, remainder 0);
-- create table events1 partition of events for values with (modulus 4, remainder 1);
-- create table events2 partition of events for values with (modulus 4, remainder 2);
-- create table events3 partition of events for values with (modulus 4, remainder 3);