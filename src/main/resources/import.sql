insert into role(name, redistribution_rate, salary) values ('Responsable', 40, 30000)
insert into role(name, redistribution_rate, salary) values ('CDI', 35, 20000)


insert into user(first_name, last_name, password, phone, username, admin, role_id) values ('Ramon', 'Cruz', '$2a$10$UCEisteDN7GY7d7voMGYZOeGncH4Jd4daExzPeninBel6v8ybqIzG', '123456789', 'ramon.cruz',1, 1)
insert into user(first_name, last_name, password, phone, username, admin, role_id) values ('Test', 'Test', '$2a$10$UCEisteDN7GY7d7voMGYZOeGncH4Jd4daExzPeninBel6v8ybqIzG', '123456789', 'Test',0, 2)

insert into product(name, clean_money, dirty_money, stock) values ('Cigarette', 70, 35, 1000)
insert into product(name, clean_money, dirty_money, stock) values ('Menthol', 90, 60, 100)

insert into contract(company, reduction) values ('Cantina', 10)
insert into contract(company, reduction) values ('Brasserie', 25)

insert into stock(date, type_of_stock_movement, product_id, operation_stock, quantity, stock, user_id) values ('2025-06-26', 1, 1, 1, 100, 1000, 1)