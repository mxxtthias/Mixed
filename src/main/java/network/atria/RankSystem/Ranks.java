package network.atria.RankSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Util.RanksConfig;
import org.bukkit.ChatColor;

public class Ranks {

  public static String getNextRank(UUID uuid) {
    final String now = MySQLSetterGetter.getRank(uuid);

    final List<String> ranks =
        new ArrayList<>(
            RanksConfig.getCustomConfig().getConfigurationSection("Ranks").getKeys(false));

    for (int i = ranks.size() - 1; i >= 0; i--) {
      if (now.equalsIgnoreCase(ranks.get(i))) {
        try {
          return ranks.get(i + 1);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
          return getCurrentRank(uuid);
        }
      }
    }
    return null;
  }

  public static String getCurrentRank(UUID uuid) {
    return MySQLSetterGetter.getRank(uuid);
  }

  public static Integer getCurrentPoint(UUID uuid) {
    return MySQLSetterGetter.getPoints(uuid);
  }

  public static Integer getRequirePoint(UUID uuid) {
    final Integer next =
        RanksConfig.getCustomConfig().getInt("Ranks." + getNextRank(uuid) + ".Points");
    final Integer now = getCurrentPoint(uuid);

    return next - now;
  }

  public static boolean canRankUp(UUID uuid) {
    final String rank = getNextRank(uuid);
    final Integer next = RanksConfig.getCustomConfig().getInt("Ranks." + rank + ".Points");
    final Integer now = getCurrentPoint(uuid);

    if (rank.equalsIgnoreCase(getCurrentRank(uuid))) {
      return false;
    } else {
      int result = next - now;
      return result <= 0;
    }
  }

  public static String getRankCurrent(UUID uuid) {
    final String current = getCurrentRank(uuid);
    String rank = null;

    switch (current) {
      case "wood_iii":
      case "wood_ii":
      case "wood_i":
        rank = ChatColor.GOLD + "" + ChatColor.BOLD + current;
        break;
      case "stone_iii":
      case "stone_ii":
      case "stone_i":
        rank = ChatColor.GRAY + "" + ChatColor.BOLD + current;
        break;
      case "iron_iii":
      case "iron_ii":
      case "iron_i":
        rank = ChatColor.WHITE + "" + ChatColor.BOLD + current;
        break;
      case "gold_iii":
      case "gold_ii":
      case "gold_i":
        rank = ChatColor.YELLOW + "" + ChatColor.BOLD + current;
        break;
      case "emerald_iii":
      case "emerald_ii":
      case "emerald_i":
        rank = ChatColor.GREEN + "" + ChatColor.BOLD + current;
        break;
      case "diamond_iii":
      case "diamond_ii":
      case "diamond_i":
        rank = ChatColor.AQUA + "" + ChatColor.BOLD + current;
        break;
      case "obsidian":
        rank = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + current;
    }

    return rank;
  }

  /*

  Designed Rank
   */

  public static String getRankNext(UUID uuid) {
    String rank = null;
    switch (Objects.requireNonNull(getNextRank(uuid))) {
      case "wood_iii":
      case "wood_ii":
      case "wood_i":
        rank = ChatColor.GOLD + "" + ChatColor.BOLD + format(uuid);
        break;
      case "stone_iii":
      case "stone_ii":
      case "stone_i":
        rank = ChatColor.GRAY + "" + ChatColor.BOLD + format(uuid);
        break;
      case "iron_iii":
      case "iron_ii":
      case "iron_i":
        rank = ChatColor.WHITE + "" + ChatColor.BOLD + format(uuid);
        break;
      case "gold_iii":
      case "gold_ii":
      case "gold_i":
        rank = ChatColor.YELLOW + "" + ChatColor.BOLD + format(uuid);
        break;
      case "emerald_iii":
      case "emerald_ii":
      case "emerald_i":
        rank = ChatColor.GREEN + "" + ChatColor.BOLD + format(uuid);
        break;
      case "diamond_iii":
      case "diamond_ii":
      case "diamond_i":
        rank = ChatColor.AQUA + "" + ChatColor.BOLD + format(uuid);
        break;
      case "obsidian":
        rank = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + format(uuid);
    }
    return rank;
  }

  private static String format(UUID uuid) {
    return Objects.requireNonNull(getNextRank(uuid)).replace("_", " ").toUpperCase();
  }
}
