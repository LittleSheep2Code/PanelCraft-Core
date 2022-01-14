package club.smartsheep.panelcraftcore.Server.HTTP;

import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.ErrorResponse;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PanelWebExchange {
    public enum HeaderTypes {
        CONTENT_TYPE("Content-Type");

        final String name;

        HeaderTypes(String name) {
            this.name = name;
        }
    }

    private final HttpExchange exchange;

    private int statusCode = 200;

    public PanelWebExchange(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public HttpExchange getOriginalExchange() {
        return this.exchange;
    }

    public String getRequestBody() throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    public String getRequestBodySafety() {
        try {
            return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            ErrorResponse.BodyProcessErrorResponse(exchange, "failed to format your request body to String!");
            return null;
        }
    }

    public JSONObject getRequestBodyJSON() throws IOException {
        return new JSONObject(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
    }

    public JSONObject getRequestBodyJSONSafety() {
        try {
            return new JSONObject(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            ErrorResponse.BodyProcessErrorResponse(exchange, "failed to format your request body to JSONObject!");
            return null;
        }
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeader(String name, String value) {
        exchange.getResponseHeaders().put(name, Collections.singletonList(value));
    }

    public void setHeader(String name, List<String> value) {
        exchange.getResponseHeaders().put(name, value);
    }

    public void setHeader(HeaderTypes type, String value) {
        exchange.getResponseHeaders().put(type.name, Collections.singletonList(value));
    }

    public void setHeader(HeaderTypes type, List<String> value) {
        exchange.getResponseHeaders().put(type.name, value);
    }

    public void send(String data) throws IOException {
        OutputStream responseStream = this.exchange.getResponseBody();
        this.exchange.sendResponseHeaders(this.statusCode, data.getBytes(StandardCharsets.UTF_8).length);
        responseStream.write(data.getBytes(StandardCharsets.UTF_8));
        responseStream.flush();
        responseStream.close();
    }

    public void send(JSONObject data) throws IOException {
        this.send(data.toString());
    }

    public void send(Map<Object, Object> data) throws IOException {
        this.send(new JSONObject(data).toString());
    }
}
