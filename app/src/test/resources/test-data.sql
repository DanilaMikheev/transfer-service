create table account(id varchar(20), amount integer, clientid integer);
insert into account values ('40817810123456789011',1000,1);
insert into account values ('40817810123456789012',1000,2);

create table transfer(id varchar(36),accFrom varchar(20), accTo varchar(20), amount bigint, status integer, updated timestamp);
insert into transfer values ('d9bbad7e0cea42b9bcd933ae524230db','40817810123456789011','40817810123456789012',100,0,null);
