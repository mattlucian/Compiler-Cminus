import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by matt on 2/28/15.
 */

public class Grammar {

    public ArrayList<Transition> transitions = new ArrayList<Transition>();

    /*
    0. start -> beginDeclarations
    1. beginDeclarations -> declaration moreDeclarations
    2. moreDeclarations -> declaration moreDeclarations | ε
    3. declaration -> keywordType ID extendedDeclaration
    4. extendedDeclaration-> arrayCheck | ( parameters ) compoundStmt
    5. arrayCheck -> ; | [ NUM ] ;
    6. keywordType -> int | float | void
    7. parameters -> void | int ID paramArrayCheck moreParams | float ID paramArrayCheck moreParams
    8. voidParamCheck -> ID paramArrayCheck moreParams | ε
    9. moreParams -> , param moreParams | ε
    10. param -> keywordType ID paramArrayCheck
    11. paramArrayCheck -> [ ] | ε
    12. compoundStmt -> { declarationsInScope allStatements }
    13. declarationsInScope -> newDeclaration declarationsInScope | ε
    14. newDeclaration -> keywordType ID arrayCheck
    15. allStatements-> statement allStatements | ε
    16. statement -> expressionStmt | compoundStmt | selectionStmt | iterateStmt | returnStmt
    17. selectionStmt -> if ( expression ) statement elseStmt
    18. expressionStmt -> expression ; | ;
    19. elseStmt -> else statement
    20. iterateStmt -> while ( expression ) statement
    21. returnStmt -> return returnComponent
    22. returnComponent -> expression ; | ;
    23. expression -> ID remainingIDComponent | ( expression ) remainingExpressionComponent | NUM remainingExpressionComponent | FLON remainingExpressionComponent
    24. variableArrayCheck -> [ expression ] | ε
    25. remainingVariableComponent -> = expression | remainingExpressionComponent
    26. remainingIDComponent -> variableArrayCheck remainingVariableComponent | ( arguments ) remainingExpressionComponent
    27. remainingExpressionComponent -> moreTerms moreAddExpressions relation
    28. variable -> ID variableArrayCheck
    29. relation -> operation addExpression | ε
    30. operation -> <= | < | > | >= | == | !=
    31. addExpression -> term moreAddExpressions
    32. moreAddExpressions -> additionOperations term moreAddExpressions | ε
    33. additionOperations -> + | -
    34. term -> factor moreTerms
    35. moreTerms -> timesOperations factor moreTerms | ε
    36. timesOperations -> * | /
    37. factor -> ( expression ) | variable | call | NUM | FLON
    38. call -> ID ( arguments )
    39. arguments -> allArgs | ε
    40. allArgs -> expression moreArgs
    41. moreArgs -> , expression moreArgs | ε
     */

    // to read in a Grammar
    Grammar(String filePath){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (Exception e) {
            System.out.println("Grammar File doesn't exist, try again");
            System.exit(1);
        }

        TokenAnalyzer ta = new TokenAnalyzer();
        String line = null;
        try{
            ArrayList<Transition> trans = new ArrayList<Transition>();
            ArrayList<String> lines = new ArrayList<String>();
            while((line = reader.readLine()) != null){
                if(line.trim().isEmpty()){
                    continue;
                }
                lines.add(line);

                boolean lineIsValid = true;
                String currentToken = "";
                for(int i = 0; i < line.length(); i++){
                    while(line.charAt(i) != '-'){
                        currentToken += line.charAt(i++);
                    }
                    trans.add(new Transition(currentToken,false)); // store all transitions first
                }
            }

            int count = 0;
            for(String l : lines){
                int i = 0;
                while(l.charAt(i) != '>'){
                    i++;
                }
                i++;
                // Skips the first transition, get to the choices
                String remainingLine = l.substring(i);

                ArrayList<Choices> choices = new ArrayList<Choices>();

                String[] choiceStrings = remainingLine.split("|");
                for(String choice : choiceStrings){
                    Choices c = new Choices();
                    String[] ts = choice.split(" ");
                    for(String t : ts){
                        Transition localT = new Transition(t.trim(),true);

                        if(trans.contains(localT)){ // non-terminal
                            int index = trans.indexOf(localT);
                            c.addTransition(trans.get(index));
                        }else{ // terminal
                            c.addTransition(localT);
                        }
                    }
                    choices.add(c);
                }
                Choices[] passC = new Choices[choices.size()];
                for(int z = 0; z < choices.size(); z++){
                    passC[z] = choices.get(z);
                }
                trans.get(count).addChoices(passC);
                count++;
            }


        }catch (Exception e){
            System.out.println("Error, "+e.getMessage());
        }





    }

    Grammar(){

        // Adds all transitions
        transitions.add(new Transition("start",false));
        transitions.add(new Transition("beginDeclarations",false));
        transitions.add(new Transition("moreDeclarations",false));
        transitions.add(new Transition("declaration",false));
        transitions.add(new Transition("extendedDeclaration",false));
        transitions.add(new Transition("arrayCheck",false));
        transitions.add(new Transition("keywordType",false));
        transitions.add(new Transition("parameters",false));
        transitions.add(new Transition("voidParamCheck",false));
        transitions.add(new Transition("moreParams",false));
        transitions.add(new Transition("param",false));
        transitions.add(new Transition("paramArrayCheck",false));
        transitions.add(new Transition("compoundStmt",false));
        transitions.add(new Transition("declarationsInScope",false));
        transitions.add(new Transition("newDeclaration",false));
        transitions.add(new Transition("allStatements",false));
        transitions.add(new Transition("statement",false));
        transitions.add(new Transition("selectionStmt",false));
        transitions.add(new Transition("expressionStmt",false));
        transitions.add(new Transition("elseStmt",false));
        transitions.add(new Transition("iterateStmt",false));
        transitions.add(new Transition("returnStmt",false));
        transitions.add(new Transition("returnComponent",false));
        transitions.add(new Transition("expression",false));
        transitions.add(new Transition("variableArrayCheck",false));
        transitions.add(new Transition("remainingVariableComponent",false));
        transitions.add(new Transition("remainingIDComponent",false));
        transitions.add(new Transition("remainingExpressionComponent",false));
        transitions.add(new Transition("variable",false));
        transitions.add(new Transition("relation",false));
        transitions.add(new Transition("operation",false));
        transitions.add(new Transition("addExpression",false));
        transitions.add(new Transition("moreAddExpressions",false));
        transitions.add(new Transition("additionOperations",false));
        transitions.add(new Transition("term",false));
        transitions.add(new Transition("moreTerms",false));
        transitions.add(new Transition("timesOperations",false));
        transitions.add(new Transition("factor",false));
        transitions.add(new Transition("call",false));
        transitions.add(new Transition("arguments",false));
        transitions.add(new Transition("allArgs",false));
        transitions.add(new Transition("moreArgs",false));


        // 0. start -> 1
        Choices[] currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(1) });
        transitions.get(0).addChoices(currentChoices);

        // 1. beginDeclarations -> 3 2
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(3), transitions.get(2) });
        transitions.get(1).addChoices(currentChoices);

        // 2. moreDeclarations -> 3 2 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(3), transitions.get(2) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("@", true ) });
        transitions.get(2).addChoices(currentChoices);

        // 3. declaration -> 6 ID 4
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(6), new Transition("ID", true ) , transitions.get(4) });
        transitions.get(3).addChoices(currentChoices);

        // 4. extendedDeclaration-> 5 | ( 7 ) 12
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(5) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("(",true), transitions.get(7), new Transition(")", true ), transitions.get(12) });
        transitions.get(4).addChoices(currentChoices);

        // 5. arrayCheck -> ; | [ NUM ] ;
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition(";", true) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("[",true), new Transition("NUM",true), new Transition("]", true ), new Transition(";", true) });
        transitions.get(5).addChoices(currentChoices);

        // 6. keywordType -> int | float | void
        currentChoices = new Choices[3];
        currentChoices[0] = new Choices(new Transition[] { new Transition("int", true)   });
        currentChoices[1] = new Choices(new Transition[] { new Transition("float", true) });
        currentChoices[2] = new Choices(new Transition[] { new Transition("void", true)  });
        transitions.get(6).addChoices(currentChoices);

        // 7. parameters -> void | int ID 11 9 | float ID 11 9
        // used to be "void 8"
        currentChoices = new Choices[3];
        currentChoices[0] = new Choices(new Transition[] { new Transition("void", true) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("int", true), new Transition("ID", true), transitions.get(11), transitions.get(9) });
        currentChoices[2] = new Choices(new Transition[] { new Transition("float", true), new Transition("ID",true), transitions.get(11), transitions.get(9) });
        transitions.get(7).addChoices(currentChoices);

        // 8. voidParamCheck -> ID 11 9 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition("ID", true), transitions.get(11), transitions.get(9) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("@",true) });
        transitions.get(8).addChoices(currentChoices);

        // 9. moreParams -> , 10 9 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition(",", true), transitions.get(10), transitions.get(9) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("@",true) });
        transitions.get(9).addChoices(currentChoices);

        // 10. param -> 6 ID 11
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(6), new Transition("ID", true), transitions.get(11)});
        transitions.get(10).addChoices(currentChoices);

        // 11. paramArrayCheck -> [ ] | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition("[", true), new Transition("]", true)});
        currentChoices[1] = new Choices(new Transition[] { new Transition("@",true) });
        transitions.get(11).addChoices(currentChoices);

        // 12. compoundStmt -> { 13 15 }
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { new Transition("{", true), transitions.get(13), transitions.get(15),new Transition("}", true) });
        transitions.get(12).addChoices(currentChoices);


        // 13. declarationsInScope -> 14 13 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] {transitions.get(14), transitions.get(13)});
        currentChoices[1] = new Choices(new Transition[] { new Transition("@",true) });
        transitions.get(13).addChoices(currentChoices);

        // 14. newDeclaration -> 6 ID 5
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(6), new Transition("ID", true), transitions.get(5) });
        transitions.get(14).addChoices(currentChoices);

        // 15. allStatements-> 16 15 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] {transitions.get(16), transitions.get(15)});
        currentChoices[1] = new Choices(new Transition[] { new Transition("@",true) });
        transitions.get(15).addChoices(currentChoices);

        // 16. statement -> 18 | 12 | 17 | 20 | 21
        currentChoices = new Choices[5];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(18) });
        currentChoices[1] = new Choices(new Transition[] { transitions.get(12) });
        currentChoices[2] = new Choices(new Transition[] { transitions.get(17) });
        currentChoices[3] = new Choices(new Transition[] { transitions.get(20) });
        currentChoices[4] = new Choices(new Transition[] { transitions.get(21) });
        transitions.get(16).addChoices(currentChoices);


        // 17. selectionStmt -> if ( 23 ) 16 19
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { new Transition("if", true), new Transition("(", true),
                transitions.get(23), new Transition(")", true), transitions.get(16), transitions.get(19) });
        transitions.get(17).addChoices(currentChoices);


        // 18. expressionStmt -> 23 ; | ;
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(23), new Transition(";", true) });
        currentChoices[1] = new Choices(new Transition[] { new Transition(";", true) });
        transitions.get(18).addChoices(currentChoices);

        // 19. elseStmt -> else 16 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition("else", true), transitions.get(16) });
        currentChoices[1] = new Choices(new Transition[]{ new Transition("@",true)});
        transitions.get(19).addChoices(currentChoices);

        // 20. iterateStmt -> while ( 23 ) 16
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { new Transition("while", true), new Transition("(", true), transitions.get(23), new Transition(")", true), transitions.get(16) });
        transitions.get(20).addChoices(currentChoices);

        // 21. returnStmt -> return 22
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { new Transition("return", true), transitions.get(22) });
        transitions.get(21).addChoices(currentChoices);


        // 22. returnComponent -> 23 ; | ;
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(23), new Transition(";", true) });
        currentChoices[1] = new Choices(new Transition[] { new Transition(";", true) });
        transitions.get(22).addChoices(currentChoices);

        // 23. expression -> ID 26 | ( 23 ) 27 | NUM 27 | FLON 27
        currentChoices = new Choices[4];
        currentChoices[0] = new Choices(new Transition[] { new Transition("ID", true), transitions.get(26) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("(", true), transitions.get(23), new Transition(")", true), transitions.get(27) });
        currentChoices[2] = new Choices(new Transition[] { new Transition("NUM", true), transitions.get(27) });
        currentChoices[3] = new Choices(new Transition[] { new Transition("FLON", true), transitions.get(27) });
        transitions.get(23).addChoices(currentChoices);


        // 24. variableArrayCheck -> [ 23 ] | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition("[", true), transitions.get(23), new Transition("]", true) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("@", true) });
        transitions.get(24).addChoices(currentChoices);

        // 25. remainingVariableComponent -> = 23 | 27
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition("=",true), transitions.get(23) });
        currentChoices[1] = new Choices(new Transition[] { transitions.get(27) });
        transitions.get(25).addChoices(currentChoices);

        // 26. remainingIDComponent -> 24 25 | ( 39 ) 27
        currentChoices = new Choices[2];
        currentChoices[1] = new Choices(new Transition[] { transitions.get(24), transitions.get(25) });
        currentChoices[0] = new Choices(new Transition[] { new Transition("(", true), transitions.get(39), new Transition(")", true), transitions.get(27) });
        transitions.get(26).addChoices(currentChoices);

        // 27. remainingExpressionComponent -> 35 32 29
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(35), transitions.get(32), transitions.get(29) });
        transitions.get(27).addChoices(currentChoices);

        // 28. variable -> ID 24
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { new Transition("ID", true), transitions.get(24) });
        transitions.get(28).addChoices(currentChoices);

        // 29. relation -> 30 31 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(30), transitions.get(31) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("@", true) });
        transitions.get(29).addChoices(currentChoices);

        // 30. operation -> <= | < | > | >= | == | !=
        currentChoices = new Choices[6];
        currentChoices[0] = new Choices(new Transition[] { new Transition("<=", true) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("<", true) });
        currentChoices[2] = new Choices(new Transition[] { new Transition(">", true) });
        currentChoices[3] = new Choices(new Transition[] { new Transition(">=", true) });
        currentChoices[4] = new Choices(new Transition[] { new Transition("==", true) });
        currentChoices[5] = new Choices(new Transition[] { new Transition("!=", true) });
        transitions.get(30).addChoices(currentChoices);

        // 31. addExpression -> 34 32
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(34), transitions.get(32) });
        transitions.get(31).addChoices(currentChoices);

        // 32. moreAddExpressions -> 33 34 32 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(33), transitions.get(34), transitions.get(32) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("@", true) });
        transitions.get(32).addChoices(currentChoices);

        // 33. additionOperations -> + | -
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition("+", true) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("-", true) });
        transitions.get(33).addChoices(currentChoices);


        // 34. term -> 37 35
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(37), transitions.get(35) });
        transitions.get(34).addChoices(currentChoices);


        // 35. moreTerms -> 36 37 35 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(36), transitions.get(37), transitions.get(35) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("@", true) });
        transitions.get(35).addChoices(currentChoices);

        // 36. timesOperations -> * | /
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition("*", true) });
        currentChoices[1] = new Choices(new Transition[] { new Transition("/", true) });
        transitions.get(36).addChoices(currentChoices);

        // 37. factor -> ( 23 ) | 28 | 38 | NUM | FLON
        currentChoices = new Choices[5];
        currentChoices[0] = new Choices(new Transition[] { new Transition("(", true), transitions.get(23), new Transition(")", true) });
        currentChoices[1] = new Choices(new Transition[] { transitions.get(28) });
        currentChoices[2] = new Choices(new Transition[] { transitions.get(38) });
        currentChoices[3] = new Choices(new Transition[] { new Transition("NUM", true) });
        currentChoices[4] = new Choices(new Transition[] { new Transition("FLON", true) });
        transitions.get(37).addChoices(currentChoices);

        // 38. call -> ID ( 39 )
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { new Transition("ID", true), new Transition("(", true), transitions.get(39), new Transition(")", true) });
        transitions.get(38).addChoices(currentChoices);

        // 39. arguments -> 40 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(40)});
        currentChoices[1] = new Choices(new Transition[] { new Transition("@", true) });
        transitions.get(39).addChoices(currentChoices);

        // 40. allArgs -> 23 41
        currentChoices = new Choices[1];
        currentChoices[0] = new Choices(new Transition[] { transitions.get(23), transitions.get(41) });
        transitions.get(40).addChoices(currentChoices);

        // 41. moreArgs -> , 23 41 | @
        currentChoices = new Choices[2];
        currentChoices[0] = new Choices(new Transition[] { new Transition(",",true), transitions.get(23), transitions.get(41)});
        currentChoices[1] = new Choices(new Transition[] { new Transition("@", true) });
        transitions.get(41).addChoices(currentChoices);
    }

    public Transition getStart(){
        return transitions.get(0);
    }
}