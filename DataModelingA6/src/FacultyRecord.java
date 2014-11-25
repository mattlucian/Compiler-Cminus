import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by matt on 11/24/14.
 */
public class FacultyRecord {
    public int fac_id;
    public String first_name;
    public String last_name;
    public String fac_type;
    public int isAdministrator;
    private String fac_password;

    FacultyRecord(int id, String fName, String lName, String type, int administrator, String password){
        fac_id = id; first_name = fName; last_name = lName; fac_type = type; isAdministrator = administrator; fac_password = password;
    }

    public boolean insertFacultyRecord(Connection establishedConnection){
        String query = "INSERT INTO faculty (n_number, first_name, last_name, is_administrator, password, faculty_type ) VALUES ( ";
        query += this.fac_id+" , '"+ this.first_name +"' , '" + this.last_name + "' , " + this.isAdministrator + " , '" + this.fac_password + "' , '" + this.fac_type +"' )";

        try{
            System.out.println(query);
            Statement statement = establishedConnection.createStatement();
            statement.executeUpdate(query);
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

}
