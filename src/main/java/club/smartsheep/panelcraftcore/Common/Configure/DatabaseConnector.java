package club.smartsheep.panelcraftcore.Common.Configure;

import club.smartsheep.panelcraftcore.PanelCraft;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

import static club.smartsheep.panelcraftcore.PanelCraft.LOGGER;

public class DatabaseConnector {

    public final String CONNECTOR_DRIVER_NAME = "org.sqlite.JDBC";
    public Connection DATABASE_SESSION = null;

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
        String sqlName = PanelCraft.getPlugin(PanelCraft.class).getConfig().getString("database.sqlname");
        File sqlFile = new File(PanelCraft.getPlugin(PanelCraft.class).getDataFolder(), "data/" + sqlName + ".db");
        if (!sqlFile.exists()) {
            try {
                if(!sqlFile.getParentFile().exists()) {
                    new File(PanelCraft.getPlugin(PanelCraft.class).getDataFolder(), "data").mkdir();
                }
                sqlFile.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "File write error: data/" + sqlName + ".db");
            }
        }
        try {
            if (DATABASE_SESSION != null && !DATABASE_SESSION.isClosed()) {
                return DATABASE_SESSION;
            }
            Class.forName(CONNECTOR_DRIVER_NAME);
            DATABASE_SESSION = DriverManager.getConnection("jdbc:sqlite:" + sqlFile);
            return DATABASE_SESSION;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLite exception on initialize", e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }
}
