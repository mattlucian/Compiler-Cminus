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
    
    //private Semester semester;
    //year is bound to chosen semeseter!
    //private int year;
    private Entity period;
    //Upto 6 course selections
    private List<CourseChoice> selections = new ArrayList<CourseChoice>();

    public Request(){
        
    }
    public Request(Semester s, int yr){
        //semester = s;
        //year = yr;
        period = new Entity(s,yr);
    }

    public Semester getSemester() {
        return period.getSemester();
    }

    public void setSemester(Semester semester) {
        period.setSemester(semester);
    }

    public int getYear() {
        return period.getYear();
    }

    public void setYear(int year) {
        period.setYear(year);
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
