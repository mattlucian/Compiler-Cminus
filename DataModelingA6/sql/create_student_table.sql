
/* Create student table */
CREATE TABLE student(
	n_number NUMBER NOT NULL CONSTRAINT student_id_pk primary key,
	student_name VARCHAR2(50) NULL,
	degree_program VARCHAR2(2) NULL,
	semester VARCHAR2(10) NULL,
	
	
	class_times VARCHAR2(10) NULL,
	Birth_date DATE NULL,
	Gender VARCHAR2(15) NULL,
	Home_phone_number VARCHAR2(20) NULL
);

CREATE TABLE students_courses (
	sc_id NUMBER NOT NULL CONSTRAINT sc_id_pk primary key,
	stature VARCHAR(20) NULL,
	
)

/* 10 inserts into students 
insert into student values (1, 'Matt Myers', '5030 Dusty Rise Trace', 'Leatherman', 'NH', '03734', to_date('12/21/92','MM/DD/YY'), 'Male', '603-363-8920');
insert into student values (2, 'Jennifer Donahue', '3086 Quaking Key', 'Hollsopple', 'NH', '03749', to_date('09/17/89','MM/DD/YY'), 'Female', '603-702-9588');
insert into student values (3, 'Josh Gruel', '4756 Misty Berry Diversion', 'Baltimore', 'NH', '03292', to_date('04/01/78','MM/DD/YY'), 'Male', '603-027-0483');
insert into student values (4, 'Christopher Deal', '4774 Amber Spring Promenade', 'Powhattan', 'HI', '96712', to_date('12/21/92','MM/DD/YY'), 'Male', '808-620-7022');
insert into student values (5, 'Jane Smith', '6188 Green Pike', 'Cherry Box', 'VA', '22105', to_date('11/10/91','MM/DD/YY'), 'Female', '276-711-0474');
insert into student values (6, 'Tracy Adams', '1031 Iron Shadow Chase', 'Comox', 'IA', '52530', to_date('02/18/90','MM/DD/YY'), 'Male', '563-468-1988');
insert into student values (7, 'Darrel Johnson', '3145 Rocky Isle', 'Shiny Rock', 'IA', '51704', to_date('10/01/85','MM/DD/YY'), 'Male', '808-221-8876');
insert into student values (8, 'Ciara Tucker', '453 Honey Alley', 'Wapwallopen', 'HI', '96841', to_date('12/21/92','MM/DD/YY'), 'Female', '603-363-8920');
insert into student values (9, 'Jackie Neveau', '5456 Cotton Willow Hill', 'Idlewild', 'MO', '63679', to_date('08/14/95','MM/DD/YY'), 'Female', '573-975-0266');
insert into student values (10, 'Dennis Franklin', '1630 Amber Wagon Trace', 'Five Pound Island', 'OR', '97023',  to_date('02/28/67','MM/DD/YY'), 'Male','(503) 851-7647');


 Create faculty table 
create table faculty(
	Faculty_id NUMBER NOT NULL constraint Faculty_id_pk primary key,
	First_name VARCHAR2(50) NULL,
	Last_name VARCHAR2(50) NULL,
	Gender VARCHAR2(15) NULL,
	Birth_date DATE NULL,
	Street_address VARCHAR2(40) NULL,
	City VARCHAR2(30) NULL,
	Phone_number VARCHAR2(20) NULL,
	Salary NUMBER(7,2) NULL,
	Area_of_expertise VARCHAR2(100) NULL
);

 10 inserts into faculty
insert into faculty values (1, 'Jan', 'Wurster', 'Female', to_date('12/03/64','MM/DD/YY'), '5500 Colonial Nectar Corner', 'Cupar', '814-405-0197', 40000,'Website Development');
insert into faculty values (2, 'Moti', 'Jackson', 'Male', to_date('04/24/82','MM/DD/YY'), '2925 Umber Parkway', 'Pomonkey Landing', '575-196-4673', 75000,'Mobile Technologies');
insert into faculty values (3, 'Megan', 'Riley', 'Female', to_date('09/30/72','MM/DD/YY'), '350 Wishing Panda Wynd', 'Pelly', '360-679-6236', 60000,'Data Modeling');
insert into faculty values (4, 'Brittany', 'Smith', 'Female', to_date('08/15/73','MM/DD/YY'), '9074 Noble Log Impasse', 'Notown', '313-695-8939', 45000,'Computational Structures');
insert into faculty values (5, 'Mark', 'Kelm', 'Male', to_date('06/17/86','MM/DD/YY'), '8053 Merry Autumn Port', 'Cornwall', '253-530-3372', 55000, 'Computer Hardware');
insert into faculty values (6, 'Jack', 'Robinson', 'Male', to_date('05/06/78','MM/DD/YY'), '7395 Heather Quay', 'Mervin', '872-834-8478', 70000, 'Robotics');
insert into faculty values (7, 'Matthew', 'Whitman', 'Male', to_date('07/25/81','MM/DD/YY'), '7839 Hidden Brook Bank', 'Lulu', '815-871-3835', 65000, 'Artificial Intelligence');
insert into faculty values (8, 'Morgan', 'Lawson', 'Female', to_date('04/30/68','MM/DD/YY'), '4963 Broad Heights', 'Studebaker', '773-018-3128', 50000, 'Statistics');
insert into faculty values (9, 'Hunter', 'Robertson', 'Male', to_date('11/03/75','MM/DD/YY'), '7058 Burning Lane', 'Bummerville', '616-843-4434', 75000, 'Software Engineering');
insert into faculty values (10,'Elle', 'Seuni', 'Female', to_date('02/12/81','MM/DD/YY'), '8513 Princeton Road', 'Clidesdale', '616-518-4891', 95000, 'Information Security');

grant select on faculty to abbassi5dml;
grant select on student to abbassi5dml;
