package club.smartsheep.panelcraftcore.Server.HTTP;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public class PanelWebErrorSender {
    private PanelWebExchange exchange;

    public PanelWebErrorSender(PanelWebExchange exchange) {
        this.exchange = exchange;
    }

    @SneakyThrows
    public void CustomErrorResponse(String errorName, String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", errorName);
        response.put("message", errorMessage);
        this.exchange.setStatusCode(400);
        this.exchange.send(response);
    }

    @SneakyThrows
    public void CustomErrorResponse(String errorName, String errorMessage, int statusCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", errorName);
        response.put("message", errorMessage);
        this.exchange.setStatusCode(statusCode);
        this.exchange.send(response);
    }

    public void BodyProcessErrorResponse(String reason) {
        this.CustomErrorResponse("FormatBodyFailed", "Please check your body, " + reason + ".");
    }

    public void InsufficientPermissionsErrorResponse() {
        this.CustomErrorResponse("InsufficientPermissions", "Please check your password or reset it, the root password is wrong.");
    }

    public void DataErrorResponse(String message) {
        this.CustomErrorResponse("DataError", message);
    }

    public void ModuleUnactivatedErrorResponse(String unactivatedModuleName) {
        this.CustomErrorResponse("ModuleUnactivatedError", unactivatedModuleName + " module isn't activate, please check it api is hooked(installed) and reload try again!", 500);
    }

    public void SQLErrorResponse(String state, String message) {
        this.CustomErrorResponse("SQLExecuteFailedError", "Execute script (state: " + state + ") failed with message: " + message, 500);
    }

    public void MethodNotAllowResponse() {
        this.CustomErrorResponse("MethodNotAllowError", "This API do not allow you used method! Please query the developer document!", 405);
    }

    /**
     * Return missing parameter error response
     * @param exchange The handler can handle parameter
     * @param missingThings What parameter missed, split by space
     */
    public void MissingArgumentsErrorResponse(String missingThings) {
        this.CustomErrorResponse("MissingArguments", "Missing " + missingThings + ".");
    }
}
