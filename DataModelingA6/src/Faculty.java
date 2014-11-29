import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Nick on 11/24/14.
 */
public class Faculty {
    private static List<String> courses = new ArrayList<String>();//the available course based on chosen semester
    public static int current_fac_id = 123;

    //pretend initializer: in reality, it will change based on chosen semester
    static {
        courses.add("COP2220 - Computer Science I");
        courses.add("COT3100 - Comp Structures");
        courses.add("COP3503 - Computer Science II");
        courses.add("COP3530 - Data Structures");
        courses.add("CDA3101 - Intro to Computer Hardware");
        courses.add("CIS4253 - Legal Ethical Issues");
        courses.add("COP4710 - Data Modeling");
        courses.add("COP4610 - Operating Systems");
        courses.add("COT3210 - Computability and Automata");
        courses.add("COP3601 - Intro to Systems Software");
        courses.add("COP4620 - Constr of Language Trans");
        courses.add("CEN4010 - Software Engineering");
        courses.add("CNT4504 - Networking and Distributed Processing");
        courses.add("COP4813 - Internet Programming");
        courses.add("CAP4620 - Artificial Intelligence");
        courses.add("CAP4831 - Modeling & Simulation");
        courses.add("CAP4770 - Data Mining");
        courses.add("CEN4801 - Systems Integration");
        courses.add("COT4400 - Analysis of Algorithms");
        courses.add("COT4111 - Comp Structures II");
        courses.add("COT4461 - Computational Biology");
        courses.add("COT4560 - Applied Graph Theory");
        courses.add("CIS4362 - Computer Cryptography");
        courses.add("CEN4943 - Software Dev Practicum");
    }

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
            System.out.println("1. Enter New Course Preference Form");
            System.out.println("2. View / Edit Previous Course Preference Forms");
            System.out.println("3. Request Course Print-Out");
            System.out.println("4. Log out");
            choice = inputReader.nextInt();

            if (choice == 1) {
                newCoursePreferenceForm();
            } else if (choice == 2) {
                viewCoursePreferenceForms();
            } else if (choice == 3) {
                requestCoursePrintout();
            } else if (choice == 4) {
                // log out
            }  else {
                System.out.println("Error, invalid selection please try again");
            }

        }while(choice != 4);
        System.out.println("Logged out, exiting program");
    }
    //endregion

    //submit a new course preference form
    public void newCoursePreferenceForm(){

        //sql to create new course preference form
        System.out.println("Creating a new Course Preference Form...");
        PreferenceFormRecord preference_form = new PreferenceFormRecord(current_fac_id);

        System.out.println("Preference Form Id before insert:" + preference_form.preference_form_id);
        if(preference_form.insertPreferenceFormRecord(connection))
        {
            System.out.println("Preference Form Id after insert:" + preference_form.preference_form_id);
            //view the course preference form menu
            coursePreferenceFormMenu(preference_form);
        }

        System.out.println("An error occurred creating your new course preference form.");

    }

    public void viewCoursePreferenceForms(){

        int choice = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        PreferenceFormRecord preference_form;

        //load Course Preference Forms
        ArrayList<String> coursePreferenceForms = new ArrayList<String>();
        coursePreferenceForms.add(dateFormat.format(date));

        //menu to select a course preference form
        System.out.println("Available Course Preference Forms:");

        for(int i = 0; i < coursePreferenceForms.size(); i++) {
            System.out.println((i+1)+". "+coursePreferenceForms.get(i));
        }
        System.out.println((coursePreferenceForms.size()+1)+". Back to Faculty Menu");
        choice = inputReader.nextInt();

        if (choice < 0 || choice > coursePreferenceForms.size()+1) {
            System.out.println("Error, invalid selection please try again");
        } else if(choice != coursePreferenceForms.size()+1){

            preference_form = PreferenceFormRecord.loadById(connection, choice);
            //dump the course preference form information
            System.out.println("All data currently related to Course Preference Form:");

            //show options for managing course preference form
            coursePreferenceFormMenu(preference_form);
        }

    }

    public void editCoursePreferenceForm(PreferenceFormRecord preference_form)
    {
        System.out.println("You are editing Course Preference Form ID#" + preference_form.preference_form_id);

        coursePreferenceFormMenu(preference_form);
    }

    public void coursePreferenceFormMenu(PreferenceFormRecord preference_form)
    {
        int choice = 0;

        //show menu of editable items
        do{
            System.out.println("You are managing Course Preference Form #" + preference_form.preference_form_id +": \n");
            System.out.println("Course Preference Form Menu:");
            System.out.println("1. Course Rankings");
            System.out.println("2. Fall Preferences");
            System.out.println("3. Spring Preferences");
            System.out.println("4. Summer Preferences");
            System.out.println("5. Back to Faculty Menu");
            choice = inputReader.nextInt();

            if (choice == 1) {
                courseRankingMenu(preference_form);
            } else if (choice == 2) {
                semesterPreferenceForm(preference_form, "Fall");
            } else if (choice == 3) {
                semesterPreferenceForm(preference_form, "Spring");
            } else if (choice == 4) {
                semesterPreferenceForm(preference_form, "Summer");
            } else if (choice == 5) {
                //back to Faculty menu
            }  else {
                System.out.println("Error, invalid selection please try again");
            }

        }while(choice != 5);

    }

    //region Reporting Menu Functions
    public void requestCoursePrintout(){
        System.out.println("Your request has been received. Please contact Professor Abbassi for your paper copy.");
    }

    public void courseRankingMenu(PreferenceFormRecord preference_form)
    {
        int choice = 0;
        ArrayList<String> coursesRanked = new ArrayList<String>();
        String cardinalPosition = "first";

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
            System.out.println("Please select your " + cardinalPosition + " choice:");

            for(int i = 0; i < courses.size(); i++) {
                System.out.println((i+1)+". "+courses.get(i));
            }
            System.out.println((courses.size()+1)+". Done ranking courses");
            choice = inputReader.nextInt();

            if (choice < 0 || choice > courses.size()+1) {
                System.out.println("Error, invalid selection please try again");
            } else if(choice != courses.size()+1){
                //add course ranking record
                coursesRanked.add(courses.get(choice-1));
            }

            if(coursesRanked.size() >= 5) {
                System.out.println("You have ranked the maximum number of courses for this form.");
            }

        }while(choice != (courses.size()+1) && coursesRanked.size() < 5);

        //Display Courses Ranked
        System.out.println("Courses Ranked: " + coursesRanked.size());
        for(int i = 0; i < coursesRanked.size(); i++)
        {
            System.out.println((i+1) + ": " + coursesRanked.get(i));
        }
    }

    public void semesterPreferenceForm(PreferenceFormRecord preference_form, String semester)
    {
        int choice = 0;
        FormSemesterInfoRecord form_semester_info = FormSemesterInfoRecord.loadOne(connection, preference_form, semester);

        //show menu of editable items
        do{
            System.out.println("Semester Preference Form Menu:");
            System.out.println("Semester: " + semester);

            System.out.println("1. Course Load Preference");
            System.out.println("2. Scheduling Factors");
            System.out.println("3. Times of Day Preference");
            System.out.println("4. Days of Week Preference");
            System.out.println("5. Back to Course Preference Forms Menu");

            choice = inputReader.nextInt();
            if (choice == 1) {
                courseLoadPreference(form_semester_info);
            } else if (choice == 2) {
                schedulingFactors(form_semester_info);
            } else if (choice == 3) {
                timesOfDayPreference(form_semester_info);
            } else if (choice == 4) {
                daysOfWeekPreference(form_semester_info);
            } else if (choice == 5) {
                // back to Course Preference Forms Menu
            }  else {
                System.out.println("Error, invalid selection please try again");
            }
        }while(choice != 5);
    }

    public void courseLoadPreference(FormSemesterInfoRecord form_semester_info)
    {
        int choice = 0;

        do {
            System.out.println("How many courses would you like to teach this semester?");
            choice = inputReader.nextInt();
            if(choice < 1 || choice > 3)
            {
                System.out.println("Invalid choice, please select a number between 1 and 3.");
            }
            else
            {
                System.out.println("Your selection has been saved.");
            }
        }while(choice < 1 || choice > 3);
    }

    public void schedulingFactors(FormSemesterInfoRecord form_semester_info)
    {
        String input = "";
        boolean inputValid = false;
        System.out.println("Enter in a, b, and c in the corresponding order of importance to your scheduling.\n" +
                "For example \"c a b\" to indicate that Times of the Day are the most important factor, \n" +
                "and Days of the Week are the lease important factor:");
        System.out.println("a: Course Preference \tb: Days of the Week \tc: Times of the Day");

        //clear buffer of any existing input
        inputReader.nextLine();

        do {
            inputValid = false;
            input = inputReader.nextLine().trim();
            if(input.split(" ").length != 3 && !input.equals("quit"))
            {
                System.out.println("Invalid input. Enter \"quit\" to go back to the previous menu.");
            }
            else
            {
                if(!input.equals("quit"))
                {
                    String[] tokens = input.split(" ");

                    if(tokens[0].length()==1 && tokens[1].length()==1 && tokens[2].length()==1)
                    {
                        inputValid = true;
                        System.out.println("Your selection has been saved.");
                    }
                    else
                    {
                        System.out.println("Your input was invalid. Please try again.");
                    }
                }
                else
                {
                    inputValid = true;
                }
            }
        }while(!inputValid);
    }

    public void timesOfDayPreference(FormSemesterInfoRecord form_semester_info)
    {
        String input = "";
        boolean inputValid = false;

        System.out.println("Enter in a, b, and c in the corresponding order of preference for Time of the Day.\n" +
                "For example \"c a b\" to indicate that you prefer Evening, \n" +
                "and you least prefer Afternoon:");
        System.out.println("a: Morning \nb: Afternoon \nc: Evening");

        //clear buffer of any existing input
        inputReader.nextLine();

        do {
            inputValid = false;
            input = inputReader.nextLine().trim();
            if(input.split(" ").length != 3 && !input.equals("quit"))
            {
                System.out.println("Invalid input. Enter \"quit\" to go back to the previous menu.");
            }
            else
            {
                if(!input.equals("quit"))
                {
                    String[] tokens = input.split(" ");

                    if(tokens[0].length()==1 && tokens[1].length()==1 && tokens[2].length()==1)
                    {
                        inputValid = true;
                        System.out.println("Your selection has been saved.");
                    }
                    else
                    {
                        System.out.println("Your input was invalid. Please try again.");
                    }
                }
                else
                {
                    inputValid = true;
                }
            }
        }while(!inputValid);
    }

    public void daysOfWeekPreference(FormSemesterInfoRecord form_semester_info)
    {
        String input = "";
        boolean inputValid = false;

        System.out.println("Enter in a, b, and c in the corresponding order of preference for Days of the Week.\n" +
                "For example \"c a b\" to indicate that you prefer TR, \n" +
                "and you least prefer MW:");
        System.out.println("a: MWF (3 credits, 7am - 3pm) \nb: MW \nc: TR");

        //clear buffer of any existing input
        inputReader.nextLine();

        do {
            inputValid = false;
            input = inputReader.nextLine().trim();
            if(input.split(" ").length != 3 && !input.equals("quit"))
            {
                System.out.println("Invalid input. Enter \"quit\" to go back to the previous menu.");
            }
            else
            {
                if(!input.equals("quit"))
                {
                    String[] tokens = input.split(" ");

                    if(tokens[0].length()==1 && tokens[1].length()==1 && tokens[2].length()==1)
                    {
                        inputValid = true;
                        System.out.println("Your selection has been saved.");
                    }
                    else
                    {
                        System.out.println("Your input was invalid. Please try again.");
                    }
                }
                else
                {
                    inputValid = true;
                }
            }
        }while(!inputValid);
    }
}