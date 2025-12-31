
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

delete from photo_product;

delete from restaurant_user_responsible;


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
alter table `order` auto_increment = 1;
alter table order_item auto_increment = 1;

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
insert ignore into payment_method (id, description, updated_at) values (1, 'Cartão de crédito', UTC_TIMESTAMP());
insert ignore into payment_method (id, description, updated_at) values (2, 'Cartão de débito', UTC_TIMESTAMP());
insert ignore into payment_method (id, description, updated_at) values (3, 'Dinheiro', UTC_TIMESTAMP());
insert ignore into payment_method (id, description, updated_at) values (4, 'PIX', UTC_TIMESTAMP());
insert ignore into payment_method (id, description, updated_at) values (5, 'Vale refeição', UTC_TIMESTAMP());
insert ignore into payment_method (id, description, updated_at) values (6, 'Vale alimentação', UTC_TIMESTAMP());

-- Restaurant
insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at, 
    active, 
    open    
) values (
    1, 'Thai Gourmet', 10, 1, 
    '38400-000', 'Rua Tiradentes', '500', 
    'Sala 101', 'Centro', 1, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    2, 'Thai Delivery', 9.50, 1, 
    '38400-100', 'Avenida João Pinheiro', '1234', 
    null, 'Marta Helena', 1, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    false, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    3, 'Tuk Tuk Comida Indiana', 15, 2, 
    '01310-100', 'Avenida Paulista', '1578', 
    'Loja 45', 'Bela Vista', 3, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    4, 'Sabor do Oriente', 12.50, 1, 
    '30130-010', 'Rua da Bahia', '1200', 
    '2º andar', 'Centro', 2, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    5, 'Curry House', 14, 2, 
    '13083-000', 'Rua Barão de Jaguara', '500', 
    null, 'Centro', 4, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    6, 'Pad Thai Express', 8.50, 1, 
    '60060-100', 'Avenida Beira Mar', '2000', 
    'Kiosque 12', 'Meireles', 5, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    7, 'Ganesh Indian Restaurant', 16, 2, 
    '01310-200', 'Rua Augusta', '2345', 
    'Sala 3', 'Consolação', 3, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    8, 'Bangkok Thai', 11, 1, 
    '38400-200', 'Rua Benjamin Constant', '789', 
    null, 'Fundinho', 1, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    9, 'La Trattoria', 12, 3, 
    '20020-020', 'Avenida Atlântica', '1234', 
    'Loja 2', 'Copacabana', 6, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    10, 'Pasta & Cia', 10.50, 3, 
    '01310-100', 'Rua Oscar Freire', '567', 
    '1º andar', 'Jardins', 3, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    11, 'Sushi Master', 18, 4, 
    '01310-100', 'Avenida Paulista', '2000', 
    'Sala 15', 'Bela Vista', 3, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    12, 'Sakura Sushi', 15.50, 4, 
    '20020-020', 'Rua Barata Ribeiro', '789', 
    null, 'Copacabana', 6, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    13, 'Feijoada Completa', 9, 5, 
    '30130-010', 'Rua da Bahia', '500', 
    'Térreo', 'Centro', 2, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    14, 'Churrasco Gaúcho', 13, 5, 
    '90010-150', 'Avenida Borges de Medeiros', '1000', 
    'Loja 5', 'Centro Histórico', 11, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
);

insert into restaurant (
    id, name, shipping_fee, kitchen_id, 
    address_cep, address_public_place, address_number, 
    address_complement, address_neighborhood, address_city_id, 
    created_at, updated_at,
    active, 
    open
) values (
    15, 'Taco Loco', 11.50, 6, 
    '01310-100', 'Rua Augusta', '1500', 
    '2º andar', 'Consolação', 3, 
    UTC_TIMESTAMP, UTC_TIMESTAMP,
    true, true
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


-- Product
insert into product (name, description, price, active, restaurant_id) 
values ('Porco com molho agridoce', 'Deliciosa carne suína ao molho especial', 78.90, 1, 1);

insert into product (name, description, price, active, restaurant_id) 
values ('Camarão tailandês', '16 camarões grandes ao molho picante', 110, 1, 3);

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
    89, 0, 4
);

insert into product (name, description, price, active, restaurant_id) 
values (
    'Sanduíche X-Tudo', 
    'Sandubão com muito queijo, hamburger bovino, bacon, ovo, salada e maionese', 
    19, 1, 5
);

insert into product (name, description, price, active, restaurant_id) 
values ('Espetinho de Cupim', 'Acompanha farinha, mandioca e vinagrete', 8, 0, 1);

-- User
insert into `user` (name, email, password, created_at) values ('Gabriel Silva', 'gabriel58221@gmail.com', '$2a$12$FVdYUu.BDXhN.hCyxgOoXOd/8l.Ovdite.YNs2MDueL8D0fLmDCU6', UTC_TIMESTAMP);
insert into `user` (name, email, password, created_at) values ('Maria Oliveira', 'maria.oliveira@example.com', '$2a$12$FVdYUu.BDXhN.hCyxgOoXOd/8l.Ovdite.YNs2MDueL8D0fLmDCU6', UTC_TIMESTAMP);
insert into `user` (name, email, password, created_at) values ('Pedro Santos', 'pedro.santos@example.com', '$2a$12$FVdYUu.BDXhN.hCyxgOoXOd/8l.Ovdite.YNs2MDueL8D0fLmDCU6', UTC_TIMESTAMP);
insert into `user` (name, email, password, created_at) values ('Ana Paula', 'ana.paula@example.com', '$2a$12$FVdYUu.BDXhN.hCyxgOoXOd/8l.Ovdite.YNs2MDueL8D0fLmDCU6', UTC_TIMESTAMP);
insert into `user` (name, email, password, created_at) values ('Carlos Ferreira', 'carlos.ferreira@example.com', '$2a$12$FVdYUu.BDXhN.hCyxgOoXOd/8l.Ovdite.YNs2MDueL8D0fLmDCU6', UTC_TIMESTAMP);
insert into `user` (name, email, password, created_at) values ('Laura Lima', 'laura.lima@example.com', '$2a$12$FVdYUu.BDXhN.hCyxgOoXOd/8l.Ovdite.YNs2MDueL8D0fLmDCU6', UTC_TIMESTAMP);
insert into `user` (name, email, password, created_at) values ('Rafael Oliveira', 'rafael.oliveira@example.com', '$2a$12$FVdYUu.BDXhN.hCyxgOoXOd/8l.Ovdite.YNs2MDueL8D0fLmDCU6', UTC_TIMESTAMP);

-- Permission
insert into permission (id, name, description) values (1, 'EDITAR_COZINHAS', 'Permite editar cozinhas');
insert into permission (id, name, description) values (2, 'EDITAR_FORMAS_PAGAMENTO', 'Permite criar ou editar formas de pagamento');
insert into permission (id, name, description) values (3, 'EDITAR_CIDADES', 'Permite criar ou editar cidades');
insert into permission (id, name, description) values (4, 'EDITAR_ESTADOS', 'Permite criar ou editar estados');
insert into permission (id, name, description) values (5, 'CONSULTAR_USUARIOS_GRUPOS_PERMISSOES', 'Permite consultar usuários, grupos e permissões');
insert into permission (id, name, description) values (6, 'EDITAR_USUARIOS_GRUPOS_PERMISSOES', 'Permite criar ou editar usuários, grupos e permissões');
insert into permission (id, name, description) values (7, 'EDITAR_RESTAURANTES', 'Permite criar, editar ou gerenciar restaurantes');
insert into permission (id, name, description) values (8, 'CONSULTAR_PEDIDOS', 'Permite consultar pedidos');
insert into permission (id, name, description) values (9, 'GERENCIAR_PEDIDOS', 'Permite gerenciar pedidos');
insert into permission (id, name, description) values (10, 'GERAR_RELATORIOS', 'Permite gerar relatórios');


-- Group
insert into `group` (id, name) values (1, 'Gerente'), (2, 'Vendedor'), (3, 'Secretária'), (4, 'Cadastrador');

-- group permission

# Adiciona todas as permissoes no grupo do gerente
insert into group_permission (group_id, permission_id)
select 1, id from permission;

# Adiciona permissoes no grupo do vendedor
insert into group_permission (group_id, permission_id)
select 2, id from permission where name like 'CONSULTAR_%';

insert into group_permission (group_id, permission_id)
select 2, id from permission where name = 'EDITAR_RESTAURANTES';

# Adiciona permissoes no grupo do auxiliar
insert into group_permission (group_id, permission_id)
select 3, id from permission where name like 'CONSULTAR_%';

# Adiciona permissoes no grupo cadastrador
insert into group_permission (group_id, permission_id)
select 4, id from permission where name like '%_RESTAURANTES';

-- group user
insert into group_user (user_id, group_id) values (1, 1), (2, 2), (3, 3);

-- restaurant user responsible
insert into restaurant_user_responsible (user_id, restaurant_id) values (1, 1), (2, 2), (3, 3), (7, 3), (6, 4);

-- order
insert into `order` (id, code, restaurant_id, client_id, payment_method_id, address_city_id, address_cep, 
    address_public_place, address_number, address_complement, address_neighborhood,
    status, created_at, confirmed_at, canceled_at, delivered_at, subtotal, fee_shipping, total_value)
values (1, "9f5f3b1f-67e1-4b3a-9f61-c042db67443f", 3, 1, 1, 3, '01310-100', 'Avenida Paulista', '1578', 'Loja 45', 'Bela Vista',
    'CREATED', "2019-11-02 20:35:10", null, null, null, 174.00, 15.00, 189.00);

insert into `order` (id, code, restaurant_id, client_id, payment_method_id, address_city_id, address_cep, 
    address_public_place, address_number, address_complement, address_neighborhood,
    status, created_at, confirmed_at, canceled_at, delivered_at, subtotal, fee_shipping, total_value)
values (2, "31ef66a1-d362-4085-8dd6-7d91815bc0c2", 4, 2, 2, 2, '30130-010', 'Rua da Bahia', '500', 'Térreo', 'Centro',
    'CONFIRMED', "2025-12-18 20:35:10", "2025-12-18 20:40:00", null, null, 168.00, 12.50, 180.50);

insert into `order` (id, code, restaurant_id, client_id, payment_method_id, address_city_id, address_cep, 
    address_public_place, address_number, address_complement, address_neighborhood,
    status, created_at, confirmed_at, canceled_at, delivered_at, subtotal, fee_shipping, total_value)
values (3, "b0969c7c-3348-441f-bb15-eaf435f7ad83", 1, 7, 3, 1, '38400-000', 'Rua Tiradentes', '500', 'Sala 101', 'Centro',
    'DELIVERED', "2025-12-11 20:35:10", "2025-12-11 20:40:00", null, "2025-12-11 21:00:00", 78.90, 10.00, 88.90);

insert into `order` (id, code, restaurant_id, client_id, payment_method_id, address_city_id, address_cep, 
    address_public_place, address_number, address_complement, address_neighborhood,
    status, created_at, confirmed_at, canceled_at, delivered_at, subtotal, fee_shipping, total_value)
values (4, "c3a5e8b2-4f1c-4d8e-a5f2-9c8e7d6f5b4a", 5, 3, 1, 4, '13083-000', 'Rua Barão de Jaguara', '500', null, 'Centro',
    'CREATED', "2025-12-20 18:20:00", null, null, null, 64.00, 14.00, 78.00);

insert into `order` (id, code, restaurant_id, client_id, payment_method_id, address_city_id, address_cep, 
    address_public_place, address_number, address_complement, address_neighborhood,
    status, created_at, confirmed_at, canceled_at, delivered_at, subtotal, fee_shipping, total_value)
values (5, "d7b9f4c1-5e2d-4a9f-b6c3-8d7e6f5a4b3c", 9, 4, 2, 6, '20020-020', 'Avenida Atlântica', '1234', 'Loja 2', 'Copacabana',
    'CONFIRMED', "2025-12-22 19:15:00", "2025-12-22 19:20:00", null, null, 150.00, 12.00, 162.00);

insert into `order` (id, code, restaurant_id, client_id, payment_method_id, address_city_id, address_cep, 
    address_public_place, address_number, address_complement, address_neighborhood,
    status, created_at, confirmed_at, canceled_at, delivered_at, subtotal, fee_shipping, total_value)
values (6, "e8c2f5d3-6f4e-5b1a-c7d4-9e8f7a6b5c4d", 11, 5, 4, 3, '01310-100', 'Avenida Paulista', '2000', 'Sala 15', 'Bela Vista',
    'CREATED', "2025-12-25 20:00:00", null, null, null, 200.00, 18.00, 218.00);

-- order item
insert into order_item (id, order_id, product_id, quantity, unit_price, total_price, observation)
values (1, 1, 2, 1, 110.00, 110.00, 'Sem pimenta');

insert into order_item (id, order_id, product_id, quantity, unit_price, total_price, observation)
values (2, 1, 4, 1, 21.00, 21.00, 'Bem assado');

insert into order_item (id, order_id, product_id, quantity, unit_price, total_price, observation)
values (3, 1, 5, 1, 43.00, 43.00, null);

insert into order_item (id, order_id, product_id, quantity, unit_price, total_price, observation)
values (4, 2, 6, 1, 79.00, 79.00, 'Ao ponto');

insert into order_item (id, order_id, product_id, quantity, unit_price, total_price, observation)
values (5, 2, 7, 1, 89.00, 89.00, null);

insert into order_item (id, order_id, product_id, quantity, unit_price, total_price, observation)
values (6, 3, 1, 1, 78.90, 78.90, null);
