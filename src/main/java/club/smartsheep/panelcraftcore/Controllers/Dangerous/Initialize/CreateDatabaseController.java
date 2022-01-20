package club.smartsheep.panelcraftcore.Controllers.Dangerous.Initialize;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseInitialization;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateDatabaseController extends PanelHttpHandler {
    @Override
    public void handle(PanelHttpExchange exchange) {
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
