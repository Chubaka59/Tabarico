insert into role(name, redistribution_rate, salary) values ('Responsable', 40, 30000)
insert into role(name, redistribution_rate, salary) values ('CDI', 35, 20000)


insert into user(first_name, last_name, password, phone, username, admin, role_id) values ('Ramon', 'Cruz', '$2a$10$UCEisteDN7GY7d7voMGYZOeGncH4Jd4daExzPeninBel6v8ybqIzG', '123456789', 'ramon.cruz',1, 1)
insert into user(first_name, last_name, password, phone, username, admin, role_id) values ('Test', 'Test', '$2a$10$UCEisteDN7GY7d7voMGYZOeGncH4Jd4daExzPeninBel6v8ybqIzG', '123456789', 'Test',0, 2)

insert into product(name, clean_money, dirty_money) values ('Cigarette', 70, 35)
insert into product(name, clean_money, dirty_money) values ('Menthol', 90, 60)

insert into contract(company, reduction) values ('Cantina', 10)
insert into contract(company, reduction) values ('Brasserie', 25)