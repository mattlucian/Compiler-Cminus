import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 11/29/14.
 */
public class PreferenceFormRecord {
    public int preference_form_id;
    public int fac_id;

    PreferenceFormRecord(int fac_id){
        this.preference_form_id = -1;
        this.fac_id = fac_id;
    }

    PreferenceFormRecord(int id, int fac_id){
        this.preference_form_id = id;
        this.fac_id = fac_id;
    }

    public boolean insertPreferenceFormRecord(Connection establishedConnection){
        String query = "SELECT NVL(MAX(preference_form.preference_form_id)+1, 1) AS max_id FROM preference_form";
        int max_id = -1;
        try{
            Statement statement = establishedConnection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                max_id = rs.getInt("max_id");
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }

        if(max_id == -1)
        {
            System.out.println("Error: Could not load max id");
            return false;
        }

        query = "INSERT INTO preference_form (preference_form_id, n_number) VALUES ( ";
        query += max_id + ", " + this.fac_id + ")";

        try{
            System.out.println(query);
            Statement statement = establishedConnection.createStatement();
            statement.executeUpdate(query);
            this.preference_form_id = max_id;
            return true;
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

    public static PreferenceFormRecord loadById(Connection establishedConnection, int preference_form_id){

        String query = "SELECT preference_form_id, n_number FROM preference_form WHERE preference_form_id=";
        query += preference_form_id + " LIMIT 1";

        try{
            List<PreferenceFormRecord> preference_form_list = new ArrayList<PreferenceFormRecord>();

            try{
                Statement statement = establishedConnection.createStatement();
                ResultSet rs = statement.executeQuery(query);

                while(rs.next()){
                    preference_form_id = rs.getInt("preference_form_id");
                    int n_number = rs.getInt("n_number");
                    String password = rs.getString("password");

                    preference_form_list.add(new PreferenceFormRecord(preference_form_id, n_number));
//                    System.out.println( "(" + Integer.toString(count++) + ") : " + first_name + " " + last_name + " [ "+ faculty_type +" ]");
                }

            }catch (Exception e){
                System.out.println("Failed to pull down records: "+ e.getMessage());
                return preference_form_list.get(0);
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        return null;
    }
}
