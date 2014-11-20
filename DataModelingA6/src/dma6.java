import java.sql.*;
import java.util.Scanner;

/**
 * Created by matt on 11/10/14.
    Data Modeling - Group A6
 */

public class dma6 {
    public static void main(String[] args){
        AdminClass adminClass = new AdminClass();
        adminClass.mainMenu();
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
            connection = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "myersm","n48663");
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

class AdminClass {

    private Scanner inputReader = new Scanner(System.in); // for reading input

    //region Main Menu
    public void mainMenu(){
        int choice = 0;
        do{
            System.out.println("Admin Menu:");
            System.out.println("1. Reports");
            System.out.println("2. Create Faculty/Admin Account");
            System.out.println("3. Delete Faculty/Admin Account");
            System.out.println("4. Log out");
            choice = inputReader.nextInt();

            if (choice == 1) {
                reportingMenu();
            } else if (choice == 2) {
                createAccount();
            } else if (choice == 3) {
                deleteAccount();
            } else if (choice == 4) {
              // destroy session, log out

            } else {
                System.out.println("Error, invalid selection please try again");
            }

        }while(choice != 4);
        System.out.println("Logged out, exiting program");
    }
    //endregion

    //region Main Menu Functions
    public void reportingMenu(){
        int choice = 0;
        do{
            System.out.println("Reporting Menu:");
            System.out.println("1. Course Report");
            System.out.println("2. Days Report");
            System.out.println("3. Times Report");
            System.out.println("4. Faculty Report");
            System.out.println("5. Student Report");
            System.out.println("6. Back to Admin Menu");
            choice = inputReader.nextInt();

            if (choice == 1) {
                printCourseReport();
            } else if (choice == 2) {
                printDayReport();
            } else if (choice == 3) {
                printTimeReport();
            } else if (choice == 4) {
                printFacultyReport();
            } else if (choice == 5) {
                printStudentReport();
            } else if (choice == 6) {
                // exit loop, goes back to admin
            } else {
                System.out.println("Error, invalid selection please try again");
            }

        }while(choice != 6);
        return;
    }

    public void createAccount(){
        System.out.println("Test create account");
    }

    public void deleteAccount(){
        System.out.println("Test delete account");
    }
    //endregion

    //region Reporting Menu Functions
    public void printCourseReport(){
        System.out.println("Test print course report");
    }

    public void printDayReport(){
        System.out.println("Test print day report");
    }

    public void printTimeReport(){
        System.out.println("Test print time report");
    }

    public void printFacultyReport(){
        System.out.println("Test print faculty report");
    }

    public void printStudentReport(){
        System.out.println("Test print student report");
    }
    //endregion
}
