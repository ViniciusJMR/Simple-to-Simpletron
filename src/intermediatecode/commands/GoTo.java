package intermediatecode.commands;

import core.Token;
import intermediatecode.ACommand;
import intermediatecode.CommandType;
import intermediatecode.TemporarySymbol;

import java.util.List;
import java.util.Map;

public class GoTo extends ACommand {

    private String placeholder;
    public static final String MARKER = "GT";

    public GoTo(List<Token> tokens) {
        super(tokens);
    }

    @Override
    public String getCodeLines(int index, Map<Integer, TemporarySymbol> symbols, Map<Integer, String> reversedSymbolMap) {
        placeholder = MARKER + index + MARKER;
        return CommandType.BRANCH + placeholder;
    }

    public int getTargetLine(Map<Integer, String> reversedSymbolMap) {
        return Integer.parseInt(reversedSymbolMap.get(tokens.get(1).getAddress()));
    }

    public String getPlaceholder() {
        return placeholder;
    }


}
