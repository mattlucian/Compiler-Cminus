--------------------
-- Get Available Courses
--------------------
SELECT DISTINCT
    code,
    course_number,
    course_name
FROM
    course
GROUP BY
    code,
    course_number,
    course_name
ORDER BY
    code

--------------------
-- Get Current Faculty Member's Course Preference Forms
--------------------
    -- n_number is the N Number of the currently logged in Faculty member
SELECT
    preference_form_id,
    n_number,
    date_added
FROM
    preference_form
WHERE
    n_number = :n_number
ORDER BY
    date_added DESC

--------------------
-- Load Course Rankings For a Course Preference Form
--------------------
    -- DISTINCT is necessary because the courses table can contain multiple records for the same course
    -- LEFT JOIN makes sure we keep all course ranking records
    -- preference_form_id is the ID of the Preference Form we want to load Course Rankings for

SELECT DISTINCT
    course_ranking.code,
    course_ranking.rank_order,
    course.course_number,
    course.course_name
FROM
    course_ranking
LEFT JOIN
    course ON course_ranking.code = course.code
WHERE
    course_ranking.preference_form_id = :preference_form_id
ORDER BY
    course_ranking.rank_order


--------------------
-- Delete Course Rankings to start over
--------------------
    -- preference_form_id is the ID of the Preference Form we want to delete Course Rankings for

DELETE FROM
    course_ranking
WHERE
    preference_form_id = :preference_form_id


--------------------
-- Insert Bulk Course Rankings based on selected entries
--------------------
    -- preference_form_id is the ID of the Preference Form we want to delete Course Rankings for
    -- this query adjusts to the number of course rankings to insert, from 1 to n (up to 5)

INSERT ALL
    INTO course_ranking (preference_form_id, code, n_number, rank_order)
        VALUES (:preference_form_id_1, :code_1, :n_number_1, :rank_order_1)
    -- INTO ...
    INTO course_ranking (preference_form_id, code, n_number, rank_order)
        VALUES (:preference_form_id_n, :code_n, :n_number_n, :rank_order_n)
SELECT * FROM DUAL


--------------------
-- Get Next Available ID for new Preference Form
--------------------
    -- NVL() returns the first non-null of its arguments,
        -- allowing for the case when no course preference forms exist to return a 1

SELECT
    NVL(MAX(preference_form.preference_form_id)+1, 1) AS max_id
FROM
    preference_form


--------------------
-- Insert a New Course Preference Form
--------------------
    -- max_id is the value returned by the Get Next Available ID Query
    -- n_number is the faculty's N Number of whom the form belongs to
    -- today is a string containing the current date
INSERT INTO
    preference_form (preference_form_id, n_number, date_added)
VALUES
    (:max_id, :n_number, TO_DATE(:today, 'YYYY-MM-DD'))

--------------------
-- Insert a New Semester Form Info
--------------------
    -- preference_form_id is the ID of the relevant preference_form
    -- n_number is the faculty's N Number of whom the form belongs to
    -- semester is a string, one of Spring, Summer or Fall

INSERT INTO
    form_semester_info (preference_form_id, n_number, semester)
VALUES
    (:preference_form_id, :n_number, :semester)

--------------------
-- Save Scheduling Factors
--------------------
    -- preference_form_id is the ID of the relevant preference_form
    -- semester is a string, one of Spring, Summer or Fall
    -- importance fields are integer values as ranked by the faculty member

UPDATE form_semester_info
SET
    course_importance=:course_importance,
    day_importance=:day_importance,
    time_importance=:time_importance
WHERE
    preference_form_id = :preference_form_id AND semester = :semester


--------------------
-- Save Course Load Preference
--------------------
    -- preference_form_id is the ID of the relevant preference_form
    -- semester is a string, one of Spring, Summer or Fall
    -- number_of_courses if the course load preference for the semester

UPDATE form_semester_info
SET
    number_of_courses = :number_of_courses
WHERE
    preference_form_id = :preference_form_id AND semester = :semester



--------------------
-- Save Time Of Day Preference
--------------------
    -- preference_form_id is the ID of the relevant preference_form
    -- semester is a string, one of Spring, Summer or Fall
    -- time_of_day_id is the ID of the topmost time of day preference by the faculty member

UPDATE form_semester_info
SET
    time_of_day_id = :time_of_day_id
WHERE
    preference_form_id = :preference_form_id AND semester = :semester


--------------------
-- Save Days of Week Preference
--------------------
    -- preference_form_id is the ID of the relevant preference_form
    -- semester is a string, one of Spring, Summer or Fall
    -- days_of_week_id is the ID of the topmost time of day preference by the faculty member

UPDATE form_semester_info
SET
    days_of_week_id = :days_of_week_id
WHERE
    preference_form_id = :preference_form_id AND semester = :semester

--------------------
-- Load A Single Form Semester Info
--------------------
    -- loads a form semester info record by prefernce form and semester
    -- preference_form_id is the ID of the relevant preference_form
    -- semester is a string, one of Spring, Summer or Fall

SELECT
    preference_form_id,
    n_number,
    time_of_day_id,
    days_of_week_id,
    semester
    number_of_courses,
    course_importance,
    day_importance,
    time_importance
FROM
    form_semester_info
WHERE preference_form_id= :preference_form_id AND semester = :semester


--------------------
-- Load all Form Semester Info records for a Preference Form
--------------------
    -- loads a form semester info record by prefernce form and semester
    -- preference_form_id is the ID of the relevant preference_form
    -- semester is a string, one of Spring, Summer or Fall

SELECT
    preference_form_id,
    n_number,
    time_of_day_id,
    days_of_week_id,
    semester,
    number_of_courses,
    course_importance,
    day_importance,
    time_importance
FROM
    form_semester_info
WHERE
    preference_form_id = :preference_form_id