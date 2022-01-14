package club.smartsheep.panelcraftcore.Controllers;

import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class DetailController extends PanelWebHandler {
    @Override
    @SneakyThrows
    public void handle(PanelWebExchange exchange) {
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
