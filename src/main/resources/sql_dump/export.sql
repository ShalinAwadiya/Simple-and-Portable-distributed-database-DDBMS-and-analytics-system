drop table if exists customers;
create table customers(customerID(int),customerName(varchar(255)),dob(date),pID(int),primary key(customerID),foreign key(pID) references persons(personID);
insert into table customers values (1, nishit, 24-07-1999, 5),(2, harsh, 02-05-1999, 7);

drop table if exists orders;
create table orders(orderID(int),orderName(varchar(255)),dob(date),cID(int),primary key(orderID),foreign key(cID) references customers(customerID);
insert into table orders values (1, nishit, 24-07-1999, 5),(2, harsh, 02-05-1999, 7);

