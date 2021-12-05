package club.smartsheep.panelcraftcore.Controllers;

import club.smartsheep.panelcraftcore.Common.BodyProcessor;
import club.smartsheep.panelcraftcore.Common.Responsor.JSONResponse;
import club.smartsheep.panelcraftcore.Common.Responsor.NullResponse;
import club.smartsheep.panelcraftcore.PanelCraft;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PowerOffController implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            NullResponse.Response(exchange, 405);
            return;
        }

        JSONObject body = BodyProcessor.getJSONObject(exchange);
        if(!body.has("password") || !body.getString("password").equals(PanelCraft.getPlugin(PanelCraft.class).getConfig().getString("security.root"))) {
            Map<String, Object> response = new HashMap<>();

            if(body.has("password")) {
                response.put("error", "InsufficientPermissions");
                response.put("message", "Please check your password or reset it, the root password is wrong.");
            }
            else {
                response.put("error", "MissingArguments");
                response.put("message", "Need root password to execute power off operation.");
            }

            JSONResponse.Response(exchange, response, 400);
        }

        Bukkit.getServer().shutdown();
        NullResponse.Response(exchange, 201);
    }
}
