CREATE TABLE events (
    event_id uuid default gen_random_uuid() primary key,
    event_name varchar(64) not null,
    aggregate_id uuid not null,
    payload text not null,
    status varchar(10) default 'PENDING'
);