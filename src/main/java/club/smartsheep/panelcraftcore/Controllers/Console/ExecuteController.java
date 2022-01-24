package club.smartsheep.panelcraftcore.Controllers.Console;

import club.smartsheep.panelcraftcore.Common.ActionRecorder.RecordAction;
import club.smartsheep.panelcraftcore.Common.CommandExecutor;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import club.smartsheep.panelcraftcore.PanelCraft;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.IOException;

public class ExecuteController extends PanelHttpHandler {
    @Override
    public void handle(PanelHttpExchange exchange) {
        JSONObject body = exchange.getRequestBodyJSONSafety();
        if(body == null) {
            return;
        }

        if(!body.has("command")) {
            exchange.getErrorSender().MissingArgumentsErrorResponse("command");
        }

        Bukkit.getScheduler().runTask(PanelCraft.getPlugin(PanelCraft.class), () -> {
            CommandExecutor executor = new CommandExecutor(Bukkit.getConsoleSender());
            Bukkit.dispatchCommand(executor, body.getString("command"));
            JSONObject response = new JSONObject();
            response.put("status", "success");
            response.put("data", executor.getRawMessageLog());
            executor.clearMessageLog();
            RecordAction.recordDown("Root", "Executed the command: " + body.getString("command"), exchange.getClientIP());
            try {
                exchange.send(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
