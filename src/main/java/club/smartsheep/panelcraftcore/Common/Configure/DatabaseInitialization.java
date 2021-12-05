package club.smartsheep.panelcraftcore.Common.Configure;

import lombok.SneakyThrows;

public class DatabaseInitialization {
    @SneakyThrows
    public static void setupDatabases() {
        DatabaseConnector.get().DATABASE_SESSION.createStatement().execute(
                "SET NAMES utf8mb4;" +
                "SET FOREIGN_KEY_CHECKS = 0;" +
                "" +
                "DROP TABLE IF EXISTS `activities`;" +
                "CREATE TABLE `activities` (" +
                "  `id` int NOT NULL AUTO_INCREMENT," +
                "  `content` text NOT NULL," +
                "  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;" +
                "" +
                "SET FOREIGN_KEY_CHECKS = 1;"
        );
    }
}
