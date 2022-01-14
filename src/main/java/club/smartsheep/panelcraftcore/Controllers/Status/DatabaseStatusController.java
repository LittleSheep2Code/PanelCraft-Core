package club.smartsheep.panelcraftcore.Controllers.Status;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.JSONResponse;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.NullResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseStatusController implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            NullResponse.Response(exchange, 405);
            return;
        }

        try {
            DatabaseConnector.get().connect();
            boolean threeSecondTest = DatabaseConnector.get().connect().isValid(3);
            boolean thirtySecondTest = DatabaseConnector.get().connect().isValid(30);

            Map<String, Object> response = new HashMap<>();
            response.put("threeSecondTest", threeSecondTest);
            response.put("thirtySecondTest", thirtySecondTest);

            JSONResponse.Response(exchange, response, 200);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getLocalizedMessage());
            response.put("cause", e.getCause().getLocalizedMessage());

            JSONResponse.Response(exchange, response, 500);
        }
    }
}
