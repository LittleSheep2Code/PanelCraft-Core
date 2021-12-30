package club.smartsheep.panelcraftcore.Common;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import static club.smartsheep.panelcraftcore.PanelCraft.LOGGER;

public class BodyProcessor {
    public static JSONObject getJSONObject(HttpExchange exchange) {
        try {
            return new JSONObject(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.warning("Failed to process a body to JSONObject, sender is " + exchange.getRemoteAddress().getHostString());
            return null;
        }
    }
}
