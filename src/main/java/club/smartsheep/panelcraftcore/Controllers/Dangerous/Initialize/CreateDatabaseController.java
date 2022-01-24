package club.smartsheep.panelcraftcore.Controllers.Dangerous.Initialize;

import club.smartsheep.panelcraftcore.Common.ActionRecorder.RecordAction;
import club.smartsheep.panelcraftcore.Common.Configure.DatabaseInitialization;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public class CreateDatabaseController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        try {
            DatabaseInitialization.setupDatabases();
        } catch (Exception e) {
            e.printStackTrace();
            exchange.getErrorSender().SQLErrorResponse(null, "Error when creating database");
            return;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        exchange.send(response);
    }
}
