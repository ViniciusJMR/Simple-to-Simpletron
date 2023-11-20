package intermediatecode;

import core.Symbol;
import core.Token;

import java.util.Map;

public class TemporarySymbol {

    private Token token;
    private int index;
    private int currentLine;
    private Token previousToken;

    public TemporarySymbol(Token token, int index, int currentLine, Token previousToken) {
        this.token = token;
        this.index = index;
        this.currentLine = currentLine;
        this.previousToken = previousToken;
    }

    public String getPlaceHolder() {
        return "%" + this.index +"%";
    }

    public String getReplacement() {
        return String.format("%02d", currentLine);
    }

    public String getDeclaration(Map<Integer, String> reversedSymbolMap, int currentLine){
        this.currentLine = currentLine;

        if (token.getType() == Symbol.INTEGER){
            var symbol = reversedSymbolMap.get(token.getAddress());
            if(previousToken.getType() == Symbol.SUBTRACT) {
                return String.format("-%04d", Integer.parseInt(symbol));
            }
            return String.format("+%04d", Integer.parseInt(symbol));

        } else {
            return "+0000";
        }
    }



}
