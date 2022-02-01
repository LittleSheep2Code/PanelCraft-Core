package club.smartsheep.panelcraftcore.Controllers.Security.UserManagement;

import club.smartsheep.panelcraftcore.Common.ActionRecorder.RecordAction;
import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        JSONObject body = exchange.getRequestBodyJSONSafety();
        if(!body.has("username") || !body.has("password")) {
            exchange.getErrorSender().MissingArgumentsErrorResponse("want register user's password or username");
            return;
        }

        // Check registered
        String script = String.format("SELECT COUNT(*) FROM system__accounts WHERE username='%s'", body.getString("username"));
        try {
            Statement statement = DatabaseConnector.get().connect().createStatement();
            ResultSet set = statement.executeQuery(script);
            if(set.getInt("COUNT(*)") != 0) {
                exchange.getErrorSender().DataErrorResponse("This username is repeated, please change it");
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.getErrorSender().SQLErrorResponse(null, "Error when creating account(script: " + script + ")");
            return;
        }

        // Build script
        String insert_columns = "'username', 'password'";
        String insert_values = String.format("'%s', '%s'", body.getString("username"), DigestUtils.md5Hex(body.getString("password")));

        if(body.has("permissions")) {
            insert_columns += ", 'permissions'";
            insert_values += String.format(", '%s'", body.get("permissions"));
        }

        if(body.has("role")) {
            insert_columns += ", 'role'";
            insert_values += String.format(", '%s'", body.get("role"));
        }

        if(body.has("mail")) {
            insert_columns += ", 'mail'";
            insert_values += String.format(", '%s'", body.get("mail"));
        }

        if(body.has("description")) {
            insert_columns += ", 'description'";
            insert_values += String.format(", '%s'", body.get("description"));
        }

        script = String.format("INSERT INTO  'system__accounts' (%s) VALUES (%s);", insert_columns, insert_values);

        // Execute script
        try {
            Statement statement = DatabaseConnector.get().connect().createStatement();
            statement.execute(script);
        } catch (Exception e) {
            e.printStackTrace();
            exchange.getErrorSender().SQLErrorResponse(null, "Error when creating account(script: " + script + ")");
            return;
        }

        RecordAction.recordDown("Root", "Create user: " + body.getString("username"), exchange.getClientIP());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        exchange.send(response);
    }
}
