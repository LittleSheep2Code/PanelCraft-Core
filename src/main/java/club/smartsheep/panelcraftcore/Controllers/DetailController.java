package club.smartsheep.panelcraftcore.Controllers;

import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class DetailController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "running");

        Map<String, Object> serverStatus = new HashMap<>();
        serverStatus.put("name", Bukkit.getServer().getName());
        serverStatus.put("version", Bukkit.getServer().getVersion());
        serverStatus.put("bukkitVersion", Bukkit.getServer().getBukkitVersion());

        response.put("server", serverStatus);

        exchange.send(response);
    }
}
