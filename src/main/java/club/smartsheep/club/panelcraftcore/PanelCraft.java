package club.smartsheep.club.panelcraftcore;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class PanelCraft extends JavaPlugin {

    private HttpServer webserviceServer;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        int servicePort = getConfig().getInt("webservice.port");
        getLogger().info("Deploy web service at: " + servicePort);
        getLogger().info("You can access it on: http://localhost:" + servicePort);

        try {
            webserviceServer = HttpServer.create(new InetSocketAddress(servicePort), 0);
            PanelCraftWebserver.setup(webserviceServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        webserviceServer.stop(0);
    }
}
