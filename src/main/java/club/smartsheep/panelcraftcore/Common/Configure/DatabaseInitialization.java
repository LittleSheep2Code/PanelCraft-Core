package club.smartsheep.panelcraftcore.Common.Configure;

import club.smartsheep.panelcraftcore.PanelCraft;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInitialization {
    public static void setupDatabases() throws SQLException {
        StringBuilder builder = new StringBuilder();

        // Admin Tables
        // Event records table
        builder.append("CREATE TABLE IF NOT EXISTS \"admin__event_records\" (\n" +
                "  \"id\" integer PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"executor\" text,\n" +
                "  \"content\" text,\n" +
                "  \"execute_time\" text\n" +
                ");");

        if(DatabaseConnector.get().connect() == null || DatabaseConnector.get().connect().isClosed()) {
            return;
        }
        PreparedStatement statement = DatabaseConnector.get().connect().prepareStatement(builder.toString());

        statement.execute();
        statement.close();
    }
}
