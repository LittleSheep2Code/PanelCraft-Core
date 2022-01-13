package club.smartsheep.panelcraftcore;

import club.smartsheep.panelcraftcore.Controllers.Console.Dangerous.CreateDatabaseController;
import club.smartsheep.panelcraftcore.Controllers.Console.ExecuteController;
import club.smartsheep.panelcraftcore.Controllers.Console.Placeholder.PlaceholderProcessorController;
import club.smartsheep.panelcraftcore.Controllers.Console.PowerOffController;
import club.smartsheep.panelcraftcore.Controllers.Console.ReloadController;
import club.smartsheep.panelcraftcore.Controllers.Console.Vault.VaultEconomyController;
import club.smartsheep.panelcraftcore.Controllers.DetailController;
import club.smartsheep.panelcraftcore.Controllers.Status.DatabaseStatusController;
import club.smartsheep.panelcraftcore.Controllers.Status.PermissionStatusController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static club.smartsheep.panelcraftcore.PanelCraft.*;

public class PanelCraftWebserver {

    public static List<String> RegisteredNamespace = new ArrayList<>();

    /**
     * Set the server router
     * @param server You create HTTPServer instance
     */
    private static void routeSetup(HttpServer server) {
        server.setExecutor(Executors.newCachedThreadPool());

        server.createContext("/", new DetailController());

        RegisteredNamespace.add("/console");
        server.createContext("/console/power-off", new PowerOffController());
        server.createContext("/console/reload", new ReloadController());
        server.createContext("/console/execute", new ExecuteController());
        // Dangerous
        server.createContext("/console/danger-zone/setup-database", new CreateDatabaseController());
        // Hooks
        server.createContext("/console/placeholder/process", new PlaceholderProcessorController());
        server.createContext("/console/vault/economy", new VaultEconomyController());

        RegisteredNamespace.add("/status");
        server.createContext("/status/database", new DatabaseStatusController());
        server.createContext("/status/permission", new PermissionStatusController());

        server.start();
    }

    public static void setup() {
        int servicePort = PanelCraft.getPlugin(PanelCraft.class).getConfig().getInt("webservice.port");
        LOGGER.info("Deploy web service at: " + servicePort);
        LOGGER.info("You can access it on: http://localhost:" + servicePort);

        try {
            webserviceServer = HttpServer.create(new InetSocketAddress(servicePort), 0);
            PanelCraftWebserver.routeSetup(webserviceServer);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warning("");
            LOGGER.warning("FAILED SETUP PANELCRAFT WEBSERVICE!!!!!!");
            LOGGER.warning("     PanelCraft are shutting down!      ");
            LOGGER.warning("      Please check the configure        ");
            LOGGER.warning(" Or remove panelcraft and make a issues ");
            LOGGER.warning("FAILED SETUP PANELCRAFT WEBSERVICE!!!!!!");
            LOGGER.warning("");
            PanelCraft.getPlugin(PanelCraft.class).getPluginLoader().disablePlugin(PanelCraft.getPlugin(PanelCraft.class));
        }
    }
}
