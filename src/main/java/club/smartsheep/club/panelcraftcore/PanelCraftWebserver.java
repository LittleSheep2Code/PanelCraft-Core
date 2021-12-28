package club.smartsheep.club.panelcraftcore;

import club.smartsheep.club.panelcraftcore.Controllers.DetailController;
import com.sun.net.httpserver.HttpServer;

import java.util.concurrent.Executors;

public class PanelCraftWebserver {
    public static void setup(HttpServer server) {
        server.setExecutor(Executors.newCachedThreadPool());

        server.createContext("/", new DetailController());

        server.start();
    }
}
