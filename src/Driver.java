/**
 *
 * 
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;
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
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    public Driver(){
        super();
    }
    
    /*
        The main method of the Driver class is the entry point into this application

        It begins by authenticating users to show them the appropriate menus.
     */
    public static void main(String[] args) {
        Authentication auth = new Authentication(conn);
    }
}
