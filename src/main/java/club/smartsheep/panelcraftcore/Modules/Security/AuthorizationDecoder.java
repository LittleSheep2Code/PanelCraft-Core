package club.smartsheep.panelcraftcore.Modules.Security;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
import club.smartsheep.panelcraftcore.Common.Tokens.AuthorizationTokenChecker;
import club.smartsheep.panelcraftcore.Common.Tokens.CheckPassword;
import club.smartsheep.panelcraftcore.PanelCraft;
import club.smartsheep.panelcraftcore.Server.HTTP.PanelHttpServer;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AuthorizationDecoder {
    final private String code;

    public AuthorizationDecoder(String authorizationCode) {
        this.code = authorizationCode;
    }

    public String getUsername() {
        if (isJWTAuthorizationCode()) {
            List<String> username = JWT.decode(this.code).getAudience();
            if (username == null || username.size() == 0) {
                return "Unknown";
            }
            return username.get(0);
        } else {
            return "Root";
        }
    }

    public boolean isJWTAuthorizationCode() {
        try {
            JWT.decode(this.code);
        } catch (JWTDecodeException e) {
            return false;
        }
        return true;
    }

    private short singleCheck() throws SQLException {
        String username;
        try {
            username = JWT.decode(this.code).getAudience().get(0);
        } catch (JWTDecodeException e) {
            return 400;
        }

        if (username == null) {
            return 400;
        }

        String script = String.format("SELECT password FROM system__accounts WHERE username='%s'", username);
        Statement statement = DatabaseConnector.get().connect().createStatement();
        ResultSet set = statement.executeQuery(script);
        String password = set.getString("password");

        if (password == null) {
            return 400;
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password)).build();
            verifier.verify(this.code);
        } catch (JWTVerificationException e) {
            return 400;
        }
        return 200;
    }

    private short roleCheck(String require) throws SQLException {
        String username;
        try {
            username = JWT.decode(this.code).getAudience().get(0);
        } catch (JWTDecodeException e) {
            return 400;
        }

        if (username == null) {
            return 400;
        }

        String script = String.format("SELECT password, role FROM system__accounts WHERE username='%s'", username);
        Statement statement = DatabaseConnector.get().connect().createStatement();
        ResultSet set = statement.executeQuery(script);
        String password = set.getString("password");
        String role = set.getString("role");

        if (role == null || !role.equalsIgnoreCase(require)) {
            return 401;
        }

        if (password == null) {
            return 400;
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password)).build();
            verifier.verify(this.code);
        } catch (JWTVerificationException e) {
            return 400;
        }

        return 200;
    }

    private short permissionCheck(String require) throws SQLException {
        String username;
        try {
            username = JWT.decode(this.code).getAudience().get(0);
        } catch (JWTDecodeException e) {
            return 400;
        }

        if (username == null) {
            return 400;
        }

        String script = String.format("SELECT password, permissions FROM system__accounts WHERE username='%s'", username);
        Statement statement = DatabaseConnector.get().connect().createStatement();
        ResultSet set = statement.executeQuery(script);
        String password = set.getString("password");

        JSONObject permissions;
        try {
            permissions = new JSONObject(set.getString("permissions"));
        } catch (JSONException e) {
            new ErrorLoggers().JSONProcessError("check user AuthorizationCode", "user permission");
            return 400;
        }

        if (!permissions.has(require)) {
            return 401;
        } else if (!permissions.getBoolean(require)) {
            return 401;
        }

        if (password == null) {
            return 400;
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password)).build();
            verifier.verify(this.code);
        } catch (JWTVerificationException e) {
            return 400;
        }

        return 200;
    }

    public DecoderResponse checkRoleToAccess(PanelHttpServer.SecurityLevel securityLevel) {
        switch (securityLevel) {
            case PLAYER:
                if (isJWTAuthorizationCode()) {
                    try {
                        if (this.singleCheck() == 400) {
                            return DecoderResponse.AuthorizationCodeUnavailable;
                        } else {
                            return DecoderResponse.OK;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return DecoderResponse.Error;
                    }
                }
                return DecoderResponse.OK;
            case ADMIN:
                if (isJWTAuthorizationCode()) {
                    try {
                        short responseCode = this.roleCheck("ADMIN");
                        if (responseCode == 400) {
                            return DecoderResponse.AuthorizationCodeUnavailable;
                        } else if (responseCode == 401) {
                            return DecoderResponse.InsufficientPermissions;
                        } else {
                            return DecoderResponse.OK;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return DecoderResponse.Error;
                    }
                }
                if (CheckPassword.checkRootPassword(this.code)) {
                    return DecoderResponse.OK;
                } else {
                    return DecoderResponse.AuthorizationCodeUnavailable;
                }
            case ROOT:
                if (isJWTAuthorizationCode() || !CheckPassword.checkRootPassword(this.code)) {
                    return DecoderResponse.AuthorizationCodeUnavailable;
                } else {
                    return DecoderResponse.OK;
                }
            default:
                PanelCraft.LOGGER.warning("Code Error, AuthorizationCheck check failed, reason: Couldn't found wanted role in role library");
                return DecoderResponse.Error;
        }
    }

    public DecoderResponse checkPermissionToAccess(String requirePermission) {
        if (!isJWTAuthorizationCode()) {
            if(CheckPassword.checkRootPassword(this.code)) {
                return DecoderResponse.OK;
            } else {
                return DecoderResponse.AuthorizationCodeUnavailable;
            }
        }

        if (requirePermission.startsWith("player")) {
            try {
                if (this.roleCheck("ADMIN") == 200) {
                    return DecoderResponse.OK;
                } else {
                    short responseCode = this.permissionCheck(requirePermission);
                    if(responseCode == 400) {
                        return DecoderResponse.AuthorizationCodeUnavailable;
                    } else if(responseCode == 401) {
                        return DecoderResponse.InsufficientPermissions;
                    } else {
                        return DecoderResponse.OK;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return DecoderResponse.Error;
            }
        } else if (requirePermission.startsWith("admin")) {
            try {
                if (this.roleCheck("ADMIN") == 200) {
                    short responseCode = this.permissionCheck(requirePermission);
                    if(responseCode == 400) {
                        return DecoderResponse.AuthorizationCodeUnavailable;
                    } else if(responseCode == 401) {
                        return DecoderResponse.InsufficientPermissions;
                    } else {
                        return DecoderResponse.OK;
                    }
                } else {
                    return DecoderResponse.AuthorizationCodeUnavailable;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return DecoderResponse.Error;
            }
        } else {
            try {
                short responseCode = this.permissionCheck(requirePermission);
                if(responseCode == 400) {
                    return DecoderResponse.AuthorizationCodeUnavailable;
                } else if(responseCode == 401) {
                    return DecoderResponse.InsufficientPermissions;
                } else {
                    return DecoderResponse.OK;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return DecoderResponse.Error;
            }
        }
    }

    public enum DecoderResponse {
        OK,
        AuthorizationCodeUnavailable,
        InsufficientPermissions,
        Error
    }
}
