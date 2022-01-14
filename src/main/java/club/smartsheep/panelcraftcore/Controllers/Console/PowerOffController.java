package club.smartsheep.panelcraftcore.Controllers.Console;

import club.smartsheep.panelcraftcore.Common.BodyProcessor;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.ErrorResponse;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.NullResponse;
import club.smartsheep.panelcraftcore.Common.Tokens.CheckPassword;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.IOException;

public class PowerOffController implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            NullResponse.Response(exchange, 405);
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

        Bukkit.getServer().shutdown();
        NullResponse.Response(exchange, 200);
    }
}
