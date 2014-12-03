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
    private String[] times = {"Morning (9am-12pm)","Afternoon (12pm-4pm)","Evening (4pm-9pm)"};
    private String[] days = {"MW","TR","MWF","MWTRF"};

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

        System.out.println("-----------");
        System.out.println("| Faculty |");
        System.out.println("-----------");
        //region Faculty Portion
        // selects distinct courses that have course rankings
        String query = "select distinct c.course_name, c.code "+
                        "from course c "+
                        "inner join course_ranking cr "+
                        "on cr.code = c.code";

        try{
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);
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
                            System.out.println("---- Time Of Day: "+times[lastRS.getInt(4)]);
                            System.out.println("---- Days of Week: "+days[lastRS.getInt(5)]);
                        }
                        count++;
                    }
                }
                System.out.println("--------------");
            }
        }catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        //endregion

        System.out.println("------------");
        System.out.println("| Students |");
        System.out.println("------------");
        //region Student Portion
        // selects distinct courses that have course requests
        String query3 = "select distinct c.course_name, c.code "+
                "from course c "+
                "inner join course_request cr "+
                "on cr.CRN = c.CRN ";
        List<String> foundCourses = new ArrayList<String>();

        try{
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query3);
            while(rs.next()) {
                if(foundCourses.contains(rs.getString(2))){ // if already has course


                }else{
                    foundCourses.add(rs.getString(2)); // add code to found
                    System.out.println(rs.getString(1)+" ("+rs.getString(2)+")");

                }
                // grab student information linking to the course request
                String query2 = "select distinct s.n_number, s.first_name, s.last_name, cr.semester, cr.year, cr.days_id, cr.times_id "+
                        "FROM student s "+
                        "INNER JOIN course_request cr "+
                        " ON s.n_number = cr.n_number " +
                        "INNER JOIN course c " +
                        " ON cr.CRN = c.CRN "+
                        "WHERE c.code = '"+rs.getString(2)+"'";

                Statement stmt2 = connection.createStatement();
                ResultSet rs2 = stmt2.executeQuery(query2);
                while(rs2.next()){
                    // prints student name
                    System.out.println("- "+rs2.getString(2)+" "+rs2.getString(3));

                    // get all details pertaining to student
                    System.out.println("-- "+rs2.getString(4)+" "+rs2.getString(5));
                    System.out.println("--- "+days[rs2.getInt(6)]+" "+times[rs2.getInt(7)]);
                }
                System.out.println("--------------");
            }
        }catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        //endregion

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

        String getFaculty = "select f.first_name, f.last_name " +
                "from faculty f " +
                "inner join " +
                "inner join course_ranking cr " +
                " on f.n_number = cr.n_number " +
                "where cr.semester = '";


        System.out.println("Fall Semester");
        //region Fall Semester
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getFaculty+"Fall'");

        }catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
        }

        //endregion

        System.out.println("Spring Semester");
        //region Spring Semester
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getFaculty+"Spring'");

        }catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
        }

        //endregion

        System.out.println("Summer Semester");
        //region Summer Semester
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getFaculty+"Summer'");
            while(rs.next()){
                System.out.println(" - "+rs.getString(1)+" "+rs.getString(2));




            }


        }catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
        }

        //endregion
    }

   /*
    *   Prints out a report of what the students requested and
    *   the additional information linked to each student:
    *   Course_Ranking, Course_RequestPreference_Form,
    *   Form_Semester_Info, Faculty, and Student tables
    */
    public void printStudentReport(){

        //region Fall Semester
        System.out.println("Fall Semester:");
        String queryForFall = "select distinct s.first_name, s.last_name, s.n_number, cr.days_id, cr.times_id "+
                "FROM student s " +
                "INNER JOIN course_request cr " +
                " ON s.n_number = cr.n_number " +
                "WHERE cr.semester = 'Fall'";
        try{
            int count = 1;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(queryForFall);
            while(rs.next()){
                System.out.println(count+++": "+rs.getString(1)+" "+rs.getString(2)+" ("+rs.getString(3)+")");
                System.out.println("-- "+days[rs.getInt(4)]+" | "+times[rs.getInt(5)]);
                String queryToGetClasses = "select c.course_name, c.code from course c " +
                        "inner join course_request cr " +
                        "on c.CRN = cr.CRN " +
                        "where cr.n_number = "+rs.getInt(3);
                Statement stm2 = connection.createStatement();
                ResultSet rs2 = stm2.executeQuery(queryToGetClasses);
                // for each class
                while(rs2.next()){
                    System.out.println("-- "+rs2.getString(1)+" ("+rs2.getString(2)+")");

                    String queryToGetFacultyDetails = "select f.first_name, f.last_name " +
                            "from faculty f " +
                            "inner join course_ranking cr " +
                            "on f.n_number = cr.n_number " +
                            "where cr.code = '"+rs2.getString(2)+"'";
                    Statement stm3 = connection.createStatement();
                    ResultSet rs3 = stm3.executeQuery(queryToGetFacultyDetails);

                    while(rs3.next()){
                        System.out.println("--- Instructed By: "+rs3.getString(1)+" "+rs3.getString(2));
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("Error getting Fall: "+e.getMessage());
        }
        //endregion

        //region Spring Semester
        System.out.println("Spring Semester:");
        String queryForSpring = "select distinct s.first_name, s.last_name, s.n_number, cr.days_id, cr.times_id "+
                "FROM student s " +
                "INNER JOIN course_request cr " +
                " ON s.n_number = cr.n_number " +
                "WHERE cr.semester = 'Spring'";
        try{
            int count =1;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(queryForSpring);
            while(rs.next()){
                System.out.println(count+++": "+rs.getString(1)+" "+rs.getString(2)+" ("+rs.getString(3)+")");
                System.out.println("-- "+days[rs.getInt(4)]+" | "+times[rs.getInt(5)]);
                String queryToGetClasses = "select c.course_name, c.code from course c " +
                        "inner join course_request cr " +
                        "on c.CRN = cr.CRN " +
                        "where cr.n_number = "+rs.getInt(3);
                Statement stm2 = connection.createStatement();
                ResultSet rs2 = stm2.executeQuery(queryToGetClasses);
                // for each class
                while(rs2.next()){
                    System.out.println("-- "+rs2.getString(1)+" ("+rs2.getString(2)+")");

                    String queryToGetFacultyDetails = "select f.first_name, f.last_name " +
                            "from faculty f " +
                            "inner join course_ranking cr " +
                            "on f.n_number = cr.n_number " +
                            "where cr.code = '"+rs2.getString(2)+"'";
                    Statement stm3 = connection.createStatement();
                    ResultSet rs3 = stm3.executeQuery(queryToGetFacultyDetails);

                    while(rs3.next()){
                        System.out.println("--- Instructed By: "+rs3.getString(1)+" "+rs3.getString(2));
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("Error getting Fall: "+e.getMessage());
        }


        //endregion

        //region Summer Semester
        String queryForSummer = "select distinct s.first_name, s.last_name, s.n_number, cr.days_id, cr.times_id "+
                "FROM student s " +
                "INNER JOIN course_request cr " +
                " ON s.n_number = cr.n_number " +
                "WHERE cr.semester = 'Spring'";

        try{
            int count = 1;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(queryForSummer);
            while(rs.next()){
                System.out.println(count+++": "+rs.getString(1)+" "+rs.getString(2)+" ("+rs.getString(3)+")");
                System.out.println("-- "+days[rs.getInt(4)]+" | "+times[rs.getInt(5)]);
                String queryToGetClasses = "select c.course_name, c.code from course c " +
                        "inner join course_request cr " +
                        "on c.CRN = cr.CRN " +
                        "where cr.n_number = "+rs.getInt(3);
                Statement stm2 = connection.createStatement();
                ResultSet rs2 = stm2.executeQuery(queryToGetClasses);
                // for each class
                while(rs2.next()){
                    System.out.println("-- "+rs2.getString(1)+" ("+rs2.getString(2)+")");

                    String queryToGetFacultyDetails = "select f.first_name, f.last_name " +
                            "from faculty f " +
                            "inner join course_ranking cr " +
                            "on f.n_number = cr.n_number " +
                            "where cr.code = '"+rs2.getString(2)+"'";
                    Statement stm3 = connection.createStatement();
                    ResultSet rs3 = stm3.executeQuery(queryToGetFacultyDetails);

                    while(rs3.next()){
                        System.out.println("--- Instructed By: "+rs3.getString(1)+" "+rs3.getString(2));
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("Error getting Fall: "+e.getMessage());
        }


        //endregion

    }
    //endregion
}
