package club.smartsheep.panelcraftcore.Server.HTTP;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PanelHttpExchange {
    public enum HeaderTypes {
        CONTENT_TYPE("Content-Type");

        final String name;

        HeaderTypes(String name) {
            this.name = name;
        }
    }

    private final HttpExchange exchange;

    private int statusCode = 200;

    public String Authorization_Code;
    public String Authorization_Username;

    public PanelHttpExchange(HttpExchange exchange) {
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
            this.getErrorSender().BodyProcessErrorResponse("failed to format your request body to String!");
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
            this.getErrorSender().BodyProcessErrorResponse("failed to format your request body to JSONObject!");
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

    public String getHeader(String name) {
        return exchange.getResponseHeaders().get(name).get(0);
    }

    public String getHeader(String name, int index) {
        return exchange.getResponseHeaders().get(name).get(index);
    }

    public List<String> getHeaders(String name) {
        return exchange.getResponseHeaders().get(name);
    }

    public String getClientIP() {
        return exchange.getRemoteAddress().getHostString();
    }

    public PanelHttpErrorSender getErrorSender() { return new PanelHttpErrorSender(this); }

    public void send() throws IOException {
        OutputStream responseStream = this.exchange.getResponseBody();
        this.exchange.sendResponseHeaders(this.statusCode, 0);
        responseStream.flush();
        responseStream.close();
    }

    public void send(String data, String contentType) throws IOException {
        this.setHeader(HeaderTypes.CONTENT_TYPE, contentType);
        OutputStream responseStream = this.exchange.getResponseBody();
        this.exchange.sendResponseHeaders(this.statusCode, data.getBytes(StandardCharsets.UTF_8).length);
        responseStream.write(data.getBytes(StandardCharsets.UTF_8));
        responseStream.flush();
        responseStream.close();
    }

    public void send(String data) throws IOException {
        this.send(data, "plain/text");
    }

    public void send(JSONObject data) throws IOException {
        this.send(data.toString(), "application/json");
    }

    public void send(Map<String, Object> data) throws IOException {
        this.send(new JSONObject(data).toString(), "application/json");
    }
}
