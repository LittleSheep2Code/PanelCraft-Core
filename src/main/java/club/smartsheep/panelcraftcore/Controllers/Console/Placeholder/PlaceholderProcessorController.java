package club.smartsheep.panelcraftcore.Controllers.Console.Placeholder;

import club.smartsheep.panelcraftcore.Common.BodyProcessor;
import club.smartsheep.panelcraftcore.Common.Responsor.ErrorResponse;
import club.smartsheep.panelcraftcore.Common.Responsor.JSONResponse;
import club.smartsheep.panelcraftcore.Common.Responsor.NullResponse;
import club.smartsheep.panelcraftcore.Common.Tokens.CheckPassword;
import club.smartsheep.panelcraftcore.PanelCraft;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlaceholderProcessorController implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            NullResponse.Response(exchange, 405);
            return;
        }

        if(!PanelCraft.HookStatues.get("placeholderAPI")) {
            ErrorResponse.ModuleUnactivatedErrorResponse(exchange, "placeholderAPI");
            return;
        }

        JSONObject body = BodyProcessor.getJSONObject(exchange);
        if(body == null) {
            ErrorResponse.BodyProcessErrorResponse(exchange, "JSON");
            return;
        }

        if(!body.has("password") || !CheckPassword.checkRootPassword(body.getString("password"))) {
            if(body.has("password")) {
                ErrorResponse.InsufficientPermissionsErrorResponse(exchange);
            }
            else {
                ErrorResponse.MissingArgumentsErrorResponse(exchange, "root password");
            }
            return;
        }

        if(!body.has("message")) {
            ErrorResponse.MissingArgumentsErrorResponse(exchange, "message");
            return;
        }

        if(!body.has("player")) {
            ErrorResponse.MissingArgumentsErrorResponse(exchange, "player");
            return;
        }

        Player player = Bukkit.getPlayerExact(body.getString("player"));
        if(player == null) {
            ErrorResponse.DataErrorResponse(exchange, "Cannot use your provided player name get player!");
            return;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", PlaceholderAPI.setPlaceholders(player, body.getString("message")));

        JSONResponse.Response(exchange, response, 200);
    }
}
