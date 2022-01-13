package club.smartsheep.panelcraftcore;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Hooks.PlaceholderHook;
import club.smartsheep.panelcraftcore.Hooks.ProtocolLibHook;
import club.smartsheep.panelcraftcore.Hooks.VaultHook;
import com.comphenix.protocol.ProtocolManager;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
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

    public static HttpServer webserviceServer;

    // Static Values
    public static Logger LOGGER;
    public static Map<String, Boolean> HookStatues = new HashMap<>();

    // Provide hooks for library
    public static FileConfiguration getPanelConfigure() {
        return getPlugin(PanelCraft.class).getConfig();
    }

    public static boolean registerHttpHandler(HttpHandler handler, String namespace, String path) {
        if(!namespace.startsWith("/")) {
            return false;
        } else if(!path.startsWith("/")) {
            return false;
        } else if(PanelCraftWebserver.RegisteredNamespace.contains(namespace)) {
            return false;
        } else {
            webserviceServer.createContext(namespace + path, handler);
            return true;
        }
    }

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

        // Webservice setup
        PanelCraftWebserver.setup();

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
        webserviceServer.stop(1);
        // Close database connection
        DatabaseConnector.get().DATABASE_SESSION.close();
    }
}
