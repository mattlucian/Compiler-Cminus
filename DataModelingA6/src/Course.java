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
public class Course {
    
    private int crn;
    private String code;//COP2220
    private String category;//COP
    private int courseNumber;//2220
    private String courseName;

    public Course(){
        super();
    }
    public Course(int crn, String code, String courseName){
        this.crn = crn;
        this.code = code;
        this.courseName = courseName;
    }
    //simple constructor for displaying courses only
    public Course(int crn, String code, int courseNumber, String courseName){
        this.crn = crn;
        this.code = code;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
    }

    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
}
