package club.smartsheep.panelcraftcore.Controllers.Console.Placeholder;

import club.smartsheep.panelcraftcore.Common.ActionRecorder.RecordAction;
import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import club.smartsheep.panelcraftcore.PanelCraft;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderProcessorController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        if(!PanelCraft.HookStatues.getOrDefault("placeholderAPI", false)) {
            new ErrorLoggers().ModuleDoNotEnable("placeholderAPI");
            exchange.getErrorSender().ModuleUnactivatedErrorResponse("placeholderAPI");
            return;
        }

        JSONObject body = exchange.getRequestBodyJSONSafety();
        if(body == null) {
            return;
        }

        if(!body.has("message")) {
            exchange.getErrorSender().MissingArgumentsErrorResponse("message");
            return;
        }

        if(body.getString("message").length() >= 1200) {
            exchange.getErrorSender().DataErrorResponse("message length must less than 1200 character!");
        }

        if(!body.has("player")) {
            exchange.getErrorSender().MissingArgumentsErrorResponse("player");
            return;
        }

        Player player = Bukkit.getPlayerExact(body.getString("player"));
        if(player == null) {
            exchange.getErrorSender().DataErrorResponse("Cannot use your provided player name get player!");
            return;
        }

        Map<String, Object> response = new HashMap<>();
        RecordAction.recordDown(exchange.Authorization_Username,
                "Use PlaceholderAPI formatted a message: " + body.getString("message"),
                exchange.getClientIP());
        response.put("status", "success");
        response.put("data", PlaceholderAPI.setPlaceholders(player, body.getString("message")));

        exchange.send(response);
    }
}
