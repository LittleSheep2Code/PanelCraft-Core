package club.smartsheep.panelcraftcore.Server.HTTP.Errors;

public class RouteRegisterError extends Exception {
    public RouteRegisterError(String reason, String path) {
        super("Failed to register a controller(" + path + "), because " + reason);
    }
}
