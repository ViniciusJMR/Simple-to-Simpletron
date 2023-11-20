package intermediatecode;

import core.Symbol;
import core.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAnalyzer{
    protected List<Token> tokens;
    protected Token currentToken;
    protected List<Token> lineTokens;
    protected boolean hasReachedEnd = false;
    protected List<Integer> missingLineTokens;
    protected List<Exception> exceptions;
    protected List<Token> originalTokens;
    protected boolean isOnIf;
    protected List<Token> stack;
    protected boolean skipErrors = true;

    protected Token shift() {
        Token first = tokens.get(0);
        tokens.remove(0);
        currentToken = first;
        return first;
    }

    public abstract List<Exception> analyze();

    protected void extractLineTokens() {
        originalTokens = new ArrayList<>(tokens);

        lineTokens = new ArrayList<>();
        boolean isEndOfLine = false;
        missingLineTokens = new ArrayList<>();

        for (int index = 0; index < tokens.size(); index++) {
            Token token = tokens.get(index);
            if (token.getType() == Symbol.LF) {
                isEndOfLine = true;
                continue;
            }
            if ((isEndOfLine || index == 0) && token.getType() == Symbol.INTEGER) {
                lineTokens.add(token);
                isEndOfLine = false;
            } else if ((isEndOfLine || index == 0) && token.getType() != Symbol.INTEGER && token.getType() != Symbol.REM) {
                missingLineTokens.add(token.getLine());
                isEndOfLine = false;
            }
        }

        tokens.removeIf(t -> lineTokens.contains(t));
        this.lineTokens = lineTokens; // FOR TESTING IF GOTO LINE EXISTS
    }

    public abstract Exception createError(String message, int line, int column);

    protected void throwError(String message, int line, int column) {
        exceptions.add(createError(message, line, column));
        if (!skipErrors) {
            skipLine();
        }
    }


    protected void skipLine() {
        Token currentToken = this.currentToken;
        while (currentToken != null && currentToken.getType() != Symbol.LF) {
            shift();
        }
        isOnIf = false;
        stack.clear();
    }
}
