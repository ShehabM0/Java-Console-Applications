package client;

public class Request {
    private String type, key, value;

    Request(String type) {
        this.type = type;
    }

    Request(String type, String key) {
        this(type);
        this.key = key;
    }

    Request(String type, String key, String value) {
        this(type, key);
        this.value = value;
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
