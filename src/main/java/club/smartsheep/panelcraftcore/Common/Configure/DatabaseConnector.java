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
        String sqlname = PanelCraft.getPlugin(PanelCraft.class).getConfig().getString("database.sqlname");
        File sqlfile = new File(PanelCraft.getPlugin(PanelCraft.class).getDataFolder(), sqlname + ".db");
        if (!sqlfile.exists()) {
            try {
                sqlfile.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "File write error: " + sqlname + ".db");
            }
        }
        try {
            if (DATABASE_SESSION != null && !DATABASE_SESSION.isClosed()) {
                return DATABASE_SESSION;
            }
            Class.forName(CONNECTOR_DRIVER_NAME);
            DATABASE_SESSION = DriverManager.getConnection("jdbc:sqlite:" + sqlfile);
            return DATABASE_SESSION;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLite exception on initialize", e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }
}
