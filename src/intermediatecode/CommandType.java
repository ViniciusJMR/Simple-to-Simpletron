package intermediatecode;

public enum CommandType {
    READ("+10"),
    WRITE("+11"),
    LOAD("+20"),
    STORE("+21"),
    ADD("+30"),
    SUBTRACT("+31"),
    DIVIDE("+32"),
    MULTIPLY("+33"),
    MODULE("+34"),
    BRANCH("+40"),
    BRANCH_NEG("+41"),
    BRANCH_ZERO("+42"),
    HALT("+4300");

    private final String uid;

    CommandType(String uid) {
        this.uid = uid;
    }

    public String getUrl(){
        return uid;
    }
}
