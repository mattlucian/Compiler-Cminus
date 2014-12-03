







-- REPORTS

  -- Days Report

--Faculty
-----------------------------------------------------------------------------------------------
  -- @semester          = The semester we're checking
  -- @days_of_week_id   = The particular day of the week we're currently checking
select distinct f.first_name, f.last_name, f.n_number
                from faculty f inner join preference_form pf
                on f.n_number = pf.n_number inner join
                form_semester_info fsi on pf.preference_form_id = fsi.preference_form_id
                where fsi.semester = @semester AND fsi.days_of_week_id = @days_of_week_id
------------------------------------------------------------------------------------------------
  -- @n_number          = The n_number of the current person we're checking
  -- @semester          = The semester we're checking
  -- @days_of_week_id   = The particular day of the week we're currently checking
select distinct preference_form_id from form_semester_info where n_number = @n_number
                AND days_of_week_id = @days_of_week_id AND semester = @semester
------------------------------------------------------------------------------------------------
  -- @n_number          = The n_number of the current person we're checking
  -- @p_id              = The id of the current preference_form we're checking
  -- @days_of_week_id   = The particular day of the week we're currently checking
select time_of_day_id, course_importance, day_importance, time_importance from form_semester_info
                where preference_form_id = @p_id AND n_number = @n_number AND days_of_week_id = @days_of_week_id
------------------------------------------------------------------------------------------------
  -- @p_id              = The id of the current preference_form we're checking
select code, rank_order from course_ranking where preference_form_id = @p_id
------------------------------------------------------------------------------------------------

--Student
------------------------------------------------------------------------------------------------
  -- @days_of_week_id   = The particular day of the week we're currently checking
select distinct s.first_name, s.last_name, s.n_number from student s
                inner join course_request cr on s.n_number = cr.n_number
                where cr.days_id = @days_of_week_id
------------------------------------------------------------------------------------------------



select distinct c.course_name, c.code
                from course c
                inner join course_ranking cr
                on cr.code = c.code

select distinct c.course_name, c.code
                from course c
                inner join course_request cr
                on cr.CRN = c.CRN

