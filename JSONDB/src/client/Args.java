package client;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = {"-t", "--type"}, required = true)
    private String type;

    @Parameter(names = {"-i", "--index"}) // EXIT not req
    private int index;

    @Parameter(names = {"-m", "--message"}) // req only SET
    private String message;

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public String getMessage() {
        return message;
    }
}
