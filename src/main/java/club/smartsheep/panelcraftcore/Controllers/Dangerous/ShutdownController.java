package club.smartsheep.panelcraftcore.Controllers.Dangerous;

import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

public class ShutdownController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        Bukkit.getServer().shutdown();
        exchange.send();
    }
}
