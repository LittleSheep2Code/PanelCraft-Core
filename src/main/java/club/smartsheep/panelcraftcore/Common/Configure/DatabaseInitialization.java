package club.smartsheep.panelcraftcore.Common.Configure;

import club.smartsheep.panelcraftcore.PanelCraft;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitialization {
    public static void setupDatabases() throws SQLException {
        if(DatabaseConnector.get().connect() == null || DatabaseConnector.get().connect().isClosed()) {
            return;
        }

        Statement statement = DatabaseConnector.get().connect().createStatement();

        // System Tables
        // Accounts table
        statement.execute("CREATE TABLE IF NOT EXISTS 'system__accounts' (\n" +
                "  'id' integer PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  'username' text NOT NULL,\n" +
                "  'password' text NOT NULL,\n" +
                "  'permissions' json DEFAULT('{}'),\n" +
                "  'role' text DEFAULT('PLAYER'),\n" +
                "  'mail' text,\n" +
                "  'description' text DEFAULT('Just a simple player.')\n" +
                ");");

        statement.close();
        statement = DatabaseConnector.get().connect().createStatement();

        // Admin Tables
        // Event records table
        statement.execute("CREATE TABLE IF NOT EXISTS 'admin__event_records' (\n" +
                "  'id' integer PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  'executor' text,\n" +
                "  'content' text,\n" +
                "  'execute_time' text\n" +
                ");");

        statement.close();
    }
}
