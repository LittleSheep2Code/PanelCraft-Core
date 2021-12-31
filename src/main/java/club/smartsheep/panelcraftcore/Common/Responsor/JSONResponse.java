package club.smartsheep.panelcraftcore.Common.Responsor;

import club.smartsheep.panelcraftcore.PanelCraft;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class JSONResponse {
    /**
     * HashMap -> JSONObject -> String -> Byte[]
     * Then the byte will write into the stream. And send it
     *
     * @param exchange HTTPExchange from your handler
     * @param response Response target
     * @param statusCode Response status code, recommend 200 or 400
     */
    public static void Response(HttpExchange exchange, Map<String, Object> response, int statusCode) {
        exchange.getResponseHeaders().put("Content-type", Collections.singletonList("application/json"));

        try {
            OutputStream responseBody = exchange.getResponseBody();
            JSONObject json = new JSONObject(response);
            exchange.sendResponseHeaders(statusCode, json.toString().getBytes(StandardCharsets.UTF_8).length);
            responseBody.write(json.toString().getBytes(StandardCharsets.UTF_8));
            responseBody.flush();
            responseBody.close();
        } catch (IOException ex) {
            PanelCraft.LOGGER.throwing("JSONResponse", "Response", ex);
        }
    }

    /**
     * JSONObject -> String -> Byte[]
     * Then the byte will write into the stream. And send it
     *
     * @param exchange HTTPExchange from your handler
     * @param response Response target
     * @param statusCode Response status code, recommend 200 or 400
     */
    public static void Response(HttpExchange exchange, JSONObject response, int statusCode) {
        try {
            exchange.getResponseHeaders().put("Content-type", Collections.singletonList("application/json"));

            OutputStream responseBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(statusCode, response.toString().getBytes(StandardCharsets.UTF_8).length);
            responseBody.write(response.toString().getBytes(StandardCharsets.UTF_8));
            responseBody.flush();
            responseBody.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
