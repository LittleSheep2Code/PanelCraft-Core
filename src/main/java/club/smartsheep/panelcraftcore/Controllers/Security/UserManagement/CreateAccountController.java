package club.smartsheep.panelcraftcore.Controllers.Security.UserManagement;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Common.Configure.DatabaseInitialization;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import lombok.SneakyThrows;
import org.json.JSONObject;

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

        // Build script
        String insert_columns = "'username', 'password'";
        String insert_values = String.format("'%s', '%s'", body.getString("username"), body.getString("password"));

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

        String script = String.format("INSERT INTO  'system__accounts' (%s) VALUES (%s);", insert_columns, insert_values);

        // Execute script
        try {
            Statement statement = DatabaseConnector.get().connect().createStatement();
            statement.execute(script);
        } catch (Exception e) {
            e.printStackTrace();
            exchange.getErrorSender().SQLErrorResponse(null, "Error when creating account(script: " + script + ")");
            return;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        exchange.send(response);
    }
}
