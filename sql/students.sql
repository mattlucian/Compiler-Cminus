
----------------
-- gets all student information
----------------
 -- @n_number       = n_number of student
SELECT n_number, first_name, last_name, degree FROM student WHERE n_number = @n_number


----------------
-- gets first and last name of student
----------------
 -- @n_number       = n_number of student
SELECT first_name, last_name FROM student WHERE n_number = @n_number

----------------
-- Inserts into students
----------------
 -- @n_number   = n_number of student
 -- @first_name = first_name of student
 -- @last_name  = last_name of student
 -- @degree     = degree of student
INSERT INTO student VALUES(@n_number, @first_name, @last_name, @degree)

----------------
-- Inserts into course request
----------------
 -- @CRN        = CRN for the course that's being requested
 -- @n_number   = N number of student requesting a course
 -- @year       = Year of course requested
 -- @semester   = Semester of course requested
 -- @days_id    = Day ID
 -- @times_id   = Times ID
INSERT INTO course_request VALUES(@CRN, @n_number, @year, @semester, @days_id, @times_id)

----------------
-- Gets a count of records from a table
----------------
 -- @table       = table to get a count from
SELECT COUNT(*) AS solution FROM @table

----------------
-- Gets CRN, Code, number, and the name of a course for a certain semester/year
---------------
 -- @is_odd_year = Odd year or not
 -- @semester    = Semester of course request
SELECT crn,code,course_number,course_name FROM course WHERE semester = @semester AND is_odd_year = @is_odd_year