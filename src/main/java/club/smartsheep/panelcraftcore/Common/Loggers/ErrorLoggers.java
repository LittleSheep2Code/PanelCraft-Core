package club.smartsheep.panelcraftcore.Common.Loggers;

import club.smartsheep.panelcraftcore.PanelCraft;

import java.util.logging.Logger;

public class ErrorLoggers {
    private Logger PluginLogger = PanelCraft.getPanelLogger();

    public void ModuleDoNotEnable(String module) {
        PluginLogger.warning("");
        PluginLogger.warning("§c[!] §e§l[ALERT] §r§eModule §6" + module + " §ehasn't hooked, require that feature has been disabled!");
        PluginLogger.warning("");
    }

    public void WebStartupErrorOccurred(String error) {
        PluginLogger.warning("");
        PluginLogger.warning("§c[!] §4§l                 ERROR!");
        PluginLogger.warning("");
        PluginLogger.warning("§c[!] §4§l[FATAL] §4§eWeb server cannot start up, because: §e" + error);
        PluginLogger.warning("");
    }
}
