package club.smartsheep.panelcraftcore;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public final class PanelCraft extends JavaPlugin {

    private HttpServer webserviceServer;

    public static Logger LOGGER;

    @Override
    public void onEnable() {
        LOGGER = getLogger();

        saveDefaultConfig();

        if (getConfig().getBoolean("database.disabled")) {
            LOGGER.warning("Now using inner database, inner database use SQLite, performance is not good, if you are production environment");
        }

        DatabaseConnector.get().connect();

        int servicePort = getConfig().getInt("webservice.port");
        LOGGER.info("Deploy web service at: " + servicePort);
        LOGGER.info("You can access it on: http://localhost:" + servicePort);

        try {
            webserviceServer = HttpServer.create(new InetSocketAddress(servicePort), 0);
            PanelCraftWebserver.setup(webserviceServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {}
}
