package club.smartsheep.panelcraftcore.Modules.Hooks;

import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
import club.smartsheep.panelcraftcore.Common.Loggers.LoadingStateLoggers;
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
            new ErrorLoggers().ModuleDoNotEnable("Vault All");
            HookStatues.put("vault", false);
            return;
        }

        RegisteredServiceProvider<Economy> ecoRegistration = getServer().getServicesManager().getRegistration(Economy.class);
        if (ecoRegistration != null) {
            new LoadingStateLoggers().ModuleLoaded("Vault Economy");
            HookStatues.put("vault.economy", true);
            economy = ecoRegistration.getProvider();
        }

        RegisteredServiceProvider<Permission> permsRegistration = getServer().getServicesManager().getRegistration(Permission.class);
        if(permsRegistration != null) {
            new LoadingStateLoggers().ModuleLoaded("Vault Permission");
            HookStatues.put("vault.permission", true);
            permission = permsRegistration.getProvider();
        }

        RegisteredServiceProvider<Chat> chatRegistration = getServer().getServicesManager().getRegistration(Chat.class);
        if(chatRegistration != null) {
            new LoadingStateLoggers().ModuleLoaded("Vault Chat");
            HookStatues.put("vault.chat", true);
            chat = chatRegistration.getProvider();
        }
    }
}
