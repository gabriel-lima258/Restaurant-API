alter table restaurant change column city_id address_city_id bigint(20) not null;
alter table `order` change column address_city_id address_city_id bigint(20) not null;