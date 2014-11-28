/**
 * Created by matt on 11/27/14.
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestQuery {

    private Connection con;

    TestQuery(Connection connection){
        con = connection;
    }

    /*
     * Accepts entire Query string and returns a result set
     * -- I only created this because SQL Plus has terrible
     * -- formatting when trying to view contents of a table
     */
    ResultSet query(String query){
        try{
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            return rs;
        }catch (SQLException e){
            System.out.println("SQL Error: "+e.getMessage());
            return null;
        }
    }
}
