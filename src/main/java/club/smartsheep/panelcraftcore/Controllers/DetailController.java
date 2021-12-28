package club.smartsheep.panelcraftcore.Controllers;

import club.smartsheep.panelcraftcore.Common.Responsor.JSONResponse;
import club.smartsheep.panelcraftcore.Common.Responsor.NullResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DetailController implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            NullResponse.Response(exchange, 405);
            return;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "running");

        Map<String, Object> serverStatus = new HashMap<>();
        serverStatus.put("name", Bukkit.getServer().getName());
        serverStatus.put("version", Bukkit.getServer().getVersion());
        serverStatus.put("bukkitVersion", Bukkit.getServer().getBukkitVersion());

        response.put("server", serverStatus);

        JSONResponse.Response(exchange, response, 200);
    }
}
