import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Nick on 11/24/14.
 */
public class Faculty {
    public int current_fac_id = 123;

    public static String[] times = {"None", "Morning (9am-12pm)", "Afternoon (12pm-4pm)", "Evening (4pm-9pm)"};
    public static String[] days = {"None", "MW", "TR", "MWF", "MTWRF", "Summer C(12 weeks) MW",
        "Summer C(12 weeks) TR", "Summer A(6 weeks) MWTR", "Summer B(6 weeks) MWTR", "8 week MWF",
        "8 week TR", "6 week MW+4 Fridays", "6 week TR+4 Fridays"};

    private Scanner inputReader = new Scanner(System.in); // for reading input
    private Connection connection;

    Faculty(Connection establishedConnection){
        connection = establishedConnection;
    }

    //faculty main menu
    public void mainMenu(){
        int choice = 0;
        do{
            System.out.println("Faculty Menu:");
            System.out.println("[1] Enter New Course Preference Form");
            System.out.println("[2] View / Edit Previous Course Preference Forms");
            System.out.println("[3] Request Course Print-Out");
            System.out.println("[4] Log out");
            choice = getChoice(4);

            if (choice == 1) {
                newCoursePreferenceForm();
            } else if (choice == 2) {
                viewCoursePreferenceForms();
            } else if (choice == 3) {
                requestCoursePrintout();
            }
        }while(choice != 4);
        System.out.println("Logged out, exiting program");
    }
    //endregion

    //submit a new course preference form
    public void newCoursePreferenceForm(){

        //sql to create new course preference form
        PreferenceFormRecord preference_form = new PreferenceFormRecord(current_fac_id);

        if(preference_form.insertPreferenceFormRecord(connection))
        {
            //view the course preference form menu
            coursePreferenceFormMenu(preference_form);
        }

        System.out.println("An error occurred creating your new course preference form.");

    }

    public void viewCoursePreferenceForms(){

        int choice = 0;
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY");
        Date date = new Date();
        PreferenceFormRecord preference_form;

        //load Course Preference Forms
        ArrayList<PreferenceFormRecord> coursePreferenceForms = getFacultyPreferenceForms();

        //menu to select a course preference form
        System.out.println("Available Course Preference Forms:");

        String preference_form_description = "";
        if(coursePreferenceForms != null)
        {
            for(int i = 0; i < coursePreferenceForms.size(); i++) {
                if(coursePreferenceForms.get(i).date_added != null)
                    preference_form_description = dateFormat.format(coursePreferenceForms.get(i).date_added) + "(#" + coursePreferenceForms.get(i).preference_form_id+")";
                else
                    preference_form_description = "Form ID #" + coursePreferenceForms.get(i).preference_form_id;
                System.out.println("["+(i+1)+"] "+preference_form_description);
            }
            System.out.println("["+(coursePreferenceForms.size()+1)+"] Back to Faculty Menu");
        }
        else
        {
            System.out.println("\nNo Course Preference Forms to show. You must create one.\n");
            return;
        }
        choice = getChoice(coursePreferenceForms.size()+1);

        if(choice != coursePreferenceForms.size()+1){

            preference_form = coursePreferenceForms.get(choice-1);

            if(preference_form == null)
            {
                System.out.println("An error occurred - could not find your preference form.");
                return;
            }

            //Display Preference Form Record
            preference_form.display(connection);

            //show options for managing course preference form
            coursePreferenceFormMenu(preference_form);
        }
    }

    public void coursePreferenceFormMenu(PreferenceFormRecord preference_form)
    {
        int choice = 0;

        //show menu of editable items
        do{
            System.out.println("------------------------------");
            System.out.println("| Course Preference Form #" + preference_form.preference_form_id + " |");
            System.out.println("------------------------------");
            System.out.println("Course Preference Form Menu:");
            System.out.println("[1] Display Preference Form Information");
            System.out.println("[2] Course Rankings");
            System.out.println("[3] Fall Preferences");
            System.out.println("[4] Spring Preferences");
            System.out.println("[5] Summer Preferences");
            System.out.println("[6] Back to Faculty Menu");
            choice = getChoice(6);

            if (choice == 1) {
                preference_form.display(connection);
            } else if (choice == 2) {
                courseRankingMenu(preference_form);
            } else if (choice == 3) {
                semesterPreferenceForm(preference_form, Semester.Fall);
            } else if (choice == 4) {
                semesterPreferenceForm(preference_form, Semester.Spring);
            } else if (choice == 5) {
                semesterPreferenceForm(preference_form, Semester.Summer);
            }

        }while(choice != 6);

    }

    //region Reporting Menu Functions
    public void requestCoursePrintout(){
        System.out.println("Your request has been received. Please contact Professor Abbassi for your paper copy.");
    }

    public void courseRankingMenu(PreferenceFormRecord preference_form)
    {
        int choice = 0;
        ArrayList<Course> courses = getAvailableCourses();
        ArrayList<Course> coursesRanked = new ArrayList<Course>();
        String cardinalPosition = "first";
        preference_form.loadCourseRankings(connection);
        if(preference_form.courseRankings.size() > 0)
        {
            System.out.println("\nRanking one or more courses here will overwrite your previous rankings. Would you like to continue? " +
                    "Type 'Y' to confirm, anything else to cancel.");

            //clean buffer
            inputReader.nextLine();

            String input = getInput();

            if(!input.toUpperCase().equals("Y"))
            {
                return;
            }
        }

        //show menu of editable items
        do{
            switch(choice)
            {
                case 0:
                    cardinalPosition = "first";
                    break;
                case 1:
                    cardinalPosition = "second";
                    break;
                case 2:
                    cardinalPosition = "third";
                    break;
                case 3:
                    cardinalPosition = "fourth";
                    break;
                case 4:
                    cardinalPosition = "fifth";
                    break;

            }
            System.out.println("\nPlease select your " + cardinalPosition + " choice:");

            for(int i = 0; i < courses.size(); i++) {
                System.out.println("["+(i+1)+"] "+courses.get(i).getCode() + " - " + courses.get(i).getCourseName());
            }
            System.out.println("["+(courses.size()+1)+"] Done ranking courses");
            choice = getChoice(courses.size()+1);
            System.out.println("");

            if(choice != courses.size()+1){
                //add course ranking record
                Course course = courses.get(choice-1);
                coursesRanked.add(course);
            }

            if(coursesRanked.size() >= 5) {
                System.out.println("You have ranked the maximum number of courses for this form.");
            }

        }while(choice != (courses.size()+1) && coursesRanked.size() < 5);

        //Save Courses Ranked
        if( coursesRanked.size() > 0 )
        {
            if( preference_form.saveCourseRankings(connection, coursesRanked) )
            {
                //Display Courses Ranked
                System.out.println("Courses Ranked: " + coursesRanked.size());
                for(int i = 0; i < coursesRanked.size(); i++)
                {
                    System.out.println("["+(i+1) + "] " + coursesRanked.get(i).getCode() + " - " + coursesRanked.get(i).getCourseName());
                }
            }
            else
            {
                System.out.println("An error occurred saving your course rankings.");
            }
        }

    }

    public void semesterPreferenceForm(PreferenceFormRecord preference_form, Semester semester)
    {
        int choice = 0;
        FormSemesterInfoRecord form_semester_info = FormSemesterInfoRecord.loadOne(connection, preference_form, semester);

        //show menu of editable items
        do{
            System.out.println("Semester Preference Form Menu:");
            System.out.println("Semester: " + semester);

            System.out.println("[1] Display Semester Info Form");
            System.out.println("[2] Course Load Preference");
            System.out.println("[3] Scheduling Factors");
            System.out.println("[4] Times of Day Preference");
            System.out.println("[5] Days of Week Preference");
            System.out.println("[6] Back to Course Preference Forms Menu");

            choice = getChoice(6);
            if(choice == 1) {
                form_semester_info.display();
            } else if (choice == 2) {
                courseLoadPreference(form_semester_info);
            } else if (choice == 3) {
                schedulingFactors(form_semester_info);
            } else if (choice == 4) {
                timesOfDayPreference(form_semester_info);
            } else if (choice == 5) {
                daysOfWeekPreference(form_semester_info);
            } else if (choice == 6) {
                // back to Course Preference Forms Menu
            }  else {
                System.out.println("Error, invalid selection please try again");
            }
        }while(choice != 6);
    }

    public void courseLoadPreference(FormSemesterInfoRecord form_semester_info)
    {
        int number_of_courses = 0;

        do {
            System.out.println("How many courses would you like to teach this semester?");
            number_of_courses = getChoice(3);
            if(number_of_courses < 1 || number_of_courses > 3)
            {
                System.out.println("Invalid choice, please select a number between 1 and 3.");
            }
            else
            {
                //save the rankings to the form semester form
                if(form_semester_info.saveNumberOfCourses(connection, number_of_courses))
                {
                    System.out.println("Your selection has been saved.");
                }
                else
                {
                    System.out.println("An error occurred saving your number of courses.");
                }
            }
        }while(number_of_courses < 1 || number_of_courses > 3);
    }

    public void schedulingFactors(FormSemesterInfoRecord form_semester_info)
    {
        String input = "";
        boolean inputValid = false;
        System.out.println("Enter in a, b, and c in the corresponding order of importance to your scheduling.\n" +
                "For example \"c a b\" to indicate that Times of the Day are the most important factor, \n" +
                "and Days of the Week are the least important factor:");
        System.out.println("[a] Course Preference \n[b] Days of the Week \n[c] Times of the Day");

        //clear buffer of any existing input
        inputReader.nextLine();

        do {
            inputValid = false;
            input = getInput();
            if(input.equals("quit"))
            {
                inputValid = true; // quitting
            }
            else
            {
                input = input.replaceAll("\\s", "");
                if(input.matches("abc|acb|bac|bca|cab|cba"))
                {
                    inputValid = true;

                    //get the rankings
                    int course_importance = input.indexOf("a")+1;
                    int days_of_week_importance = input.indexOf("b")+1;
                    int time_of_day_importance = input.indexOf("c")+1;

                    //save the rankings to the semester form
                    if(form_semester_info.saveSchedulingFactors(connection, course_importance, days_of_week_importance, time_of_day_importance))
                    {
                        System.out.println("Your selection has been saved.");
                    }
                    else
                    {
                        System.out.println("An error occurred saving your scheduling factors.");
                    }
                }
                else
                {
                    System.out.println("Your input was invalid. Please try again.");
                }
            }
        }while(!inputValid);
    }

    public void timesOfDayPreference(FormSemesterInfoRecord form_semester_info)
    {
        int time_of_day = 0;

        do {
            System.out.println("Which time of the day would you prefer?");
            for(int i = 1; i < times.length; i++)
            {
                System.out.println("["+i+"] "+times[i]);
            }
            time_of_day = getChoice(3);
            if(time_of_day < 1 || time_of_day > 3)
            {
                System.out.println("Invalid choice, please select a number between 1 and 3.");
            }
            else
            {
                //save the time of day to the semester form
                if(form_semester_info.saveTimeOfDay(connection, time_of_day))
                {
                    System.out.println("Your selection has been saved.");
                }
                else
                {
                    System.out.println("An error occurred saving your time of day preference.");
                }
            }
        }while(time_of_day < 1 || time_of_day > 3);
    }

    public void daysOfWeekPreference(FormSemesterInfoRecord form_semester_info)
    {
        int choice = 0;
        int days_of_week = 0;

        do {
            System.out.println("Which days of the week would you prefer?");

            switch(form_semester_info.semester)
            {
                case Fall:
                case Spring:
                    System.out.println("[1] MWF");
                    System.out.println("[2] MW");
                    System.out.println("[3] TR");

                    choice = getChoice(3);
                    days_of_week = choice;
                    break;

                case Summer:
                    System.out.println("[1] Summer C(12 weeks) MW");
                    System.out.println("[2] Summer C(12 weeks) TR");
                    System.out.println("[3] Summer A(6 weeks) MWTR");
                    System.out.println("[4] Summer B(6 weeks) MWTR");
                    System.out.println("[5] 8 week MWF");
                    System.out.println("[6] 8 week TR");
                    System.out.println("[7] 6 week MW+4 Fridays");
                    System.out.println("[8] 6 week TR+4 Fridays");

                    choice = getChoice(8);
                    days_of_week = choice+4; //offset by Spring/Fall days
                    break;
            }


            if(days_of_week > 0 && days_of_week < days.length)
            {
                //save the time of day to the semester form
                if(form_semester_info.saveDaysOfWeek(connection, days_of_week))
                {
                    System.out.println("Your selection has been saved.");
                }
                else
                {
                    System.out.println("An error occurred saving your days of week preference.");
                }
            }
        }while(days_of_week < 1 || days_of_week > days.length);
    }

    private ArrayList<Course> getAvailableCourses(){
        ArrayList<Course> courses = new ArrayList<Course>();
        PreparedStatement ps = null;
        ResultSet rset = null;
        try {
            ps = connection.prepareStatement("SELECT MAX(crn),code,course_number,course_name FROM course GROUP BY code, course_number, course_name ORDER BY code");
            rset = ps.executeQuery();
            while(rset.next()){
                courses.add(new Course(rset.getInt(1), rset.getString(2), rset.getInt(3), rset.getString(4)));
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e.getMessage());
        } finally {
            clean(rset, ps);
        }
        return courses;
    }

    private ArrayList<PreferenceFormRecord> getFacultyPreferenceForms(){
        ArrayList<PreferenceFormRecord> preference_forms = new ArrayList<PreferenceFormRecord>();
        PreparedStatement ps = null;
        ResultSet rset = null;
        try {
            ps = connection.prepareStatement("SELECT preference_form_id, n_number, date_added FROM preference_form WHERE date_added IS NOT NULL ORDER BY date_added DESC");
            rset = ps.executeQuery();
            while(rset.next()){
                preference_forms.add(new PreferenceFormRecord(rset.getInt("preference_form_id"),rset.getInt("n_number"), rset.getDate("date_added")));
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e.getMessage());
        } finally {
            clean(rset, ps);
        }
        return preference_forms;
    }

    private static void clean(ResultSet rset, Statement stmt){
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
                if(num > 0 && num <= last) return num;
            } catch (Exception e) { /* Politely ignore */ }
            System.out.println("\"===========Please enter a valid selection.===========\"");
        } while (true);
    }

    private String getInput(){
        System.out.print(">>  ");
        return inputReader.nextLine().trim();
    }
}
