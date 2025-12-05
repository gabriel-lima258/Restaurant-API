create table `order` (
    id bigint not null auto_increment,
    subtotal decimal(10,2) not null,
    fee_shipping decimal(10,2) not null,
    total_value decimal(10,2) not null,

    restaurant_id bigint not null,
    client_id bigint not null,
    payment_method_id bigint not null,
    
    address_city_id bigint(20) not null,
    address_cep varchar(9) not null,
    address_public_place varchar(100) not null,
    address_number varchar(20) not null,
    address_complement varchar(60) null,
    address_neighborhood varchar(60) not null,
    
    status varchar(10) not null,    
    created_at datetime not null,
    confirmed_at datetime null,
    canceled_at datetime null,
    delivered_at datetime null,

    primary key (id),

    constraint fk_pedido_address_city foreign key (address_city_id) references city (id),
    constraint fk_pedido_restaurant foreign key (restaurant_id) references restaurant (id),
    constraint fk_pedido_client foreign key (client_id) references `user` (id),
    constraint fk_pedido_payment_method foreign key (payment_method_id) references payment_method (id)
) engine=InnoDB default charset=utf8;

create table order_item (
    id bigint not null auto_increment,
    quantity smallint(6) not null,
    unit_price decimal(10,2) not null,
    total_price decimal(10,2) not null,
    observation varchar(255) null,
    order_id bigint not null,
    product_id bigint not null,
    
    primary key (id),
    unique key uk_item_order_product (order_id, product_id),

    constraint fk_item_order_order foreign key (order_id) references `order` (id),
    constraint fk_item_pedido_product foreign key (product_id) references product (id)
) engine=InnoDB default charset=utf8;