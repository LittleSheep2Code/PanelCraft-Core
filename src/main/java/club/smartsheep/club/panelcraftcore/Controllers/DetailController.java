package club.smartsheep.club.panelcraftcore.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class DetailController implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            return;
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");;
        exchange.sendResponseHeaders(200, 0);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "running");

        Map<String, Object> serverStatus = new HashMap<>();
        serverStatus.put("name", Bukkit.getServer().getName());
        serverStatus.put("version", Bukkit.getServer().getVersion());
        serverStatus.put("bukkitVersion", Bukkit.getServer().getBukkitVersion());

        response.put("server", serverStatus);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(new ObjectMapper().writeValueAsString(response).getBytes());
        responseBody.close();
    }
}
