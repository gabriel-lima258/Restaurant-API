alter table `order` add column code varchar(36) not null after id;
update `order` set code = uuid();
alter table `order` add constraint uk_order_code unique (code);