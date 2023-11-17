package intermediatecode.commands;

import core.Token;
import intermediatecode.ACommand;
import intermediatecode.CommandType;
import intermediatecode.TemporarySymbol;

import java.util.List;
import java.util.Map;

public class Read extends ACommand {
    public Read(List<Token> tokens) {
        super(tokens);
    }

    @Override
    public String getCodeLines(int index, Map<Integer, TemporarySymbol> symbols, Map<Integer, String> reversedSymbolMap){
        var printT = this.tokens.get(0);
        var valueT = this.tokens.get(1);
        return CommandType.READ + getPlaceholder(valueT, symbols, reversedSymbolMap);

    }
}