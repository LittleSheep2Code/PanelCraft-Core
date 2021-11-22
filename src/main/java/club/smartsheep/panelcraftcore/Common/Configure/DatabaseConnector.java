package club.smartsheep.panelcraftcore.Common.Configure;

import club.smartsheep.panelcraftcore.PanelCraft;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;

import static club.smartsheep.panelcraftcore.PanelCraft.LOGGER;

public class DatabaseConnector {

    public String CONNECTOR_DRIVER_NAME;
    public Connection DATABASE_SESSION = null;

    private static DatabaseConnector instance;

    private DatabaseConnector() {
        FileConfiguration configuration = PanelCraft.getPlugin(PanelCraft.class).getConfig();

        if (configuration.getBoolean("database.disabled")) {
            CONNECTOR_DRIVER_NAME = "org.sqlite.JDBC";
        } else {
            CONNECTOR_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
        }
    }

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public boolean connect() {
        if (DATABASE_SESSION == null) return true;

        try {
            Class.forName(CONNECTOR_DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            LOGGER.throwing("DatabaseConnector", "connect", e.getException());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            return false;
        }

        FileConfiguration configuration = PanelCraft.getPlugin(PanelCraft.class).getConfig();

        try {
            if (configuration.getBoolean("database.disabled")) {
                DATABASE_SESSION = DriverManager.getConnection("jdbc:sqlite::resource:database.sqlite");
            } else {
                String url = String.format("//%s:%d/%s?%s",
                        configuration.getString("database.host"),
                        configuration.getInt("database.port"),
                        configuration.getString("database.database"),
                        configuration.getString("database.parameters"));
                LOGGER.info("Connection url: " + url);
                DATABASE_SESSION = DriverManager.getConnection("jdbc:mysql:" + url,
                        configuration.getString("database.username"),
                        configuration.getString("database.password"));
            }


            if (!DATABASE_SESSION.isValid(3)) {
                LOGGER.warning("Connection is un unavailable, please check it!");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                DATABASE_SESSION = null;
                return false;
            }
            if (DATABASE_SESSION == null) {
                LOGGER.warning("Database open task a fatal error has occurred!");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            }
            return true;
        } catch (Exception e) {
            LOGGER.throwing("DatabaseConnector", "connect", e.getCause());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            return false;
        }
    }
}
