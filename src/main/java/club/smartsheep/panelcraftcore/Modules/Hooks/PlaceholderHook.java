package club.smartsheep.panelcraftcore.Modules.Hooks;

import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
import club.smartsheep.panelcraftcore.Common.Loggers.LoadingStateLoggers;
import org.bukkit.Bukkit;

import static club.smartsheep.panelcraftcore.PanelCraft.HookStatues;

public class PlaceholderHook {
    public static void hookPlaceholder() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            new ErrorLoggers().ModuleDoNotEnable("PlaceholderAPI");
            HookStatues.put("placeholderAPI", false);
            return;
        }
        new LoadingStateLoggers().ModuleLoaded("PlaceholderAPI");
        HookStatues.put("placeholderAPI", true);
    }
}
