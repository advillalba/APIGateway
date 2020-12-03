package es.advillalba.apigateway.example.post;

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
    public Response success(){
        this.httpStatus = 200;
        return this;
    }
    public Response body(String url){

        this.body = String.format("\"createdFile\": \"%s\"",url);
        return this;
    }
    public Response error(){
        this.httpStatus = 409;
        this.body = "{\"message\": \"Error al procesar el payload\"}";
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
