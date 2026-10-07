package common;

import com.google.gson.JsonElement;

public class Response {
    private String response, reason;
    private JsonElement value;

    public Response(String response) {
        this.response = response;
    }

    public Response(String response, JsonElement value) {
        this(response);
        this.value = value;
    }

    public Response(String response, JsonElement value, String reason) {
        this(response, value);
        this.reason = reason;
    }
}
