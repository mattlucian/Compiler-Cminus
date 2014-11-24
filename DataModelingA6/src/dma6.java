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
            // you have a valid connection now to use
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
                for(int i = 1; i < 9; i++){
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
    private  Connection connection;

    AdminClass(){ } // filler constructor
    AdminClass(Connection establishedConnection){
        connection = establishedConnection;
    }

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

        int choice = 0;
        do{
            System.out.println("Which type of Account would you like to create?");
            System.out.println("1. Admin Account");
            System.out.println("2. Faculty Account");
            System.out.println("3. Exit to menu");
            choice = inputReader.nextInt();

            if (choice == 1) {
                // create admin account
                getInputFor(true);
                System.out.println(" Administrator account has been created");
            } else if (choice == 2) {
                // create faculty account
                getInputFor(false);
                System.out.println(" Faculty account has been created");
            } else if (choice == 3) {
                // exit

            } else {
                System.out.println("Error, invalid selection please try again");
            }

        }while(choice != 3);
    }

    public void getInputFor(boolean isAdmin){
        System.out.println("Enter Account n# (exclude the n) and press enter key: ");

        String random = inputReader.nextLine(); // there's a newline character prior to this still in scanner, this eliminates it
        // can fix later

        String nNumber = inputReader.nextLine();
        Integer validNumber = 0;

        boolean validated = false;
        while(!validated){
            try{
                validNumber = Integer.parseInt(nNumber);
                validated = true;
            }catch (Exception e){
                System.out.println("~Error, please enter a valid number");
                nNumber = inputReader.nextLine();
            }
        }

        System.out.println("Enter Account First Name: ");
        String firstName = inputReader.nextLine().trim();

        System.out.println("Enter Account Last Name: ");
        String lastName = inputReader.nextLine().trim();

        System.out.println("Enter Faculty Type (director, etc.): ");
        String facultyType = inputReader.nextLine().trim();

        System.out.println("Enter a password for this account: ");
        String passWord = inputReader.nextLine().trim();

        String query = "INSERT INTO faculty ( n_number, first_name, last_name, is_administrator, password, faculty_type) VALUES (";
        query += ( Integer.toString(validNumber) + " , '" + firstName + "' , '" + lastName + "' , " + Boolean.toString(isAdmin) + " , '" + passWord + "' , '" + facultyType + "')" );

        System.out.println(query);
    }

    public void deleteAccount(){
        System.out.println("Choose an account to delete: ");

        String query = "SELECT n_number,first_name,last_name FROM faculty";

        String firstName = "Matt";
        String lastName = "Myers"; // fillers, can be deleted later
        boolean isValid = false;
        // store in list

        int count = 1;
        // while(rs.next()) {
            System.out.println( "(" + Integer.toString(count) + ") : " + firstName + " " + lastName );
            count++;
        // }

        while(!isValid) {
            int choice = inputReader.nextInt();
            for (int i = 1; i < count; i++) {
                if (choice == i) {
                    isValid = true;
                }
            }
            if (!isValid)
                System.out.println("Invalid choice, please try again.");
            else {
                System.out.println("Account Deleted: "+firstName+" "+lastName);
            }
        }
    }
    //endregion

    //region Reporting Menu Functions
    public void printCourseReport(){


        // while(rs.next()) {



        // }


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

class FacultyRecord{

    public int fac_id;
    public String first_name;
    public String last_name;
    public String fac_type;
    public boolean isAdministrator;
    private String fac_password;

    FacultyRecord(int id, String fName, String lName, String type, boolean administrator, String password){
        fac_id = id; first_name = fName; last_name = lName; fac_type = type; isAdministrator = administrator; fac_password = password;
    }

}