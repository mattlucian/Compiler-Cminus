
-- Revised Table Structure  11/28/2014
-- Includes Sizes for Numbers

CREATE TABLE course (
       CRN                  NUMBER(7)     NOT NULL,
       code                 VARCHAR2(10)  NULL,
       category             VARCHAR2(3)   NOT NULL,
       course_number        NUMBER(4)     NOT NULL,
       course_name          VARCHAR2(50)  NULL,
       is_required          NUMBER(1)     NULL,
       is_odd_year          NUMBER(1)     NULL,
       semester             VARCHAR2(10)  NULL,
       PRIMARY KEY (CRN)
);


CREATE TABLE faculty (
       n_number             NUMBER(8)     NOT NULL,
       first_name           VARCHAR2(25)  NULL,
       last_name            VARCHAR2(25)  NULL,
       is_administrator     NUMBER(1)     NULL,
       password             VARCHAR2(100) NULL,
       faculty_type         VARCHAR2(20)  NULL,
       PRIMARY KEY (n_number)
);

CREATE TABLE student (
       n_number             NUMBER(8)     NOT NULL,
       first_name           VARCHAR2(25)  NULL,
       last_name            VARCHAR2(25)  NULL,
       degree               VARCHAR2(2)   NULL,
       PRIMARY KEY (n_number),
);

CREATE TABLE preference_form (
       preference_form_id   NUMBER(6)     NOT NULL,
       n_number             NUMBER(8)     NOT NULL,
       date_added           DATE          NULL,
       PRIMARY KEY (preference_form_id, n_number),
       FOREIGN KEY (n_number) REFERENCES faculty
);


CREATE TABLE form_semester_info (
       semester             VARCHAR2(10)  NOT NULL,
       preference_form_id   NUMBER(6)     NOT NULL,
       n_number             NUMBER(8)     NOT NULL,
       time_of_day_id       NUMBER(2)     NULL,
       days_of_week_id      NUMBER(2)     NULL,
       number_of_courses    NUMBER(1)     NULL,
       course_importance    NUMBER(1)     NULL,
       day_importance       NUMBER(1)     NULL,
       time_importance      NUMBER(1)     NULL,
       PRIMARY KEY (semester, preference_form_id, n_number),
       FOREIGN KEY (preference_form_id, n_number) REFERENCES preference_form,
);


CREATE TABLE course_ranking (
       preference_form_id   NUMBER(6)     NOT NULL,
       code                 VARCHAR2(10)  NOT NULL,
       n_number             NUMBER(8)     NOT NULL,
       rank_order           NUMBER(1)     NULL,
       PRIMARY KEY (preference_form_id, code, n_number),
--       FOREIGN KEY (code) REFERENCES course,
       FOREIGN KEY (preference_form_id, n_number) REFERENCES preference_form
);


CREATE TABLE course_request (
       CRN                  NUMBER(7)     NOT NULL,
       n_number             NUMBER(8)     NOT NULL,
       year                 number(4)     NULL;
       semester             VARCHAR(10)   NULL,
       days_id              number(5)     NULL;
       times_id             number(5)     NULL;
       PRIMARY KEY (CRN, n_number, semester), --Composite keys needs 3 attribs for uniqueness
       FOREIGN KEY (n_number) REFERENCES student,
       FOREIGN KEY (CRN) REFERENCES course
);