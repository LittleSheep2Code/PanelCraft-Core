package club.smartsheep.panelcraftcore.Hooks;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;

import static club.smartsheep.panelcraftcore.PanelCraft.*;

public class ProtocolLibHook {
    public static void hookProtocolLib() {
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
            HookStatues.put("ProtocolLib", true);
        }

        HookStatues.put("ProtocolLib", false);
        LOGGER.warning("Failed to hook into ProtocolLib, please check ProtocolLib is installed. ProtocolLib need feature is disabled.");
    }
}
