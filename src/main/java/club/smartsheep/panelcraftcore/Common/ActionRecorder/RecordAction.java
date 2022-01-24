package club.smartsheep.panelcraftcore.Common.ActionRecorder;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;

import java.sql.SQLException;
import java.sql.Statement;

public class RecordAction {
    public static void recordDown(String username, String action, String clientIp) {
        String script = String.format("INSERT INTO  'admin__event_records' ('executor', 'content', 'executor_ip') VALUES ('%s', '%s', '%s');", username, action, clientIp);
        try {
            Statement statement = DatabaseConnector.get().connect().createStatement();
            statement.execute(script);
            statement.close();
        } catch (SQLException e) {
            new ErrorLoggers().FailedRecordDown(script);
        }
    }
}
