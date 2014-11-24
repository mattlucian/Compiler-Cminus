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
    
    //days
    public static final int MON_WED = 1;
    public static final int TUE_THR = 2;
    public static final int MON_WED_FRI = 3;
    
    //times
    public static final int MORNING = 1;
    public static final int EVENING = 2;
    public static final int NIGHT = 3;
    
    private int courseIndex;
    private int days;
    private int times;

    public CourseChoice(){
        
    }
    public CourseChoice(int course, int day, int time){
        courseIndex = course;
        days = day;
        times = time;
    }

    public int getCourseIndex() {
        return courseIndex;
    }

    public void setCourseIndex(int courseIndex) {
        this.courseIndex = courseIndex;
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
    
}
