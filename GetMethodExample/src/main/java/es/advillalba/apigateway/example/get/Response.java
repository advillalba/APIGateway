package es.advillalba.apigateway.example.get;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private int httpStatus;
    private String body;
    private Map<String, String> headers;

    public Response() {
        this.headers = new HashMap<>();
        this.headers.put("Content-Type", "application/json");
        this.headers.put("Access-Control-Allow-Headers", "*");
        this.headers.put("Access-Control-Allow-Origin", "*");
        this.headers.put("Access-Control-Allow-Methods", "OPTIONS,POST,GET");

    }

    public Response success() {
        this.httpStatus = 200;
        return this;
    }
    public Response withPayload(String payload) {
        this.body = payload;
        return this;
    }
    public Response error() {
        this.httpStatus = 409;
        this.body = "{\"message\": \"Se ha producido un error al deserializar\"}";
        return this;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
