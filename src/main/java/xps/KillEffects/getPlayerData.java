package xps.KillEffects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xps.Config.KillEffectsConfig;
import xps.Database.MySQLSetterGetter;
import xps.RankSystem.Ranks;

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
        if(Ranks.getCurrentPoint(uuid) >= require) {
            return ChatColor.GREEN + "Unlocked";
        } else {
            int current = MySQLSetterGetter.getPoints(uuid);
            int result = current - require;
            return ChatColor.RED + "You need" + ChatColor.YELLOW + result + ChatColor.RED + "more points";
        }
    }
}
