create table state (
    id bigint not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB default charset=utf8;

create table city (
    id bigint not null auto_increment,
    name varchar(255) not null,
    state_id bigint not null,
    primary key (id),
    constraint fk_city_state foreign key (state_id) references state (id)
) engine=InnoDB default charset=utf8;

