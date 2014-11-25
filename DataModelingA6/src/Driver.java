/**
 *
 * 
 *
 */

package dmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author chris
 */
public class Driver {
    private static Scanner in = new Scanner(System.in);
    
    private static List<String> courses = new ArrayList<>();//the available course based on chosen semester
    private static final int MAX_COURSES = 6;
    
    //private Student student = new Student();//save info
    
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
        courses.add("CAP4620 - Artifical Intelligence");
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
    //pre-built for speed
    private static final String SEPARATOR = "------------------------------";
    private static final String DAYS_MENU = "\nAvailable Days\n" + SEPARATOR + "\n[1] Monday/Wednesday\n[2] Tuesday/Thursday\n[3] Monday/Wednesday/Friday\n[4] Cancel\n";
    private static final String TIMES_MENU = "\nAvailable Times\n" + SEPARATOR + "\n[1] Morning\n[2] Evening\n[3] Night\n[4] Cancel\n";
    private static final int MAX_DAYS_TIMES = 4;

    public Driver(){
        super();
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
    public static void studentSelection(Student student){
        //this data already secured
        //prepare available courses for chosen semester
        int selectedCourse;
        int selectedDays;
        int selectedTimes;
        int choices = 0;
        
        while(showNavMenu(choices >= MAX_COURSES, choices)){
            selectedCourse = showCourseMenu(courses);
            if(selectedCourse == courses.size() + 1){
                System.out.println("Cancelling current course");
                continue;
            } else if(student.containsCourse(selectedCourse)) {
                //Handle selecting the same course multiple times
                System.out.println("Course has already been chosen. Please try another.");
                continue;
            }
            selectedDays = showDaysMenu();
            if(selectedDays == MAX_DAYS_TIMES){
                System.out.println("Cancelling current course");
                continue;
            }
            selectedTimes = showTimesMenu();
            if(selectedTimes == MAX_DAYS_TIMES){
                System.out.println("Cancelling current course");
                continue;
            }
            
            //log the entire choice
            student.addPreference(new CourseChoice(selectedCourse, selectedDays, selectedTimes, courses.get(selectedCourse - 1)));
            //mapping from selection to course title: selectedCourse => courses.get(selectedCourse - 1)
            choices++;
            System.out.println("Course logged.");
        }
        
        
        System.out.println("Have a nice day.");
    }
    /**
     * Main Navigation Menu
     * 
     * Returns true to select a class
     * False means cancel or abort
     * @param reachedLimit
     * @return 
     */
    public static boolean showNavMenu(boolean reachedLimit, int chosen){
        int temp = 1;
        StringBuilder result = new StringBuilder();
        result.append(String.format("\nNavigation Menu  [Classes: %d]\n", chosen)).append(SEPARATOR).append('\n');
        if(!reachedLimit){
            result.append(String.format("[%d] Select a class\n", temp++));
        }
        result.append(String.format("[%d] Quit and submit\n", temp));
        
        System.out.println(result.toString());
        return getChoice(temp) != temp;
    }
    /**
     * Available Courses Menu
     * @param available
     * @return 
     */
    public static int showCourseMenu(List<String> available){
        StringBuilder result = new StringBuilder();
        result.append("\nAvailable Course\n");
        result.append(String.format("%s\n", SEPARATOR));
        int size = available.size();
        for (int c = 0; c < size; c++) {
            result.append(String.format("[%2d] %s\n", (c + 1), available.get(c)));
        }
        result.append(String.format("\n[%2d] Cancel\n", size + 1));
        System.out.println(result.toString());
        return getChoice(size + 1);
        
    }
    /**
     * Days Menu
     * 
     * Returns false to cancel
     */
    public static int showDaysMenu(){
        System.out.println(DAYS_MENU);
        return getChoice(MAX_DAYS_TIMES);
        
    }
    /**
     * Times Menu
     * 
     * @return 
     */
    public static int showTimesMenu(){
        System.out.println(TIMES_MENU);
        return getChoice(MAX_DAYS_TIMES);
    }
    /**
     * Retrieves user's choice following a menu.
     * @param last valid menu choice
     * @return selection as integer
     */
    private static int getChoice(int last){
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
    
    //Testing
    public static void main(String[] args) {
        Student chris = new Student(1234, "Chris", "Raley");
        Driver.studentSelection(chris);
        
    }
}
