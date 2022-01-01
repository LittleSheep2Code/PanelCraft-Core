package club.smartsheep.panelcraftcore.Common.Tokens;

import club.smartsheep.panelcraftcore.PanelCraft;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CheckPassword {
    /**
     *
     * @param password Use MD5 encrypted password
     * @return Is the password correct
     */
    public static boolean checkRootPassword(String password) {
        String rootPassword = DigestUtils.md5Hex(PanelCraft.getPlugin(PanelCraft.class).getConfig().getString("security.root"));
        return password.equalsIgnoreCase(rootPassword);
    }
}
