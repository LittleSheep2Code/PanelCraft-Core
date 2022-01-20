package club.smartsheep.panelcraftcore.Controllers.Status;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebHandler;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.JSONResponse;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.NullResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseStatusController extends PanelWebHandler {
    @Override
    @SneakyThrows
    public void handle(PanelWebExchange exchange) {
        try {
            boolean threeSecond = DatabaseConnector.get().connect().isValid(3);
            boolean thirtySecond = DatabaseConnector.get().connect().isValid(30);

            Map<String, Object> response = new HashMap<>();
            response.put("3-seconds", threeSecond);
            response.put("30-seconds", thirtySecond);
            exchange.send(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getLocalizedMessage());
            response.put("cause", e.getCause().getLocalizedMessage());
            exchange.setStatusCode(500);
            exchange.send(response);
        }
    }
}
