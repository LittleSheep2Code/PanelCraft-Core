package club.smartsheep.panelcraftcore.Server.HTTP;

import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
import club.smartsheep.panelcraftcore.Common.Loggers.LoadingStateLoggers;
import club.smartsheep.panelcraftcore.Common.Tokens.CheckPassword;
import club.smartsheep.panelcraftcore.Controllers.Console.ExecuteController;
import club.smartsheep.panelcraftcore.Controllers.Console.Placeholder.PlaceholderProcessorController;
import club.smartsheep.panelcraftcore.Controllers.Console.Vault.VaultEconomyController;
import club.smartsheep.panelcraftcore.Controllers.Dangerous.Initialize.CreateDatabaseController;
import club.smartsheep.panelcraftcore.Controllers.Dangerous.ReloadController;
import club.smartsheep.panelcraftcore.Controllers.Dangerous.ShutdownController;
import club.smartsheep.panelcraftcore.Controllers.DetailController;
import club.smartsheep.panelcraftcore.Controllers.Security.UserManagement.CreateAccountController;
import club.smartsheep.panelcraftcore.PanelCraft;
import club.smartsheep.panelcraftcore.Server.HTTP.Errors.RouteRegisterError;
import club.smartsheep.panelcraftcore.Server.HTTP.Responsor.ErrorResponse;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class PanelHttpServer {
    public enum RequestMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE"),
        PATCH("PATCH"),
        TRACE("TRACE"),
        OPTIONS("OPTIONS");

        public final String name;

        RequestMethod(String name) {
            this.name = name;
        }
    }

    public enum SecurityLevel {
        ANYONE,
        PLAYER,
        ADMIN,
        ROOT
    }

    private static PanelHttpServer instance = null;

    private HttpServer server;

    private PanelHttpServer() {
        boolean started = true;
        try {
            server = HttpServer.create(new InetSocketAddress(PanelCraft.getPanelConfigure().getInt("webserver.port")), 0);
        } catch (IOException e) {
            started = false;
            new ErrorLoggers().WebStartupErrorOccurred(e.getMessage());
            PanelCraft.getPlugin(PanelCraft.class).getPluginLoader().disablePlugin(PanelCraft.getPlugin(PanelCraft.class));
        }
        if (started) {
            new LoadingStateLoggers().WebServerStarted();
        }
    }

    public static PanelHttpServer get() {
        if (instance == null) {
            instance = new PanelHttpServer();
        }
        return instance;
    }

    public boolean addRoute(RequestMethod method, PanelHttpHandler handler, String path) throws RouteRegisterError {
        if (!path.equals("/") && (!path.startsWith("/") || path.endsWith("/"))) {
            throw new RouteRegisterError("Path is need start by a slash, and cannot end by slash", path);
        }
        server.createContext(path, exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase(method.name)) {
                ErrorResponse.MethodNotAllowResponse(exchange);
                return;
            }

            handler.handle(new PanelHttpExchange(exchange));
        });
        return true;
    }

    public boolean addRoute(RequestMethod method, SecurityLevel security, PanelHttpHandler handler, String path) throws RouteRegisterError {
        if (!path.equals("/") && (!path.startsWith("/") || path.endsWith("/"))) {
            throw new RouteRegisterError("Path is need start by a slash, and cannot end by slash", path);
        }
        server.createContext(path, exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase(method.name)) {
                ErrorResponse.MethodNotAllowResponse(exchange);
                return;
            }
            PanelHttpExchange wExchange = new PanelHttpExchange(exchange);
            switch (security) {
                case ROOT:
                    List<String> authorizationCode = exchange.getRequestHeaders().get("Authorization-Code");
                    if (authorizationCode == null || authorizationCode.size() <= 0) {
                        wExchange.getErrorSender().MissingHeaderErrorResponse("Authorization-Code");
                        return;
                    }
                    if (!CheckPassword.checkRootPassword(authorizationCode.get(0))) {
                        wExchange.getErrorSender().InsufficientPermissionsErrorResponse();
                        return;
                    }
                    break;
                default:
                    break;
            }

            handler.handle(wExchange);
        });
        return true;
    }

    @SneakyThrows
    public void autoAddRoute() {
        this.addRoute(RequestMethod.GET, new DetailController(), "/");
        this.addRoute(RequestMethod.PUT, SecurityLevel.ROOT, new ExecuteController(), "/console");
        this.addRoute(RequestMethod.PUT, SecurityLevel.ROOT, new VaultEconomyController(), "/console/vault/economy");
        this.addRoute(RequestMethod.PUT, SecurityLevel.ROOT, new PlaceholderProcessorController(), "/console/placeholder");
        this.addRoute(RequestMethod.PATCH, SecurityLevel.ROOT, new ReloadController(), "/danger-zone/reload");
        this.addRoute(RequestMethod.PATCH, SecurityLevel.ROOT, new ShutdownController(), "/danger-zone/shutdown");
        this.addRoute(RequestMethod.PATCH, SecurityLevel.ROOT, new CreateDatabaseController(), "/danger-zone/initialize/database");
        this.addRoute(RequestMethod.PUT, SecurityLevel.ROOT, new CreateAccountController(), "/security/account-management/create");
    }

    public void startup() {
        this.server.start();
    }

    public void shutdown() {
        this.shutdown(0);
    }

    public void shutdown(int delay) {
        server.stop(delay);
    }
}
