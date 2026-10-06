package server;

public class Response {
    private String response, value, reason;

    Response(String response) {
        this.response = response;
    }

    Response(String response, String value) {
        this(response);
        this.value = value;
    }

    Response(String response, String value, String reason) {
        this(response, value);
        this.reason = reason;
    }
}
