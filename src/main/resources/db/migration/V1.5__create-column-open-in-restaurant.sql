alter table restaurant add column open tinyint(1) not null;
update restaurant set open = true;