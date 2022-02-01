package club.smartsheep.panelcraftcore.Server.HTTP;

import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
import club.smartsheep.panelcraftcore.Common.Loggers.LoadingStateLoggers;
import club.smartsheep.panelcraftcore.Controllers.Console.ExecuteController;
import club.smartsheep.panelcraftcore.Controllers.Console.Placeholder.PlaceholderProcessorController;
import club.smartsheep.panelcraftcore.Controllers.Console.Vault.VaultEconomyController;
import club.smartsheep.panelcraftcore.Controllers.Dangerous.Initialize.CreateDatabaseController;
import club.smartsheep.panelcraftcore.Controllers.Dangerous.ReloadController;
import club.smartsheep.panelcraftcore.Controllers.Dangerous.ShutdownController;
import club.smartsheep.panelcraftcore.Controllers.DetailController;
import club.smartsheep.panelcraftcore.Controllers.Security.AuthorizationController;
import club.smartsheep.panelcraftcore.Controllers.Security.UserManagement.CreateAccountController;
import club.smartsheep.panelcraftcore.Modules.Security.AuthorizationDecoder;
import club.smartsheep.panelcraftcore.PanelCraft;
import club.smartsheep.panelcraftcore.Server.HTTP.Errors.RouteRegisterError;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;

public class PanelHttpServer {
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
            PanelHttpExchange panelExchange = new PanelHttpExchange(exchange);
            if (!exchange.getRequestMethod().equalsIgnoreCase(method.name)) {
                panelExchange.getErrorSender().MethodNotAllowResponse();
                return;
            }

            handler.handle(panelExchange);
        });
        return true;
    }

    public boolean addRoute(RequestMethod method, SecurityLevel security, PanelHttpHandler handler, String path) throws RouteRegisterError {
        if (!path.equals("/") && (!path.startsWith("/") || path.endsWith("/"))) {
            throw new RouteRegisterError("Path is need start by a slash, and cannot end by slash", path);
        }
        server.createContext(path, exchange -> {
            PanelHttpExchange panelExchange = new PanelHttpExchange(exchange);
            if (!exchange.getRequestMethod().equalsIgnoreCase(method.name)) {
                panelExchange.getErrorSender().MethodNotAllowResponse();
                return;
            }

            String authorizationCode = exchange.getRequestHeaders().get("Authorization-Code").get(0);

            if (authorizationCode == null) {
                panelExchange.getErrorSender().MissingHeaderErrorResponse("Authorization-Code");
                return;
            }

            switch(new AuthorizationDecoder(authorizationCode).checkRoleToAccess(security))  {
                case Error:
                    return;
                case InsufficientPermissions:
                    switch (security) {
                        case ROOT:
                            panelExchange.getErrorSender().InsufficientPermissionsErrorResponse("root role");
                        case ADMIN:
                            panelExchange.getErrorSender().InsufficientPermissionsErrorResponse("admin role");
                        case PLAYER:
                            panelExchange.getErrorSender().InsufficientPermissionsErrorResponse("player role");
                    }
                    return;
                case AuthorizationCodeUnavailable:
                    panelExchange.getErrorSender().UnavailableAuthorizationCodeError();
                    return;
                case OK:
                    break;
            }

            panelExchange.Authorization_Code = authorizationCode;
            panelExchange.Authorization_Username = new AuthorizationDecoder(authorizationCode).getUsername();

            handler.handle(panelExchange);
        });
        return true;
    }

    public boolean addRoute(RequestMethod method, String permission, PanelHttpHandler handler, String path) throws RouteRegisterError {
        if (!path.equals("/") && (!path.startsWith("/") || path.endsWith("/"))) {
            throw new RouteRegisterError("Path is need start by a slash, and cannot end by slash", path);
        }
        server.createContext(path, exchange -> {
            PanelHttpExchange panelExchange = new PanelHttpExchange(exchange);
            if (!exchange.getRequestMethod().equalsIgnoreCase(method.name)) {
                panelExchange.getErrorSender().MethodNotAllowResponse();
                return;
            }

            String authorizationCode = exchange.getRequestHeaders().get("Authorization-Code").get(0);

            if (authorizationCode == null) {
                panelExchange.getErrorSender().MissingHeaderErrorResponse("Authorization-Code");
                return;
            }

            switch (new AuthorizationDecoder(authorizationCode).checkPermissionToAccess(permission)) {
                case Error:
                    return;
                case InsufficientPermissions:
                    panelExchange.getErrorSender().InsufficientPermissionsErrorResponse(permission + " permission");
                    return;
                case AuthorizationCodeUnavailable:
                    panelExchange.getErrorSender().UnavailableAuthorizationCodeError();
                    return;
                case OK:
                    break;
            }

            panelExchange.Authorization_Code = authorizationCode;
            panelExchange.Authorization_Username = new AuthorizationDecoder(authorizationCode).getUsername();

            handler.handle(panelExchange);
        });
        return true;
    }

    @SneakyThrows
    public void autoAddRoute() {
        this.addRoute(RequestMethod.GET, new DetailController(), "/");
        this.addRoute(RequestMethod.PUT, "admin.console.execute", new ExecuteController(), "/console");
        this.addRoute(RequestMethod.PUT, "player.data.vault.economy", new VaultEconomyController(), "/console/vault/economy");
        this.addRoute(RequestMethod.PUT, "player.data.placeholder", new PlaceholderProcessorController(), "/console/placeholder");
        this.addRoute(RequestMethod.PATCH, "admin.power.reload", new ReloadController(), "/danger-zone/reload");
        this.addRoute(RequestMethod.PATCH, "admin.power.shutdown", new ShutdownController(), "/danger-zone/shutdown");
        this.addRoute(RequestMethod.PATCH, SecurityLevel.ADMIN, new CreateDatabaseController(), "/danger-zone/initialize/database");
        this.addRoute(RequestMethod.PUT, SecurityLevel.ROOT, new CreateAccountController(), "/security/account-management/create");
        this.addRoute(RequestMethod.PUT, new AuthorizationController(), "/security/authorization");
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
        PLAYER,
        ADMIN,
        ROOT
    }
}
