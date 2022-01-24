package club.smartsheep.panelcraftcore.Modules.Security;

import club.smartsheep.panelcraftcore.Common.ActionRecorder.RecordAction;
import club.smartsheep.panelcraftcore.Common.Tokens.DynamicPasswordGenerator;
import club.smartsheep.panelcraftcore.PanelCraft;
import org.bukkit.Bukkit;

public class RootRandomPasswordGenerator {
    public static String CurrentRootPassword;

    public static void startup() {
        CurrentRootPassword = new DynamicPasswordGenerator().GenerateRandomPassword(DynamicPasswordGenerator.PasswordStrength.strong);
        PanelCraft.LOGGER.info("");
        PanelCraft.LOGGER.info("§a[*] §eCurrent root password is: §6" + CurrentRootPassword);
        PanelCraft.LOGGER.info("§a[*] §6Next time update password is 1 day later.");
        PanelCraft.LOGGER.info("");
        Bukkit.getScheduler().runTaskTimerAsynchronously(PanelCraft.getPlugin(PanelCraft.class), () -> {
            PanelCraft.LOGGER.info("");
            PanelCraft.LOGGER.info("§a[*] §lUpdating root password, the current update cycle is 1 day(86400 seconds).");
            CurrentRootPassword = new DynamicPasswordGenerator().GenerateRandomPassword(DynamicPasswordGenerator.PasswordStrength.strong);
            RecordAction.recordDown("System", "Update root password", "127.0.0.1");
            PanelCraft.LOGGER.info("§a[*] §eAfter update root password is: §6" + CurrentRootPassword);
            PanelCraft.LOGGER.info("§a[*] §lRoot password has been updated.");
            PanelCraft.LOGGER.info("");
        }, 86400 * 20, 86400 * 20);
    }
}
