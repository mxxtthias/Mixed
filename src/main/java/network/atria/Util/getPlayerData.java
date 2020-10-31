package network.atria.Util;

import java.util.UUID;
import network.atria.Database.MySQLSetterGetter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class getPlayerData {

  public static boolean hasRequirePoint(UUID uuid, int require) {
    return MySQLSetterGetter.getPoints(uuid) >= require;
  }

  public static boolean hasDonorRank(Player player) {
    return player.hasPermission("pgm.group.donor");
  }

  public static Integer getRequirePoints(String effect) {
    return KillEffectsConfig.getCustomConfig().getInt(effect + ".points");
  }

  public static String canUseEffects(UUID uuid, Integer require) {
    if (MySQLSetterGetter.getPoints(uuid) >= require) {
      return ChatColor.GREEN + "" + ChatColor.BOLD + "✔ Unlocked";
    } else {
      final int current = MySQLSetterGetter.getPoints(uuid);
      final int result = require - current;
      return ChatColor.RED
          + ""
          + ChatColor.BOLD
          + "✖ You need "
          + ChatColor.YELLOW
          + result
          + ChatColor.RED
          + " more points";
    }
  }
}
