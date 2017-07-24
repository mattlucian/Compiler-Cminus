import java.util.ArrayList;

/**
 * Created by matt on 2/26/15.
 */
public class Transition {

    public boolean isTerminal = false;
    public String label = "";
    public Transition parentTransition = null;
    public ArrayList<Choices> choices = new ArrayList<Choices>();


    Transition(Transition t){
        this.isTerminal = t.isTerminal;
        this.label = t.label;
        this.choices = t.choices;
    }

    Transition(String lbl, boolean terminal){
        this.label = lbl;
        this.isTerminal = terminal;
    }

    public void addChoices(Choices[] choices){
        for(Choices c : choices){
            this.choices.add(c);
        }
    }

    public boolean equals(Object o){
        Transition t = (Transition)o;
        return (this.label.equals(t.label));
    }

    public String toString(){
        return this.label + " : "+this.choices.size();
    }
}

class Choices {
    public ArrayList<Transition> transitions = new ArrayList<Transition>();
    public boolean foundSomething = false;
    Choices(){

    }
    Choices(Transition[] trans){
        for(Transition t : trans){
            transitions.add(t);
        }
    }
    public void addTransition(Transition t){
        transitions.add(t);
    }
}
