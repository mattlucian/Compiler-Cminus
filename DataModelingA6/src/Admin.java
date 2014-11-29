import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by matt on 11/24/14.
 */
public class Admin {

    //region Properties + Constructor
    private Scanner inputReader = new Scanner(System.in); // for reading input
    private Connection connection;

    /*
     * Admin constructor. Accepts the established
     * connection to Oracle for later user
     */
    Admin(Connection establishedConnection){
        connection = establishedConnection;
    }
    // endregion

    //region Main Menu
    /*
     * Displays the Admin main menu to get input
     */
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
                // destroy session, log out -- move this close to official program exit
                try {
                    connection.close();
                }catch (SQLException e){
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Error, invalid selection please try again");
            }

        }while(choice != 4);
        System.out.println("Logged out, exiting program");
    }
    //endregion

    //region Main Menu Functions
    /*
     * Displays a reporting menu so the admin can choose what type of
     * report they would like to view. Each selection calls the corresponding
     * reporting method
     */
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

    /*
     * Displays a menu to let the admin choose whether to create an Admin
     * account or a standard faculty account
     */
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
                getInputAndInsertFaculty(1);
            } else if (choice == 2) {
                // create faculty account
                getInputAndInsertFaculty(0);
            } else if (choice == 3) {
                // exit

            } else {
                System.out.println("Error, invalid selection please try again");
            }

        }while(choice != 3);
    }

    /*
     *  Gets the input to create either an Admin account or
     *  a standard faculty account. The parameter determines
     *  whether an admin account or faculty account is being created
    */
    public void getInputAndInsertFaculty(int isAdmin){
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

    /*
    *   Allows an Admin to Delete an Account from the Faculty Table
    *   Pulls a list of current faculty members and lets the user
    *   select a choice of which account to delete
    */
    public void deleteAccount(){
        System.out.println("Choose an account to delete: ");

        String query = "SELECT * FROM faculty";

        List<FacultyRecord> facultyList = new ArrayList<FacultyRecord>();

        int count = 1;
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            System.out.println("(0) : Back to Previous Menu");
            while(rs.next()){
                int n_number = rs.getInt("n_number");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String faculty_type = rs.getString("faculty_type");
                int isAdmin = rs.getInt("is_administrator");
                String password = rs.getString("password");
                boolean isAnAdmin = false;
                if(isAdmin == 1)
                    isAnAdmin = true;

                facultyList.add(new FacultyRecord(n_number, first_name, last_name, faculty_type , isAdmin, password));
                System.out.println( "(" + Integer.toString(count++) + ") : " + first_name + " " + last_name + " [ "+ faculty_type+ ((isAnAdmin)?"-(Admin)":"") +" ]");
            }

        }catch (Exception e){
            System.out.println("Failed to pull down records: "+ e.getMessage());
            return;
        }

        boolean isValid = false;

        while(!isValid) {
            int choice = inputReader.nextInt();
            for (int i = 0; i < count; i++) {
                if (choice == i) {
                    isValid = true;
                }
            }
            if (!isValid)
                System.out.println("Invalid choice, please try again.");
            else {
                if(choice == 0){
                    break;
                }
                String deleteQuery = "DELETE FROM faculty WHERE n_number = " + facultyList.get(choice-1).fac_id;

                try{
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(deleteQuery);
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
    /*
    *   Pulls from the Course_Request and Course_Ranking Tables
    *   to Print out a report of all the courses requested by
    *   Faculty and Students. It also gets additional information, such
    *   as the days and times of each request
    */
    public void printCourseReport(){

        // selects distinct courses that have course rankings
        String query = "select distinct c.course_name, c.code "+
                        "from course c "+
                        "inner join course_ranking cr "+
                        "on cr.code = c.code";

        try{
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            System.out.println("--------------");
            while(rs.next()) {
                // select faculty members that have ranked it
                String query2 = "select distinct f.n_number, f.first_name, f.last_name "+
                                "FROM faculty f "+
                                "INNER JOIN course_ranking cr "+
                                " ON f.n_number = cr.n_number "+
                                "WHERE cr.code = '"+rs.getString(2).trim()+"'";
                Statement newstm = connection.createStatement();
                ResultSet innerRS = newstm.executeQuery(query2);
                System.out.println(rs.getString(1)+" ("+rs.getString(2)+")");
                while(innerRS.next()){
                    // prints faculty name + n_number
                    System.out.println("- "+innerRS.getString(2)+" "+innerRS.getString(3));

                    // get all details per faculty
                    String queryFacultyDetails = "select preference_form_id from preference_form where n_number = "+innerRS.getInt(1);
                    Statement anotherStm = connection.createStatement();
                    ResultSet internalRS = anotherStm.executeQuery(queryFacultyDetails);
                    int count = 1;
                    while(internalRS.next()){
                        // prints preference form for particular faculty member
                        System.out.println("-- Preference Form: "+Integer.toString(count));
                        String remainingQuery = "Select * from form_semester_info where preference_form_id = "+internalRS.getInt(1);
                        Statement lastStm = connection.createStatement();
                        ResultSet lastRS = lastStm.executeQuery(remainingQuery);
                        while(lastRS.next()){
                            // prints details per semester for preference form
                            System.out.println("--- Semester: "+lastRS.getString(1));
                            System.out.println("---- Time Of Day: "+lastRS.getInt(4));
                            System.out.println("---- Days of Week: "+lastRS.getInt(5));
                        }
                        count++;
                    }


                }
                System.out.println("--------------");
            }
        }catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }


    /*
    *   Prints out a report of the days requested and
    *   the additional information linked to those days from
    *   the Course_Ranking, Course_RequestPreference_Form,
    *   Form_Semester_Info, Faculty, and Student tables
    */
    public void printDayReport(){
        System.out.println("Test print day report");
    }

    /*
    *   Prints out a report of the times requested and
    *   the additional information linked to those times from
    *   the Course_Ranking, Course_RequestPreference_Form,
    *   Form_Semester_Info, Faculty, and Student tables
    */
    public void printTimeReport(){
        System.out.println("Test print time report");
    }

    /*
    *   Prints out a report of what the faculty requested and
    *   the additional information linked to each faculty member:
    *   Course_Ranking, Course_RequestPreference_Form,
    *   Form_Semester_Info, Faculty, and Student tables
    */
    public void printFacultyReport(){
        System.out.println("Test print faculty report");
    }

   /*
    *   Prints out a report of what the students requested and
    *   the additional information linked to each student:
    *   Course_Ranking, Course_RequestPreference_Form,
    *   Form_Semester_Info, Faculty, and Student tables
    */
    public void printStudentReport(){
        System.out.println("Test print student report");
    }
    //endregion
}
