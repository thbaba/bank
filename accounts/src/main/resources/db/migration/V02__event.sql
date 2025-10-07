create table events (
    id serial primary key,
    topic varchar(128) not null,
    key int not null,
    payload text not null,
    status varchar(8) not null default 'PENDING'
);