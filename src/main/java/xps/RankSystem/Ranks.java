package xps.RankSystem;

import org.bukkit.ChatColor;
import xps.CustomConfig;
import xps.Database.MySQLSetterGetter;

import java.util.ArrayList;
import java.util.List;

public class Ranks {

    public static String getNextRank(String uuid) {

        String now = MySQLSetterGetter.getRank(uuid);

        List<String> ranks = new ArrayList<>(CustomConfig.getCustomConfig().getConfigurationSection("Ranks").getKeys(false));

        for (int i = ranks.size() - 1; i >= 0; i--) {
            if (now.equalsIgnoreCase(ranks.get(i))) {
                try {
                    return ranks.get(i + 1);
                } catch (IndexOutOfBoundsException | NullPointerException e) {
                    e.printStackTrace();
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
        Integer next = CustomConfig.getCustomConfig().getInt("Ranks." + getNextRank(uuid) + ".Points");
        Integer now = getCurrentPoint(uuid);

        return next - now;
    }

    public static boolean canRankUp(String uuid) {
        Integer next = CustomConfig.getCustomConfig().getInt("Ranks." + getNextRank(uuid) + ".Points");
        Integer now = getCurrentPoint(uuid);
        int result = next - now;

        return result <= 0;
    }

    public static String getPrefix(String uuid) {
        return ChatColor.translateAlternateColorCodes('&', CustomConfig.getCustomConfig().getString("Ranks." + getCurrentRank(uuid) + ".prefix"));
    }

    public static String getNextPrefix(String uuid) {
        return ChatColor.translateAlternateColorCodes('&', CustomConfig.getCustomConfig().getString("Ranks." + getNextRank(uuid) + ".prefix"));
    }

    public static String getRankCurrent(String uuid) {
        String rank = null;
        if (getCurrentRank(uuid).equals("WOOD_III") || getCurrentRank(uuid).equals("WOOD_II") || getCurrentRank(uuid).equals("WOOD_I")) {
            rank = ChatColor.GOLD + "" + ChatColor.BOLD + getCurrentRank(uuid);
        }

        if (getCurrentRank(uuid).equals("STONE_III") || getCurrentRank(uuid).equals("STONE_II") || getCurrentRank(uuid).equals("STONE_I")) {
            rank = ChatColor.GRAY + "" + ChatColor.BOLD + getCurrentRank(uuid);
        }

        if (getCurrentRank(uuid).equals("IRON_III") || getCurrentRank(uuid).equals("IRON_II") || getCurrentRank(uuid).equals("IRON_I")) {
            rank = ChatColor.WHITE + "" + ChatColor.BOLD + getCurrentRank(uuid);
        }

        if (getCurrentRank(uuid).equals("GOLD_III") || getCurrentRank(uuid).equals("GOLD_II") || getCurrentRank(uuid).equals("GOLD_I")) {
            rank = ChatColor.YELLOW + "" + ChatColor.BOLD + getCurrentRank(uuid);
        }

        if (getCurrentRank(uuid).equals("EMERALD_III") || getCurrentRank(uuid).equals("EMERALD_II") || getCurrentRank(uuid).equals("EMERALD_I")) {
            rank = ChatColor.GREEN + "" + ChatColor.BOLD + getCurrentRank(uuid);
        }

        if (getCurrentRank(uuid).equals("DIAMOND_III") || getCurrentRank(uuid).equals("DIAMOND_II") || getCurrentRank(uuid).equals("DIAMOND_I")) {
            rank = ChatColor.AQUA + "" + ChatColor.BOLD + getCurrentRank(uuid);
        }

        if (getCurrentRank(uuid).equals("OBSIDIAN")) {
            rank = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + getCurrentRank(uuid);
        }
        return rank;
    }

    public static String getRankNext(String uuid) {
        String rank = null;

        try {
            if (getNextRank(uuid).equals("WOOD_III") || getNextRank(uuid).equals("WOOD_II") || getNextRank(uuid).equals("WOOD_I")) {
                rank = ChatColor.GOLD + "" + ChatColor.BOLD + getNextRank(uuid);
            }

            if (getNextRank(uuid).equals("STONE_III") || getNextRank(uuid).equals("STONE_II") || getNextRank(uuid).equals("STONE_I")) {
                rank = ChatColor.GRAY + "" + ChatColor.BOLD + getNextRank(uuid);
            }

            if (getNextRank(uuid).equals("IRON_III") || getNextRank(uuid).equals("IRON_II") || getNextRank(uuid).equals("IRON_I")) {
                rank = ChatColor.WHITE + "" + ChatColor.BOLD + getNextRank(uuid);
            }

            if (getNextRank(uuid).equals("GOLD_III") || getNextRank(uuid).equals("GOLD_II") || getNextRank(uuid).equals("GOLD_I")) {
                rank = ChatColor.YELLOW + "" + ChatColor.BOLD + getNextRank(uuid);
            }

            if (getNextRank(uuid).equals("EMERALD_III") || getNextRank(uuid).equals("EMERALD_II") || getNextRank(uuid).equals("EMERALD_I")) {
                rank = ChatColor.GREEN + "" + ChatColor.BOLD + getNextRank(uuid);
            }

            if (getNextRank(uuid).equals("DIAMOND_III") || getNextRank(uuid).equals("DIAMOND_II") || getNextRank(uuid).equals("DIAMOND_I")) {
                rank = ChatColor.AQUA + "" + ChatColor.BOLD + getNextRank(uuid);
            }

            if (getNextRank(uuid).equals("OBSIDIAN")) {
                rank = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + getNextRank(uuid);
            }
        } catch (NullPointerException ignored) {
        }
        return rank;
    }
}
