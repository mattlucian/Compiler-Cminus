







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
  -- @n_number          = The n_number of the user we're checking
  -- @days_of_week_id   = The particular day of the week we're currently checking
select distinct c.course_name, c.code
                from course c
                inner join course_request cr
                on cr.CRN = c.CRN
                where cr.n_number = @n_number AND cr.days_id = @days_of_week_id
------------------------------------------------------------------------------------------------


-- Times Report
  -- Faculty
------------------------------------------------------------------------------------------------
  -- @semester          = The semester we're checking
  -- @time_of_day_id    = the id for the time of the day we're checking
select distinct f.first_name, f.last_name, f.n_number
                  from faculty f inner join preference_form pf
                  on f.n_number = pf.n_number inner join
                  form_semester_info fsi on pf.preference_form_id = fsi.preference_form_id
                  where fsi.semester = @semester AND fsi.time_of_day_id = @time_of_day_id
------------------------------------------------------------------------------------------------
  -- @n_number          = n_number of the current faculty member we're checking
  -- @semester          = The semester we're checking
  -- @time_of_day_id    = the id for the time of the day we're checking
select distinct preference_form_id from form_semester_info where n_number = @n_number
                   AND time_of_day_id = @time_of_day_id AND semester = @semester
------------------------------------------------------------------------------------------------
  -- @preference_form_id= The current form ID that we're grabbing the rankings from
select code, rank_order from course_ranking where preference_form_id = @preference_form_id
------------------------------------------------------------------------------------------------

  -- Students
------------------------------------------------------------------------------------------------
-- @time_of_day_id    = the id for the time of the day we're checking
select distinct s.first_name, s.last_name,
                s.n_number from student s
                inner join course_request cr
                on s.n_number = cr.n_number
                where cr.times_id = @time_of_day_id
------------------------------------------------------------------------------------------------
  -- @n_number          = n_number of the current student we're checking
  -- @time_of_day_id    = the id for the time of the day we're checking
select c.code, c.course_name, cr.days_id
                from course c inner join course_request cr
                on c.CRN = cr.CRN " +
                where cr.n_number = @n_number AND times_id = @time_of_day_id



 --------- faculty report
 ------------------------------------------------------------------------------------------------
 select distinct f.n_number, f.first_name, f.last_name
                 FROM faculty f
                 INNER JOIN course_ranking cr
                 ON f.n_number = cr.n_number
------------------------------------------------------------------------------------------------
  -- @n_number          = n_number of the current student we're checking
select preference_form_id from preference_form where n_number = @n_number
------------------------------------------------------------------------------------------------
  -- @preference_form_id= The current form ID that we're grabbing the rankings from
Select time_of_day_id, days_of_week_id, number_of_courses, course_importance, day_importance, time_importance, semester
                   from form_semester_info where preference_form_id = @preference_form_id
------------------------------------------------------------------------------------------------
  -- @preference_form_id= The current form ID that we're grabbing the rankings from
  -- @n_number          = n_number of the current student we're checking
select code, rank_order from course_ranking where preference_form_id = @preference_form_id AND n_number = @n_number



-------- student report
------------------------------------------------------------------------------------------------
select first_name, last_name, n_number from student
------------------------------------------------------------------------------------------------
  -- @n_number          = n_number of the current student we're checking
select semester, year from course_request where n_number = @n_number
------------------------------------------------------------------------------------------------
  -- @n_number          = n_number of the current student we're checking
  -- @semester          = The semester we're checking
  -- @year              = The year that we're checking
select c.code, c.course_name from course c
                 inner join course_request cr on cr.CRN = c.CRN " +
                 where cr.semester = @semester AND cr.n_number = @n_number AND cr.year = @year
------------------------------------------------------------------------------------------------
  -- @code       = refers to the class code that we're checking faculty for
select distinct f.first_name, f.last_name from faculty f
                 inner join course_ranking cr on f.n_number = cr.n_number
                 inner join course c on cr.code = c.code
                 where cr.code = @code




--- faculty
------------------------------------------------------------------------------------------------
select distinct c.course_name, c.code
               from course c
               left outer join course_ranking crank
               on crank.code = c.code
-----------------------------------------------------------------------------------------------------
  -- @code       = refers to the class code that we're checking faculty for
select distinct f.first_name, f.last_name, f.n_number" +
                        from faculty f inner join course_ranking cr
                        on f.n_number = cr.n_number where cr.code = @code

-----------------------------------------------------------------------------------------------------
  -- @n_number          = n_number of the current student we're checking
  -- @code       = refers to the class code that we're checking faculty for
select preference_form_id
                       from course_ranking where code = @code AND n_number = @n_number

-----------------------------------------------------------------------------------------------------
  -- @preference_form_id= ID of the current preference form we're getting details from
  -- @n_number          = n_number of the faculty member we're checking
select time_of_day_id, days_of_week_id,
                              course_importance, day_importance, time_importance, semester
                              from form_semester_info where preference_form_id = @preference_form_id AND n_number = @n_number


------- students
-----------------------------------------------------------------------------------------------------
select distinct c.course_name, c.code
                    from course c
                    inner join course_request cr
                    on cr.CRN = c.CRN
-----------------------------------------------------------------------------------------------------
  -- @code       = refers to the class code that we're checking faculty for
select distinct s.first_name, s.last_name, s.n_number
                        from student s inner join course_request cr
                        on s.n_number = cr.n_number inner join course c
                        on cr.CRN = c.CRN where c.code = @code
-----------------------------------------------------------------------------------------------------
  -- @n_number          = n_number of the current student we're checking
  -- @code       = refers to the class code that we're checking faculty for
select cr.semester, cr.days_id, cr.times_id, cr.year " +
                        from course_request cr inner join course c
                        on cr.CRN = c.CRN where c.code = @code AND cr.n_number = @n_number






---- delete
-- shows all faculty to select one to delete
SELECT * FROM faculty

  -- @n_number          = n_number of faculty member we're deleting
DELETE FROM faculty WHERE n_number = @n_number


---- insert
 -- @n_number         = n_number of faculty member to insert
 -- @first_name       = first name of faculty member
 -- @last_name        = last name of faculty member
 -- @is_administrator = determines whether or not has admin rights to view reports / delete / create accounts
 -- @password         = stores the user's password
 -- @faculty_type     = Stores the type of faculty member the user is
INSERT INTO faculty (n_number, first_name, last_name, is_administrator, password, faculty_type )
VALUES ( @n_number, @first_name, @last_name, @is_administrator, @password, @faculty_type )