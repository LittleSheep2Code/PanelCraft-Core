package club.smartsheep.club.panelcraftcore;

import com.sun.net.httpserver.HttpServer;

import java.util.concurrent.Executors;

public class PanelCraftWebserver {
    public static void setup(HttpServer server) {
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }
}
