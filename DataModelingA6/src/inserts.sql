#
# running this script using mysql client:
# :\mysql\bin\mysql --local-infile=1 -u root -p < CreateDatabase.sql
# server start: sudo start mysql
#           or: sudo service mysql start
#

--create database taskdb CHARACTER SET = utf8;

--use taskdb;

insert into student values(47939, 'Chris', 'Raley', 'CS');
insert into student values(67823, 'Amy', 'Smith', 'CS');
insert into student values(82253, 'Fred', 'Roberts', 'CS');
insert into student values(14487, 'Wilbur', 'Williams', 'CS');
insert into student values(58726, 'Samantha', 'Simmons', 'CS');
