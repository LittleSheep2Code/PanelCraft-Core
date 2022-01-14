package club.smartsheep.panelcraftcore.Common.Configure;

import lombok.SneakyThrows;

import java.sql.SQLException;

public class DatabaseInitialization {
    public static void setupDatabases() throws SQLException {
        DatabaseConnector.get().DATABASE_SESSION.createStatement().execute("create table if not exists activities\n" +
                "(\n" +
                "    id          int auto_increment\n" +
                "        primary key,\n" +
                "    context     text     not null,\n" +
                "    submitter   text     not null,\n" +
                "    create_time datetime not null,\n" +
                "    constraint activities_id_uindex\n" +
                "        unique (id)\n" +
                ");");
    }
}
