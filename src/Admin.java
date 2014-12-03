import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
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
    public static String[] days = {"None", "MW", "TR", "MWF", "MTWRF", "Summer C(12 weeks) MW",
            "Summer C(12 weeks) TR", "Summer A(6 weeks) MWTR", "Summer B(6 weeks) MWTR", "8 week MWF",
            "8 week TR", "6 week MW+4 Fridays", "6 week TR+4 Fridays"};
    private String[] semesters = {"Fall","Spring","Summer"};

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
            System.out.println("-----------");
            System.out.println("[1] Reporting Menu");
            System.out.println("[2] Create Faculty/Admin Account");
            System.out.println("[3] Delete Faculty/Admin Account");
            System.out.println("[4] Log out");
            System.out.print(">>  ");
            choice = inputReader.nextInt();

            if (choice == 1) {
                reportingMenu();
            } else if (choice == 2) {
                createAccount();
            } else if (choice == 3) {
                deleteAccount();
            } else if (choice == 4) {
                return;
            } else {
                System.out.println("===========Please enter valid selection.===========");
            }

        }while(choice != 4);
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
            System.out.println("----------------");
            System.out.println("[1] Course Report");
            System.out.println("[2] Days Report");
            System.out.println("[3] Times Report");
            System.out.println("[4] Faculty Report");
            System.out.println("[5] Student Report");
            System.out.println("[6] Back to Admin Menu");
            System.out.print(">>  ");

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
                System.out.println("===========Please enter valid selection.===========");
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
            System.out.println("What type of account would you like to create?");
            System.out.println("[1] Admin Account");
            System.out.println("[2] Faculty Account");
            System.out.println("[3] Back to menu");
            System.out.print(">>  ");
            choice = inputReader.nextInt();
            String consumesEnterCharacter = inputReader.nextLine(); // necessary

            if (choice == 1) {
                // create admin account
                getInputAndInsertFaculty(1);
            } else if (choice == 2) {
                // create faculty account
                getInputAndInsertFaculty(0);
            } else if (choice == 3) {
                // exit

            } else {
                System.out.println("===========Please enter valid selection.===========");
            }

        }while(choice != 3);
    }

    /*
     *  Gets the input to create either an Admin account or
     *  a standard faculty account. The parameter determines
     *  whether an admin account or faculty account is being created
    */
    public void getInputAndInsertFaculty(int isAdmin){
        System.out.println("Enter n# (i.e n00748663): ");
        System.out.print(">>  ");

        String nNumber = inputReader.nextLine();
        if(nNumber.toUpperCase().startsWith("N")){
            nNumber = nNumber.substring(1);
        }
        Integer validNumber = 0;

        boolean validated = false;
        while(!validated){
            try{
                validNumber = Integer.parseInt(nNumber);
                validated = true;
            }catch (Exception e){
                System.out.println("===========Please enter valid number.===========");
                nNumber = inputReader.nextLine();
            }
        }

        System.out.println("Enter Account First Name: ");
        System.out.print(">> ");
        String firstName = inputReader.nextLine().trim();

        System.out.println("Enter Account Last Name: ");
        System.out.print(">> ");
        String lastName = inputReader.nextLine().trim();

        System.out.println("Enter Faculty Type (director, etc.): ");
        System.out.print(">> ");
        String facultyType = inputReader.nextLine().trim();

        System.out.println("Enter a password for this account: ");
        System.out.print(">> ");
        String passWord = inputReader.nextLine().trim();

        // insert
        FacultyRecord facultyObject = new FacultyRecord(validNumber,firstName,lastName,facultyType,isAdmin,passWord);
        boolean result = facultyObject.insertFacultyRecord(connection);
        if(result){
            System.out.println("Successfully created account!");
        }else {
            System.out.println("Failed to create account");
        }
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

            System.out.println("[0] : Back to Previous Menu");
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
                System.out.println( "[" + Integer.toString(count++) + "] : " + first_name + " " + last_name + " [ "+ faculty_type+ ((isAnAdmin)?"-(Admin)":"") +" ]");
            }
            System.out.print(">> ");
            clean(rs,statement);
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
                System.out.println("===========Please enter valid selection.===========");
            else {
                if(choice == 0){
                    break;
                }
                String deleteQuery = "DELETE FROM faculty WHERE n_number = " + facultyList.get(choice-1).fac_id;

                try{
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(deleteQuery);
                    statement.close();
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
    public void printCourseReport() {

        System.out.println("Course Report");
        System.out.println("--------------");

        //region GET ALL COURSES for faculty
        String selectAllCourses = "select distinct c.course_name, c.code " +
                " from course c " +
                " left outer join course_ranking crank " +
                " on crank.code = c.code ";

        try {
            Statement courseStatement = connection.createStatement();
            ResultSet courseResult = courseStatement.executeQuery(selectAllCourses);

            int course_count=1;
            System.out.println("\tFaculty");
            System.out.println("\t-------");
            while (courseResult.next()) {

                String course_name = courseResult.getString(1);
                String course_code = courseResult.getString(2);
                System.out.println(" ["+course_count+++"] "+course_name+" ("+course_code+")");

                String getAllFacultyForCourse = "select distinct f.first_name, f.last_name, f.n_number" +
                        " from faculty f inner join course_ranking cr" +
                        " on f.n_number = cr.n_number where cr.code = '"+course_code+"'";

                Statement facultyStatement = connection.createStatement();
                ResultSet facultyResult = facultyStatement.executeQuery(getAllFacultyForCourse);

                //region GETS ALL FACULTY & PREFERENCE FORMS
                int count = 1;
                while (facultyResult.next()) {

                    System.out.println("\t ["+count+++"] " + facultyResult.getString(1) + " " + facultyResult.getString(2));

                    String getAllPreferenceFormsWithCourse = "select preference_form_id " +
                            " from course_ranking where code = '" + course_code + "' AND n_number = "+facultyResult.getInt(3);
                    Statement preferenceFormStatement = connection.createStatement();
                    ResultSet preferenceFormResult = preferenceFormStatement.executeQuery(getAllPreferenceFormsWithCourse);

                    while (preferenceFormResult.next()) {
                        int preference_form_id = preferenceFormResult.getInt(1);

                        String getFormDetails = "select time_of_day_id, days_of_week_id,  " +
                                " course_importance, day_importance, time_importance, semester " +
                                " from form_semester_info where preference_form_id = " + preference_form_id+" AND n_number = "+facultyResult.getInt(3);
                        Statement detailStatement = connection.createStatement();
                        ResultSet detailResult = detailStatement.executeQuery(getFormDetails);

                        while (detailResult.next()) {
                            int time_id = detailResult.getInt(1) - 1;
                            int day_id = detailResult.getInt(2) - 1;
                            int course_importance = detailResult.getInt(3);
                            int day_importance = detailResult.getInt(4);
                            int time_importance = detailResult.getInt(5);
                            String semester = detailResult.getString(6);

                            System.out.println("\t\t  " + semester + " | " + days[day_id] + " | " + times[time_id]);
                            System.out.println("\t\t  Preferences: " + " Course[" + course_importance + "]" + " Day[" + day_importance + "]" + " Time[" + time_importance + "]");

                        }
                        clean(detailResult,detailStatement);
                    }
                    clean(preferenceFormResult,preferenceFormStatement);
                }
                clean(facultyResult, facultyStatement);
                //endregion
            }
            clean(courseResult, courseStatement);


            int count = 1;
            String getallcoursesforstudents = "select distinct c.course_name, c.code " +
                    " from course c " +
                    " inner join course_request cr " +
                    " on cr.CRN = c.CRN ";

            Statement courseResult2 = connection.createStatement();
            ResultSet studResultSet = courseResult2.executeQuery(getallcoursesforstudents);

            System.out.println("------------------------------");
            System.out.println("\tStudents");
            System.out.println("\t--------");
            while(studResultSet.next()){
                System.out.println(" ["+count+++"] "+studResultSet.getString(1)+" ("+studResultSet.getString(2)+")");
                String course_code = studResultSet.getString(2);
                //region GET ALL STUDENTS
                String getAllStudentsWithCourseRequests = "select distinct s.first_name, s.last_name, s.n_number " +
                        " from student s inner join course_request cr " +
                        " on s.n_number = cr.n_number inner join course c " +
                        " on cr.CRN = c.CRN where c.code = '"+course_code+"'";

                Statement studentStatement = connection.createStatement();
                ResultSet studentResult = studentStatement.executeQuery(getAllStudentsWithCourseRequests);

                count = 1;
                while (studentResult.next()){
                    System.out.println("\t ["+count+++"] "+studentResult.getString(1)+" "+studentResult.getString(2));
                    String getAllRequests = "select cr.semester, cr.days_id, cr.times_id, cr.year " +
                            " from course_request cr inner join course c " +
                            " on cr.CRN = c.CRN where c.code = '"+course_code+"' AND cr.n_number = "+studentResult.getInt(3);
                    Statement requestsStatement = connection.createStatement();
                    ResultSet requestResults = requestsStatement.executeQuery(getAllRequests);

                    while(requestResults.next()){
                        String semester = requestResults.getString(1);
                        int day_id = requestResults.getInt(2)-1;
                        int time_id = requestResults.getInt(3)-1;
                        int year = requestResults.getInt(4);

                        System.out.println("\t\t"+semester+" "+year+" | "+days[day_id]+" | "+times[time_id]);

                    }
                    clean(requestResults,requestsStatement);

                }
                clean(studentResult,studentStatement);
                //endregion


            }



        } catch (Exception e) {
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

        int j = -1;

        int summerBeginIndex = 4;
        int summerEndIndex = 12;
        int springBeginIndex = 1;
        int springEndIndex = 3;

        while(true){
            System.out.println("Days Report");
            System.out.println("------------");


            System.out.println("Please make a selection: ");
            System.out.println("[0] Back to Reporting Menu");
            System.out.println("[1] Fall Semester: ");
            System.out.println("[2] Spring Semester: ");
            System.out.println("[3] Summer Semester: ");

            j = getChoice(3);

            if(j == 0) break;

            int actualBegin = 0;
            int actualEnd = 0;
            if(j == 1 || j == 2){
                actualBegin = springBeginIndex;
                actualEnd = springEndIndex;
            }else{
                actualBegin = summerBeginIndex;
                actualEnd = summerEndIndex;
            }

            System.out.println("\t"+semesters[j-1]+" Semester");
            System.out.println("\t------------------");
            for(int i = actualBegin; i <= actualEnd; i++){
                System.out.println("\t\t"+days[i]);
                System.out.println("\t\t--------");

                //region Faculty Days
                System.out.println("\t\t\tFaculty");
                System.out.println("\t\t\t--------");

                String selectFac = "select distinct f.first_name, f.last_name, f.n_number " +
                        " from faculty f inner join preference_form pf " +
                        " on f.n_number = pf.n_number inner join " +
                        " form_semester_info fsi on pf.preference_form_id = fsi.preference_form_id " +
                        " where fsi.semester = '"+semesters[j-1]+"' AND fsi.days_of_week_id = "+i;


//                String selectAllFacultyMembersThatHaveForms = "select distinct f.first_name, f.last_name," +
//                        " f.n_number, fsi.time_of_day_id, fsi.course_importance, fsi.day_importance, fsi.time_importance, fsi.preference_form_id " +
//                        " from faculty f inner join preference_form pf " +
//                        " on f.n_number = pf.n_number inner join form_semester_info fsi " +
//                        " on pf.preference_form_id = fsi.preference_form_id " +
//                        " where fsi.days_of_week_id = "+i+" AND fsi.semester = '"+semesters[j]+"'";

                try{
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(selectFac);
                    int count = 1;
                    while(rs.next()){ // prints names
                        System.out.println("\t\t\t["+count+++"] "+rs.getString(1)+" "+rs.getString(2));
                        int n_number = rs.getInt(3);

                        // gets forms that have that time
                        String getForms = "select distinct preference_form_id from form_semester_info where n_number = "+n_number+" " +
                                "AND days_of_week_id = "+i+" AND semester = '"+semesters[j-1]+"'";
                        Statement formsStatement = connection.createStatement();
                        ResultSet formsResult = formsStatement.executeQuery(getForms);

                        while(formsResult.next()){
                            int p_id = formsResult.getInt(1);
                            String getDetails = "select time_of_day_id, course_importance, day_importance, time_importance from form_semester_info " +
                                    " where preference_form_id = "+p_id+" AND n_number = "+n_number+" AND days_of_week_id = "+i;

                            Statement detailsStatement = connection.createStatement();
                            ResultSet detailsResult = detailsStatement.executeQuery(getDetails);
                            while(detailsResult.next()){
                                System.out.println("\t\t\t\t"+times[detailsResult.getInt(1)-1]);
                                System.out.println("\t\t\t\tImportance Ranks: Time("+detailsResult.getInt(4)+")"+" Day("+detailsResult.getInt(3)+")"+" Course("+detailsResult.getInt(2)+")");

                            }
                            clean(detailsResult,detailsStatement);


                            // gets courses
                            String getAllRankedCourses = "select code, rank_order from course_ranking where preference_form_id = "+p_id;
                            Statement stmt2 = connection.createStatement();
                            ResultSet rs2 = stmt2.executeQuery(getAllRankedCourses);

                            System.out.println("\t\t\t\tCourses:");
                            while(rs2.next()){
                                System.out.println("\t\t\t\t\tRank "+rs2.getInt(2)+": "+rs2.getString(1));
                            }
                            clean(rs2,stmt2);


                        }
                        clean(formsResult,formsStatement);
                    }
                    clean(rs,stmt);
                }catch (Exception e){
                    System.out.println("Error: "+e.getMessage());
                }
                //endregion

                //region Student Days
                System.out.println("\t\t\tStudents");
                System.out.println("\t\t\t---------");

                String getAllStudentsWithTime = "select distinct s.first_name, s.last_name, " +
                        " s.n_number from student s " +
                        " inner join course_request cr " +
                        " on s.n_number = cr.n_number " +
                        " where cr.days_id = "+i;

                try{
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(getAllStudentsWithTime);

                    // for each student with a time
                    int count = 1;
                    while(rs.next()){
                        System.out.println("\t\t\t"+count+++": "+rs.getString(1)+" "+rs.getString(2));
                        String getAllClassesForStudent = "select c.code, c.course_name, cr.times_id" +
                                " from course c inner join course_request cr " +
                                " on c.CRN = cr.CRN " +
                                " where cr.n_number = "+rs.getInt(3) + " AND cr.days_id = "+i;

                        Statement stmt2 = connection.createStatement();
                        ResultSet rs2 = stmt2.executeQuery(getAllClassesForStudent);

                        // get all classes for each student
                        System.out.println("\t\t\t\tCourses:");
                        while(rs2.next()){
                            System.out.println("\t\t\t\t\t"+rs2.getString(2)+" ("+rs2.getString(1)+") | "+times[rs2.getInt(3)-1]);
                        }
                        clean(rs2,stmt2);
                    }

                }catch(Exception e){
                    System.out.println("Error: "+ e.getMessage());
                }


                //endregion
            }
        }




    }

    /*
    *   Prints out a report of the times requested and
    *   the additional information linked to those times from
    *   the Course_Ranking, Course_RequestPreference_Form,
    *   Form_Semester_Info, Faculty, and Student tables
    */
    public void printTimeReport(){

        System.out.println("Times Report");
        System.out.println("------------");
        for(int j = 0; j < 3; j++){

            System.out.println("\t"+semesters[j]+" Semester");
            System.out.println("\t------------------");

            for(int i = 1; i <= 3; i++){
                System.out.println("\t\t"+times[i-1]);
                System.out.println("\t\t--------");

                //region Faculty Days
                System.out.println("\t\t\tFaculty");
                System.out.println("\t\t\t--------");

                String selectFac = "select distinct f.first_name, f.last_name, f.n_number " +
                        " from faculty f inner join preference_form pf " +
                        " on f.n_number = pf.n_number inner join " +
                        " form_semester_info fsi on pf.preference_form_id = fsi.preference_form_id " +
                        " where fsi.semester = '"+semesters[j]+"' AND fsi.time_of_day_id = "+i;


//                String selectAllFacultyMembersThatHaveForms = "select distinct f.first_name, f.last_name," +
//                        " f.n_number, fsi.time_of_day_id, fsi.course_importance, fsi.day_importance, fsi.time_importance, fsi.preference_form_id " +
//                        " from faculty f inner join preference_form pf " +
//                        " on f.n_number = pf.n_number inner join form_semester_info fsi " +
//                        " on pf.preference_form_id = fsi.preference_form_id " +
//                        " where fsi.days_of_week_id = "+i+" AND fsi.semester = '"+semesters[j]+"'";

                try{
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(selectFac);
                    int count = 1;
                    while(rs.next()){ // prints names
                        System.out.println("\t\t\t["+count+++"] "+rs.getString(1)+" "+rs.getString(2));
                        int n_number = rs.getInt(3);

                        // gets forms that have that time
                        String getForms = "select distinct preference_form_id from form_semester_info where n_number = "+n_number+" " +
                                "AND time_of_day_id = "+i+" AND semester = '"+semesters[j]+"'";
                        Statement formsStatement = connection.createStatement();
                        ResultSet formsResult = formsStatement.executeQuery(getForms);

                        while(formsResult.next()){
                            int p_id = formsResult.getInt(1);
                            String getDetails = "select days_of_week_id, course_importance, day_importance, time_importance from form_semester_info " +
                                    " where preference_form_id = "+p_id+" AND n_number = "+n_number+" AND time_of_day_id = "+i;

                            Statement detailsStatement = connection.createStatement();
                            ResultSet detailsResult = detailsStatement.executeQuery(getDetails);
                            while(detailsResult.next()){
                                System.out.println("\t\t\t\t"+days[detailsResult.getInt(1)-1]);
                                System.out.println("\t\t\t\tImportance Ranks: Time("+detailsResult.getInt(4)+")"+" Day("+detailsResult.getInt(3)+")"+" Course("+detailsResult.getInt(2)+")");

                            }
                            clean(detailsResult,detailsStatement);


                            // gets courses
                            String getAllRankedCourses = "select code, rank_order from course_ranking where preference_form_id = "+p_id;
                            Statement stmt2 = connection.createStatement();
                            ResultSet rs2 = stmt2.executeQuery(getAllRankedCourses);

                            System.out.println("\t\t\t\tCourses:");
                            while(rs2.next()){
                                System.out.println("\t\t\t\t\tRank "+rs2.getInt(2)+": "+rs2.getString(1));
                            }
                            clean(rs2,stmt2);


                        }
                        clean(formsResult,formsStatement);
                    }
                    clean(rs,stmt);
                }catch (Exception e){
                    System.out.println("Error: "+e.getMessage());
                }
                //endregion

                //region Student Days
                System.out.println("\t\t\tStudents");
                System.out.println("\t\t\t---------");

                String getAllStudentsWithTime = "select distinct s.first_name, s.last_name, " +
                        " s.n_number from student s " +
                        " inner join course_request cr " +
                        " on s.n_number = cr.n_number " +
                        " where cr.times_id = "+i;

                try{
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(getAllStudentsWithTime);

                    // for each student with a time
                    int count = 1;
                    while(rs.next()){
                        System.out.println("\t\t\t"+count+++": "+rs.getString(1)+" "+rs.getString(2));
                        String getAllClassesForStudent = "select c.code, c.course_name, cr.days_id" +
                                " from course c inner join course_request cr " +
                                " on c.CRN = cr.CRN " +
                                " where cr.n_number = "+rs.getInt(3)+" AND times_id ="+i;

                        Statement stmt2 = connection.createStatement();
                        ResultSet rs2 = stmt2.executeQuery(getAllClassesForStudent);

                        // get all classes for each student
                        System.out.println("\t\t\t\tCourses:");
                        while(rs2.next()){
                            System.out.println("\t\t\t\t\t"+rs2.getString(2)+" ("+rs2.getString(1)+") | "+days[rs2.getInt(3)-1]);
                        }
                        clean(rs2,stmt2);
                    }

                }catch(Exception e){
                    System.out.println("Error: "+ e.getMessage());
                }


                //endregion
            }
        }
    }

    /*
    *   Prints out a report of what the faculty requested and
    *   the additional information linked to each faculty member:
    *   Course_Ranking, Course_RequestPreference_Form,
    *   Form_Semester_Info, Faculty, and Student tables
    */
    public void printFacultyReport(){

        //region FacultyReport

        // select faculty members that have ranked it
        String query2 = "select distinct f.n_number, f.first_name, f.last_name "+
                "FROM faculty f "+
                "INNER JOIN course_ranking cr "+
                " ON f.n_number = cr.n_number ";
        try{
            Statement newstm = connection.createStatement();
            ResultSet innerRS = newstm.executeQuery(query2);
            while(innerRS.next()) {
                // prints faculty name
                System.out.println("\t" + innerRS.getString(2) + " " + innerRS.getString(3));

                // gets preference form for faculty
                String queryFacultyDetails = "select preference_form_id from preference_form where n_number = " + innerRS.getInt(1);
                Statement anotherStm = connection.createStatement();
                ResultSet internalRS = anotherStm.executeQuery(queryFacultyDetails);
                int count = 1;
                while (internalRS.next()) { // for every preference form, show the details
                    // prints preference form for particular faculty member
                    System.out.println("\t\tPreference Form: " + Integer.toString(count));
                    String seasonQuery = "Select time_of_day_id, days_of_week_id, number_of_courses, course_importance, day_importance, time_importance, semester" +
                            " from form_semester_info where preference_form_id = " + internalRS.getInt(1);

                    Statement seasonStatement = connection.createStatement();
                    ResultSet seasonResultSet = seasonStatement.executeQuery(seasonQuery);
                    while (seasonResultSet.next()) {
                        System.out.println("\t\t\t"+seasonResultSet.getString(7)); // semester & details
                        System.out.println("\t\t\t\t"+times[seasonResultSet.getInt(1)-1]+" | "+days[seasonResultSet.getInt(2)-1]+" | "+"# Of Courses: "+seasonResultSet.getInt(3));
                        System.out.println("\t\t\t\tImportance Ranks: Time("+seasonResultSet.getInt(6)+") Day("+seasonResultSet.getInt(5)+") Courses("+seasonResultSet.getInt(4)+")");
                    }
                    clean(seasonResultSet,seasonStatement);

                    String getFacultyCourses = "select code, rank_order from course_ranking where preference_form_id = "+internalRS.getInt(1)+" AND n_number = "+innerRS.getInt(1);
                    Statement stmt3 = connection.createStatement();
                    ResultSet coursesRS = stmt3.executeQuery(getFacultyCourses);
                    while(coursesRS.next()){
                        System.out.println("\t\t\t"+coursesRS.getString(1)+" Ranked: "+coursesRS.getInt(2));
                    }
                    clean(coursesRS,stmt3);
                }
                clean(internalRS,anotherStm);
            }
            clean(innerRS,newstm);
        }catch (SQLException ex){
            System.out.println("Error: "+ex.getMessage());
        }
        //endregion
    }

   /*
    *   Prints out a report of what the students requested and
    *   the additional information linked to each student:
    *   Course_Ranking, Course_RequestPreference_Form,
    *   Form_Semester_Info, Faculty, and Student tables
    */
    public void printStudentReport(){ // change to show student then semesters


        String getAllStudents = "select first_name, last_name, n_number from student";
        try{
            Statement getStudentsStmt = connection.createStatement();
            ResultSet getStudentRS = getStudentsStmt.executeQuery(getAllStudents);
            while(getStudentRS.next()){
                int n_number = getStudentRS.getInt(3);
                System.out.println("\t"+getStudentRS.getString(1)+" "+getStudentRS.getString(2));

                String getAllRequests = "select semester, year from course_request where n_number = "+n_number;
                Statement getRequests = connection.createStatement();
                ResultSet requestRS = getRequests.executeQuery(getAllRequests);

                while(requestRS.next()){
                    String semester = requestRS.getString(1);
                    int year = requestRS.getInt(2);
                    System.out.println("\t\t"+semester+" | "+year);

                    String getCourses = "select c.code, c.course_name from course c " +
                            " inner join course_request cr on cr.CRN = c.CRN " +
                            " where cr.semester = '"+semester + "' AND cr.n_number = "+n_number + " AND cr.year = "+year ;

                    Statement courseStm = connection.createStatement();
                    ResultSet courseRS = courseStm.executeQuery(getCourses);

                    int c_count = 1;
                    while(courseRS.next()){
                        System.out.println("\t\t["+c_count+"] "+courseRS.getString(2)+" ("+courseRS.getString(1)+")"); // pulls the courses

                        String getFacThatTeach = " select distinct f.first_name, f.last_name from faculty f " +
                                " inner join course_ranking cr on f.n_number = cr.n_number " +
                                " inner join course c on cr.code = c.code " +
                                " where cr.code = '"+courseRS.getString(1)+"'";

                        Statement getFacStmt = connection.createStatement();
                        ResultSet getFacRS = getFacStmt.executeQuery(getFacThatTeach);
                        System.out.println("\t\t\tInstructed By: ");
                        while(getFacRS.next()){
                            System.out.println("\t\t\t\t"+getFacRS.getString(1)+" "+getFacRS.getString(2));
                        }
                        clean(getFacRS,getFacStmt);
                    }
                    clean(courseRS,courseStm);

                }
                clean(requestRS,getRequests);
            }
            clean(getStudentRS,getStudentsStmt);
        }catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
        }


        //endregion
    }
    //endregion

    private void clean(ResultSet rset, Statement stmt){
        try {
            if(rset != null) rset.close();
            if(stmt != null) stmt.close();
        } catch (SQLException se) { }
    }

    private int getChoice(int last){
        String selection = null;
        do {
            System.out.print(">>  ");
            selection = inputReader.next();
            try {
                int num = Integer.parseInt(selection);
                if(num >= 0 && num <= last) return num;
            } catch (Exception e) { /* Politely ignore */ }
            System.out.println("===========Please enter valid selection.===========");
        } while (true);
    }

}
