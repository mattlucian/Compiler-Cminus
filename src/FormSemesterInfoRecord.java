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
    public int semester;
    public int time_of_day_id;
    public int days_of_week_id;
    public int number_of_courses;
    public int course_importance;
    public int day_importance;
    public int time_importance;

    FormSemesterInfoRecord(int id, String semester, int fac_id, int time_of_day_id, int days_of_week_id, int number_of_courses,
                           int course_importance, int day_importance, int time_importance){
        this.preference_form_id = id;
        this.fac_id = fac_id;
        this.time_of_day_id = time_of_day_id;
        this.days_of_week_id = days_of_week_id;
        this.number_of_courses = number_of_courses;
        this.course_importance = course_importance;
        this.day_importance = day_importance;
        this.time_importance = time_importance;
    }

    public boolean insertFormSemesterInfoRecord(Connection establishedConnection){
        String query = "INSERT INTO form_semester_info (n_number) VALUES ( ";
        query += this.fac_id + ")";

        try{
            System.out.println(query);
            Statement statement = establishedConnection.createStatement();
            this.preference_form_id = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

    public static FormSemesterInfoRecord loadOne(Connection establishedConnection, PreferenceFormRecord preference_form, String semester){

        String query = "SELECT preference_form_id, n_number FROM form_semester_info WHERE preference_form_id=";
        query += preference_form.preference_form_id + " AND semester=\"" + semester + "\" LIMIT 1";

        try{

            List<FormSemesterInfoRecord> form_semester_info_list = new ArrayList<FormSemesterInfoRecord>();

            try{
                Statement statement = establishedConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);

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
                return form_semester_info_list.get(0);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        return null;
    }
}
