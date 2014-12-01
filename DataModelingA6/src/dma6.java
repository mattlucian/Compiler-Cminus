import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by matt on 11/10/14.
    Data Modeling - Group A6
 */

public class dma6 {
    public static void main(String[] args){

        Connection connection;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC .jar not found: " + e.getMessage());
            return;
        }

        // gets connection to olympia
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "teama6dm2f14","team6ccgmpr");
        } catch (SQLException e) {
            System.out.println("Connection to Olympia Failed!");
            return;
        }

        if (connection == null) {
            System.out.println("Failed to establish connection somewhere!");
            return;
        }

//        Admin adminClass = new Admin(connection);
//        adminClass.mainMenu();
//
        Faculty facultyClass = new Faculty(connection);
        facultyClass.mainMenu();


//        String query = "INSERT ALL INTO course_ranking (preference_form_id, code, n_number, rank_order) VALUES (31, 'CAP4630', 123, 1) INTO course_ranking (preference_form_id, code, n_number, rank_order) VALUES (31, 'COT3210', 123, 2) SELECT * FROM DUAL";
//        String query = "SELECT column_name FROM all_tab_cols WHERE table_name='PREFERENCE_FORM'";
//
//        try{
//            try{
//                Statement statement = connection.createStatement();
//                ResultSet rs = statement.executeQuery(query);
//
//                while(rs.next()){
//                        System.out.println(rs.getString("column_name"));
////                    System.out.println( "(" + Integer.toString(count++) + ") : " + first_name + " " + last_name + " [ "+ faculty_type +" ]");
//                }
//
//            }catch (Exception e){
//                System.out.println("Failed to run query: "+ e.getMessage());
//            }
//        }catch (Exception e){
//            System.out.println("Error: "+e.getMessage());
//        }

    }
}