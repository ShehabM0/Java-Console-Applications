package client;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = {"-in", "--input"})
    private String inputFile;

    @Parameter(names = {"-t", "--type"})
    private String type;

    @Parameter(names = {"-k", "--key"}) // EXIT not req
    private String key;

    @Parameter(names = {"-v", "--value"}) // req only SET
    private String value;

    public String getInputFile() {
        return inputFile;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
