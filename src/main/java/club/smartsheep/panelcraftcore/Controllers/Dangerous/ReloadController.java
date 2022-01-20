package club.smartsheep.panelcraftcore.Controllers.Dangerous;

import club.smartsheep.panelcraftcore.PanelCraft;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

public class ReloadController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        Bukkit.getScheduler().runTask(PanelCraft.getPlugin(PanelCraft.class), () -> Bukkit.getServer().reload());
        exchange.send();
    }
}
