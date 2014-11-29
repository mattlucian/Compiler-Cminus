/**
 *
 * 
 *
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Compile:  javac -cp ojdbc6.jar:. Driver.java
 * Run:      java  -cp ojdbc6.jar:. Driver
 * Designed to run on Osprey with oracle jdbc driver jar file.
 */
public class Driver {
    private static final String dbURL = "jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl";
    private static final String db_username = "teama6dm2f14";
    private static final String db_password = "team6ccgmpr";
    
    private static Connection conn;
    
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(dbURL, db_username, db_password);
        } catch ( Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    public Driver(){
        super();
    }
    
    //Testing
    public static void main(String[] args) {
        
        //Simulating login: acquired n-Number
        String number = "n00045673";
        
        //keep this instantiated as a singleton
        StudentController controller = new StudentController(conn);//delivers connection
        
        //everytime the student option selected, call this method:
        controller.startSession(number);

//        TestQuery ts = new TestQuery(conn);
//        String query = "SELECT * FROM course";
//        ResultSet rs = ts.query(query);
//        try{
//            while(rs.next()){
//                System.out.println("CRN: "+rs.getString(1));
//                System.out.println("Code: "+rs.getString(2));
//                System.out.println("Category: "+rs.getString(3));
//                System.out.println("Course#: "+rs.getString(4));
//                System.out.println("CourseName: "+rs.getString(5));
//                System.out.println("Is_Required: "+rs.getString(6));
//                System.out.println("Is_Odd_Year: "+rs.getString(7));
//                System.out.println("Semester: "+rs.getString(8));
//                System.out.println("---------------");
//            }
//            conn.close();
//        }catch (SQLException ex){
//            System.out.println("Error: "+ex.getMessage());
//        }
       // Admin admin = new Admin(conn);
    //    admin.mainMenu();
    }
}
