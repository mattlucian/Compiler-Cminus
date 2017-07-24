import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by matt on 1/14/15.
 *
 * Description:
 * - The "Reader" is passed a bufferedReader of the file and reads
 * through the contents of the file. It does this in the following manner:
 *  1. Scan each line of the file
 *  2. If the line is empty, disregard, otherwise
 *  3. Check character by character and compare them to the valid values
 *     that the program can accept
 *  4. Once a token has been identified, store it with a value, type, and line #
 *  5. Once all tokens have been analyzed, print the results
 *      5.A: First print the input line that was scanned
 *      5.B: For any tokens that belong to that line, print the type and value
 *
 */
public class Reader {

    private String currentLine;
    public BufferedReader file;
    private boolean isInComment = false;
    public ArrayList<LexToken> tokens = new ArrayList<LexToken>();
    public ArrayList<String> inputLines = new ArrayList<String>();
    Stack<String> commentLevel = new Stack<String>();
    public int bracketLevel = 0;

    Reader(BufferedReader fileReader){
        file = fileReader;
    }

    public void scanContents(){
        try{
            int count = 1;
            while((currentLine = file.readLine()) != null){
                if(currentLine.trim().isEmpty()){
                    count++;
                    continue;
                }
                inputLines.add(currentLine);
                analyzeLine(count++);
            }
        }catch (Exception e){
            System.out.println("Error! "+e.getMessage());
        }
    }

    public void printLines(){
        int count = 0;
        for(String line : inputLines){
            System.out.println("INPUT: "+line);
            for(LexToken lt : tokens){
                if(lt.lineNumber == (count)){
                    System.out.println(lt.toString());
                }
            }
            count++;
        }
    }

    private void analyzeLine(int lineNumber){
        LexToken currentToken = new LexToken(lineNumber,bracketLevel);

        TokenAnalyzer analyzer = new TokenAnalyzer();
        for(int i = 0; i < currentLine.length(); i++){

            //region End Line Character
            if(currentLine.charAt(i) == '\n') {
                return;
            }
            //endregion

            //region Spaces and Tabs
            else if((currentLine.charAt(i) == ' ' || currentLine.charAt(i) == '\t')&&(!isInComment)){

                currentToken = new LexToken(lineNumber,bracketLevel);

            }
            //endregion

            //region Operators
            else if(analyzer.isOperator(currentLine.charAt(i))) {
                if(!currentToken.token_type.equals("")){
                    // if NOT an operator, new token
                    currentToken = new LexToken(lineNumber,bracketLevel);
                }
                //Expandable Operators
                if (analyzer.isExpandableOperator(currentLine.charAt(i)) && ((i + 1) < currentLine.length())) {
                    // Expansion is valid, not something like "!>" for example
                    if (analyzer.isValidExpandedOperator(currentLine.charAt(i), currentLine.charAt(i + 1))) {

                        String operator = "" + currentLine.charAt(i) + currentLine.charAt(i + 1);

                        if (operator.equals("//")) {
                            if(!isInComment) {
                                return;
                            }
                        } else if (operator.equals("/*")) {
                            commentLevel.push("InAComment");
                            isInComment = true;
                        } else if (operator.equals("*/")) {
                            if(!commentLevel.isEmpty())
                                commentLevel.pop();
                            else {
                                tokens.add(new LexToken(lineNumber, "*",bracketLevel));
                                tokens.add(new LexToken(lineNumber, "/",bracketLevel));

                                // new token
                                currentToken = new LexToken(lineNumber,bracketLevel);
                            }

                            if (commentLevel.isEmpty())
                                isInComment = false;

                        }else if (operator.equals("==") && !isInComment){
                            tokens.add(new LexToken(lineNumber, "==",bracketLevel));

                        }else if (operator.equals("!=") && !isInComment){
                            tokens.add(new LexToken(lineNumber, "!=",bracketLevel));

                        }else if (operator.equals("<=") && !isInComment){
                            tokens.add(new LexToken(lineNumber, "<=",bracketLevel));

                        }else if (operator.equals(">=") && !isInComment){
                            tokens.add(new LexToken(lineNumber, ">=",bracketLevel));
                        }

                        i++;
                    }else{
                        // single equals sign, etc.
                        if(!isInComment) {
                            char firstChar = currentLine.charAt(i);
                            switch (firstChar) {
                                case '=':
                                case '<':
                                case '>':
                                case '*':
                                case '/':
                                    tokens.add(new LexToken(lineNumber, ""+firstChar,bracketLevel));
                                    break;
                                default:
                                    tokens.add(new LexToken(lineNumber,"ERROR:",""+firstChar,bracketLevel));
                                    break;
                            }
                        }
                    }
                }else{
                    // single parenthesis, etc.
                    if(!isInComment){
                        char firstChar = currentLine.charAt(i);

                        if(firstChar == '{') {
                            bracketLevel += 1;
                        }else if (firstChar == '}' && bracketLevel != 0){
                            bracketLevel -= 1;
                        }

                        switch (firstChar){
                            case '{':
                            case '}':
                            case '[':
                            case ']':
                            case '(':
                            case ')':
                            case '+':
                            case '-':
                            case ';':
                            case ',':
                                tokens.add(new LexToken(lineNumber, ""+firstChar,bracketLevel));
                                break;
                            default:
                                tokens.add(new LexToken(lineNumber, "ERROR:", ""+firstChar,bracketLevel));
                                break;
                        }
                    }
                }
            }
            //endregion

            //region Everything Else
            else if (!isInComment){
                currentToken.value += currentLine.charAt(i);

                if(analyzer.isDigit(currentLine.charAt(i))){
                    while((i+1 < currentLine.length()) && analyzer.isDigit(currentLine.charAt(i + 1))){
                        currentToken.value += currentLine.charAt(++i);
                        if(i+1 >= currentLine.length()){
                            break;
                        }
                    }
                    if(!currentToken.token_type.equals("ERROR:"))
                        currentToken.token_type = "NUM:";

                    // check for floats
                    if( (i+1 < currentLine.length()) &&  (currentLine.charAt(i+1) == 'E')){
                        currentToken.token_type = "FLON:";
                        currentToken.value += currentLine.charAt(++i);

                        // detect first digit
                        if((i+1 < currentLine.length()) &&  (analyzer.isDigit(currentLine.charAt(i+1)))){
                            // more digits
                            while( (i+1 < currentLine.length()) && analyzer.isDigit(currentLine.charAt(i+1))){
                                currentToken.value += currentLine.charAt(++i);
                                if(i+1 >= currentLine.length()){
                                    break;
                                }
                            }

                        // +/- sign first, not digit
                        }else if( (i+1 < currentLine.length()) && ( currentLine.charAt(i+1) == '+' || currentLine.charAt(i+1) == '-')){
                            currentToken.value += currentLine.charAt(++i);

                            // if followed by a digit
                            if((i+1 < currentLine.length()) && (analyzer.isDigit(currentLine.charAt(i+1)))){
                                currentToken.value += currentLine.charAt(++i);
                                // more digits
                                while((i+1 < currentLine.length()) && (analyzer.isDigit(currentLine.charAt(i+1)))){
                                    currentToken.value += currentLine.charAt(++i);
                                    if(i+1 >= currentLine.length()){
                                        break;
                                    }
                                }
                            }else{
                                currentToken.token_type = "ERROR:";
                            }
                        }

                    }else if( (i+1 < currentLine.length()) && (currentLine.charAt(i+1) == '.')){
                        currentToken.value += currentLine.charAt(++i);
                        currentToken.token_type = "FLON:";

                        if((i+1 < currentLine.length()) && (analyzer.isDigit(currentLine.charAt(i+1)))){
                            while((i+1 < currentLine.length()) && analyzer.isDigit(currentLine.charAt(i+1))){
                                currentToken.value += currentLine.charAt(++i);
                                if(i+1 >= currentLine.length()){
                                    break;
                                }
                            }

                            if((i+1 < currentLine.length()) && (currentLine.charAt(i+1) == 'E')){
                                currentToken.value += currentLine.charAt(++i);

                                if((i+1 < currentLine.length()) && (analyzer.isDigit(currentLine.charAt(i+1)))){
                                    // more digits
                                    while((i+1 < currentLine.length()) && (analyzer.isDigit(currentLine.charAt(i+1)))){
                                        currentToken.value += currentLine.charAt(++i);
                                        if(i+1 >= currentLine.length()){
                                            break;
                                        }
                                    }

                                    // +/- sign first, not digit
                                }else if((i+1 < currentLine.length()) && (currentLine.charAt(i+1) == '+' || currentLine.charAt(i+1) == '-')){
                                    currentToken.value += currentLine.charAt(++i);

                                    // if followed by a digit
                                    if((i+1 < currentLine.length())&&(analyzer.isDigit(currentLine.charAt(i+1)))){
                                        currentToken.value += currentLine.charAt(++i);
                                        // more digits
                                        while((i+1 < currentLine.length())&& (analyzer.isDigit(currentLine.charAt(i+1)))) {
                                            currentToken.value += currentLine.charAt(++i);
                                            if(i+1 >= currentLine.length()){
                                                break;
                                            }
                                        }
                                    }else{
                                        currentToken.token_type = "ERROR:";
                                    }
                                }
                            }

                        }else{
                            currentToken.token_type = "ERROR:";
                        }

                    }

                    tokens.add(currentToken);
                    currentToken = new LexToken(lineNumber,bracketLevel);

                }

                else if (analyzer.isCharacter(currentLine.charAt(i))){
                    while( (i+1 < currentLine.length()) && (analyzer.isCharacter(currentLine.charAt(i+1)))){
                        currentToken.value += currentLine.charAt(++i);
                        if(i+1 >= currentLine.length()){
                            break;
                        }
                    }
                    if(!currentToken.token_type.equals("ERROR:")){
                        if(analyzer.isKeyword(currentToken.value))
                            currentToken.token_type = "KEYWORD:";
                        else
                            currentToken.token_type = "ID:";
                    }

                    tokens.add(currentToken);
                    currentToken = new LexToken(lineNumber,bracketLevel);

                    if((i+1 < currentLine.length()) && (analyzer.isDigit(currentLine.charAt(i+1)))){
                        currentToken.token_type = "ERROR:";
                        currentToken.value += currentLine.charAt(++i);
                        while((i+1 < currentLine.length())&&(analyzer.isDigit(currentLine.charAt(i+1)))){
                            currentToken.value += currentLine.charAt(++i);
                        }
                        tokens.add(currentToken);
                        currentToken = new LexToken(lineNumber,bracketLevel);
                    }

                }else{
                    currentToken.token_type = "ERROR:";
                }
            }
            //endregion

        }
    }

    public ArrayList<LexToken> getTokens(){
        return tokens;
    }
}