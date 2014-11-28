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
