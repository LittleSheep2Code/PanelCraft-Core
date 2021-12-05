package club.smartsheep.panelcraftcore;

import club.smartsheep.panelcraftcore.Controllers.DetailController;
import club.smartsheep.panelcraftcore.Controllers.PowerOffController;
import club.smartsheep.panelcraftcore.Controllers.Status.DatabaseStatusController;
import com.sun.net.httpserver.HttpServer;

import java.util.concurrent.Executors;

public class PanelCraftWebserver {
    public static void setup(HttpServer server) {
        server.setExecutor(Executors.newCachedThreadPool());

        server.createContext("/", new DetailController());

        server.createContext("/console/poweroff", new PowerOffController());

        server.createContext("/statues/database", new DatabaseStatusController());

        server.start();
    }
}
