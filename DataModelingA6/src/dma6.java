import java.sql.*;
import java.util.Scanner;


/**
 * Created by matt on 11/10/14.
    Data Modeling - Group A6
 */

public class dma6 {

    public static void main(String[] args){

        Scheduler scheduler = new Scheduler();
        int input = 1;
        while(input == 1){
            System.out.println("Press 1 to print example query, 2 to exit ");
            Scanner sc = new Scanner(System.in);
            if((input = sc.nextInt()) == 1){
                scheduler.printQuery();
            }else{
            }


        }
    }

}

class Scheduler {

    private Connection connection = null;

    Scheduler(){

        // registers driver for use
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC .jar not found!");
            return;
        }

        // gets connection to olympia
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "myersm","n48663");
        } catch (SQLException e) {
            System.out.println("Connection to Olympia Failed!");
            return;
        }

        if (connection == null) {
            System.out.println("Failed to establish connection somewhere!");
            // exit
        }else{
            System.out.println("Made it!");
        }
    }

    public void printQuery(){
        String query = "SELECT * FROM student";

        Statement stmt = null;
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                for(int i = 1; i < 10; i++){
                    // can do rs.getString("COLUMN NAME")
                    System.out.print(rs.getString(i)+"\t");
                }
                System.out.print("\n");
            }
        }catch(SQLException e){
            System.out.println("Query ERROR: "+e.getMessage());
        }
    }
}
