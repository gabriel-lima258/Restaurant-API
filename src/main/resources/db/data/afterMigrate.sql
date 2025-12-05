
-- ============================================
-- DELETE DATA
-- ============================================

set foreign_key_checks = 0;

delete from city;
delete from kitchen;
delete from state;
delete from payment_method;
delete from `group`;
delete from group_permission;
delete from permission;
delete from product;
delete from restaurant;
delete from restaurant_payment_method;
delete from `user`;
delete from group_user;
delete from order_item;
delete from `order`;


set foreign_key_checks = 1;

alter table city auto_increment = 1;
alter table kitchen auto_increment = 1;
alter table state auto_increment = 1;
alter table payment_method auto_increment = 1;
alter table `group` auto_increment = 1;
alter table permission auto_increment = 1;
alter table product auto_increment = 1;
alter table restaurant auto_increment = 1;
alter table `user` auto_increment = 1;
-- ============================================
-- INSERT DATA
-- ============================================

-- Kitchen
insert into kitchen (id, name) values (1, 'Tailandesa');
insert into kitchen (id, name) values (2, 'Indiana');
insert into kitchen (id, name) values (3, 'Italiana');
insert into kitchen (id, name) values (4, 'Japonesa');
insert into kitchen (id, name) values (5, 'Brasileira');
insert into kitchen (id, name) values (6, 'Mexicana');
insert into kitchen (id, name) values (7, 'Francesa');
insert into kitchen (id, name) values (8, 'Árabe');
insert into kitchen (id, name) values (9, 'Chinesa');
insert into kitchen (id, name) values (10, 'Vegetariana');

-- State
insert into state (id, name) values (1, 'Minas Gerais');
insert into state (id, name) values (2, 'São Paulo');
insert into state (id, name) values (3, 'Ceará');
insert into state (id, name) values (4, 'Rio de Janeiro');
insert into state (id, name) values (5, 'Paraná');
insert into state (id, name) values (6, 'Bahia');
insert into state (id, name) values (7, 'Rio Grande do Sul');
insert into state (id, name) values (8, 'Santa Catarina');
insert into state (id, name) values (9, 'Goiás');
insert into state (id, name) values (10, 'Pernambuco');

-- City
insert into city (id, name, state_id) values (1, 'Uberlândia', 1);
insert into city (id, name, state_id) values (2, 'Belo Horizonte', 1);
insert into city (id, name, state_id) values (3, 'São Paulo', 2);
insert into city (id, name, state_id) values (4, 'Campinas', 2);
insert into city (id, name, state_id) values (5, 'Fortaleza', 3);
insert into city (id, name, state_id) values (6, 'Rio de Janeiro', 4);
insert into city (id, name, state_id) values (7, 'Niterói', 4);
insert into city (id, name, state_id) values (8, 'Curitiba', 5);
insert into city (id, name, state_id) values (9, 'Londrina', 5);
insert into city (id, name, state_id) values (10, 'Salvador', 6);
insert into city (id, name, state_id) values (11, 'Porto Alegre', 7);
insert into city (id, name, state_id) values (12, 'Florianópolis', 8);
insert into city (id, name, state_id) values (13, 'Goiânia', 9);
insert into city (id, name, state_id) values (14, 'Recife', 10);
insert into city (id, name, state_id) values (15, 'Santos', 2);
insert into city (id, name, state_id) values (16, 'Ribeirão Preto', 2);

-- Payment Method
insert into payment_method (id, description) values (1, 'Cartão de crédito');
insert into payment_method (id, description) values (2, 'Cartão de débito');
insert into payment_method (id, description) values (3, 'Dinheiro');
insert into payment_method (id, description) values (4, 'PIX');
insert into payment_method (id, description) values (5, 'Vale refeição');
insert into payment_method (id, description) values (6, 'Vale alimentação');

-- Restaurant
insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    1, 'Thai Gourmet', 10, 1, 
    '38400-000', 'Rua Tiradentes', '500', 
    'Sala 101', 'Centro', 1, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    2, 'Thai Delivery', 9.50, 1, 
    '38400-100', 'Avenida João Pinheiro', '1234', 
    null, 'Marta Helena', 1, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    3, 'Tuk Tuk Comida Indiana', 15, 2, 
    '01310-100', 'Avenida Paulista', '1578', 
    'Loja 45', 'Bela Vista', 3, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    4, 'Sabor do Oriente', 12.50, 1, 
    '30130-010', 'Rua da Bahia', '1200', 
    '2º andar', 'Centro', 2, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    5, 'Curry House', 14, 2, 
    '13083-000', 'Rua Barão de Jaguara', '500', 
    null, 'Centro', 4, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    6, 'Pad Thai Express', 8.50, 1, 
    '60060-100', 'Avenida Beira Mar', '2000', 
    'Kiosque 12', 'Meireles', 5, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    7, 'Ganesh Indian Restaurant', 16, 2, 
    '01310-200', 'Rua Augusta', '2345', 
    'Sala 3', 'Consolação', 3, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    8, 'Bangkok Thai', 11, 1, 
    '38400-200', 'Rua Benjamin Constant', '789', 
    null, 'Fundinho', 1, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    9, 'La Trattoria', 12, 3, 
    '20020-020', 'Avenida Atlântica', '1234', 
    'Loja 2', 'Copacabana', 6, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    10, 'Pasta & Cia', 10.50, 3, 
    '01310-100', 'Rua Oscar Freire', '567', 
    '1º andar', 'Jardins', 3, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    11, 'Sushi Master', 18, 4, 
    '01310-100', 'Avenida Paulista', '2000', 
    'Sala 15', 'Bela Vista', 3, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    12, 'Sakura Sushi', 15.50, 4, 
    '20020-020', 'Rua Barata Ribeiro', '789', 
    null, 'Copacabana', 6, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    13, 'Feijoada Completa', 9, 5, 
    '30130-010', 'Rua da Bahia', '500', 
    'Térreo', 'Centro', 2, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    14, 'Churrasco Gaúcho', 13, 5, 
    '90010-150', 'Avenida Borges de Medeiros', '1000', 
    'Loja 5', 'Centro Histórico', 11, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    15, 'Taco Loco', 11.50, 6, 
    '01310-100', 'Rua Augusta', '1500', 
    '2º andar', 'Consolação', 3, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    16, 'El Mariachi', 12, 6, 
    '20020-020', 'Avenida Atlântica', '800', 
    null, 'Copacabana', 6, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    17, 'Bistro Francês', 16.50, 7, 
    '01310-100', 'Alameda Santos', '200', 
    'Sala 10', 'Jardins', 3, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    18, 'Le Petit Paris', 17, 7, 
    '20020-020', 'Rua Barata Ribeiro', '456', 
    '1º andar', 'Copacabana', 6, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    19, 'Shawarma Express', 10, 8, 
    '01310-100', 'Rua Augusta', '3000', 
    null, 'Consolação', 3, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    20, 'Aladdin Food', 11.50, 8, 
    '30130-010', 'Rua da Bahia', '800', 
    'Loja 3', 'Centro', 2, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    21, 'Dragão Dourado', 13, 9, 
    '01310-100', 'Rua dos Três Irmãos', '600', 
    '2º andar', 'Vila Madalena', 3, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    22, 'Wok Express', 9.50, 9, 
    '20020-020', 'Avenida Atlântica', '1500', 
    null, 'Copacabana', 6, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    23, 'Verde Vida', 8, 10, 
    '01310-100', 'Rua Harmonia', '400', 
    'Loja 8', 'Vila Madalena', 3, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    24, 'Natureza Viva', 9, 10, 
    '30130-010', 'Rua da Bahia', '900', 
    '1º andar', 'Centro', 2, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    25, 'Spice Garden', 14.50, 2, 
    '80020-010', 'Avenida Sete de Setembro', '1200', 
    'Sala 20', 'Centro', 8, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    26, 'Bella Italia', 12, 3, 
    '88015-700', 'Avenida Beira Mar Norte', '500', 
    'Loja 12', 'Centro', 12, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    27, 'Sushi Bar', 16, 4, 
    '74015-010', 'Avenida T-4', '1500', 
    '2º andar', 'Setor Bueno', 13, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    28, 'Acarajé da Bahia', 7.50, 5, 
    '40020-000', 'Rua Chile', '200', 
    null, 'Pelourinho', 10, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    29, 'Taco Fiesta', 11, 6, 
    '50010-000', 'Avenida Boa Viagem', '3000', 
    'Loja 5', 'Boa Viagem', 14, 
    NOW(), NOW()
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, city_id, 
    created_at, updated_at
) values (
    30, 'Green Garden', 8.50, 10, 
    '86010-000', 'Avenida Brasil', '1000', 
    '1º andar', 'Centro', 9, 
    NOW(), NOW()
);

-- Restaurant Payment Method
insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (1, 1), (1, 2), (1, 3), (1, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (2, 1), (2, 3), (2, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (3, 1), (3, 2), (3, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (4, 1), (4, 2), (4, 3), (4, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (5, 1), (5, 2), (5, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (6, 1), (6, 3), (6, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (7, 1), (7, 2), (7, 3), (7, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (8, 1), (8, 2), (8, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (9, 1), (9, 2), (9, 3), (9, 4), (9, 5);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (10, 1), (10, 2), (10, 4), (10, 5);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (11, 1), (11, 2), (11, 4), (11, 5), (11, 6);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (12, 1), (12, 2), (12, 3), (12, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (13, 1), (13, 2), (13, 3), (13, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (14, 1), (14, 2), (14, 4), (14, 5);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (15, 1), (15, 2), (15, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (16, 1), (16, 3), (16, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (17, 1), (17, 2), (17, 4), (17, 5);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (18, 1), (18, 2), (18, 4), (18, 5);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (19, 1), (19, 2), (19, 3), (19, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (20, 1), (20, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (21, 1), (21, 2), (21, 4), (21, 5);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (22, 1), (22, 2), (22, 3), (22, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (23, 1), (23, 2), (23, 4), (23, 5), (23, 6);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (24, 1), (24, 2), (24, 4), (24, 5);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (25, 1), (25, 2), (25, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (26, 1), (26, 2), (26, 3), (26, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (27, 1), (27, 2), (27, 4), (27, 5);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (28, 1), (28, 3), (28, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (29, 1), (29, 2), (29, 4);

insert into restaurant_payment_method (restaurant_id, payment_method_id) 
values (30, 1), (30, 2), (30, 4), (30, 5), (30, 6);

-- Product
insert into product (name, description, price, active, restaurant_id) 
values ('Porco com molho agridoce', 'Deliciosa carne suína ao molho especial', 78.90, 1, 1);

insert into product (name, description, price, active, restaurant_id) 
values ('Camarão tailandês', '16 camarões grandes ao molho picante', 110, 1, 1);

insert into product (name, description, price, active, restaurant_id) 
values (
    'Salada picante com carne grelhada', 
    'Salada de folhas com cortes finos de carne bovina grelhada e nosso molho especial de pimenta vermelha', 
    87.20, 1, 2
);

insert into product (name, description, price, active, restaurant_id) 
values ('Garlic Naan', 'Pão tradicional indiano com cobertura de alho', 21, 1, 3);

insert into product (name, description, price, active, restaurant_id) 
values ('Murg Curry', 'Cubos de frango preparados com molho curry e especiarias', 43, 1, 3);

insert into product (name, description, price, active, restaurant_id) 
values (
    'Bife Ancho', 
    'Corte macio e suculento, com dois dedos de espessura, retirado da parte dianteira do contrafilé', 
    79, 1, 4
);

insert into product (name, description, price, active, restaurant_id) 
values (
    'T-Bone', 
    'Corte muito saboroso, com um osso em formato de T, sendo de um lado o contrafilé e do outro o filé mignon', 
    89, 1, 4
);

insert into product (name, description, price, active, restaurant_id) 
values (
    'Sanduíche X-Tudo', 
    'Sandubão com muito queijo, hamburger bovino, bacon, ovo, salada e maionese', 
    19, 1, 5
);

insert into product (name, description, price, active, restaurant_id) 
values ('Espetinho de Cupim', 'Acompanha farinha, mandioca e vinagrete', 8, 1, 6);

-- Permission
insert into permission (id, name, description) 
values (1, 'CONSULTAR_COZINHAS', 'Permite consultar cozinhas');

insert into permission (id, name, description) 
values (2, 'EDITAR_COZINHAS', 'Permite editar cozinhas');
