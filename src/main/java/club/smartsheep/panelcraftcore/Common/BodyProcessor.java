package club.smartsheep.panelcraftcore.Common;

import club.smartsheep.panelcraftcore.Common.Responsor.JSONResponse;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static club.smartsheep.panelcraftcore.PanelCraft.LOGGER;

public class BodyProcessor {
    public static JSONObject getJSONObject(HttpExchange exchange) {
        try {
            return new JSONObject(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.warning("Failed to process a body to JSONObject, sender is " + exchange.getRemoteAddress().getHostString());
        }
    }
}
