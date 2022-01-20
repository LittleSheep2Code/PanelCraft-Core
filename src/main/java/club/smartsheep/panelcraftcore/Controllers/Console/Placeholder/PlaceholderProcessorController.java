package club.smartsheep.panelcraftcore.Controllers.Console.Placeholder;

import club.smartsheep.panelcraftcore.Common.BodyProcessor;
import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
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
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlaceholderProcessorController extends PanelWebHandler {
    @Override
    @SneakyThrows
    public void handle(PanelWebExchange exchange) {
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
        response.put("status", "success");
        response.put("data", PlaceholderAPI.setPlaceholders(player, body.getString("message")));

        exchange.send(response);
    }
}
