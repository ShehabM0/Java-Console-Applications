package common;

import com.google.gson.JsonElement;

public class Request {
    private String type;
    private Object key;
    private JsonElement value;

    public Request(String type) {
        this.type = type;
    }

    public Request(String type, Object key) {
        this(type);
        this.key = key;
    }

    public Request(String type, Object key, JsonElement value) {
        this(type, key);
        this.value = value;
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
