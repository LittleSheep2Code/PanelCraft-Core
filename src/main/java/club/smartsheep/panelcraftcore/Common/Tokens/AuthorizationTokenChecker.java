package club.smartsheep.panelcraftcore.Common.Tokens;

import club.smartsheep.panelcraftcore.Common.Configure.DatabaseConnector;
import club.smartsheep.panelcraftcore.Common.Loggers.ErrorLoggers;
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

public class AuthorizationTokenChecker {
    public static boolean test(String token) {
        try {
            JWT.decode(token);
        } catch (JWTDecodeException e) {
            return false;
        }
        return true;
    }

    public static boolean check(String token) throws SQLException {
        String username;
        try {
            username = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            return false;
        }

        if(username == null) {
            return false;
        }

        String script = String.format("SELECT password FROM system__accounts WHERE username='%s'", username);
        Statement statement = DatabaseConnector.get().connect().createStatement();
        ResultSet set = statement.executeQuery(script);
        String password = set.getString("password");

        if(password == null) {
            return false;
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password)).build();
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }

        return true;
    }

    public static boolean check(String token, String requireRole) throws SQLException {
        String username;
        try {
            username = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            return false;
        }

        if(username == null) {
            return false;
        }

        String script = String.format("SELECT password, role FROM system__accounts WHERE username='%s'", username);
        Statement statement = DatabaseConnector.get().connect().createStatement();
        ResultSet set = statement.executeQuery(script);
        String password = set.getString("password");
        String role = set.getString("role");

        if(role == null || !role.equalsIgnoreCase(requireRole)) {
            return false;
        }

        if(password == null) {
            return false;
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password)).build();
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }

        return true;
    }

    public static boolean check(String token, String[] requirePermissions) throws SQLException {
        String username;
        try {
            username = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            return false;
        }

        if(username == null) {
            return false;
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
            return false;
        }

        for (String requirePermission : requirePermissions) {
            if(!permissions.has(requirePermission)) {
                return false;
            } else if(!permissions.getBoolean(requirePermission)) {
                return false;
            }
        }

        if(password == null) {
            return false;
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password)).build();
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }

        return true;
    }
}
