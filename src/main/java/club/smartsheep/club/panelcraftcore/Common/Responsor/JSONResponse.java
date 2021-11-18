package club.smartsheep.club.panelcraftcore.Common.Responsor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

public class JSONResponse {
    public static void Response(HttpExchange exchange, Map<String, Object> response, int statusCode) {
        try {
            exchange.getResponseHeaders().put("Content-type", Collections.singletonList("application/json"));

            OutputStream responseBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, new ObjectMapper().writeValueAsString(response).length());
            responseBody.write(new ObjectMapper().writeValueAsString(response).getBytes());
            responseBody.flush();
            responseBody.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
