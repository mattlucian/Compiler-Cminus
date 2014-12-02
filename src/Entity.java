/**
 *
 * 
 *
 */

import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.semester);
        hash = 89 * hash + this.year;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if(obj != null && obj instanceof Entity){
            final Entity other = (Entity)obj;
            return this.semester == other.semester && this.year == other.year;
        }
        return false;
    }
    
}
