package network.atria.RankSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Util.RanksConfig;
import org.bukkit.ChatColor;

public class Ranks {

  public static String getNextRank(String uuid) {

    String now = MySQLSetterGetter.getRank(uuid);

    List<String> ranks =
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

  public static String getCurrentRank(String uuid) {
    return MySQLSetterGetter.getRank(uuid);
  }

  public static Integer getCurrentPoint(String uuid) {
    return MySQLSetterGetter.getPoints(uuid);
  }

  public static Integer getRequirePoint(String uuid) {
    Integer next = RanksConfig.getCustomConfig().getInt("Ranks." + getNextRank(uuid) + ".Points");
    Integer now = getCurrentPoint(uuid);

    return next - now;
  }

  public static boolean canRankUp(String uuid) {
    Integer next = RanksConfig.getCustomConfig().getInt("Ranks." + getNextRank(uuid) + ".Points");
    Integer now = getCurrentPoint(uuid);

    if (getNextRank(uuid).equals(getCurrentRank(uuid))) {
      return false;
    } else {
      int result = next - now;
      return result <= 0;
    }
  }

  public static String getRankCurrent(String uuid) {
    String rank = null;

    if (getCurrentRank(uuid).equals("wood_iii")
        || getCurrentRank(uuid).equals("wood_ii")
        || getCurrentRank(uuid).equals("wood_i")) {
      rank = ChatColor.GOLD + "" + ChatColor.BOLD + getCurrentRank(uuid);
    }

    if (getCurrentRank(uuid).equals("stone_iii")
        || getCurrentRank(uuid).equals("stone_ii")
        || getCurrentRank(uuid).equals("stone_i")) {
      rank = ChatColor.GRAY + "" + ChatColor.BOLD + getCurrentRank(uuid);
    }

    if (getCurrentRank(uuid).equals("iron_iii")
        || getCurrentRank(uuid).equals("iron_ii")
        || getCurrentRank(uuid).equals("iron_i")) {
      rank = ChatColor.WHITE + "" + ChatColor.BOLD + getCurrentRank(uuid);
    }

    if (getCurrentRank(uuid).equals("gold_iii")
        || getCurrentRank(uuid).equals("gold_ii")
        || getCurrentRank(uuid).equals("gold_i")) {
      rank = ChatColor.YELLOW + "" + ChatColor.BOLD + getCurrentRank(uuid);
    }

    if (getCurrentRank(uuid).equals("emerald_iii")
        || getCurrentRank(uuid).equals("emerald_ii")
        || getCurrentRank(uuid).equals("emerald_i")) {
      rank = ChatColor.GREEN + "" + ChatColor.BOLD + getCurrentRank(uuid);
    }

    if (getCurrentRank(uuid).equals("diamond_iii")
        || getCurrentRank(uuid).equals("diamond_ii")
        || getCurrentRank(uuid).equals("diamond_i")) {
      rank = ChatColor.AQUA + "" + ChatColor.BOLD + getCurrentRank(uuid);
    }

    if (getCurrentRank(uuid).equals("obsidian")) {
      rank = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + getCurrentRank(uuid);
    }
    return rank;
  }

  public static String getRankNext(String uuid) {
    String rank = null;

    try {
      if (getNextRank(uuid).equals("wood_iii")
          || getNextRank(uuid).equals("wood_ii")
          || getNextRank(uuid).equals("wood_i")) {
        rank = ChatColor.GOLD + "" + ChatColor.BOLD + format(uuid);
      }

      if (getNextRank(uuid).equals("stone_iii")
          || getNextRank(uuid).equals("stone_ii")
          || getNextRank(uuid).equals("stone_i")) {
        rank = ChatColor.GRAY + "" + ChatColor.BOLD + format(uuid);
      }

      if (getNextRank(uuid).equals("iron_iii")
          || getNextRank(uuid).equals("iron_ii")
          || getNextRank(uuid).equals("iron_i")) {
        rank = ChatColor.WHITE + "" + ChatColor.BOLD + format(uuid);
      }

      if (getNextRank(uuid).equals("gold_iii")
          || getNextRank(uuid).equals("gold_ii")
          || getNextRank(uuid).equals("gold_i")) {
        rank = ChatColor.YELLOW + "" + ChatColor.BOLD + format(uuid);
      }

      if (getNextRank(uuid).equals("emerald_iii")
          || getNextRank(uuid).equals("emerald_ii")
          || getNextRank(uuid).equals("emerald_i")) {
        rank = ChatColor.GREEN + "" + ChatColor.BOLD + format(uuid);
      }

      if (getNextRank(uuid).equals("diamond_iii")
          || getNextRank(uuid).equals("diamond_ii")
          || getNextRank(uuid).equals("diamond_i")) {
        rank = ChatColor.AQUA + "" + ChatColor.BOLD + format(uuid);
      }

      if (getNextRank(uuid).equals("obsidian")) {
        rank = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + format(uuid);
      }
    } catch (NullPointerException e) {
      if (getNextRank(uuid).equals("wood_iii")
          || getNextRank(uuid).equals("wood_ii")
          || getNextRank(uuid).equals("wood_i")) {
        rank = ChatColor.GOLD + "" + ChatColor.BOLD + getNextRank(uuid);
      }

      if (getNextRank(uuid).equals("stone_iii")
          || getNextRank(uuid).equals("stone_ii")
          || getNextRank(uuid).equals("stone_i")) {
        rank = ChatColor.GRAY + "" + ChatColor.BOLD + getNextRank(uuid);
      }

      if (getNextRank(uuid).equals("iron_iii")
          || getNextRank(uuid).equals("iron_ii")
          || getNextRank(uuid).equals("iron_i")) {
        rank = ChatColor.WHITE + "" + ChatColor.BOLD + getNextRank(uuid);
      }

      if (getNextRank(uuid).equals("gold_iii")
          || getNextRank(uuid).equals("gold_ii")
          || getNextRank(uuid).equals("gold_i")) {
        rank = ChatColor.YELLOW + "" + ChatColor.BOLD + getNextRank(uuid);
      }

      if (getNextRank(uuid).equals("emerald_iii")
          || getNextRank(uuid).equals("emerald_ii")
          || getNextRank(uuid).equals("emerald_i")) {
        rank = ChatColor.GREEN + "" + ChatColor.BOLD + getNextRank(uuid);
      }

      if (getNextRank(uuid).equals("diamond_iii")
          || getNextRank(uuid).equals("diamond_ii")
          || getNextRank(uuid).equals("diamond_i")) {
        rank = ChatColor.AQUA + "" + ChatColor.BOLD + getNextRank(uuid);
      }

      if (getNextRank(uuid).equals("obsidian")) {
        rank = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + getNextRank(uuid);
      }
    }
    return rank;
  }

  private static String format(String uuid) {
    return Objects.requireNonNull(getNextRank(uuid)).replace("_", " ").toUpperCase();
  }
}
