package club.smartsheep.panelcraftcore.Common.Loggers;

import club.smartsheep.panelcraftcore.PanelCraft;

import java.util.logging.Logger;

public class ErrorLoggers {
    private Logger PluginLogger = PanelCraft.getPanelLogger();

    public void FailedRecordDown(String script) {
        PluginLogger.warning("");
        PluginLogger.warning("§c[!] §e§l[ALERT] §r§eFailed to record down! Script: " + script);
        PluginLogger.warning("");
    }

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

    public void JSONProcessError(String when, String what) {
        PluginLogger.warning("");
        PluginLogger.warning("§c[!] §4§l                 ERROR!");
        PluginLogger.warning("");
        PluginLogger.warning("§c[!] §4§l[FATAL] §4§eCouldn't covert " + what + " to usable json object at " + when);
        PluginLogger.warning("");
    }
}
