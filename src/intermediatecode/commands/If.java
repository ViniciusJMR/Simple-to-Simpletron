package intermediatecode.commands;

import core.Token;
import intermediatecode.ACommand;
import intermediatecode.CommandType;
import intermediatecode.TemporarySymbol;

import java.util.AbstractCollection;
import java.util.List;
import java.util.Map;

public class If extends ACommand {
    private String placeholder;
    private Token targetLine;

    public If(List<Token> tokens) {
        super(tokens);
    }

    @Override
    public String getCodeLines(int index, Map<Integer, TemporarySymbol> symbols, Map<Integer, String> reversedSymbolMap) {
        var firstValue = tokens.get(1);
        var relationalOp = tokens.get(2);
        var secondValue = tokens.get(3);
        var goToLine = tokens.get(5);

        placeholder = GoTo.MARKER + index + GoTo.MARKER;
        targetLine = goToLine;
        var code = new StringBuilder();
        var firstValueHolder = ACommand.getPlaceholder(firstValue, symbols, reversedSymbolMap);
        var secondValueHolder = ACommand.getPlaceholder(secondValue, symbols, reversedSymbolMap);

        switch (relationalOp.getType()){
            case EQ -> {
                code.append(CommandType.LOAD.getUid()).append(firstValueHolder).append("\n")
                        .append(CommandType.SUBTRACT.getUid()).append(secondValueHolder).append("\n")
                        .append(CommandType.BRANCH_ZERO.getUid()).append(placeholder);
            }
            case NE-> {
                code.append(CommandType.LOAD.getUid()).append(firstValueHolder).append("\n")
                        .append(CommandType.SUBTRACT.getUid()).append(secondValueHolder).append("\n")
                        .append(CommandType.BRANCH_ZERO.getUid()).append(placeholder).append("\n")
                        .append(CommandType.BRANCH.getUid()).append(placeholder);
            }
            case LT -> {
                code.append(CommandType.LOAD.getUid()).append(firstValueHolder).append("\n")
                        .append(CommandType.SUBTRACT.getUid()).append(secondValueHolder).append("\n")
                        .append(CommandType.BRANCH_NEG.getUid()).append(placeholder);
            }
            case GT -> {
                code.append(CommandType.LOAD.getUid()).append(secondValueHolder).append("\n")
                        .append(CommandType.SUBTRACT.getUid()).append(firstValueHolder).append("\n")
                        .append(CommandType.BRANCH_NEG.getUid()).append(placeholder);
            }
            case GE -> {
                code.append(CommandType.LOAD.getUid()).append(secondValueHolder).append("\n")
                        .append(CommandType.SUBTRACT.getUid()).append(firstValueHolder).append("\n")
                        .append(CommandType.BRANCH_NEG.getUid()).append(placeholder).append("\n")
                        .append(CommandType.BRANCH.getUid()).append(placeholder);
            }
            case LE -> {
                code.append(CommandType.LOAD.getUid()).append(firstValueHolder).append("\n")
                        .append(CommandType.SUBTRACT.getUid()).append(secondValueHolder).append("\n")
                        .append(CommandType.BRANCH_NEG.getUid()).append(placeholder).append("\n")
                        .append(CommandType.BRANCH.getUid()).append(placeholder);

            }
        }

        return code.toString();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public int getTargetLine(Map<Integer, String> reversedSymbolMap) {
        return Integer.parseInt(reversedSymbolMap.get(targetLine.getAddress()));
    }
}
