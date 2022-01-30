package club.smartsheep.panelcraftcore.Modules.Security;

import club.smartsheep.panelcraftcore.Common.Tokens.AuthorizationTokenChecker;
import club.smartsheep.panelcraftcore.Common.Tokens.CheckPassword;
import club.smartsheep.panelcraftcore.PanelCraft;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpServer;
import com.auth0.jwt.JWT;

import java.sql.SQLException;
import java.util.List;

public class AuthorizationDecoder {
    final private String code;

    public AuthorizationDecoder(String authorizationCode) {
        this.code = authorizationCode;
    }

    public String getUsername() {
        if(isJWTAuthorizationCode()) {
            List<String> username = JWT.decode(this.code).getAudience();
            if(username == null || username.size() == 0) {
                return "Unknown";
            }
            return username.get(0);
        } else {
            return "Root";
        }
    }

    public boolean isJWTAuthorizationCode() {
        return AuthorizationTokenChecker.test(this.code);
    }

    public boolean checkRoleToAccess(PanelHttpServer.SecurityLevel securityLevel) {
        switch (securityLevel) {
            case PLAYER:
                if(isJWTAuthorizationCode()) {
                    try {
                        return AuthorizationTokenChecker.check(this.code);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return CheckPassword.checkRootPassword(this.code);
            case ADMIN:
                if(isJWTAuthorizationCode()) {
                    try {
                        return AuthorizationTokenChecker.check(this.code, "ADMIN");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return CheckPassword.checkRootPassword(this.code);
            case ROOT:
                if(isJWTAuthorizationCode()) {
                    return false;
                } else {
                    return CheckPassword.checkRootPassword(this.code);
                }
            default:
                PanelCraft.LOGGER.warning("Code Error, AuthorizationCheck check failed, reason: Couldn't found wanted role in role library");
                return false;
        }
    }

    public boolean checkPermissionToAccess(String requirePermission) {
        if(!isJWTAuthorizationCode()) {
            return CheckPassword.checkRootPassword(this.code);
        }

        if(requirePermission.startsWith("player")) {
            try {
                if(AuthorizationTokenChecker.check(this.code, "ADMIN")) {
                    return true;
                } else {
                    return AuthorizationTokenChecker.check(this.code, new String[]{requirePermission});
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        else if(requirePermission.startsWith("admin")) {
            try {
                if(AuthorizationTokenChecker.check(this.code, "ADMIN")) {
                    return AuthorizationTokenChecker.check(this.code, new String[]{requirePermission});
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        else {
            try {
                return AuthorizationTokenChecker.check(this.code, new String[]{requirePermission});
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
