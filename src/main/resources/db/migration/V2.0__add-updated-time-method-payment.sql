alter table payment_method add column updated_at datetime null;
update payment_method set updated_at = UTC_TIMESTAMP();
alter table payment_method modify column updated_at datetime not null;