package club.smartsheep.panelcraftcore.Hooks;

import org.bukkit.Bukkit;

import static club.smartsheep.panelcraftcore.PanelCraft.HookStatues;
import static club.smartsheep.panelcraftcore.PanelCraft.LOGGER;

public class PlaceholderHook {
    public static void hookPlaceholder() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            LOGGER.warning("Failed to hook into placeholder api, please check placeholder api is installed. PlaceHolder need feature is disabled.");
            HookStatues.put("placeholderAPI", false);
            return;
        }
        LOGGER.info("PlaceholderAPI hooked!");
        HookStatues.put("placeholderAPI", true);
    }
}
