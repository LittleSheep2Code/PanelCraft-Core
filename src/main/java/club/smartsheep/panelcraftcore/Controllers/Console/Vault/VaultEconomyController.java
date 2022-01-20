package club.smartsheep.panelcraftcore.Controllers.Console.Vault;

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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VaultEconomyController extends PanelWebHandler {
    @Override
    @SneakyThrows
    public void handle(PanelWebExchange exchange) {
        if(!PanelCraft.HookStatues.getOrDefault("vault.economy", false)) {
            new ErrorLoggers().ModuleDoNotEnable("Vault Economy");
            exchange.getErrorSender().ModuleUnactivatedErrorResponse("Vault Economy");
            return;
        }

        JSONObject body = exchange.getRequestBodyJSONSafety();
        if(body == null) {
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

        if(!body.has("operation")) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", PanelCraft.economy.getBalance(player));
            exchange.send(response);
            return;
        }

        if(!body.has("amount")) {
            exchange.getErrorSender().MissingArgumentsErrorResponse("amount");
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
                exchange.getErrorSender().DataErrorResponse("Unknown operation: " + body.getString("operation"));
                return;
        }

        exchange.send(response);
    }
}
