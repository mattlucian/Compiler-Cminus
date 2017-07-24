/*
import java.util.ArrayList;
import java.util.Stack;

*/
/**
 * Created by matt on 4/12/15.
 *//*

class CodeGeneration {

    ArrayList<Generation> generatedCode = new ArrayList<Generation>();
    ArrayList<Generation> standbyCodes = new ArrayList<Generation>();
    Stack<LexToken> operators = new Stack<LexToken>();
    ArrayList<LexToken> output = new ArrayList<LexToken>();

    Stack<LexToken> paused_operators = null;
    ArrayList<LexToken> paused_output = null;

    public boolean evaluating = false;
    public boolean assignTemp = false;
    public boolean argTemp = false;
    public String opTrigger = "null";
    public String assignVal = "";

    public Generation specialLine = null;
    public Generation tempSpecial = null;

    String currentRelation = "";
    int blockBeginning = 0;

    int depth = 0;

    String currentTempVariable = "_t0";
    int currentTempCount = 0;

    public void addLine(String one, String two, String three, String four){
        generatedCode.add(new Generation((generatedCode.size()+1), one, two, three, four));
    }

    public void putLineOnStandby(String one, String two, String three, String four){
        standbyCodes.add(new Generation(one, two, three, four));
    }

    public void putSpecialLineOnStandby(int position, String one, String two, String three, String four){
        Generation g = new Generation(position+1,one, two, three, four);
        specialLine = g;
    }

    public void storeSpecialLine(){
        tempSpecial = specialLine;
    }

    public void dropSpecialLine(){
        if(specialLine == null)
            return;

        generatedCode.add(specialLine.line,specialLine);
        for(int i = specialLine.line; i < generatedCode.size(); i++){
            generatedCode.get(i).line = generatedCode.get(i).line + 1;
        }
        specialLine = null;
    }

    public void printLines(){
        for(Generation g : generatedCode)
            System.out.println(g.toString());
    }

    public void pauseEvaluations(){
        evaluating = false;
        paused_operators = operators;
        paused_output = output;

        output = new ArrayList<LexToken>();
        operators = new Stack<LexToken>();
    }

    public void resumeEvaluations(){
        output = paused_output;
        operators = paused_operators;
    }

    public String getSizeForType(String type){
        if(type.equals("int")){
            return "4";
        }else if(type.equals("float")){
            return "8";
        }else{
            return "0";
        }
    }


    public void checkLastOperation(int weight, LexToken operation){

        if(operators.size() == 0){
            operators.push(operation);
            return;
        }

        LexToken last = operators.peek();
        if(last.value.equals("+") || last.value.equals("-")){
            if(weight == 1){
                // + or -
                while(operators.size() > 0){
                    last = operators.pop(); // * or / to start

                    if(last.value.equals("(")){
                        break;
                    }

                    output.add(last);
                }
                operators.push(operation);

            }else if(weight == 2){
                // * or /
                operators.push(operation);

            }else if(weight == 3){ // <, >, ==, != etc
                while(operators.size() > 0){
                    last = operators.pop(); // * or / to start

                    if(last.value.equals("(")){
                        break;
                    }

                    output.add(last);
                }
                operators.push(operation);
            }

        }else if(last.value.equals("*") || last.value.equals("/")){

            while(operators.size() > 0){

                last = operators.peek();
                if(last.value.equals("+") || last.value.equals("-"))
                    break;

                last = operators.pop(); // * or / to start

                if(last.value.equals("(")){
                    break;
                }

                output.add(last);
            }
            operators.push(operation);
        }

    }

    public void addEvalToken(LexToken t){
        if(t.token_type.equals("ID:") || t.token_type.equals("FLON:") || t.token_type.equals("NUM:")){
            output.add(t);

        }else if(t.token_type.equals("")){

            LexToken temp = null;


            //todo
            // account for arrays

            if(t.value.equals("(")){
                operators.push(t);
            }else if(t.value.equals("+")){

                checkLastOperation(1,t); // weight, token

            }else if(t.value.equals("-")){

                checkLastOperation(1,t); // weight, token

            }else if(t.value.equals("*")){

                checkLastOperation(2,t); // weight, token

            }else if(t.value.equals("/")){

                checkLastOperation(2,t); // weight, token

            }else if(t.value.equals(")")){

                while((temp = operators.pop()).value.equals("(")){
                    output.add(temp);
                }
                operators.pop(); // (

            }else if(isRelational(t)){

                checkLastOperation(3,t);

            }
        }
    }

    public void stopEvaluating(){
        while(operators.size() > 0){
            output.add(operators.pop());
        }
        this.evaluating = false;

        evaluate();

    }

    public String convertTypeToSize(Enums.VariableType type){
        if(type.equals(Enums.VariableType.FLOAT))
            return "4";
        else
            return "4";
    }

    public boolean isOperator(LexToken t){
        if(t.token_type.equals("")){
            if(t.value.equals("+")){
                return true;
            }else if(t.value.equals("-")){
                return true;
            }else if(t.value.equals("/")){
                return true;
            }else if(t.value.equals("*")){
                return true;
            }else{
                return isRelational(t);
            }
        }else{
            return false;
        }
    }

    public boolean isRelational(LexToken t){
        if(t.token_type.equals("")){
            if(t.value.equals("==")){
                currentRelation = "NEQ";
                return true;
            }else if(t.value.equals("!=")){
                currentRelation = "EQ";
                return true;
            }else if(t.value.equals("<=")){
                currentRelation = "GT";
                return true;
            }else if(t.value.equals(">=")){
                currentRelation = "LT";
                return true;
            }else if(t.value.equals(">")){
                currentRelation = "LTEQ";
                return true;
            }else if(t.value.equals("<")){
                currentRelation = "GTEQ";
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    public String operationLabel(String op){

        if(op.equals("*")){
            return "times";
        }else if(op.equals("/")){
            return "div\t";
        }else if(op.equals("+")){
            return "add\t";
        }else if(op.equals("-")){
            return "sub\t";
        }else if(isRelational(new LexToken(0,"",op,0))){
            return "comp";
        }else{
            return "";
        }

    }

    public void evaluate(){

        while(output.size() > 1){
            for(int i = 0; i < output.size(); i++){
                if(isOperator(output.get(i))){
                    LexToken operator = output.get(i);
                    output.remove(i); // operator gone
                    LexToken var1 = output.get(i-2);
                    LexToken var2 = output.get(i-1);
                    output.remove(i-1); //var2 gone

                    String next = nextTVariable();
                    LexToken n = new LexToken(0,"",next,0);

                    output.set(i-2,n); // var1 replaced

                    this.addLine(operationLabel(operator.value),var1.value,var2.value,next);
                    break;
                }

            }
        }
        output = new ArrayList<LexToken>();


    }

    public void dropLines(){
        int current = generatedCode.size()+1;
        for(Generation g : standbyCodes){
            g.line = current++;
            generatedCode.add(g);
        }
        standbyCodes = new ArrayList<Generation>();
    }

    public String nextTVariable(){
        if(!assignTemp)
            assignTemp = true;
        if(!argTemp)
            argTemp = true;

        String t = "_t";
        t += currentTempCount++;
        currentTempVariable = t;
        return t;
    }
}

class Generation{

    public String operation;
    public String column2;
    public String column3;
    public String result;
    public int line;

    Generation(int l, String o, String c2, String c3, String r){
        this.operation = o;
        this.column2 = c2;
        this.column3 = c3;
        this.result = r;
        this.line = l;
    }
    Generation( String o, String c2, String c3, String r){
        this.operation = o;
        this.column2 = c2;
        this.column3 = c3;
        this.result = r;
    }

    public String toString(){
        if(operation.isEmpty())
            operation = "";
        if(column2.isEmpty())
            column2 = "";
        if(column3.isEmpty())
            column3 = "";



        return line+"\t"+operation+"\t\t"+tabbedString(column2)+"\t"+tabbedString(column3)+"\t"+ result;
    }

    public String tabbedString(String input){
        if(input.length() < 4)
            input += "\t\t";
        else if(input.length() < 6)
            input += "\t";
        return input;
    }
}

*/
