/**
 *
 * 
 *
 */

package dmodel;

/**
 *
 * @author chris
 */
public class CourseChoice {
    
    //day classifications: export as public
    public static final int MON_WED = 1;
    public static final int TUE_THR = 2;
    public static final int MON_WED_FRI = 3;
    
    //time classifications
    public static final int MORNING = 1;
    public static final int EVENING = 2;
    public static final int NIGHT = 3;
    
    private int courseCRN;//maps to DB
    private int days;
    private int times;

    public CourseChoice(){
        
    }
    public CourseChoice(int crn, int day, int time){
        days = day;
        times = time;
        courseCRN = crn;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getCourseCRN() {
        return courseCRN;
    }

    public void setCourseCRN(int courseCRN) {
        this.courseCRN = courseCRN;
    }
    
}
