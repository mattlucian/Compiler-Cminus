

CREATE TABLE course (
       CRN                  NUMBER NOT NULL,
       code                 VARCHAR2(10) NULL,
       category             VARCHAR2(3) NOT NULL,
       course_number               NUMBER NOT NULL,
       course_name                 VARCHAR2(30) NULL,
       is_required          NUMBER NULL,
       is_odd_year          NUMBER NULL,
       semester             VARCHAR2(10) NULL,
       PRIMARY KEY (CRN)
);


CREATE TABLE faculty (
       n_number             NUMBER NOT NULL,
       first_name           VARCHAR2(25) NULL,
       last_name            VARCHAR2(25) NULL,
       is_administrator     NUMBER NULL,
       password             VARCHAR2(100) NULL,
       faculty_type         VARCHAR2(20) NULL,
       PRIMARY KEY (n_number)
);


CREATE TABLE days_of_week (
       days_of_week_id      NUMBER NOT NULL,
       semester             VARCHAR2(10) NULL,
       days                 VARCHAR2(5) NULL,
       PRIMARY KEY (days_of_week_id)
);


CREATE TABLE time_of_day (
       time_of_day_id       NUMBER NOT NULL,
       timeslot             VARCHAR2(15) NULL,
       PRIMARY KEY (time_of_day_id)
);


CREATE TABLE student (
       n_number             NUMBER NOT NULL,
       days_of_week_id      NUMBER NULL,
       time_of_day_id       NUMBER NULL,
       first_name           VARCHAR2(25) NULL,
       last_name            VARCHAR2(25) NULL,
       degree               VARCHAR2(2) NULL,
       semester             VARCHAR2(10) NULL,
       year                 NUMBER NULL,
       PRIMARY KEY (n_number), 
       FOREIGN KEY (days_of_week_id)
                             REFERENCES days_of_week, 
       FOREIGN KEY (time_of_day_id)
                             REFERENCES time_of_day
);


CREATE TABLE preference_form (
       preference_form_id   NUMBER NOT NULL,
       n_number             NUMBER NOT NULL,
       date_added           DATE NULL,
       PRIMARY KEY (preference_form_id, n_number), 
       FOREIGN KEY (n_number)
                             REFERENCES faculty
);


CREATE TABLE form_semester_info (
       semester             VARCHAR2(10) NOT NULL,
       preference_form_id   NUMBER NOT NULL,
       n_number             NUMBER NOT NULL,
       time_of_day_id       NUMBER NULL,
       days_of_week_id      NUMBER NULL,
       number_of_courses    NUMBER NULL,
       course_importance    NUMBER NULL,
       day_importance       NUMBER NULL,
       time_importance      NUMBER NULL,
       PRIMARY KEY (semester, preference_form_id, n_number), 
       FOREIGN KEY (preference_form_id, n_number)
                             REFERENCES preference_form, 
       FOREIGN KEY (time_of_day_id)
                             REFERENCES time_of_day, 
       FOREIGN KEY (days_of_week_id)
                             REFERENCES days_of_week
);


CREATE TABLE course_ranking (
       preference_form_id   NUMBER NOT NULL,
       CRN                  NUMBER NOT NULL,
       n_number             NUMBER NOT NULL,
       rank_order           NUMBER NULL,
       PRIMARY KEY (preference_form_id, CRN, n_number), 
       FOREIGN KEY (CRN)
                             REFERENCES course, 
       FOREIGN KEY (preference_form_id, n_number)
                             REFERENCES preference_form
);


CREATE TABLE course_request (
       CRN                  NUMBER NOT NULL,
       n_number             NUMBER NOT NULL,
       PRIMARY KEY (CRN, n_number), 
       FOREIGN KEY (n_number)
                             REFERENCES student, 
       FOREIGN KEY (CRN)
                             REFERENCES course
);



