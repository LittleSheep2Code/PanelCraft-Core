package club.smartsheep.panelcraftcore.Controllers.Security;

import club.smartsheep.panelcraftcore.Common.ActionRecorder.RecordAction;
import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Common.Tokens.CommonToken;
import club.smartsheep.panelcraftcore.Common.Tokens.SummonAuthorizationToken;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpExchange;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpHandler;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.JSONResponse;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.NullResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationController extends PanelHttpHandler {
    @Override
    @SneakyThrows
    public void handle(PanelHttpExchange exchange) {
        JSONObject body = exchange.getRequestBodyJSONSafety();
        if(!body.has("username") || !body.has("password")) {
            exchange.getErrorSender().MissingArgumentsErrorResponse("want get authorization code's user password(md5 encrypted) or username");
            return;
        }

        int accountFound;
        String script = String.format("SELECT COUNT(*) FROM system__accounts WHERE username='%s' AND password='%s'", body.getString("username"), body.getString("password"));
        try {
            Statement statement = DatabaseConnector.get().connect().createStatement();
            ResultSet set = statement.executeQuery(script);
            accountFound = set.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
            exchange.getErrorSender().SQLErrorResponse(e.getSQLState(), "Login error, script: " + script);
            return;
        }


        if(accountFound == 0) {
            exchange.getErrorSender().DataErrorResponse("cannot found account with your username and password");
            return;
        }

        String token = SummonAuthorizationToken.summonToken(604800, body.getString("username"), body.getString("password"));

        RecordAction.recordDown(body.getString("username"), "Login", exchange.getClientIP());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", token);
        exchange.send(response);
    }
}
