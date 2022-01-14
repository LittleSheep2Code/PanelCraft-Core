package club.smartsheep.panelcraftcore.Common.Loggers;

import club.smartsheep.panelcraftcore.PanelCraft;

import java.util.logging.Logger;

public class LoadingStateLoggers {
    private Logger PluginLogger = PanelCraft.getPanelLogger();

    public void ModuleLoaded(String module) {
        PluginLogger.warning("§a[√] §6§l[MODULES] §lModule §r§6" + module + " §r§e§lhooked!");
    }

    public void WebServerStarted() {
        PluginLogger.warning("§a[√] §6§l[HTTP] §lServer has been started!");
    }
}
