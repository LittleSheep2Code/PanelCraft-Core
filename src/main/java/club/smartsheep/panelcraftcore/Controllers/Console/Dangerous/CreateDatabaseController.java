package club.smartsheep.panelcraftcore.Controllers.Console.Dangerous;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseInitialization;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateDatabaseController extends PanelWebHandler {
    @Override
    public void handle(PanelWebExchange exchange) {
        try {
            DatabaseInitialization.setupDatabases();
        } catch (Exception e) {
            exchange.getErrorSender().SQLErrorResponse(null, "Error when creating database");
            return;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        try {
            exchange.send(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
