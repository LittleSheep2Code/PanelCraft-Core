package club.smartsheep.panelcraftcore.Hooks;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import static club.smartsheep.panelcraftcore.PanelCraft.*;
import static org.bukkit.Bukkit.getServer;

public class VaultHook {
    public static void hookVault() {
        if(Bukkit.getPluginManager().getPlugin("Vault") == null) {
            LOGGER.warning("Failed to hook into vault, please check vault is installed. Vault need feature is disabled.");
            HookStatues.put("vault", false);
            return;
        }

        RegisteredServiceProvider<Economy> ecoRegistration = getServer().getServicesManager().getRegistration(Economy.class);
        if (ecoRegistration != null) {
            LOGGER.info("Vault Economy hooked!");
            HookStatues.put("vault.economy", true);
            economy = ecoRegistration.getProvider();
        }

        RegisteredServiceProvider<Permission> permsRegistration = getServer().getServicesManager().getRegistration(Permission.class);
        if(permsRegistration != null) {
            LOGGER.info("Vault Permission hooked!");
            HookStatues.put("vault.permission", true);
            permission = permsRegistration.getProvider();
        }

        RegisteredServiceProvider<Chat> chatRegistration = getServer().getServicesManager().getRegistration(Chat.class);
        if(chatRegistration != null) {
            LOGGER.info("Vault Chat hooked!");
            HookStatues.put("vault.chat", true);
            chat = chatRegistration.getProvider();
        }
    }
}
