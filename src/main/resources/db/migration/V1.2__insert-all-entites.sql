create table `group` (
    id bigint not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table group_permission (
    group_id bigint not null,
    permission_id bigint not null
) engine=InnoDB;

create table group_user (
    group_id bigint not null,
    user_id bigint not null
) engine=InnoDB;

create table payment_method (
    id bigint not null auto_increment,
    description varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table permission (
    id bigint not null auto_increment,
    description varchar(255) not null,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table product (
    id bigint not null auto_increment,
    restaurant_id bigint,
    active bit not null,
    price decimal(38,2) not null,
    description varchar(255) not null,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table restaurant (
    id bigint not null auto_increment,
    city_id bigint,
    kitchen_id bigint,
    shipping_fee decimal(38,2) not null,
    created_at datetime not null,
    updated_at datetime not null,
    address_cep varchar(255),
    address_complement varchar(255),
    address_neighborhood varchar(255),
    address_number varchar(255),
    address_public_place varchar(255),
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table restaurant_payment_method (
    restaurant_id bigint not null,
    payment_method_id bigint not null
) engine=InnoDB;

create table `user` (
    id bigint not null auto_increment,
    created_at datetime not null,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
) engine=InnoDB;

-- ============================================
-- FOREIGN KEY CONSTRAINTS
-- ============================================

alter table group_permission 
    add constraint fk_group_permission_permission 
    foreign key (permission_id) references permission (id);

alter table group_permission 
    add constraint fk_group_permission_group 
    foreign key (group_id) references `group` (id);

alter table group_user 
    add constraint fk_group_user_group 
    foreign key (group_id) references `group` (id);

alter table group_user 
    add constraint fk_group_user_user 
    foreign key (user_id) references `user` (id);

alter table product 
    add constraint fk_product_restaurant 
    foreign key (restaurant_id) references restaurant (id);

alter table restaurant 
    add constraint fk_restaurant_city 
    foreign key (city_id) references city (id);

alter table restaurant 
    add constraint fk_restaurant_kitchen 
    foreign key (kitchen_id) references kitchen (id);

alter table restaurant_payment_method 
    add constraint fk_restaurant_payment_method_payment_method 
    foreign key (payment_method_id) references payment_method (id);

alter table restaurant_payment_method 
    add constraint fk_restaurant_payment_method_restaurant 
    foreign key (restaurant_id) references restaurant (id);