package club.smartsheep.panelcraftcore.Controllers.Dangerous;

import club.smartsheep.panelcraftcore.PanelCraft;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelWebHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

public class ReloadController extends PanelWebHandler {
    @Override
    @SneakyThrows
    public void handle(PanelWebExchange exchange) {
        Bukkit.getScheduler().runTask(PanelCraft.getPlugin(PanelCraft.class), () -> Bukkit.getServer().reload());
        exchange.send();
    }
}
