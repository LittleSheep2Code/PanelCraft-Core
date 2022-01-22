package club.smartsheep.panelcraftcore.Controllers.Status;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public class DatabaseStatusController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
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
