package club.smartsheep.panelcraftcore.Common.Tokens;

import club.smartsheep.panelcraftcore.PanelCraft;

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
        try {
            MessageDigest encrypt = MessageDigest.getInstance("MD5");
            encrypt.update(PanelCraft.getPlugin(PanelCraft.class).getConfig().getString("security.root").getBytes(StandardCharsets.UTF_8));
            byte[] digest = encrypt.digest();
            String rootPassword = new String(digest, StandardCharsets.UTF_8).toUpperCase();
            return password.equalsIgnoreCase(rootPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
}
