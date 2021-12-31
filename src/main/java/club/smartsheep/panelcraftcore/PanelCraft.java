package club.smartsheep.panelcraftcore;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sun.net.httpserver.HttpServer;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class PanelCraft extends JavaPlugin {

    // Static Values
    public static Logger LOGGER;
    public static Map<String, Boolean> HookStatues = new HashMap<>();

    // ProtocolLib Hook
    public static ProtocolManager protocolManager;

    private void setupProtocolLib() {
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
            HookStatues.put("ProtocolLib", true);
        }

        HookStatues.put("ProtocolLib", false);
        LOGGER.warning("Failed to hook into ProtocolLib, please check ProtocolLib is installed. ProtocolLib need feature is disabled.");
    }

    // Vault Hook
    public static Economy economy;
    public static Permission permission;
    public static Chat chat;

    private void setupVaultModule() {
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

    // PAPI Hook
    private void setupPAPIModule() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            LOGGER.warning("Failed to hook into placeholder api, please check placeholder api is installed. PlaceHolder need feature is disabled.");
            HookStatues.put("placeholderAPI", false);
            return;
        }
        LOGGER.info("PlaceholderAPI hooked!");
        HookStatues.put("placeholderAPI", true);
    }

    // Webservice
    private HttpServer webserviceServer;

    private void setupWebserviceModule() {
        int servicePort = getConfig().getInt("webservice.port");
        LOGGER.info("Deploy web service at: " + servicePort);
        LOGGER.info("You can access it on: http://localhost:" + servicePort);

        try {
            webserviceServer = HttpServer.create(new InetSocketAddress(servicePort), 0);
            PanelCraftWebserver.setup(webserviceServer);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warning("");
            LOGGER.warning("FAILED SETUP PANELCRAFT WEBSERVICE!!!!!!");
            LOGGER.warning("     PanelCraft are shutting down!      ");
            LOGGER.warning("      Please check the configure        ");
            LOGGER.warning(" Or remove panelcraft and make a issues ");
            LOGGER.warning("FAILED SETUP PANELCRAFT WEBSERVICE!!!!!!");
            LOGGER.warning("");
            getPluginLoader().disablePlugin(getPlugin(PanelCraft.class));
        }
    }

    // Registration Logic
    @Override
    public void onEnable() {
        LOGGER = getLogger();
        saveDefaultConfig();

        DatabaseConnector.get().connect();

        // Webservice setup
        setupWebserviceModule();

        // Start hooking
        // Vault
        setupVaultModule();
        // PAPI
        setupPAPIModule();
        // ProtocolLib
        setupProtocolLib();

        // Show hook status
        LOGGER.info("Hooking status: " + HookStatues.toString());
    }

    @Override
    public void onDisable() {
        webserviceServer.stop(1);
    }
}
