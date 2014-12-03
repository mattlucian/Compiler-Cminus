/**
 *
 * 
 *
 */


/**
 *
 * @author chris
 */
public class Entity {
    private Semester semester;
    private int year;

    public Entity(){
        
    }
    public Entity(Semester sem, int yr){
        semester = sem;
        year = yr;
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
    
}
