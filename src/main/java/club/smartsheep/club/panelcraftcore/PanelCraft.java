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

        try {
            webserviceServer = HttpServer.create(new InetSocketAddress(8080), 0);
            PanelCraftWebserver.setup(webserviceServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
