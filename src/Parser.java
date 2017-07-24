import java.util.ArrayList;

/**
 * Created by matt on 2/24/15.
 */
public class Parser {
    private ArrayList<LexToken> tokens = new ArrayList<LexToken>();
    private ArrayList<LexToken> tokensConsumed = new ArrayList<LexToken>();
    public Transition currentTransition = null;
    private int tokenIndex = 0;
    public LexToken currentToken = null;
    public boolean NO_MORE_TOKENS = false;
    static enum State { FOUND_MATCH, EMPTY, DEAD_END, EMPTY_BUT_SUCCESS, FAILURE, WTF }

    Parser(ArrayList<LexToken> passedTokens){
        this.tokens = passedTokens;
        if(this.tokens.size() > 0){
            currentToken = getNextToken();
        }
    }

    public LexToken getNextToken(){
        if(tokenIndex < tokens.size()){
            LexToken tokenToReturn = tokens.get(tokenIndex);
            tokenIndex++;
            return tokenToReturn;
        }else{
            return null;
        }
    }

    public State checkTransition(Transition t){
        currentTransition = t;

        if (t.isTerminal) {
            if (t.label.equals("@"))
                return State.EMPTY;

            if(currentToken != null){
                if(currentToken.token_type.contains("KEYWORD") || currentToken.token_type.equals("")){
                    if(currentToken.value.equals(t.label))
                        return State.FOUND_MATCH;

                }else if(currentToken.token_type.contains(t.label)) {
                    return State.FOUND_MATCH;
                }
            }

            return State.DEAD_END;

        }else{
            int choiceCount = 1;
            State check = State.WTF;
            for(Choices c : t.choices){
                check = checkChoice(c);

                if(check == State.DEAD_END){
                    if(choiceCount == t.choices.size()){ // last choice
                        currentTransition = t;
                        if(c.foundSomething){
                            return State.FAILURE;
                        }
                        return State.DEAD_END;
                    }
                }else if(check == State.EMPTY){
                    currentTransition = t;
                    break;
                }else if (check == State.FOUND_MATCH){
                    c.foundSomething = true;
                    currentTransition = t;
                    break;
                }else if (check == State.EMPTY_BUT_SUCCESS){
                    c.foundSomething = true;
                    currentTransition = t;
                    break;
                }else if(check == State.FAILURE){
                    return State.FAILURE;
                }

                choiceCount++;
            }
            return check;
        }
    }

    public State checkChoice(Choices c){
        int transitionCount = 1;
        c.foundSomething = false;
        for(Transition t : c.transitions){
            State check = checkTransition(t);
            currentTransition = t;

            if(check == State.EMPTY_BUT_SUCCESS){
                c.foundSomething = true;
            }

            if((check == State.EMPTY || check == State.FOUND_MATCH || check == State.EMPTY_BUT_SUCCESS ) && transitionCount == c.transitions.size()){
                if(check == State.EMPTY && c.foundSomething){
                    return State.EMPTY_BUT_SUCCESS;
                }
                return check;

            }else if (check == State.FOUND_MATCH){
                //System.out.println("Absorbed: "+currentToken.value);
                tokensConsumed.add(currentToken);
                c.foundSomething = true;
                currentToken = getNextToken(); // token matched, grab the next one and keep going

                if(currentToken == null) NO_MORE_TOKENS = true;
                if(transitionCount == c.transitions.size()) return State.FOUND_MATCH;

            }else if(check == State.DEAD_END){
                if(c.foundSomething){
                    return State.FAILURE;
                }
                break;
            }else if(check == State.FAILURE){
                return State.FAILURE;
            }

            transitionCount++;
        }

        return State.DEAD_END;
    }

    public void incorrectSyntax(){
        System.out.println("-------------------------------------------");
        System.out.println("Syntax is incorrect");
        if(tokensConsumed.size() > 0)
            System.out.println("-- Error after to token: '"+tokensConsumed.get(tokensConsumed.size()-1).value +"' on line: "+tokensConsumed.get(tokensConsumed.size()-1).lineNumber);
        else
            System.out.println("-- No tokens consumed!");
        System.out.println("-------------------------------------------");
    }

    public void correctSyntax(){
        System.out.println("------------------");
        System.out.println("Syntax is correct");
        System.out.println("------------------");
    }

    public void parse(){

        Grammar g = new Grammar();
        currentTransition = g.getStart();
        State check = checkTransition(currentTransition);

        if(currentTransition.label.equals("start")  && (check.equals(State.FOUND_MATCH) || check.equals(State.EMPTY) || check.equals(State.EMPTY_BUT_SUCCESS))){
            //correctSyntax();
        }else{
            //incorrectSyntax();
            Compiler.printError(" incorrect syntax");
        }
    }
}