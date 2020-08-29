package network.atria.Util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import network.atria.Database.MySQLSetterGetter;

public class getPlayerData {

    public static boolean hasRequirePoint(String uuid, int require) {
        return MySQLSetterGetter.getPoints(uuid) >= require;
    }

    public static boolean hasDonorRank(Player player) {
        return player.hasPermission("pgm.group.donor");
    }

    public static Integer getRequirePoints(String effect) {
        return KillEffectsConfig.getCustomConfig().getInt(effect + ".points");
    }

    public static String canUseEffects(String uuid, Integer require) {
        if(MySQLSetterGetter.getPoints(uuid) >= require) {
            return ChatColor.GREEN + "" + ChatColor.BOLD + "✔ Unlocked";
        } else {
            int current = MySQLSetterGetter.getPoints(uuid);
            int result = require - current;
            return ChatColor.RED + "" +ChatColor.BOLD + "✖ You need " + ChatColor.YELLOW + result + ChatColor.RED + " more points";
        }
    }
}
