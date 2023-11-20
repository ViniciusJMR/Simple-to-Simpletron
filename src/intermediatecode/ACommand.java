package intermediatecode;

import core.Token;

import java.util.List;
import java.util.Map;

public abstract class ACommand {
    protected List<Token> tokens;
    private Token line;

    public ACommand(List<Token> tokens){
        this.tokens = tokens;
        line = this.tokens.remove(0);
    }

    protected static String getPlaceholder(
            Token token,
            Map<Integer, TemporarySymbol> symbols,
            Map<Integer, String> reversedSymbolMap
    ){

        return symbols.get(token.getAddress()).getPlaceHolder();
    }

    public String getLine(Map<Integer, String> reversedSymbolMap) {
        return reversedSymbolMap.get(this.line.getAddress());
    }

    public abstract String getCodeLines(int index, Map<Integer, TemporarySymbol> symbols, Map<Integer, String> reversedSymbolMap);

    public List<Token> getTokens(){
        return this.tokens;
    }
}
