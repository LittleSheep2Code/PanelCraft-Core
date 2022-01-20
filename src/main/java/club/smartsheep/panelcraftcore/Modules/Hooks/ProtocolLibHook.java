package club.smartsheep.panelcraftcore.Modules.Hooks;

import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
import club.smartsheep.panelcraftcore.Common.Loggers.LoadingStateLoggers;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;

import static club.smartsheep.panelcraftcore.PanelCraft.*;

public class ProtocolLibHook {
    public static void hookProtocolLib() {
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
            HookStatues.put("ProtocolLib", true);
            new LoadingStateLoggers().ModuleLoaded("ProtocolLib");
        }

        HookStatues.put("ProtocolLib", false);
        new ErrorLoggers().ModuleDoNotEnable("ProtocolLib");
    }
}
