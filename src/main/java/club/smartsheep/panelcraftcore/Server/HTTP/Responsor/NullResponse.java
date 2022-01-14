package club.smartsheep.panelcraftcore.Server.HTTP.Responsor;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class NullResponse {
    public static void Response(HttpExchange exchange, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, 0);
        exchange.getResponseBody().close();
        exchange.getResponseBody().flush();
    }
}
