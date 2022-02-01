package club.smartsheep.panelcraftcore.Controllers.Dangerous;

import club.smartsheep.panelcraftcore.Common.ActionRecorder.RecordAction;
import club.smartsheep.panelcraftcore.PanelCraft;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

public class ReloadController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        RecordAction.recordDown(exchange.Authorization_Username, "Reloaded the server", exchange.getClientIP());
        Bukkit.getScheduler().runTask(PanelCraft.getPlugin(PanelCraft.class), () -> Bukkit.getServer().reload());
        exchange.send();
    }
}
