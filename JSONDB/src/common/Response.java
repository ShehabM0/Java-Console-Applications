package common;

public class Response {
    private String response, value, reason;

    public Response(String response) {
        this.response = response;
    }

    public Response(String response, String value) {
        this(response);
        this.value = value;
    }

    public Response(String response, String value, String reason) {
        this(response, value);
        this.reason = reason;
    }
}
