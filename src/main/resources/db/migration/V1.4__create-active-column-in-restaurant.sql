alter table restaurant add column active tinyint(1) not null;
update restaurant set active = true;