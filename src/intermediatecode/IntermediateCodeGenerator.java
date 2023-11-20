package intermediatecode;

import core.Symbol;
import core.Token;
import intermediatecode.ACommand;
import intermediatecode.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.Symbol.LET;
import static core.Symbol.PRINT;

public class IntermediateCodeGenerator extends AbstractAnalyzer{
    private Map<Integer, String> reversedSymbolMap = new HashMap<>();
    private Map<String, Integer> symbolMap;
    private List<List<Token>> lines = new ArrayList<>();
    private List<Token> currentLine = new ArrayList<>();
    private List<ACommand> commands = new ArrayList<>();
    private String code = null;

    public IntermediateCodeGenerator(List<Token> tokens, Map<String, Integer> symbolMap) {
        super();
        this.tokens = tokens;
        this.symbolMap = symbolMap;
        for (Map.Entry<String, Integer> entry : symbolMap.entrySet()) {
            reversedSymbolMap.put(entry.getValue(), entry.getKey());
        }
    }

    public String getCode() {
        return code;
    }

    @Override
    public List<Exception> analyze() {
        boolean isInComment = false;

        while (!tokens.isEmpty()) {
            Token current = shift();
            if (current.getType() == Symbol.REM) {
                isInComment = true;
            }

            if (current.getType() == Symbol.LF) {
                lines.add(new ArrayList<>(currentLine));
                currentLine.clear();
                isInComment = false;
            } else if (!isInComment) {
                currentLine.add(currentToken);
            }
        }

        for (List<Token> currentLine : lines) {
            processLine(currentLine);
        }

        code = CommandProcessor.process(commands, reversedSymbolMap, false);
        return exceptions;
    }

    private void processLine(List<Token> line) {
        for (Token current : line) {
            Symbol symbol = current.getType();

            switch (symbol) {
                case GOTO:
                    commands.add(new GoTo(line));
                    return;

                case IF:
                    commands.add(new If(line));
                    return;

                case LET:
                case ASSIGNMENT:
                    commands.add(new Arithmetics(line));
                    return;

                case INPUT:
                    commands.add(new Read(line));
                    return;

                case PRINT:
                    commands.add(new Write(line));
                    return;
            }
        }
    }

    @Override
    public ICGError createError(String message, int line, int column) {
        return new ICGError(message, line, column);
    }
}
