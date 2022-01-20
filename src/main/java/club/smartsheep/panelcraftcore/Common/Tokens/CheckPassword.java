package club.smartsheep.panelcraftcore.Common.Tokens;

import club.smartsheep.panelcraftcore.Modules.Security.RootRandomPasswordGenerator;
import org.apache.commons.codec.digest.DigestUtils;

public class CheckPassword {
    /**
     *
     * @param password Use MD5 encrypted password
     * @return Is the password correct
     */
    public static boolean checkRootPassword(String password) {
        String rootPassword = DigestUtils.md5Hex(RootRandomPasswordGenerator.CurrentRootPassword);
        return password.equalsIgnoreCase(rootPassword);
    }
}
