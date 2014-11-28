
----------------------
-- Gets Courses Ranked By Faculty
----------------------
-- c    = course
-- cr   = course_ranking
-- f    = faculty
-- pf   = preference_form
SELECT c.code,
       c.course_name,
       f.first_name,
       f.last_name,
FROM course c
INNER JOIN course_ranking cr
  ON cr.CRN = c.CRN
INNER JOIN  faculty f
  ON cr.n_number = f.n_number
INNER JOIN preference_form pf
  ON cr.n_number = pf.n_number;
 -------------------------

----------------------
-- Gets Courses Requested By Students
----------------------
-- c  = course
-- cr = course_request
-- s  = student
SELECT c.code,
       c.course_name,
       s.first_name,
       s.last_name,
FROM course c
INNER JOIN course_request cr
  ON cr.CRN = c.CRN
INNER JOIN  student s
  ON cr.n_number = s.n_number
 -------------------------
