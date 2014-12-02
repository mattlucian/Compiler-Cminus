import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 11/29/14.
 */
public class FormSemesterInfoRecord {
    public int preference_form_id;
    public int fac_id;
    public Semester semester;
    public int time_of_day_id;
    public int days_of_week_id;
    public int number_of_courses;
    public int course_importance;
    public int day_importance;
    public int time_importance;

    FormSemesterInfoRecord(int id, Semester semester, int fac_id, int time_of_day_id, int days_of_week_id, int number_of_courses,
                           int course_importance, int day_importance, int time_importance){
        this.preference_form_id = id;
        this.semester = semester;
        this.fac_id = fac_id;
        this.time_of_day_id = time_of_day_id;
        this.days_of_week_id = days_of_week_id;
        this.number_of_courses = number_of_courses;
        this.course_importance = course_importance;
        this.day_importance = day_importance;
        this.time_importance = time_importance;
    }

    public boolean insertFormSemesterInfoRecord(Connection establishedConnection){
        String query = "INSERT INTO form_semester_info (preference_form_id, n_number, semester) VALUES ( ";
        query += this.preference_form_id + ", " + this.fac_id + ", '" + this.semester + "' )";

        try{
            Statement statement = establishedConnection.createStatement();
            statement.executeUpdate(query);
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

    public boolean saveSchedulingFactors(Connection establishedConnection, int course_importance, int day_importance, int time_importance){
        String query = "UPDATE form_semester_info " +
                "SET course_importance=" + course_importance + ", day_importance=" + day_importance + ", time_importance=" + time_importance +
                " WHERE preference_form_id = " + this.preference_form_id + " AND semester='" + this.semester + "'";

        try{
            Statement statement = establishedConnection.createStatement();
            statement.executeUpdate(query);
            this.course_importance = course_importance;
            this.day_importance = day_importance;
            this.time_importance = time_importance;
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

    public boolean saveNumberOfCourses(Connection establishedConnection, int number_of_courses){
        String query = "UPDATE form_semester_info " +
                "SET number_of_courses=" + number_of_courses +
                " WHERE preference_form_id = " + this.preference_form_id + " AND semester='" + this.semester + "'";

        try{
            Statement statement = establishedConnection.createStatement();
            statement.executeUpdate(query);
            this.number_of_courses = number_of_courses;
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

    public boolean saveTimeOfDay(Connection establishedConnection, int time_of_day_id){
        String query = "UPDATE form_semester_info " +
                "SET time_of_day_id=" + time_of_day_id +
                " WHERE preference_form_id = " + this.preference_form_id + " AND semester='" + this.semester + "'";

        try{
            Statement statement = establishedConnection.createStatement();
            statement.executeUpdate(query);
            this.time_of_day_id = time_of_day_id;
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
    public boolean saveDaysOfWeek(Connection establishedConnection, int days_of_week_id){
        String query = "UPDATE form_semester_info " +
                "SET days_of_week_id=" + days_of_week_id +
                " WHERE preference_form_id = " + this.preference_form_id + " AND semester='" + this.semester + "'";

        try{
            Statement statement = establishedConnection.createStatement();
            statement.executeUpdate(query);
            this.days_of_week_id = days_of_week_id;
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

    public static FormSemesterInfoRecord loadOne(Connection establishedConnection, PreferenceFormRecord preference_form, Semester semester){

        String query = "SELECT preference_form_id, n_number, time_of_day_id, days_of_week_id, semester, " +
                "number_of_courses, course_importance, day_importance, time_importance FROM form_semester_info WHERE preference_form_id=";
        query += preference_form.preference_form_id + " AND semester='" + semester + "'";

        List<FormSemesterInfoRecord> form_semester_info_list = new ArrayList<FormSemesterInfoRecord>();

        try{
            Statement statement = establishedConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            try{
                while(rs.next()){
                    int preference_form_id = rs.getInt("preference_form_id");
                    int n_number = rs.getInt("n_number");
                    int time_of_day_id = rs.getInt("time_of_day_id");
                    int days_of_week_id = rs.getInt("days_of_week_id");
                    int number_of_courses = rs.getInt("number_of_courses");
                    int course_importance = rs.getInt("course_importance");
                    int day_importance = rs.getInt("day_importance");
                    int time_importance = rs.getInt("time_importance");

                    form_semester_info_list.add(new FormSemesterInfoRecord(preference_form_id, semester, n_number, time_of_day_id, days_of_week_id,
                            number_of_courses, course_importance, day_importance, time_importance));
//                    System.out.println( "(" + Integer.toString(count++) + ") : " + first_name + " " + last_name + " [ "+ faculty_type +" ]");
                }

            }catch (Exception e){
                System.out.println("Failed to pull down records: "+ e.getMessage());
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

        if(form_semester_info_list.size()>0)
        {
            //if results were found, return the first one
            return form_semester_info_list.get(0);
        }
        else
        {
            //if no results were found, create one
            FormSemesterInfoRecord form_semester_info = new FormSemesterInfoRecord(preference_form.preference_form_id, semester, preference_form.fac_id, 0, 0, 0, 0, 0, 0);
            if(form_semester_info.insertFormSemesterInfoRecord(establishedConnection))
            {
                return form_semester_info;
            }
            else
            {
                return null;
            }
        }
    }

    public static ArrayList<FormSemesterInfoRecord> loadByPreferenceForm(Connection establishedConnection, PreferenceFormRecord preference_form){

        String query = "SELECT preference_form_id, n_number, time_of_day_id, days_of_week_id, semester, " +
                "number_of_courses, course_importance, day_importance, time_importance FROM form_semester_info WHERE preference_form_id=";
        query += preference_form.preference_form_id;

        ArrayList<FormSemesterInfoRecord> form_semester_info_list = new ArrayList<FormSemesterInfoRecord>();

        try{
            Statement statement = establishedConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            try{
                while(rs.next()){
                    int preference_form_id = rs.getInt("preference_form_id");
                    int n_number = rs.getInt("n_number");
                    Semester semester = Semester.valueOf(rs.getString("semester"));
                    int time_of_day_id = rs.getInt("time_of_day_id");
                    int days_of_week_id = rs.getInt("days_of_week_id");
                    int number_of_courses = rs.getInt("number_of_courses");
                    int course_importance = rs.getInt("course_importance");
                    int day_importance = rs.getInt("day_importance");
                    int time_importance = rs.getInt("time_importance");

                    form_semester_info_list.add(new FormSemesterInfoRecord(preference_form_id, semester, n_number, time_of_day_id, days_of_week_id,
                            number_of_courses, course_importance, day_importance, time_importance));
//                    System.out.println( "(" + Integer.toString(count++) + ") : " + first_name + " " + last_name + " [ "+ faculty_type +" ]");
                }

            }catch (Exception e){
                System.out.println("Failed to pull down records: "+ e.getMessage());
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

        if(form_semester_info_list.size()>0)
        {
            //if results were found, return them
            return form_semester_info_list;
        }
        else
        {
            return null;
        }
    }

    public void display()
    {
        System.out.println("Semester: " + this.semester);
        System.out.println("\tNumber of Courses: " + this.number_of_courses);
        System.out.println("\tDays of Week: " + Faculty.days[this.days_of_week_id]);
        System.out.println("\tTime of Day: " + Faculty.times[this.time_of_day_id]);
        System.out.println("\tCourse Importance: " + this.course_importance);
        System.out.println("\tDay Importance: " + this.day_importance);
        System.out.println("\tTime Importance: " + this.time_importance);
        System.out.println("");
    }
}
