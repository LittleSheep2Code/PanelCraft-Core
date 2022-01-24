package club.smartsheep.panelcraftcore.Controllers.Console.Vault;

import club.smartsheep.panelcraftcore.Common.ActionRecorder.RecordAction;
import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
import club.smartsheep.panelcraftcore.PanelCraft;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VaultEconomyController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        if (!PanelCraft.HookStatues.getOrDefault("vault.economy", false)) {
            new ErrorLoggers().ModuleDoNotEnable("Vault Economy");
            exchange.getErrorSender().ModuleUnactivatedErrorResponse("Vault Economy");
            return;
        }

        JSONObject body = exchange.getRequestBodyJSONSafety();
        if (body == null) {
            return;
        }

        if (!body.has("player")) {
            exchange.getErrorSender().MissingArgumentsErrorResponse("player");
            return;
        }

        Player player = Bukkit.getPlayerExact(body.getString("player"));
        if (player == null) {
            exchange.getErrorSender().DataErrorResponse("Cannot use your provided player name get player!");
            return;
        }

        if (!body.has("operation")) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", PanelCraft.economy.getBalance(player));
            RecordAction.recordDown(exchange.getAffiliatedData().getString("Authorization-Username"),
                    "Query the player " + body.getString("player") + "'s vault bank(economy) balance",
                    exchange.getClientIP());
            exchange.send(response);
            return;
        }

        if (!body.has("amount")) {
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
                RecordAction.recordDown(exchange.getAffiliatedData().getString("Authorization-Username"),
                        "Let player " + body.getString("player") + "'s vault bank(economy) added " + body.getDouble("amount") + " money",
                        exchange.getClientIP());
                break;
            case "withdraw":
            case "remove":
            case "subtract":
                PanelCraft.economy.withdrawPlayer(player, body.getDouble("amount"));
                response.put("data", "Withdraw player " + body.getString("player") + " balance " + body.getDouble("amount"));
                RecordAction.recordDown(exchange.getAffiliatedData().getString("Authorization-Username"),
                        "Let player " + body.getString("player") + "'s vault bank(economy) reduced " + body.getDouble("amount") + " money",
                        exchange.getClientIP());
                break;
            default:
                exchange.getErrorSender().DataErrorResponse("Unknown operation: " + body.getString("operation"));
                return;
        }

        exchange.send(response);
    }
}
