import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by matt on 3/26/15.
 */
public class Semantics {

    private int index = 0;
    private ArrayList<LexToken> tokens;
    private LexToken currentToken;
    private Scope scope;
    private boolean beginTrackingDepth = false;
    protected CodeGeneration cg = new CodeGeneration();
    public String currentCompare = "";


    private Enums.VariableType tempCurrentType = null;
    private Enums.VariableType tempLastType = null;
    private Enums.VariableType lastType = null;
    private Enums.VariableType currentType = null;
    private Symbol currentSymbol = null;


    Semantics(ArrayList<LexToken> t){
        this.tokens = t;
        this.scope = new Scope();
        nextToken();
    }

    public Scope convertTokens(){

        Function currentFunction = null;


        while(currentToken != null){

            //region Keyword
            if(currentToken.token_type.equals("KEYWORD:")){

                if(currentToken.value.equals("return")){

                    boolean valid = false;
                    if(currentFunction.type.equals(Enums.VariableType.VOID)){
                        if(!peek().value.equals(";"))
                            Compiler.printError("cannot return value in a void function");
                        else {
                            nextToken();
                            valid = true;
                        }
                    }
                    if(!valid)
                        checkIfValidReturnExpression(currentFunction);

                    currentFunction.hasReturn = true;

                    currentSymbol = null; //reset
                    continue;

                }else if(currentToken.value.equals("if") || currentToken.value.equals("while") || currentToken.value.equals("else")){

                    cg.opTrigger = currentToken.value;

                    cg.blockBeginning = cg.generatedCode.size()+1;



                    if(cg.specialLine != null){
                        if(cg.opTrigger.equals("else")){
                            cg.specialLine.result = ""+(cg.generatedCode.size()+3);
                            cg.dropSpecialLine();
                        }
                    }



                    cg.putSpecialLineOnStandby(cg.generatedCode.size(),"BR","","","");

                    checkIfValidBooleanExpression();
                    if(!cg.opTrigger.equals("else"))
                        cg.specialLine.operation = "BR"+cg.currentRelation;

                    currentSymbol = null; // reset

                    continue;

                }else if(currentToken.value.equals("int")){
                    currentSymbol = new Symbol();
                    currentSymbol.declaration = true;
                    currentSymbol.vType = Enums.VariableType.INT;
                    currentSymbol.inFunction = currentFunction;

                }else if(currentToken.value.equals("float")){
                    currentSymbol = new Symbol();
                    currentSymbol.declaration = true;
                    currentSymbol.vType = Enums.VariableType.FLOAT;
                    currentSymbol.inFunction = currentFunction;

                }else if(currentToken.value.equals("void")){
                    currentSymbol = new Symbol();
                    currentSymbol.vType = Enums.VariableType.VOID;
                    currentSymbol.declaration = true;
                    currentSymbol.inFunction = currentFunction;

                }

            }
            //endregion

            //region Name
            else if(currentToken.token_type.equals("ID:")){

                if(currentSymbol == null){          // symbol isn't a declaration

                    if(scope.isDeclaredFunction(currentToken.value) && peek().value.equals("(")){ // function implementation
                        currentSymbol = new Symbol();
                        currentSymbol.inFunction = currentFunction;
                        currentSymbol.name = currentToken.value;
                        currentSymbol.vType = scope.getFunctionNamed(currentSymbol.name).type;

                        currentType = currentSymbol.vType;

                        compareTypes(false);

                        tempLastType = lastType;
                        lastType = null;

                        checkIfProperParametersPassed(currentSymbol.name,true);

                        lastType = tempLastType;

                        // currentToken = ;
                    }

                    else if(scope.isDeclared(currentToken.value)){ // variable implementation


                        currentSymbol = new Symbol();
                        currentSymbol.inFunction = currentFunction;
                        currentSymbol.name = currentToken.value;

                        currentSymbol.vType = scope.hydrateVariableType(currentSymbol.name);
                        currentType = currentSymbol.vType;

                        if(peek().value.equals("[")){

                            String name = currentSymbol.name;
                            nextToken();
                            if(consultGrammar("variableArrayCheck")){
                                if(currentToken.value.equals("]")){

                                    lastType = tempLastType;
                                    currentType = tempCurrentType;
                                    if(currentType.equals(Enums.VariableType.FLOAT_ARRAY))
                                        currentType = Enums.VariableType.FLOAT;
                                    else if(currentType.equals(Enums.VariableType.INT_ARRAY))
                                        currentType = Enums.VariableType.INT;
                                }
                                if(lastType != null){
                                    if(!lastType.equals(currentType)){
                                        Compiler.printError("Mismatch types "+lastType +" compared with "+currentType+" on line "+currentToken.lineNumber);
                                    }
                                }
                                currentSymbol.vType = currentType;
                            }else{
                                Compiler.printError("invalid index in array2 "+ name +" implementation on line "+currentToken.lineNumber);
                            }

                            if(!currentToken.value.equals("]")){
                                Compiler.printError("closing bracket not there ");
                            }

                        }


                        currentType = currentSymbol.vType;

                        compareTypes(false);

                    }else{
                        Compiler.printError("Could not find variable or function declaration in scope, line "+currentToken.lineNumber);
                    }


                }else{

                    if(currentSymbol.declaration){              // DECLARATION

                        currentSymbol.name = currentToken.value;

                        if(peek().value.equals("[")){ // check if array
                            currentSymbol.sType = Enums.SymbolType.VARIABLE;

                            if(currentSymbol.vType.equals(Enums.VariableType.INT))
                                currentSymbol.vType = Enums.VariableType.INT_ARRAY;
                            else if(currentSymbol.vType.equals(Enums.VariableType.FLOAT))
                                currentSymbol.vType = Enums.VariableType.FLOAT_ARRAY;
                            else
                                Compiler.printError("symbol's variable type is unknown when converting to array");

                            currentType = currentSymbol.vType;

                            if(scope.isAlreadyAtThisDepth(currentSymbol.name)){
                                Compiler.printError("cannot declare variable twice at this depth");
                            }

                        }else if(peek().value.equals("(")){ // check if function

                            if(scope.isDeclaredFunction(currentSymbol.name)){
                                Compiler.printError("cannot declare same named function twice.");
                            }

                            currentSymbol.sType = Enums.SymbolType.FUNCTION;
                            currentFunction = new Function(currentSymbol);


                            scope.current.symbols.add(currentSymbol);

                            scope.current = scope.current.addChild(); // increase scope for params

                            checkParamDeclaration(currentFunction); // check params

                            currentSymbol = null;

                            LexToken doublebracket = currentToken;

                            nextToken();

                            if(currentToken.value.equals("{") && !doublebracket.value.equals("{")) {
                                nextToken();
                                if(!scope.current.parent.identifier.equals("0")){
                                    cg.addLine("block","","","");
                                }
                            }
                            continue;

                        }else{

                            cg.addLine("alloc",cg.convertTypeToSize(currentSymbol.vType),"",currentSymbol.name);

                            currentSymbol.sType = Enums.SymbolType.VARIABLE;

                            if(currentSymbol.vType.equals(Enums.VariableType.VOID)){
                                Compiler.printError("Cannot declare a void variable on line "+currentToken.lineNumber);
                            }

                            if(scope.isAlreadyAtThisDepth(currentSymbol.name)){
                                Compiler.printError("cannot declare variable twice at this depth");
                            }

                        }

                        scope.current.symbols.add(currentSymbol);
                        currentSymbol = null;
                    }
                }


            }
            //endregion

            //region Special Character
            else if(currentToken.token_type.equals("")){

                if(currentToken.value.equals("}")){
                    // go back a depth

                    scope.current = scope.current.popBack();

                    if(scope.current.identifier.equals("0")){

                        cg.addLine("end","func",currentFunction.name,"");

                        if(!currentFunction.hasReturn && !currentFunction.type.equals(Enums.VariableType.VOID)){
                            Compiler.printError("no return value present for function "+currentFunction.name);
                        }
                    }else{
                        cg.addLine("end","block","","");

                        if(cg.specialLine != null){
                            if(!cg.opTrigger.equals("null")) {

                                if(cg.opTrigger.equals("while")){
                                    cg.addLine("BR", "", "", "" + cg.blockBeginning);
                                }else if(cg.opTrigger.equals("if")){

                                }else if (cg.opTrigger.equals("else")){
                                    if(cg.tempSpecial != null){
                                        cg.tempSpecial.result = ""+(cg.generatedCode.size());
                                        Generation g = cg.specialLine;
                                        cg.specialLine = cg.tempSpecial;
                                        cg.dropSpecialLine();
                                        cg.specialLine = g;

                                    }

                                }

                                cg.opTrigger = "null";

                            }
                            cg.blockBeginning = 0;
                            cg.specialLine.result = ""+(cg.generatedCode.size()+2);

                            if(!peek().value.equals("else"))
                                cg.dropSpecialLine();

                        }

                    }


                }else if(currentToken.value.equals(";")){
                    if(beginTrackingDepth){
                        scope.current = scope.current.popBack();
                        beginTrackingDepth = false;
                    }
                    lastType = null;
                    currentType = null;
                    tempLastType = null;
                    tempCurrentType = null;
                    currentSymbol = null;

                }else if(currentToken.value.equals("=")){


                    cg.assignVal = currentSymbol.name;

                    nextToken();

                    cg.evaluating = true;
                    cg.assignTemp = false;
                    consultGrammar("expression");
                    cg.stopEvaluating();

                    if(cg.assignTemp){
                        cg.addLine("assign",cg.currentTempVariable,"",cg.assignVal);
                        cg.assignTemp = false;
                    }else {
                        cg.addLine("assign", currentSymbol.name, "", cg.assignVal);
                    }

                    currentSymbol = null;
                    continue;

                }else if(currentToken.value.equals("+") || currentToken.value.equals("-")){
                    currentSymbol = null;
                }else if(currentToken.value.equals("{")){

                    scope.current = scope.current.addChild();

                    cg.addLine("block","","","");
                }
            }
            //endregion

            else if(currentToken.token_type.equals("FLON:")){

                currentSymbol = null;

                currentType = Enums.VariableType.FLOAT;

                compareTypes(false);

            }else if(currentToken.token_type.equals("NUM:")){
                currentSymbol = null;

                currentType = Enums.VariableType.INT;

                compareTypes(false);
            }

            nextToken();
        }

        cg.printLines();

        if(!scope.functions.get(scope.functions.size()-1).name.equals("main")){
            Compiler.printError("function declared after main");
        }

        return scope;
    }


    private void checkIfValidReturnExpression(Function f){

        nextToken();

        boolean checkParam = false;
        if(currentToken.value.equals("(")){
            checkParam = true;
            nextToken();
        }


        cg.evaluating = true;
        if(!consultGrammar("expression")){
            Compiler.printError("invalid return expression");
        }
        cg.stopEvaluating();

        if(!currentType.equals(f.type)){
            Compiler.printError("invalid return type for function "+f.name+" on line "+currentToken.lineNumber);
        }

        if(checkParam){
            if(currentToken.value.equals(")")){
                nextToken();
            }else{
                Compiler.printError("no closing parenthesis in return expression");
            }
        }

        cg.addLine("return","","", cg.currentTempVariable);
    }

    private void checkIfValidBooleanExpression(){

        if(currentToken.value.equals("else")){

            nextToken(); // {

        }else{
            nextToken(); // (

            if(!currentToken.value.equals("(")){
                Compiler.printError("initial parenthesis missing from bool expression?");
            }

            nextToken(); //

            cg.evaluating = true;
            if(consultGrammar("expression")){
                //nextToken();
                cg.stopEvaluating();
                if(currentToken.value.equals(")")){
                    nextToken(); // { or not
                }else{
                    Compiler.printError(" parenthesis intended, received: "+currentToken.value);
                }
            }else{
                Compiler.printError("nothing inside parenthesis?");
            }
        }

        if(!currentToken.value.equals("{")){
            beginTrackingDepth = true;
        }
        lastType = null;
        //scope.current = scope.current.addChild();

    }

    private boolean consultGrammar(String identifier){

        if(identifier.equals("remainingVariableComponent")){

            if(currentToken.value.equals("=")){
                nextToken();
                cg.evaluating = true;
                if(consultGrammar("expression")){
                    cg.stopEvaluating();
                    return true;
                }
            }else if(consultGrammar("remainingExpressionComponent")){
                return true;
            }

            //remainingVariableComponent -> = expression | remainingExpressionComponent
        }else if(identifier.equals("remainingIDComponent")){ //done
            if(consultGrammar("variableArrayCheck")){
                if(currentToken.value.equals("]"))
                    nextToken();

                if(consultGrammar("remainingVariableComponent")){
                    return true;
                }
            }else if(currentToken.value.equals("(")){
                boolean wasEval = cg.evaluating;
                if(wasEval)
                    cg.evaluating = false;
                nextToken();

                cg.evaluating = wasEval;

                if(consultGrammar("arguments")){
                    if(currentToken.value.equals(")")){
                        if(consultGrammar("remainingExpressionComponent")){
                            return true;
                        }
                    }
                }
            }

        }else if(identifier.equals("remainingExpressionComponent")){ //done
            if(consultGrammar("moreTerms")){
                if(consultGrammar("moreAddExpressions")){
                    if(consultGrammar("relation")){
                        return true;
                    }
                }
            }
        }else if(identifier.equals("moreTerms")){ // done
            if(consultGrammar("timesOperations")){
                if(consultGrammar("factor")){
                    if(consultGrammar("moreTerms")){
                        return true;
                    }
                }
            }else{
                return true;
            }

        }else if(identifier.equals("moreAddExpressions")){ // done
            if(consultGrammar("additionOperations")){
                if(consultGrammar("term")){
                    if(consultGrammar("moreAddExpressions")){
                        return true;
                    }
                }
            }else{
                return true;
            }

        }else if(identifier.equals("relation")){ // done
            if(consultGrammar("operation")){
                if(consultGrammar("addExpression")){
                    return true;
                }
            }else{
                return true;
            }

        }else if(identifier.equals("timesOperations")){ //done
            if(currentToken.value.equals("*") || currentToken.value.equals("/")){
                nextToken();
                return true;
            }
        }else if(identifier.equals("factor")) { //done

            if(currentToken.value.equals("(")){
                cg.evaluating = true;
                if(consultGrammar("expression")){
                    cg.stopEvaluating();
                    nextToken();
                    if(currentToken.value.equals(")")){
                        return true;
                    }
                }
            }else if(currentToken.token_type.equals("NUM:")){


                currentType = Enums.VariableType.INT;

                compareTypes(true);

                return true;

            }else if(currentToken.token_type.equals("FLON:")){

                currentType = Enums.VariableType.FLOAT;


                compareTypes(true);

                return true;

            }else if(consultGrammar("variable")){
                return true;

            }else if(consultGrammar("call")){
                return true;
            }

        }else if(identifier.equals("expression")){
            //todo
            // ID remainingIDComponent | ( expression ) remainingExpressionComponent | NUM remainingExpressionComponent | FLON remainingExpressionComponent
            if(currentToken.token_type.equals("ID:")){
                if(currentSymbol == null){
                    currentSymbol = new Symbol();
                }
                currentSymbol.name = currentToken.value;
                if(!scope.isDeclared(currentToken.value)){
                    Compiler.printError("variable '"+currentToken.value+"' not declared on line "+currentToken.lineNumber);
                }
                currentType = scope.hydrateVariableType(currentToken.value);

                if(peek().value.equals("["))
                {
                    if(currentType.equals(Enums.VariableType.FLOAT_ARRAY))
                        currentType = Enums.VariableType.FLOAT;
                    else if(currentType.equals(Enums.VariableType.INT_ARRAY))
                        currentType = Enums.VariableType.INT;

                    if(lastType != null){
                        compareTypes(true);
                    }else{
                        nextToken();
                    }
                }else{
                    nextToken();
                }

                if(consultGrammar("remainingIDComponent")){ // check if array
                    return true;
                }


            }else if(currentToken.token_type.equals("NUM:")){

                if(currentSymbol == null){
                    currentSymbol = new Symbol();
                }
                currentSymbol.name = currentToken.value;

                currentType = Enums.VariableType.INT;


                compareTypes(true);

                if(consultGrammar("remainingExpressionComponent")){
                    return true;
                }

            }else if(currentToken.token_type.equals("FLON:")){

                if(currentSymbol == null){
                    currentSymbol = new Symbol();
                }
                currentSymbol.name = currentToken.value;

                currentType = Enums.VariableType.FLOAT;


                compareTypes(true);

                if(consultGrammar("remainingExpressionComponent")){
                    return true;
                }

            }else if(currentToken.value.equals("(")){

                nextToken();
                cg.evaluating = true;
                if(consultGrammar("expression")){
                    cg.stopEvaluating();
                    nextToken();
                    if(currentToken.value.equals(")")){
                        if(consultGrammar("remainingExpressionComponent")){
                            return true;
                        }
                    }
                }
            }


        }else if(identifier.equals("variable")){
            //variable -> ID variableArrayCheck
            if(currentToken.token_type.equals("ID:")){

                if(currentSymbol == null){
                    currentSymbol = new Symbol();
                }
                currentSymbol.name = currentToken.value;


                if(scope.isDeclared(currentToken.value))
                    currentType = scope.hydrateVariableType(currentToken.value);
                else
                    Compiler.printError("variable not declared in scope on line #"+currentToken.lineNumber);



                compareTypes(true);

                if(consultGrammar("variableArrayCheck")){

                    return true;

                }
            }

        }else if(identifier.equals("call")){
            if(currentToken.token_type.equals("ID:")){


                if(!scope.isDeclaredFunction(currentToken.value)){
                    Compiler.printError("function call: "+ currentToken.value + " not declared on line "+currentToken.lineNumber);
                }

                nextToken();
                if(currentToken.value.equals("(")){
                    nextToken();
                    if(consultGrammar("arguments")){
                        nextToken();
                        if(currentToken.value.equals(")")){
                            return true;
                        }
                    }
                }
            }
        }else if(identifier.equals("additionOperations")){
            if(currentToken.value.equals("+") || currentToken.value.equals("-")) {
                nextToken();
                return true;
            }

        }else if(identifier.equals("term")){  // term
            if(consultGrammar("factor"))
                if(consultGrammar("moreTerms"))
                    return true;

        }else if(identifier.equals("addExpression")){ // addExpression
            if (consultGrammar("term"))
                if(consultGrammar("moreAddExpressions"))
                    return true;


        }else if(identifier.equals("operation")) { // operation
            if (isRelational(currentToken)) {
                if(currentType != null) {
                    lastType = currentType;
                    currentType = null;
                }
                nextToken();
                return true;
            }
        }
        else if(identifier.equals("allArgs")){

            cg.evaluating = true;
            if(consultGrammar("expression")){
                cg.evaluate();

                if(consultGrammar("moreArgs")){
                    return true;
                }
            }
        }else if(identifier.equals("moreArgs")){
            if(currentToken.value.equals(",")){
                nextToken();
                cg.evaluating = true;
                if(consultGrammar("expression")){
                    cg.evaluate();
                    if(consultGrammar("moreArgs")){
                        return true;
                    }
                }
            }else{
                return true;
            }

        }

        else if(identifier.equals("arguments")) {


            checkIfProperParametersPassed(currentSymbol.name,false);

            return true;

        }else if(identifier.equals("variable")){
            if(currentToken.token_type.equals("ID:")){
                if(consultGrammar("variableArrayCheck")){
                    return true;
                }
            }
        }else if(identifier.equals("variableArrayCheck")) {
            if(currentToken.value.equals("[")){

                tempCurrentType = currentType;

                tempLastType = lastType;
                lastType = null;
                nextToken();
                cg.evaluating = true;
                if(consultGrammar("expression")){
                    cg.stopEvaluating();
                    if(!currentToken.value.equals("]"))
                        nextToken();
                    if(currentToken.value.equals("]")){

                        if(!currentType.equals(Enums.VariableType.INT))
                            Compiler.printError("value in index must be an integer on line "+currentToken.lineNumber);

                        lastType = tempLastType;
                        currentType = tempCurrentType;

                        if(currentType.equals(Enums.VariableType.FLOAT_ARRAY))
                            currentType = Enums.VariableType.FLOAT;
                        else if(currentType.equals(Enums.VariableType.INT_ARRAY))
                            currentType = Enums.VariableType.INT;

                        return true;
                    }

                }
            }else{
                if(!currentToken.value.equals("("))
                    return true;
            }
        }else{
            Compiler.printError("cannot determine expression order");
        }

        return false;

//        parameters -> void voidParamCheck | int ID paramArrayCheck moreParams | float ID paramArrayCheck moreParams
//        voidParamCheck -> ID paramArrayCheck moreParams | ε
//        moreParams -> , param moreParams | ε
//        param -> keywordType ID paramArrayCheck
//        paramArrayCheck -> [ ] | ε

//        arguments -> allArgs | ε
//        allArgs -> expression moreArgs
//        moreArgs -> , expression moreArgs | ε

    }

    private void compareTypes(boolean a){
        if(lastType != null){
            //System.out.println("Compared "+lastType+" with "+currentType+ " ("+currentToken.value+")" );
            if(lastType.equals(currentType)){
                if(a)
                    nextToken();
            }else{
                Compiler.printError("mismatch variable types on line #"+currentToken.lineNumber);
            }


        }else{
            lastType = currentType;

            if(a)
                nextToken();
        }
    }

    private boolean isRelational(LexToken token){
        // <= | < | > | >= | == | !=
        if(token.value.equals("!=")){
            currentCompare = "NEQ";
            return true;
        }else if(token.value.equals("==")){ // equal to
            currentCompare = "EQ";
            return true;
        }else if(token.value.equals("<")){  // less than
            currentCompare = "LT";
            return true;
        }else if(token.value.equals("<=")){ // less than equal to
            currentCompare = "LTEQ";
            return true;
        }else if(token.value.equals(">")){  // greater than
            currentCompare = "GT";
            return true;
        }else if(token.value.equals(">=")){ // greater than equal to
            currentCompare = "GTEQ";
            return true;
        }
        return false;
    }

    private void checkIfProperParametersPassed(String name, boolean nextOrNot){

        Function f = scope.getFunctionNamed(name);

        if(nextOrNot) {
            nextToken(); // (

            if (!currentToken.value.equals("(")) {
                Compiler.printError("expected ( but received " + currentToken.value);
            }

            nextToken(); // next
        }

        if(currentToken.value.equals(")")){
            if(f.parameters.size() == 0)
                return;
            else
                Compiler.printError("parameters expected for function "+f.name+" at line "+currentToken.lineNumber);
        }

        cg.pauseEvaluations();
        cg.evaluating = true;
        cg.argTemp = false;
        if(consultGrammar("expression")){
            cg.stopEvaluating();
            if(cg.argTemp){
                cg.addLine("arg","","",cg.currentTempVariable);
                cg.argTemp = false;
            }else{
                cg.addLine("arg","","",currentSymbol.name);
            }

            if(!currentType.equals(f.parameters.get(0).type)){
                Compiler.printError("param 1 supplied type doesn't match expected type");
            }

            tempLastType = lastType;
            int count = 1;
            while(currentToken.value.equals(",")){

                lastType = null;
                currentType = null;

                nextToken();
                cg.evaluating = true;
                cg.argTemp = false;
                if(consultGrammar("expression")){
                    cg.stopEvaluating();
                    if(cg.argTemp){
                        cg.addLine("arg","","",cg.currentTempVariable);
                        cg.argTemp = false;
                    }else{
                        cg.addLine("arg","","",currentSymbol.name);
                    }

                    if(count >= f.parameters.size()){
                        Compiler.printError("too many params supplied for function ");
                    }
                    if(!currentType.equals(f.parameters.get(count).type)){
                        Compiler.printError("param "+(count +1)+" supplied type doesn't match expected type");
                    }

                    count++;
                }else{
                    Compiler.printError("invalid");
                }
            }

            cg.resumeEvaluations();
            LexToken modified = cg.output.remove(cg.output.size() - 1);
            modified.value = cg.nextTVariable();
            cg.output.add(modified);
            cg.addLine("call",f.name,""+f.parameters.size(),modified.value);

            lastType = tempLastType;
            if(count < f.parameters.size()){
                Compiler.printError("not enough parameters supplied for function "+f.name+" line "+currentToken.lineNumber);
            }

            if(!currentToken.value.equals(")")){
                Compiler.printError("invalid character, expected , or closing param ) received "+currentToken.value);
            }
        }
    }

    private void checkParamDeclaration(Function f){

        nextToken(); // (
        nextToken(); // next

        while(!currentToken.value.equals(")")){

            Parameter p;
            Enums.VariableType type = Enums.VariableType.VOID;

            if(currentToken.value.equals("void")){
                type = Enums.VariableType.VOID;
                p = new Parameter("",type);

                f.addParameter(p);
                nextToken(); // )
                nextToken(); // whatever follows
                scope.functions.add(f);
                cg.addLine("func",f.name, "void", "0");
                return;

            }
            else if(currentToken.value.equals("int"))
                type = Enums.VariableType.INT;
            else if(currentToken.value.equals("float"))
                type = Enums.VariableType.FLOAT;


            cg.putLineOnStandby("param","","","");
            String t = currentToken.value;

            nextToken(); // variable name

            if(peek().value.equals("[")){ // array check
                if(type.equals(Enums.VariableType.INT))
                    type = Enums.VariableType.INT_ARRAY;
                else if(type.equals(Enums.VariableType.FLOAT))
                    type = Enums.VariableType.FLOAT_ARRAY;

                nextToken(); // [
                nextToken(); // ]
            }

            p = new Parameter(currentToken.value,type);
            Symbol s = new Symbol();
            s.name = currentToken.value;
            s.vType = type;
            s.sType = Enums.SymbolType.PARAMETER;
            scope.current.symbols.add(s);

            String size = cg.getSizeForType(t);
            cg.putLineOnStandby("alloc",size,"",p.name);

            f.addParameter(p);

            if(peek().value.equals(",")){
                nextToken(); // ,
            }


            nextToken(); // next param or )
        }

        String type = scope.getTranslatedType(f.type);

        cg.addLine("func",f.name,type, ""+f.parameters.size());
        cg.dropLines();

        scope.functions.add(f);
    }

    private void nextToken(){
        if(valid()) {
            if(cg.evaluating){
                cg.addEvalToken(currentToken);
            }
            currentToken = tokens.get(index++);
        }else {
            currentToken = null;
        }
    }

    private LexToken peek(){
        if(valid())
            return tokens.get(index);

        Compiler.printError("No token could be peeked at");
        return null;
    }

    private boolean valid(){
        return (index < tokens.size());
    }

}

class Depth {
    public ArrayList<Symbol> symbols =  new ArrayList<Symbol>();
    public String identifier;
    public int iteration = 0;
    public Depth parent;
    public ArrayList<Depth> children;

    public Depth(Depth parent, int d){
        if(parent != null) {
            this.parent = parent;
            this.identifier = parent.identifier + "_" + d;
        }else{
            this.parent = null;
            this.identifier = ""+d;
        }
    }
    public Depth(){

    }

    public Depth clone(){
        Depth d = new Depth();
        d.parent = this.parent;
        d.symbols = this.symbols;
        d.identifier = this.identifier;
        d.children = this.children;
        d.iteration = this.iteration;
        return d;
    }

    public Depth popBack(){
        return this.parent;
    }

    public Depth addChild(){
        if(this.children == null){
            this.children = new ArrayList<Depth>();
        }
        Depth newChild = new Depth(this,iteration++);
        children.add(newChild);
        return newChild;
    }

}

class Scope{

    private Depth starting;
    public Depth current;
    ArrayList<Function> functions;


    Scope(){
        functions = new ArrayList<Function>();
        starting = new Depth(null, 0);
        current = starting;
    }

    public String getTranslatedType(Enums.VariableType t){
        if(t.equals(Enums.VariableType.INT))
            return "int";
        else if(t.equals(Enums.VariableType.FLOAT))
            return "float";
        else if(t.equals(Enums.VariableType.VOID))
            return "void";
        else
            return null;
    }

    public boolean isDeclaredFunction(String name){
        for(Function f : this.functions){
            if(f.name.equals(name))
                return true;
        }
        return false;
    }

    public Function getFunctionNamed(String name){
        for(Function f: this.functions){
            if(f.name.equals(name))
                return f;
        }
        return null;
    }

    public Enums.VariableType hydrateVariableType(String name){
        Depth currentDepth = current.clone();
        for(Symbol s: currentDepth.symbols){ // check for symbols at this depth
            if(s.name.equals(name))
                return s.vType; // found same name
        }
        currentDepth = currentDepth.parent;  // check for symbols in parent depths as well
        while(currentDepth != null){
            for(Symbol s: currentDepth.symbols){
                if(s.name.equals(name))
                    return s.vType; // found same name
            }
            currentDepth = currentDepth.parent;
        }
        return null;
    }

    public boolean isDeclared(String variableName){
        Depth currentDepth = current.clone();
        for(Symbol s: currentDepth.symbols){ // check for symbols at this depth
            if(s.name.equals(variableName))
                return true; // found same name
        }
        currentDepth = currentDepth.parent;  // check for symbols in parent depths as well
        while(currentDepth != null){
            for(Symbol s: currentDepth.symbols){
                if(s.name.equals(variableName))
                    return true; // found same name
            }
            currentDepth = currentDepth.parent;
        }
        return false;
    }

    public boolean isAlreadyAtThisDepth(String name){
        for(Symbol s: current.symbols){ // check for symbols at this depth
            if(s.name.equals(name))
                return true; // found same name
        }
        return false;
    }


}


class Enums{
    public static enum SymbolType { FUNCTION, VARIABLE, PARAMETER, RELATIONAL }
    public static enum VariableType { INT, FLOAT, VOID, INT_ARRAY, FLOAT_ARRAY  }

}

class Symbol {
    public String name;
    public boolean declaration;
    public Enums.SymbolType sType;
    public Enums.VariableType vType;
    public Function inFunction = null;

    Symbol(){
        this.declaration = false;
    }

    public Symbol clone(){
        Symbol clone = new Symbol();
        clone.name = this.name;
        clone.declaration = this.declaration;
        clone.sType = this.sType;
        clone.vType = this.vType;
        clone.inFunction = this.inFunction;
        return clone;
    }

    @Override
    public String toString() {
        return "Symbol: "+this.name+" "+this.sType+" "+this.vType;
    }

}

class Parameter{
    public Enums.VariableType type;
    public String name;
    Parameter(Symbol s){
        this.name = s.name;
        this.type = s.vType;
    }
    Parameter(String n, Enums.VariableType v){
        this.name = n;
        this.type = v;
    }

}

class Function{
    public String name;
    public Enums.VariableType type;
    public boolean hasReturn = false;
    public ArrayList<Parameter> parameters;

    Function(Symbol s){
        this.name = s.name;
        this.type = s.vType;
    }

    public void addParameter(Parameter p){
        if(parameters == null)
            parameters = new ArrayList<Parameter>();
        parameters.add(p);
    }

    public boolean isVariableType(String type, boolean isArray){
        Enums.VariableType v = null;

        if(type.equals("int")){
            if(!isArray)
                v = Enums.VariableType.INT;
            else
                v = Enums.VariableType.INT_ARRAY;
        }else if(type.equals("float")){
            if(!isArray)
                v = Enums.VariableType.FLOAT;
            else
                v = Enums.VariableType.FLOAT_ARRAY;
        }

        if(v != null){
            return this.type.equals(v);
        }
        return false;
    }
}

/**
 * Created by matt on 4/12/15.
 */
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

