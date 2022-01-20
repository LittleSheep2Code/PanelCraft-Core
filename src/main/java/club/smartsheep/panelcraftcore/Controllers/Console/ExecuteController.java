package club.smartsheep.panelcraftcore.Controllers.Console;

import club.smartsheep.panelcraftcore.Common.BodyProcessor;
import club.smartsheep.panelcraftcore.Common.CommandExecutor;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebHandler;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.ErrorResponse;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.JSONResponse;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.NullResponse;
import club.smartsheep.panelcraftcore.Common.Tokens.CheckPassword;
import club.smartsheep.panelcraftcore.PanelCraft;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.IOException;

public class ExecuteController extends PanelWebHandler {
    @Override
    public void handle(PanelWebExchange exchange) {
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
            try {
                exchange.send(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
