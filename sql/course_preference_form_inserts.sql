-- preference_form
INSERT INTO
    preference_form (preference_form_id, n_number, date_added)
VALUES
    (1, 123456, TO_DATE('2014-12-03', 'YYYY-MM-DD'))

-- form_semester_info
INSERT INTO
    form_semester_info (preference_form_id, n_number, semester, time_of_day_id, days_of_week_id,
        number_of_courses, course_importance, day_importance, time_importance)
VALUES
    (1, 123456, 'Fall', 1, 1, 1, 1, 2, 3);

-- course_ranking
INSERT ALL
    INTO course_ranking (preference_form_id, code, n_number, rank_order)
        VALUES (1, 'CDA3101', 123456, 1)
    INTO course_ranking (preference_form_id, code, n_number, rank_order)
        VALUES (1, 'CIS4253', 123456, 2)
    INTO course_ranking (preference_form_id, code, n_number, rank_order)
        VALUES (1, 'COP3503', 123456, 3)
    INTO course_ranking (preference_form_id, code, n_number, rank_order)
        VALUES (1, 'COP4620', 123456, 4)
    INTO course_ranking (preference_form_id, code, n_number, rank_order)
        VALUES (1, 'COT3210', 123456, 5)
SELECT * FROM DUAL