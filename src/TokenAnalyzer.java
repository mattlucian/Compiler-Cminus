/**
 * Created by matt on 1/14/15.
 */
public class TokenAnalyzer {

    public boolean isDigit(char c){
        String input = ""+c;
        return input.matches("[0-9]");
    }

    public boolean isCharacter(char c){
        String input = ""+c;
        return input.matches("[A-Za-z]");
    }

    public boolean isOperator(char c){
        char operators[] = {'*', '+','-','/','<','=','>','!',';',',','(',')','[',']','{','}'};
        for(char g : operators){
            if (c == g)
                return true;
        }
        return false;
    }

    public boolean isExpandableOperator(char c){
        char expandableOperators[] = {'*','/','<','=','>','!'};
        for(char g : expandableOperators){
            if (c == g)
                return true;
        }
        return false;
    }

    public boolean isValidExpandedOperator(char previous, char next){
        if(next == '='){
            switch (previous){
                case '=':
                case '!':
                case '>':
                case '<':
                    return true;
            }
        }

        if(previous == '/'){
            if(next == '*' || next == '/')
                return true;
        }else if (previous == '*'){
            if(next == '/')
                return true;
        }

        return false;
    }

    public boolean isKeyword(String s){
        String keywords[] = {"int","while","if","else","return","void", "float"};
        for(String key : keywords){
            if(s.equals(key))
                return true;
        }
        return false;
    }

    public boolean isPeriod(char c){
        return (c == '.');
    }

    public boolean isExponentialSign(char c){
        return (c == 'E');
    }

}
