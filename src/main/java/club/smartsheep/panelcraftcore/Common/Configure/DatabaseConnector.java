package club.smartsheep.panelcraftcore.Common.Configure;

import club.smartsheep.panelcraftcore.PanelCraft;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    public Connection connection;

    private static DatabaseConnector instance;

    private DatabaseConnector() {
    }

    public static DatabaseConnector get() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    @SneakyThrows
    public Connection connect() {
        JavaPlugin plugin = PanelCraft.getPlugin(PanelCraft.class);
        File dataFolder = new File(plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        File sqliteFile = new File(dataFolder, plugin.getConfig().getString("database.name") + ".db");
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
