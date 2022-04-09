drop table if exists TEST
Create table TEST(oid  int,name string,id string,cid  string,primary key (oid), foreign key (cid) references CUSTOMER(id));
drop table if exists ORDER
Create table ORDER(oid  int,name string,id string,cid  string,primary key (oid), foreign key (cid) references CUSTOMER(id));
drop table if exists PEOPLE
Create table PEOPLE(dob date,name string,id string,primary key (id));
INSERT INTO PEOPLE VALUES (01-01-2000,"Harsh","0111"),(13-02-1996,"Meghdoot","0112");
drop table if exists CUSTOMER
Create table CUSTOMER(dob date,name string,id string,pid string,primary key (id), foreign key (pid) references PEOPLE(id));
INSERT INTO Customer VALUES ("fasfa","Harsh","0111"),("fasfa","Harsh","0111"),("fasfa","Shalin","0111"),("fasfa","Nishit","0111");
