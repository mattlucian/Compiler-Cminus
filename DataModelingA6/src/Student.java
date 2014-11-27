/**
 *
 * 
 *
 */

package dmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author chris
 */
public final class Student {
    
    private int n_number;//pk
    private String firstName;
    private String lastName;
    private String degree = "CS";//fixed
    //Active semester only!
    private Semester activeSemester;
    
    
    //private int year = 2014;
    //max is 6 per activeSemester
    private Map<Semester, Request> prefmap = new HashMap<Semester, Request>();
    
    
    public Student(){
        super();
    }
    public Student(int id, String first, String last){
        n_number = id;
        firstName = first;
        lastName = last;
    }
    //Test whether student has populated the given activeSemester
    public boolean containsSemester(Semester semester){
        return prefmap.containsKey(semester);
    }
    //Quickly determine if anything needs to be saved
    public boolean hasAnyData(){
        return !prefmap.isEmpty();
    }
    

    public Semester getActiveSemester() {
        return activeSemester;
    }
    
    public void setActiveSemester(Semester semester){
        this.activeSemester = semester;
    }

    public int getN_number() {
        return n_number;
    }

    public void setN_number(int n_number) {
        this.n_number = n_number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Semester getSemester() {
        return activeSemester;
    }

    public void setSemester(Semester semester) {
        this.activeSemester = semester;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
    
    //Active Semester methods
    /**
     * For the active semester, the request object is lazily
     * created on the actual preference add.
     * 
     * @return 
     */
    
    public int getActiveSemesterCourseCount(){
        return prefmap.get(activeSemester).getNumberOfSelections();
    }
    /**
     * Add a new CourseChoice to the active semester.
     * @param cc 
     */
    public void addPreferenceToActiveSemester(CourseChoice cc){
        if(!prefmap.containsKey(activeSemester)){
            //lazy
            prefmap.put(activeSemester, new Request(activeSemester));
        }
        prefmap.get(activeSemester).addChoice(cc);
    }
    public CourseChoice getActiveSemesterPreference(int n){
        if(prefmap.containsKey(activeSemester)){
            return prefmap.get(activeSemester).getChoice(n);
        }
        return null;
    }
    public int getActiveSemesterYear(){
        if(prefmap.containsKey(activeSemester)){
            return prefmap.get(activeSemester).getYear();
        }
        return -1;
    }
    public boolean containsCourse(int courseCRN){
        
        return prefmap.containsKey(activeSemester) && prefmap.get(activeSemester).containsCourse(courseCRN);
        
        //prefmap.get(activeSemester).getChoiceList().stream().anyMatch((cc) -> cc.getCourseCRN() == courseCRN);
    }
    /**
     * Returns data for storage into DB
     * @return null if no data collected.
     */
    public Request getData(){
        if(prefmap.containsKey(activeSemester)){
            return prefmap.get(activeSemester);
        }
        return null;
    }
    //End Active Semester methods
    
    /**
     * Access any of the student's semesters.
     * @param s
     * @param n
     * @return 
     */
    public CourseChoice getPreference(Semester s, int n){
        if(prefmap.containsKey(s)){
            return prefmap.get(s).getChoice(n);
        }
        return null;
    }
    
}
