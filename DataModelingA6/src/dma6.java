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
            System.out.println("Oracle JDBC .jar not found!");
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

        System.out.println("");

        AdminClass adminClass = new AdminClass(connection);
        adminClass.mainMenu();

    }
}


class AdminClass {

    private Scanner inputReader = new Scanner(System.in); // for reading input
    private  Connection connection;

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
                getInputFor(1);
            } else if (choice == 2) {
                // create faculty account
                getInputFor(0);
            } else if (choice == 3) {
                // exit

            } else {
                System.out.println("Error, invalid selection please try again");
            }

        }while(choice != 3);
    }

    public void getInputFor(int isAdmin){
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

        // insert
        FacultyRecord facultyObject = new FacultyRecord(validNumber,firstName,lastName,facultyType,isAdmin,passWord);
        boolean result = facultyObject.insertFacultyRecord(connection);
        if(result){
            System.out.println("Successfully created account!");
        }
        System.out.println("Failed to create account");
    }

    public void deleteAccount(){
        System.out.println("Choose an account to delete: ");

        String query = "SELECT * FROM faculty";

        List<FacultyRecord> facultyList = new ArrayList<FacultyRecord>();

        int count = 1;
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                int n_number = rs.getInt("n_number");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String faculty_type = rs.getString("faculty_type");
                int isAdmin = rs.getInt("is_administrator");
                String password = rs.getString("password");

                facultyList.add(new FacultyRecord(n_number, first_name, last_name, faculty_type , isAdmin, password));
                System.out.println( "(" + Integer.toString(count++) + ") : " + first_name + " " + last_name + " [ "+ faculty_type +" ]");
            }

        }catch (Exception e){
            System.out.println("Failed to pull down records: "+ e.getMessage());
            return;
        }

        boolean isValid = false;

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

                String deleteQuery = "DELETE FROM faculty WHERE n_number = " + facultyList.get(choice-1).fac_id;

                try{
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                }catch (Exception e){
                    System.out.println("Error: "+e.getMessage());
                    return;
                }

                System.out.println("Account Deleted: "+facultyList.get(choice-1).first_name+" "+facultyList.get(choice-1).last_name);
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