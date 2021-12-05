package club.smartsheep.panelcraftcore.Common.Responsor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
    public static void CustomErrorResponse(HttpExchange exchange, String errorName, String errorMessage) {
        try {
            exchange.getResponseHeaders().put("Content-type", Collections.singletonList("application/json"));

            Map<String, Object> response = new HashMap<>();
            response.put("error", errorName);
            response.put("message", errorMessage);

            OutputStream responseBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(400, new ObjectMapper().writeValueAsString(response).length());
            responseBody.write(new ObjectMapper().writeValueAsString(response).getBytes());
            responseBody.flush();
            responseBody.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void CustomErrorResponse(HttpExchange exchange, String errorName, String errorMessage, int statusCode) {
        try {
            exchange.getResponseHeaders().put("Content-type", Collections.singletonList("application/json"));

            Map<String, Object> response = new HashMap<>();
            response.put("error", errorName);
            response.put("message", errorMessage);

            OutputStream responseBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(statusCode, new ObjectMapper().writeValueAsString(response).length());
            responseBody.write(new ObjectMapper().writeValueAsString(response).getBytes());
            responseBody.flush();
            responseBody.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void BodyProcessErrorResponse(HttpExchange exchange, String formatNeed) {
        CustomErrorResponse(exchange, "FormatBodyFailed", "Please check your body, this api require a " + formatNeed + " data.");
    }
}
