/**
 *
 * 
 *
 */

package dmodel;

import java.util.ArrayList;
import java.util.List;


/**
 * n-num, first, last, cs-degree
 * @author chris
 */
public final class Student {
    private int n_number;
    private String firstName;
    private String lastName;
    private String semester;
    //max is 6
    private List<CourseChoice> prefs = new ArrayList<>();
    
    public Student(){
        super();
    }
    public Student(int id, String first, String last){
        n_number = id;
        firstName = first;
        lastName = last;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
    public int prefSize(){
        return prefs.size();
    }
    public void addPreference(CourseChoice cc){
        prefs.add(cc);
    }
    public CourseChoice getPreference(int n){
        return prefs.get(n);
    }
    public boolean containsCourse(int course){
        //Pre Java 8
//        int size = prefs.size();
//        for (int p = 0; p < size; p++) {
//            if(prefs.get(p).getCourseIndex() == course){
//                return true;
//            }
//        }
//        return false;
        
        return prefs.stream().anyMatch((cc) -> cc.getCourseIndex() == course);
    }
}
