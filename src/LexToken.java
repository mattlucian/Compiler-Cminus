/**
 * Created by matt on 1/14/15.
 */
public class LexToken {

    public String value = "";
    public String token_type = "";
    public int lineNumber = -1;
    public int bracketLvl;

    LexToken(int line, int bracketLvl){
        this.lineNumber = line;
        this.bracketLvl = bracketLvl;
    }
    LexToken(int line, String val, int bracketLvl) {
        this.lineNumber = line;
        this.value = val;
        this.bracketLvl = bracketLvl;
    }
    LexToken(int line, String type, String val, int bracketLvl){
        this.lineNumber = line;
        this.token_type = type;
        this.value = val;
        this.bracketLvl = bracketLvl;
    }

    public String toString(){
        if(token_type.equals("ID:")) {
            return ""+token_type+" "+value + " (Depth: "+bracketLvl+")";
        }
        else if(!token_type.equals(""))
            return ""+token_type+" "+value;
        else return value;
    }

}
