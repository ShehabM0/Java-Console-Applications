package client;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

class JsonElementConverter implements IStringConverter<JsonElement> {
    @Override
    public JsonElement convert(String value) {
        return new JsonPrimitive(value);
    }
}

public class Args {
    @Parameter(names = {"-in", "--input"})
    private String inputFile;

    @Parameter(names = {"-t", "--type"})
    private String type;

    @Parameter(names = {"-k", "--key"}) // EXIT not req
    private Object key; // String | Array

    @Parameter(names = {"-v", "--value"}, converter = JsonElementConverter.class) // req only SET
    private JsonElement value;

    public String getInputFile() {
        return inputFile;
    }

    public String getType() {
        return type;
    }

    public Object getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }
}
