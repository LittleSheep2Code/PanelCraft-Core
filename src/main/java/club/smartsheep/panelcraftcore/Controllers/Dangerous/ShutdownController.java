package club.smartsheep.panelcraftcore.Controllers.Dangerous;

import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

public class ShutdownController extends PanelWebHandler {
    @Override
    @SneakyThrows
    public void handle(PanelWebExchange exchange) {
        Bukkit.getServer().shutdown();
        exchange.send();
    }
}
