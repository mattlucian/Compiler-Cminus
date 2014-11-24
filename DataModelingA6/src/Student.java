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
    
}
