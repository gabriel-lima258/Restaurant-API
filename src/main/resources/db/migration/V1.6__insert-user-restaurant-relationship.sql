create table restaurant_user_responsible (
    restaurant_id bigint not null,
    user_id bigint not null
) engine=InnoDB;

alter table restaurant_user_responsible add constraint fk_restaurant_user_responsible_restaurant foreign key (restaurant_id) references restaurant (id);

alter table restaurant_user_responsible add constraint fk_restaurant_user_responsible_user foreign key (user_id) references `user` (id);