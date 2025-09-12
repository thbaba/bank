create table cards (
    card_id uuid default gen_random_uuid() primary key,
    account_id uuid not null,
    total_limit float not null,
    amount_used float not null
);