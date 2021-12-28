package club.smartsheep.panelcraftcore.Common.Tokens;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommonToken {

    public String summonToken(long expire, String secret) {
        String token = "";
        try {
            Date date = new Date(System.currentTimeMillis() + expire);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");

            token = JWT.create()
                    .withHeader(header)
                    .withClaim("password", secret).withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return token;
    }

    public String summonToken(String secret) {
        String token = "";
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");

            token = JWT.create()
                    .withHeader(header)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return token;
    }
}
