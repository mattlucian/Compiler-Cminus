import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nick on 11/29/14.
 */
public class PreferenceFormRecord {
    public int preference_form_id;
    public int fac_id;
    public Date date_added;
    public ArrayList<Course> courseRankings = new ArrayList<Course>();

    PreferenceFormRecord(int fac_id){
        this.preference_form_id = -1;
        this.fac_id = fac_id;
    }

    PreferenceFormRecord(int id, int fac_id){
        this.preference_form_id = id;
        this.fac_id = fac_id;
        this.date_added = null;
    }

    PreferenceFormRecord(int id, int fac_id, Date date_added){
        this(id, fac_id);
        this.date_added = date_added;
    }

    public ArrayList<Course> loadCourseRankings(Connection establishedConnection)
    {
        ArrayList<Course> courseRankings = new ArrayList<Course>();
        String query = "SELECT DISTINCT course_ranking.code, course_ranking.rank_order, course.course_number, course.course_name FROM course_ranking " +
                "LEFT JOIN course ON course_ranking.code = course.code " +
                "WHERE course_ranking.preference_form_id=" +
                this.preference_form_id + " ORDER BY course_ranking.rank_order";

        Statement statement = null;
        ResultSet rs = null;
        try{
            statement = establishedConnection.createStatement();
            rs = statement.executeQuery(query);
            try{
                while(rs.next()){

                    int crn = -1;
                    String code = rs.getString("code");
                    int courseNumber = rs.getInt("course_number");
                    String courseName = rs.getString("course_name");

                    courseRankings.add(new Course(crn, code, courseNumber, courseName));
//                    System.out.println( "(" + Integer.toString(count++) + ") : " + first_name + " " + last_name + " [ "+ faculty_type +" ]");
                }

                this.courseRankings = courseRankings;
                return this.courseRankings;

            }catch (Exception e){
                System.out.println("Failed to pull down records: "+ e.getMessage());
                return courseRankings;
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        } finally {
            clean(rs, statement);
        }
        return null;
    }

    public boolean saveCourseRankings(Connection establishedConnection,
                                   ArrayList<Course> courseRankings)
    {
        //remove old course rankings and save new course rankings
        String query = "DELETE FROM course_ranking WHERE preference_form_id = " + this.preference_form_id;
        Statement statement = null;
        ResultSet rs = null;
        try{
            statement = establishedConnection.createStatement();
            rs = statement.executeQuery(query);
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        } finally {
            clean(rs, statement);
        }

        query = "INSERT ALL ";
        for(int i = 0; i < courseRankings.size(); i++)
        {
            query += "INTO course_ranking (preference_form_id, code, n_number, rank_order) VALUES (" +
                    this.preference_form_id + ", " +
                    "'" + courseRankings.get(i).getCode() + "', " +
                    this.fac_id + ", " +
                    (i+1) +
                    ") ";

        }
        query += "SELECT * FROM DUAL";

        try{
            statement = establishedConnection.createStatement();
            statement.executeUpdate(query);
            this.courseRankings = courseRankings;
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        } finally {
            clean(rs, statement);
        }
    }

    public boolean insertPreferenceFormRecord(Connection establishedConnection){
        String query = "SELECT NVL(MAX(preference_form.preference_form_id)+1, 1) AS max_id FROM preference_form";
        int max_id = -1;
        Statement statement = null;
        ResultSet rs = null;
        try{
            statement = establishedConnection.createStatement();
            rs = statement.executeQuery(query);

            if(rs.next()){
                max_id = rs.getInt("max_id");
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        } finally {
            clean(rs, statement);
        }

        if(max_id == -1)
        {
            System.out.println("Error: Could not load max id");
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        Date today = new Date();
        query = "INSERT INTO preference_form (preference_form_id, n_number, date_added) VALUES ( ";
        query += max_id + ", " + this.fac_id + ", TO_DATE('" + dateFormat.format(today) + "', 'YYYY-MM-DD') )";

        statement = null;
        try{
            statement = establishedConnection.createStatement();
            statement.executeUpdate(query);
            this.preference_form_id = max_id;
            this.date_added = today;
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        } finally {
            clean(null, statement);
        }
    }

    public void display(Connection establishedConnection)
    {
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY");

        System.out.println("------------------------------");
        System.out.println("| Course Preference Form #" + this.preference_form_id + " |");
        System.out.println("------------------------------");

        if(this.date_added != null)
            System.out.println("Date Added: " + dateFormat.format(this.date_added));
        else
            System.out.println("Date Added: Unknown");

        //Display Courses Ranked
        this.loadCourseRankings(establishedConnection);
        System.out.println("Courses Ranked: " + this.courseRankings.size());
        for(int i = 0; i < this.courseRankings.size(); i++)
        {
            System.out.println((i+1) + ": " + this.courseRankings.get(i).getCode() + " - " + this.courseRankings.get(i).getCourseName());
        }
        System.out.println("");

        //Display Semester Info Records
        ArrayList<FormSemesterInfoRecord> semester_info_records = FormSemesterInfoRecord.loadByPreferenceForm(establishedConnection, this);
        if(semester_info_records != null)
        {
            for(int i = 0; i < semester_info_records.size(); i++)
            {
                semester_info_records.get(i).display();
            }
        }
    }

    private static void clean(ResultSet rset, Statement stmt){
        try {
            if(rset != null) rset.close();
            if(stmt != null) stmt.close();
        } catch (SQLException se) { }
    }
}
