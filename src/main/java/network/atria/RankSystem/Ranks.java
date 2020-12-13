package network.atria.RankSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Util.RanksConfig;
import network.atria.Util.TextFormat;

public class Ranks {

  public static String getNextRank(UUID uuid) {
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

  public static String getCurrentRank(UUID uuid) {
    return MySQLSetterGetter.getRank(uuid);
  }

  public static Integer getCurrentPoint(UUID uuid) {
    return MySQLSetterGetter.getPoints(uuid);
  }

  public static Integer getRequirePoint(UUID uuid) {
    Integer next = RanksConfig.getCustomConfig().getInt("Ranks." + getNextRank(uuid) + ".Points");
    Integer now = getCurrentPoint(uuid);

    return next - now;
  }

  public static boolean canRankUp(UUID uuid) {
    String rank = getNextRank(uuid);
    Integer next = RanksConfig.getCustomConfig().getInt("Ranks." + rank + ".Points");
    Integer now = getCurrentPoint(uuid);

    if (rank.equalsIgnoreCase(getCurrentRank(uuid))) {
      return false;
    } else {
      int result = next - now;
      return result <= 0;
    }
  }

  public static String getRankCurrent(UUID uuid) {
    String current = getCurrentRank(uuid);
    String rank = null;

    switch (current) {
      case "wood_iii":
      case "wood_ii":
      case "wood_i":
        rank = format(current, NamedTextColor.GOLD);
        break;
      case "stone_iii":
      case "stone_ii":
      case "stone_i":
        rank = format(current, NamedTextColor.GRAY);
        break;
      case "iron_iii":
      case "iron_ii":
      case "iron_i":
        rank = format(current, NamedTextColor.WHITE);
        break;
      case "gold_iii":
      case "gold_ii":
      case "gold_i":
        rank = format(current, NamedTextColor.YELLOW);
        break;
      case "emerald_iii":
      case "emerald_ii":
      case "emerald_i":
        rank = format(current, NamedTextColor.GREEN);
        break;
      case "diamond_iii":
      case "diamond_ii":
      case "diamond_i":
        rank = format(current, NamedTextColor.AQUA);
        break;
      case "obsidian":
        rank = format(current, NamedTextColor.DARK_PURPLE);
    }

    return rank;
  }

  /*

  Designed Effect
   */

  public static String getRankNext(UUID uuid) {
    String rank = null;
    String next = getNextRank(uuid);

    switch (next) {
      case "wood_iii":
      case "wood_ii":
      case "wood_i":
        rank = format(next, NamedTextColor.GOLD);
        break;
      case "stone_iii":
      case "stone_ii":
      case "stone_i":
        rank = format(next, NamedTextColor.GRAY);
        break;
      case "iron_iii":
      case "iron_ii":
      case "iron_i":
        rank = format(next, NamedTextColor.WHITE);
        break;
      case "gold_iii":
      case "gold_ii":
      case "gold_i":
        rank = format(next, NamedTextColor.YELLOW);
        break;
      case "emerald_iii":
      case "emerald_ii":
      case "emerald_i":
        rank = format(next, NamedTextColor.GREEN);
        break;
      case "diamond_iii":
      case "diamond_ii":
      case "diamond_i":
        rank = format(next, NamedTextColor.AQUA);
        break;
      case "obsidian":
        rank = format(next, NamedTextColor.DARK_PURPLE);
    }
    return rank;
  }

  private static String replace(String rank) {
    return rank.replace("_", " ").toUpperCase();
  }

  private static String format(String rank, NamedTextColor color) {
    return TextFormat.format(Component.text(replace(rank), color, TextDecoration.BOLD));
  }
}
