package club.smartsheep.panelcraftcore;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Modules.Hooks.PlaceholderHook;
import club.smartsheep.panelcraftcore.Modules.Hooks.ProtocolLibHook;
import club.smartsheep.panelcraftcore.Modules.Hooks.VaultHook;
import club.smartsheep.panelcraftcore.Modules.Security.RootRandomPasswordGenerator;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpServer;
import com.comphenix.protocol.ProtocolManager;
import lombok.SneakyThrows;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class PanelCraft extends JavaPlugin {

    // Static Values
    public static Logger LOGGER;
    public static Map<String, Boolean> HookStatues = new HashMap<>();

    // Provide hooks for library
    public static FileConfiguration getPanelConfigure() {
        return getPlugin(PanelCraft.class).getConfig();
    }
    public static Logger getPanelLogger() { return getPlugin(PanelCraft.class).getLogger(); }

    // ProtocolLib Hook
    public static ProtocolManager protocolManager;

    // Vault Hook
    public static Economy economy;
    public static Permission permission;
    public static Chat chat;

    // Registration Logic
    @Override
    public void onEnable() {
        LOGGER = getLogger();
        saveDefaultConfig();

        DatabaseConnector.get().connect();

        // Web server Startup
        PanelHttpServer.get().autoAddRoute();
        PanelHttpServer.get().startup();

        // Start automatic update root password
        RootRandomPasswordGenerator.startup();

        // Start hooking
        VaultHook.hookVault();
        PlaceholderHook.hookPlaceholder();
        ProtocolLibHook.hookProtocolLib();

        // Show hook status
        LOGGER.info("Hooking status: " + HookStatues.toString());
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        // Close web server
        PanelHttpServer.get().shutdown();
        // Close database connection
        DatabaseConnector.get().connect().close();
    }
}
