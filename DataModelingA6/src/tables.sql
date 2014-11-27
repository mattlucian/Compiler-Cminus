
CREATE TABLE course (
       CRN                  NUMBER(6) NOT NULL, --size added
       code                 VARCHAR2(10) NULL,
       category             VARCHAR2(3) NOT NULL,
       course_number        NUMBER(5) NOT NULL, --size added
       course_name          VARCHAR2(50) NULL,  --size increased from 30 to 50
       is_required          NUMBER(2) NULL, --size added
       is_odd_year          NUMBER(2) NULL, --size added
       semester             VARCHAR2(10) NULL,
       PRIMARY KEY (CRN)
);


CREATE TABLE faculty (
       n_number             NUMBER(8) NOT NULL, --size added
       first_name           VARCHAR2(25) NULL,
       last_name            VARCHAR2(25) NULL,
       is_administrator     NUMBER(2) NULL, --size added
       password             VARCHAR2(100) NULL,
       faculty_type         VARCHAR2(20) NULL,
       PRIMARY KEY (n_number)
);

--Not Needed anymore
-- CREATE TABLE days_of_week (
--        days_of_week_id      NUMBER NOT NULL,
--        semester             VARCHAR2(10) NULL,
--        days                 VARCHAR2(5) NULL,
--        PRIMARY KEY (days_of_week_id)
-- );
-- 
-- 
-- CREATE TABLE time_of_day (
--        time_of_day_id       NUMBER NOT NULL,
--        timeslot             VARCHAR2(15) NULL,
--        PRIMARY KEY (time_of_day_id)
-- );


CREATE TABLE student (
       n_number             NUMBER(8) NOT NULL, --size added
       first_name           VARCHAR2(25) NULL,
       last_name            VARCHAR2(25) NULL,
       degree               VARCHAR2(2) NULL,
       PRIMARY KEY (n_number), 
       --FOREIGN KEY (days_of_week_id) REFERENCES days_of_week, --These dont exist anymore
       --FOREIGN KEY (time_of_day_id) REFERENCES time_of_day
);


CREATE TABLE preference_form (
       preference_form_id   NUMBER NOT NULL, --NEEDS SIZES
       n_number             NUMBER(8) NOT NULL, --size added
       date_added           DATE NULL,
       PRIMARY KEY (preference_form_id, n_number), 
       FOREIGN KEY (n_number) REFERENCES faculty
);


CREATE TABLE form_semester_info (
       semester             VARCHAR2(10) NOT NULL,
       preference_form_id   NUMBER NOT NULL, --NEEDS SIZES
       n_number             NUMBER NOT NULL, --NEEDS SIZES
       time_of_day_id       NUMBER(2) NULL, --size added
       days_of_week_id      NUMBER(2) NULL, --size added
       number_of_courses    NUMBER NULL, --NEEDS SIZES
       course_importance    NUMBER NULL, --NEEDS SIZES
       day_importance       NUMBER NULL, --NEEDS SIZES
       time_importance      NUMBER NULL, --NEEDS SIZES
       PRIMARY KEY (semester, preference_form_id, n_number), 
       FOREIGN KEY (preference_form_id, n_number) REFERENCES preference_form, 
       --FOREIGN KEY (time_of_day_id) REFERENCES time_of_day, 
       --FOREIGN KEY (days_of_week_id) REFERENCES days_of_week  --These dont exist anymore
);


CREATE TABLE course_ranking (
       preference_form_id   NUMBER NOT NULL, --NEEDS SIZES
       CRN                  NUMBER NOT NULL, --NEEDS SIZES
       n_number             NUMBER NOT NULL, --NEEDS SIZES
       rank_order           NUMBER NULL,  --NEEDS SIZES
       PRIMARY KEY (preference_form_id, CRN, n_number), 
       FOREIGN KEY (CRN) REFERENCES course, 
       FOREIGN KEY (preference_form_id, n_number) REFERENCES preference_form
);


CREATE TABLE course_request (
       CRN                  NUMBER(6) NOT NULL, --size added
       n_number             NUMBER(8) NOT NULL, --size added
       semester             VARCHAR(10) NULL,
       year                 number(4) null; --changed
       days_id              number(2) null; --changed
       times_id             number(2) null;  --changed
       PRIMARY KEY (CRN, n_number, semester), --Composite keys needs 3 attribs for uniqueness
       FOREIGN KEY (n_number) REFERENCES student, 
       FOREIGN KEY (CRN) REFERENCES course
);