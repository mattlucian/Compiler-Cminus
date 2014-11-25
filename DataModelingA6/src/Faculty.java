import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Nick on 11/24/14.
 */
public class Faculty {
    private static List<String> courses = new ArrayList<String>();//the available course based on chosen semester

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
        courses.add("CAP4770 - Data Mining ");
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
        System.out.println("You are creating a new Course Preference Form");
        int coursePreferenceFormId = 1;

        //view the course preference form menu
        coursePreferenceFormMenu(coursePreferenceFormId);
    }

    public void viewCoursePreferenceForms(){

        //list all of the course preference forms

        //menu to select a course preference form

        //dump the course preference form information

    }

    public void editCoursePreferenceForm(int coursePreferenceFormId)
    {
        System.out.println("You are editing Course Preference Form ID#" + coursePreferenceFormId);

        coursePreferenceFormMenu(coursePreferenceFormId);
    }

    public void coursePreferenceFormMenu(int coursePreferenceFormId)
    {
        int choice = 0;

        //show menu of editable items
        do{
            System.out.println("You are managing Course Preference Form #" + coursePreferenceFormId +": \n");
            System.out.println("Course Preference Form Menu:");
            System.out.println("1. Course Rankings");
            System.out.println("2. Fall Preferences");
            System.out.println("3. Spring Preferences");
            System.out.println("4. Summer Preferences");
            System.out.println("5. Log out");
            choice = inputReader.nextInt();

            if (choice == 1) {
                courseRankingMenu(coursePreferenceFormId);
            } else if (choice == 2) {
//                semesterPreferenceForm("Fall");
            } else if (choice == 3) {
//                semesterPreferenceForm("Spring");
            } else if (choice == 4) {
//                semesterPreferenceForm("Summer");
            } else if (choice == 5) {
                // log out
            }  else {
                System.out.println("Error, invalid selection please try again");
            }

        }while(choice != 4);

    }

    //region Reporting Menu Functions
    public void requestCoursePrintout(){
        System.out.println("Your request has been received. Please contact Professor Abbassi for your paper copy.");
    }

    public void courseRankingMenu(int coursePreferenceFormId)
    {
        int choice = 0;
        int coursesRanked = 0;

        //show menu of editable items
        do{
            System.out.println("Please select your first choice:");

            for(int i = 0; i < courses.size(); i++) {
                System.out.println((i+1)+". "+courses.get(i));
            }
            System.out.println((courses.size()+1)+". Done ranking courses");
            choice = inputReader.nextInt();

            if (choice < 0 || choice > courses.size()) {
                System.out.println("Error, invalid selection please try again");
            } else {
                //add course ranking record
                coursesRanked++;
            }

            if(coursesRanked >= 5) {
                System.out.println("You have ranked the maximum number of courses for this form.");
            }
            else {
                System.out.println("Courses Ranked: " + coursesRanked);
            }

        }while(choice != (courses.size()+1) && coursesRanked < 5);

    }
}
