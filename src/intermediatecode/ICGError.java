package intermediatecode;

public class ICGError extends Exception {
    private int column;
    private int line;
    private String message;

    public ICGError(String message, int line, int column) {
        super("LINE: " + line + ";\nCOLUMN: " + column);
        this.line = line;
        this.column = column;
        this.message = message;
    }
}
