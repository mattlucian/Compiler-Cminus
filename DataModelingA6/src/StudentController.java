/**
 *
 * 
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author chris
 */
public class StudentController {
    //Database resource
    private Connection conn;
    //Standard In
    private Scanner in = new Scanner(System.in);
    
    private static final int MAX_COURSES = 6;
    private static final String SEPARATOR = "------------------------------";
    private static final String DAYS_MENU = "\nAvailable Days\n" + SEPARATOR + "\n[1] Monday/Wednesday\n[2] Tuesday/Thursday\n[3] Monday/Wednesday/Friday\n[4] Cancel\n";
    private static final String TIMES_MENU = "\nAvailable Times\n" + SEPARATOR + "\n[1] Morning\n[2] Evening\n[3] Night\n[4] Cancel\n";
    private static final String SEMESTER_MENU = "\nChoose a Semester\n" + SEPARATOR + "\n[1] Spring\n[2] Summer\n[3] Fall\n[4] Back\n";
    private static final int MAX_MENU = 4;
    private static final HashMap<Integer,Semester> semMap = new HashMap<Integer, Semester>();
    
    static {
        semMap.put(1, Semester.Spring);
        semMap.put(2, Semester.Summer);
        semMap.put(3, Semester.Fall);
    }
    //private static boolean DEBUG = false;

    public StudentController(){
        //for testing only
    }
    public StudentController(Connection con){
        conn = con;
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }
    /**
     * Conducts a session for a student. Assumes the N-Number was
     * acquired during login and is in the form of a String. If it
     * was already parsed into a number, this will be changed.
     */
    public void startSession(String nNumber){
        //create student object
        Student student = initStudent(nNumber);
        //append to DB, if necessary
        //checkStudent(student);
        
        //launch Semester menu
        while(showSemesterMenu(student)){//when false, go back to Semester menu
            
            //An available semester has been chosen
            List<Course> availableCourses = getAvailableCourses(student.getActive());
            //launch course menu chain
            handlePreferences(student, availableCourses);
        }
        
        //return control to caller(main menu of app)
    }
    /**
     * Displays menu for semester selection. Only
     * shows semesters that student has not filled out.
     * Anytime "BACK" is chosen, this method returns false.
     * @param s 
     */
//    private boolean showSemesterMenu(Student s){
//        int index = 1;
//        Map<Integer, Semester> smap = new HashMap<Integer, Semester>();
//        System.out.println("\nChoose a Semester\n" + SEPARATOR);
//        
//        //Dynamically display ONLY those semesters that student has not completed
//        if(!s.containsSemester(Semester.Spring)){
//            smap.put(index, Semester.Spring);
//            System.out.println(String.format("[%d] Spring", index++));
//        }
//        if(!s.containsSemester(Semester.Fall)){
//            smap.put(index, Semester.Fall);
//            System.out.println(String.format("[%d] Fall", index++));
//        }
//        if(!s.containsSemester(Semester.Summer)){
//            smap.put(index, Semester.Summer);
//            System.out.println(String.format("[%d] Summer", index++));
//        }
//        if(index == 1){
//            System.out.println("No Semesters Available");
//        }
//        System.out.println(String.format("[%d] Back", index));
//        int choice = getChoice(index);
//        if(choice == index) return false;//Student selected "BACK"
//        //Set Active Semester for other methods to use
//        s.setActiveSemester(smap.get(choice));
//        //acquire the year
//        //System.out.println("Please enter ");
//        return true;
//    }
    
    /**
     * Displays menu for semester selection. Only
     * shows semesters that student has not filled out.
     * Anytime "BACK" is chosen, this method returns false.
     * @param s 
     */
    private boolean showSemesterMenu(Student s){
        Semester stemp = null;
        int ytemp = 0;
        int choice;
        boolean valid = false;
        while(!valid){
            //Prompt for semester
            System.out.println(SEMESTER_MENU);
            choice = getChoice(4);
            if(choice == MAX_MENU){
                return false;
            }
            stemp = semMap.get(choice);
            //Prompt for year
            System.out.print("\nEnter year: ");
            ytemp = getChoice(Integer.MAX_VALUE);
            if((ytemp > 2013 && ytemp <= 2030) && (!s.containsSemester(stemp, ytemp))){
                valid = true;
            } else if(ytemp <= 2013 || ytemp > 2030){
                System.out.println("\nThe year is out of range. Please try again.");
            } else {
                System.out.println("\nThat combination of year and semester has already been recorded. Please try again.");
            }
        }
        s.setActiveSemester(stemp);
        s.setActiveYear(ytemp);
        return true;
    }
    /**
     * Displays menus and records selections in the Student option.
     * Each menu has a cancel option to return to Navigation Menu.
     * After 6 classes have been chosen, the only available option is to
     * submit the classes.
     * Protects against multiple entries of same course.
     * 
     * @param student, prebuilt with n-number, name, and semester
     */
//    private void performPreferences(Student student, List<String> availableCourses){
//        //this data already secured
//        //prepare available courses for chosen semester
//        int selectedCourse;
//        int selectedDays;
//        int selectedTimes;
//        int choices = 0;
//        
//        while(showNavMenu(choices >= MAX_COURSES, choices)){
//            selectedCourse = showCourseMenu(availableCourses);
//            if(selectedCourse == availableCourses.size() + 1){
//                System.out.println("Cancelling current course");
//                continue;
//            } else if(student.containsCourse(selectedCourse)) {
//                //Handle selecting the same course multiple times
//                System.out.println("Course has already been chosen. Please try another.");
//                continue;
//            }
//            selectedDays = showDaysMenu();
//            if(selectedDays == MAX_MENU){
//                System.out.println("Cancelling current course");
//                continue;
//            }
//            selectedTimes = showTimesMenu();
//            if(selectedTimes == MAX_MENU){
//                System.out.println("Cancelling current course");
//                continue;
//            }
//            
//            //log the entire choice
//            //student.addPreferenceToActiveSemester(new CourseChoice(selectedCourse, selectedDays, selectedTimes, availableCourses.get(selectedCourse - 1)));
//            
//            //mapping from selection to course title: selectedCourse => courses.get(selectedCourse - 1)
//            choices++;
//            System.out.println("Course logged.");
//            //write to DB
//        }
//        //leaving nav menu
//    }
    
    //Actions
    private static final int CHOOSE_CLASS = 1;
    private static final int SAVE_CHOOSE = 2;
    private static final int SAVE_QUIT = 3;
    private static final int CANCEL = 4;
    
    private void handlePreferences(Student student, List<Course> availableCourses){
        
        int selectedCourse;
        int selectedDays;
        int selectedTimes;
        int choices = 0;
        int result;
        while(true){
            int temp = 1;
            //Map selections to actions
            Map<Integer,Integer> smap = new HashMap<Integer,Integer>();
            StringBuilder menu = new StringBuilder();
            menu.append(String.format("\nPreference Menu  [Submitted Classes: %d]\n", choices)).append(SEPARATOR).append('\n');
            if(choices < MAX_COURSES){
                smap.put(temp, CHOOSE_CLASS);
                menu.append(String.format("[%d] Select a class\n", temp++));
            }
            if(choices > 0){//Options to Save only available if classes submitted
                smap.put(temp, SAVE_CHOOSE);
                smap.put(temp + 1, SAVE_QUIT);
                menu.append(String.format("[%d] Save and choose another semester\n", temp++));
                menu.append(String.format("[%d] Save and quit\n", temp++));
            }
            smap.put(temp, CANCEL);
            menu.append(String.format("[%d] Cancel without saving\n", temp));
            System.out.println(menu.toString());
            result = smap.get(getChoice(temp));
            if(result != CHOOSE_CLASS){
                if(result == CANCEL) return;
                //Store active semester in DB
                saveData(student);
                if(result == SAVE_QUIT){
                    System.out.println("Have a nice day!");
                    System.exit(0);
                }
                return;//control returns to Semester Menu
            }
            //choose class
            selectedCourse = showCourseMenu(availableCourses);
            //handle cancellation
            if(selectedCourse == availableCourses.size() + 1){
                System.out.println("Cancelling current course");
                continue;
            } else if(student.containsCourse(selectedCourse)) {
                //Handle selecting the same course multiple times
                System.out.println("Course has already been chosen. Please try another.");
                continue;
            }
            selectedDays = showDaysMenu();
            if(selectedDays == MAX_MENU){
                System.out.println("Cancelling current course");
                continue;
            }
            selectedTimes = showTimesMenu();
            if(selectedTimes == MAX_MENU){
                System.out.println("Cancelling current course");
                continue;
            }
            //log the entire choice
            Course chosen = availableCourses.get(selectedCourse - 1);//mapping to zero-based index
            student.addPreferenceToActiveSemester(new CourseChoice(chosen.getCrn(), selectedDays, selectedTimes));
            //mapping from selection to course title: selectedCourse => courses.get(selectedCourse - 1)
            choices++;
            System.out.println("Course submitted.");
        }
    }
    /**
     * Main Navigation Menu
     * 
     * Returns true to select a class
     * False means cancel or abort
     * @param reachedLimit
     * @return 
     */
//    private boolean showNavMenu(boolean reachedLimit, int chosen){
//        int temp = 1;
//        StringBuilder result = new StringBuilder();
//        result.append(String.format("\nNavigation Menu  [Classes: %d]\n", chosen)).append(SEPARATOR).append('\n');
//        if(!reachedLimit){
//            result.append(String.format("[%d] Select a class\n", temp++));
//        }
//        result.append(String.format("[%d] Quit and submit\n", temp));
//        
//        System.out.println(result.toString());
//        return getChoice(temp) != temp;
//    }
    /**
     * Available Courses Menu
     * @param available
     * @return 
     */
    private int showCourseMenu(List<Course> available){
        StringBuilder result = new StringBuilder();
        result.append("\nAvailable Course\n");
        result.append(String.format("%s\n", SEPARATOR));
        int size = available.size();
        for (int c = 0; c < size; c++) {
            Course course = available.get(c);
            result.append(String.format("[%2d] %s - %s (CRN: %d)\n", (c + 1), course.getCode(), course.getCourseName(), course.getCrn()));
        }
        result.append(String.format("\n[%2d] Cancel\n", size + 1));
        System.out.println(result.toString());
        return getChoice(size + 1);
        
    }
    /**
     * Days Menu
     * 
     * @return selection
     */
    private int showDaysMenu(){
        System.out.println(DAYS_MENU);
        return getChoice(MAX_MENU);
        
    }
    /**
     * Times Menu
     * 
     * @return selection
     */
    private int showTimesMenu(){
        System.out.println(TIMES_MENU);
        return getChoice(MAX_MENU);
    }
    private Student initStudent(String nNumber){
        Student s = new Student();
        //String firstName, lastName;
        nNumber = nNumber.toUpperCase();//force 'n' to uppercase
        //Remove 'N' if needed prior to parsing
        if(nNumber.charAt(0) == 'N'){
            nNumber = nNumber.substring(1);
        }
        int n = 0;
        try {
            n = Integer.parseInt(nNumber);
        } catch (Exception e) { /* Ignoring failure */ }
        s.setN_number(n);
        
        //System.out.println("Student Registration\n" + SEPARATOR);
        //System.out.print("Enter First Name >> ");
        //firstName = in.next();
        //System.out.print("Enter Last Name >> ");
        //lastName = in.next();
        //force upper case for consistency in DB, modify for display purposes
        //s.setFirstName(firstName.toUpperCase());
        //s.setLastName(lastName.toUpperCase());
        
        populateInfo(s);
        return s;
    }
    
    /**
     * Extracts the student's first and last name from table
     * based on provided n-number.
     * 
     */
    private void populateInfo(Student s){//expects n_number to be set
        PreparedStatement ps = null;
        ResultSet rset = null;

        try {
            ps = conn.prepareStatement("SELECT first_name, last_name FROM student WHERE n_number = " + s.getN_number());
            rset = ps.executeQuery();
            if (rset.next()) {
                s.setFirstName(rset.getString(1));
                s.setLastName(rset.getString(2));
            }
        } catch (SQLException e) {
        } finally {
            clean(rset, ps);
        }
    }
    
    
    /**
     * Retrieves user's choice following a menu.
     * @param last valid menu choice
     * @return selection as integer
     */
    private int getChoice(int last){
        String selection = null;
        do {
            System.out.print(">>  ");
            selection = in.next();
            try {
                int num = Integer.parseInt(selection);
                if(num > 0 && num <= last) return num;
            } catch (Exception e) { /* Politely ignore */ }
            System.out.println("Invalid selection. Please try again.");
        } while (true);
    }
    
    /*private List<Course> getAvailableCoursesTest(Semester s){
        List<Course> courses = new ArrayList<Course>();//crn, code, name
        courses.add(new Course(1034, "COP2220", "Computer Science I"));
        courses.add(new Course(1034, "COP3100", "Comp Structures"));
        courses.add(new Course(1034, "COP3503", "Computer Science II"));
        courses.add(new Course(1034, "COP3530", "Data Structures"));
        courses.add(new Course(1034, "CDA3101", "Intro to Computer Hardware"));
        courses.add(new Course(1034, "CIS4253", "Legal Ethical Issues"));
        courses.add(new Course(1034, "COP4710", "Data Modeling"));
        courses.add(new Course(1034, "COP4610", "Operating Systems"));
        courses.add(new Course(1034, "COT3210", "Computability and Automata"));
        courses.add(new Course(1034, "COP3601", "Intro to Systems Software"));
        courses.add(new Course(1034, "COP4620", "Constr of Language Trans"));
        courses.add(new Course(1034, "CEN4010", "Software Engineering"));
        courses.add(new Course(1034, "CNT4504", "Networking and Distributed Processing"));
        courses.add(new Course(1034, "COP4813", "Internet Programming"));
        courses.add(new Course(1034, "CAP4620", "Artifical Intelligence"));
        courses.add(new Course(1034, "CAP4831", "Modeling & Simulation"));
        courses.add(new Course(1034, "CEN4801", "Systems Integration"));
        courses.add(new Course(1034, "COT4400", "Analysis of Algorithms"));
        courses.add(new Course(1034, "COT4111", "Comp Structures II"));
        courses.add(new Course(1034, "COT4461", "Computational Biology"));
        courses.add(new Course(1034, "COT4560", "Applied Graph Theory"));
        courses.add(new Course(1034, "CIS4362", "Computer Cryptography"));
        
        
        return courses;
    }*/
    /**
     * Pulls the courses based on semester and parity of year.
     * Table: course   Form: CRN, code, category, course_number, course_name, is_required, is_odd_year, semester
     * @param unit
     * @return 
     */
    private List<Course> getAvailableCourses(Entity unit){
        List<Course> courses = new ArrayList<Course>();
        PreparedStatement ps = null;
        ResultSet rset = null;
        //Format: even years = 0, odd years = 1
        int isOdd = ((unit.getYear() & 0x01) == 0) ? 0 : 1;// if last bit is 0, assign isOdd to be 0(false)
        try {
            ps = conn.prepareStatement("SELECT crn,code,course_number,course_name FROM course WHERE semester = '?' AND is_odd_year = ?");
            ps.setString(1, unit.getSemester().toString());
            ps.setInt(2, isOdd);
            rset = ps.executeQuery();
            while(rset.next()){
                //RS: 1-crn, 2-code, 3-courseNumber, 4-courseName
                courses.add(new Course(rset.getInt(1), rset.getString(2), rset.getInt(3),rset.getString(4)));
            }
        } catch (SQLException e) {
        } finally {
            clean(rset, ps);
        }
        return courses;
    }
    /**
     * Checks database to verify if student has
     * been recorded previously. If not found,
     * the student is added.
     * @param s -1 for failure, 0 for existing, >0 for success
     */
    private int checkStudent(Student s){//table: n_number, first_name, last_name, degree
        PreparedStatement ps = null, psi = null;
        ResultSet rset = null;
        try {
            ps = conn.prepareStatement("SELECT n_number FROM student WHERE n_number = ?");
            ps.setInt(1, s.getN_number());
            rset = ps.executeQuery();
            if(rset.next()){
                //entry found - abandon ship!
                return 0;
            }
            psi = conn.prepareStatement("INSERT INTO student VALUES(?,?,?,?)");
            psi.setInt(1, s.getN_number());
            psi.setString(2, s.getFirstName());
            psi.setString(3, s.getLastName());
            psi.setString(4, s.getDegree());
            //psi.setString(5, s.getSemester().toString());
            //psi.setInt(6, s.getYear());
            return psi.executeUpdate();
        } catch (SQLException e) {
        } finally {
            clean(rset,ps);
            clean(null,psi);
        }
        return -1;
    }
    /**
     * Saves the student's active semester only.
     * Table: course_request
     * Uses JDBC transactions to rollback if needed.
     * @param s 
     */
    private void saveData(Student s){//table: (CRN, n_number, year), semester, days_id, times_id
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);//JDBC transactions
            ps = conn.prepareStatement("INSERT INTO course_request VALUES(?,?,?,?,?,?)");
            Request req = s.getData();
            int size = req.getNumberOfSelections();
            for (int i = 0; i < size; i++) {
                CourseChoice cc = req.getChoice(i);
                ps.setInt(1, cc.getCourseCRN());
                ps.setInt(2, s.getN_number());
                ps.setInt(3, req.getYear());
                ps.setString(4, req.getSemester().toString());
                ps.setInt(5, cc.getDays());
                ps.setInt(6, cc.getTimes());
                ps.executeUpdate();
                conn.commit();//JDBC transactions
            }
        } catch (SQLException e) {
            try { conn.rollback();//JDBC transactions
            } catch (SQLException ee) { }
        } finally {
            try { conn.setAutoCommit(true);
              if(ps != null) ps.close();//JDBC transactions
            } catch (SQLException e) { }
        }
    }
    public int countRows(String table){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT COUNT(*) AS solution FROM " + table);
            if(rset.next()) return rset.getInt(1);
        } catch (SQLException se) {
            System.out.println(se.toString());//Remove
        } finally {
            clean(null, stmt);
        }
        return -1;
    }
    private void clean(ResultSet rset, PreparedStatement ps){
        try {
            if(rset != null) rset.close();
            if(ps != null) ps.close();
        } catch (SQLException se) { }
    }
    private void clean(ResultSet rset, Statement stmt){
        try {
            if(rset != null) rset.close();
            if(stmt != null) stmt.close();
        } catch (SQLException se) { }
    }
}
