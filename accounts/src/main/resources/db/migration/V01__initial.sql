create table accounts(
    account_id uuid default gen_random_uuid() primary key,
    security_number varchar(16) not null,
    account_number varchar(16) not null unique,
    account_type varchar(16) not null
);

create index security_number_index on accounts(security_number);
create index account_number_index on accounts(account_number);

CREATE OR REPLACE FUNCTION assign_next_account_number()
RETURNS TRIGGER AS $$
DECLARE
max_account int;
BEGIN
    IF NEW.account_number IS NULL THEN
SELECT COALESCE(MAX(account_number::int)::int, 1000000 - 1)
INTO max_account
FROM accounts;

NEW.account_number := LPAD((max_account + 1)::text, 7, '0');
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_assign_account_number
    BEFORE INSERT ON accounts
    FOR EACH ROW
    EXECUTE FUNCTION assign_next_account_number();