insert into role(name, redistribution_rate, salary) values ('testRole', 40, 30000)
insert into role(name, redistribution_rate, salary) values ('testRoleToUpdate', 40, 30000)
insert into role(name, redistribution_rate, salary) values ('testRoleToDelete', 40, 30000)


insert into users(first_name, last_name, password, phone, username, admin, role_id, holiday, warning1, warning2, quota, exporter_quota) values ('Test', 'User', '$2a$10$UCEisteDN7GY7d7voMGYZOeGncH4Jd4daExzPeninBel6v8ybqIzG', '123456789', 'testUser', 1, 1, false, false, false, true, true)
insert into users(first_name, last_name, password, phone, username, admin, role_id, holiday, warning1, warning2, quota, exporter_quota) values ('TestToUpdate', 'UserToUpdate', '$2a$10$UCEisteDN7GY7d7voMGYZOeGncH4Jd4daExzPeninBel6v8ybqIzG', '123456789', 'testUserToUpdate', 1, 1, false, false, false, true, true)
insert into users(first_name, last_name, password, phone, username, admin, role_id, holiday, warning1, warning2, quota, exporter_quota) values ('TestToDelete', 'UserToDelete', '$2a$10$UCEisteDN7GY7d7voMGYZOeGncH4Jd4daExzPeninBel6v8ybqIzG', '123456789', 'testUserToDelete', 1, 1, false, false, false, true, true)

insert into product(name, clean_money, dirty_money, stock) values ('testProduct', 70, 35, 2000)
insert into product(name, clean_money, dirty_money, stock) values ('testProductToUpdate', 70, 35, 0)
insert into product(name, clean_money, dirty_money, stock) values ('testProductToDelete', 70, 35, 0)

insert into contract(company, reduction) values ('testCompany', 10)
insert into contract(company, reduction) values ('testCompanyToUpdate', 10)
insert into contract(company, reduction) values ('testCompanyToDelete', 10)

insert into customer_dirty_sale_rate(id, customer_dirty_sale_rate) values (1, 35)