/**
 *
 * 
 *
 */

package dmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author chris
 */
public class Driver {
    private static Scanner in = new Scanner(System.in);
    private static final String separator = "---------------------------";
    
    private static List<String> courses = new ArrayList<>();
    private static List<Integer> chosen = new ArrayList<>();
    private static final int MAX_COURSES = 6;
    
    private Student student = new Student();//save info
    
    //pretend initializer
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

    public Driver(){
        
    }
    
    
    public static void showStudentMenu(){
        
        System.out.println("\n\n\tStudent Menu");
        System.out.println(String.format("%s", separator));
        System.out.println("[1] Select courses");
        System.out.println("[2] Select days of week");
        System.out.println("[3] Select time of day");
        System.out.println("[5] Back");
        System.out.println("[6] Quit");
        
        int choice = getChoice(6);
        switch(choice){
            case 1:
                if(chosen.size() < MAX_COURSES){
                    showCourseMenu();
                } else {
                    System.out.println("Course Limit Has Been Reached.");
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                System.out.println("You chose " + chosen.size() + " selections");
                return;
        }
        
    }
    public static void showCourseMenu(){
        StringBuilder result = new StringBuilder();
        result.append("Available Course\n");
        int size = courses.size();
        for (int c = 0; c < size; c++) {
            result.append(String.format("[%2d] %s\n", (c + 1), courses.get(c)));
        }
        result.append(String.format("\n[%2d] Cancel\n", size));
        System.out.println(result.toString());
        int choice = getChoice(courses.size() + 1);
        //record selection
        if(choice < size){
            chosen.add(choice);
        }
        
    }
    public static void showDaysMenu(){
        
        System.out.println("[1] Monday");
        System.out.println("[2] Tuesday");
        System.out.println("[3] Wednesday");
        System.out.println("[4] Thursday");
        System.out.println("[5] Friday");
        System.out.println("[6] Cancel");
        int choice = getChoice(5);
        
    }
    public static void showTimesMenu(){
        
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
}
