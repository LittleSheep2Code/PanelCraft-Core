package club.smartsheep.club.panelcraftcore.Common;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class StatusCodeSetter {
    public static void setStatusCode(HttpExchange exchange, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, 0);
        exchange.getResponseBody().close();
        exchange.getResponseBody().flush();
    }
}
