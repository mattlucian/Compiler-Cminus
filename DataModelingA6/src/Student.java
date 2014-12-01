/**
 *
 * 
 *
 */

package dmodel;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author chris
 */
public final class Student {
    
    private int n_number;//pk
    private String firstName;
    private String lastName;
    private String degree = "CS";//permanent
    
    private Entity active = new Entity();//active year and semester
    
    //max is 6 per activeSemester
    private Map<Entity, Request> prefmap = new HashMap<Entity, Request>();
    
    
    public Student(){
        super();
    }
    public Student(int id, String first, String last){
        n_number = id;
        firstName = first;
        lastName = last;
    }
    //Test whether student has populated the given activeSemester
    public boolean containsSemester(Semester semester, int year){
        return prefmap.containsKey(new Entity(semester, year));
    }
    //Quickly determine if anything needs to be saved
    public boolean hasAnyData(){
        return !prefmap.isEmpty();
    }
    

    public Semester getActiveSemester() {
        return active.getSemester();
    }
    
    public void setActiveSemester(Semester semester){
        active.setSemester(semester);
    }

    public int getActiveYear() {
        return active.getYear();
    }

    public void setActiveYear(int activeYear) {
        active.setYear(activeYear);
    }
    public Entity getActive(){
        return active;
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
        return prefmap.get(active).getNumberOfSelections();
    }
    /**
     * Add a new CourseChoice to the active semester.
     * @param cc 
     */
    public void addPreferenceToActiveSemester(CourseChoice cc){
        if(!prefmap.containsKey(active)){
            //lazy
            prefmap.put(active, new Request(active.getSemester(), active.getYear()));
        }
        prefmap.get(active).addChoice(cc);
    }
    public CourseChoice getActiveSemesterPreference(int n){
        if(prefmap.containsKey(active)){
            return prefmap.get(active).getChoice(n);
        }
        return null;
    }
    public int getActiveSemesterYear(){
        if(prefmap.containsKey(active)){
            return prefmap.get(active).getYear();
        }
        return -1;
    }
    public boolean containsCourse(int courseCRN){
        
        return prefmap.containsKey(active) && prefmap.get(active).containsCourse(courseCRN);
        
        //prefmap.get(activeSemester).getChoiceList().stream().anyMatch((cc) -> cc.getCourseCRN() == courseCRN);
    }
    /**
     * Returns data for storage into DB
     * @return null if no data collected.
     */
    public Request getData(){
        if(prefmap.containsKey(active)){
            return prefmap.get(active);
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
//    public CourseChoice getPreference(Semester s, int n){
//        if(prefmap.containsKey(s)){
//            return prefmap.get(s).getChoice(n);
//        }
//        return null;
//    }
    
}
