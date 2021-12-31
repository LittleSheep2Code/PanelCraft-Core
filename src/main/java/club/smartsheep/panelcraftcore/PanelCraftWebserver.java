package club.smartsheep.panelcraftcore;

import club.smartsheep.panelcraftcore.Controllers.Console.ExecuteController;
import club.smartsheep.panelcraftcore.Controllers.Console.Placeholder.PlaceholderProcessorController;
import club.smartsheep.panelcraftcore.Controllers.Console.ReloadController;
import club.smartsheep.panelcraftcore.Controllers.Console.Vault.VaultEconomyController;
import club.smartsheep.panelcraftcore.Controllers.DetailController;
import club.smartsheep.panelcraftcore.Controllers.Console.PowerOffController;
import club.smartsheep.panelcraftcore.Controllers.Status.DatabaseStatusController;
import com.sun.net.httpserver.HttpServer;

import java.util.concurrent.Executors;

public class PanelCraftWebserver {

    /**
     * Set the server router
     * @param server You create HTTPServer instance
     */
    public static void setup(HttpServer server) {
        server.setExecutor(Executors.newCachedThreadPool());

        server.createContext("/", new DetailController());

        server.createContext("/console/poweroff", new PowerOffController());
        server.createContext("/console/reload", new ReloadController());
        server.createContext("/console/execute", new ExecuteController());

        server.createContext("/console/placeholder/process", new PlaceholderProcessorController());

        server.createContext("/console/vault/economy", new VaultEconomyController());

        server.createContext("/statues/database", new DatabaseStatusController());

        server.start();
    }
}
