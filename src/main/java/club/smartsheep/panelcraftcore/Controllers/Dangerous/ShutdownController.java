package club.smartsheep.panelcraftcore.Controllers.Dangerous;

import club.smartsheep.panelcraftcore.Common.ActionRecorder.RecordAction;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

public class ShutdownController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        RecordAction.recordDown("Root", "Shutdown the server", exchange.getClientIP());
        Bukkit.getServer().shutdown();
        exchange.send();
    }
}
