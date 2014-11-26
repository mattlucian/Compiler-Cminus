/**
 *
 * 
 *
 */

package dmodel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chris
 */
public class Request {
    
    private Semester semester;
    //year is bound to chosen semeseter!
    private int year = 2015;
    //Upto 6 course selections
    List<CourseChoice> selections = new ArrayList<>();

    public Request(){
        
    }
    public Request(Semester s){
        semester = s;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public void addChoice(CourseChoice cc){
        selections.add(cc);
    }
    public CourseChoice getChoice(int n){
        return selections.get(n);
    }
    public int getNumberOfSelections(){
        return selections.size();
    }
    public boolean hasData(){
        return !selections.isEmpty();
    }
    public List<CourseChoice> getChoiceList(){
        return selections;
    }
    public boolean containsCourse(int crn){
        int size = selections.size();
        for (int p = 0; p < size; p++) {
            if(selections.get(p).getCourseCRN() == crn){
                return true;
            }
        }
        return false;
    }
}
