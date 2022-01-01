package club.smartsheep.panelcraftcore.Controllers.Console.Vault;

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

public class VaultEconomyController implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            NullResponse.Response(exchange, 405);
            return;
        }

        if(!PanelCraft.HookStatues.get("vault.economy")) {
            ErrorResponse.ModuleUnactivatedErrorResponse(exchange, "vault economy");
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

        if(!body.has("player")) {
            ErrorResponse.MissingArgumentsErrorResponse(exchange, "player");
            return;
        }

        Player player = Bukkit.getPlayerExact(body.getString("player"));
        if(player == null) {
            ErrorResponse.DataErrorResponse(exchange, "Cannot use your provided player name get player!");
            return;
        }

        if(!body.has("operation")) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", PanelCraft.economy.getBalance(player));
            JSONResponse.Response(exchange, response, 200);
            return;
        }

        if(!body.has("amount")) {
            ErrorResponse.MissingArgumentsErrorResponse(exchange, "amount");
            return;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        switch (body.getString("operation")) {
            case "add":
            case "plus":
            case "deposit":
                PanelCraft.economy.depositPlayer(player, body.getDouble("amount"));
                response.put("data", "Deposit player " + body.getString("player") + " balance " + body.getDouble("amount"));
                break;
            case "withdraw":
            case "remove":
            case "subtract":
                PanelCraft.economy.withdrawPlayer(player, body.getDouble("amount"));
                response.put("data", "Withdraw player " + body.getString("player") + " balance " + body.getDouble("amount"));
                break;
            default:
                ErrorResponse.DataErrorResponse(exchange, "Unknown operation: " + body.getString("operation"));
                return;
        }

        JSONResponse.Response(exchange, response, 200);
    }
}
