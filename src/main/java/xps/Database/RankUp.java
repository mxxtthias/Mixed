package xps.Database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.events.PlayerJoinMatchEvent;
import xps.Main;

import java.util.*;

public class RankUp implements Listener {

    public final Map<Integer, String> ranks = new HashMap<>();

    private void setRanks() {
        FileConfiguration config = Main.getInstance().getConfig();
        // WOOD Rank
        ranks.put(config.getInt("Ranks.WOOD.III.Points"), config.getString("Ranks.WOOD.III"));
        ranks.put(config.getInt("Ranks.WOOD.II.Points"), config.getString("Ranks.WOOD.II"));
        ranks.put(config.getInt("Ranks.WOOD.I.Points"), config.getString("Ranks.WOOD.I"));

        // STONE Rank

        ranks.put(config.getInt("Ranks.STONE.III.Points"), config.getString("Ranks.STONE.III"));
        ranks.put(config.getInt("Ranks.STONE.II.Points"), config.getString("Ranks.STONE.II"));
        ranks.put(config.getInt("Ranks.STONE.I.Points"), config.getString("Ranks.STONE.I"));

        // IRON Rank
        ranks.put(config.getInt("Ranks.IRON.III.Points"), config.getString("Ranks.IRON.III"));
        ranks.put(config.getInt("Ranks.IRON.II.Points"), config.getString("Ranks.IRON.II"));
        ranks.put(config.getInt("Ranks.IRON.I.Points"), config.getString("Ranks.IRON.I"));

        // GOLD Rank
        ranks.put(config.getInt("Ranks.GOLD.III.Points"), config.getString("Ranks.GOLD.III"));
        ranks.put(config.getInt("Ranks.GOLD.II.Points"), config.getString("Ranks.GOLD.II"));
        ranks.put(config.getInt("Ranks.GOLD.I.Points"), config.getString("Ranks.GOLD.I"));

        // EMERALD Rank
        ranks.put(config.getInt("Ranks.EMERALD.III.Points"), config.getString("Ranks.EMERALD.III"));
        ranks.put(config.getInt("Ranks.EMERALD.II.Points"), config.getString("Ranks.EMERALD.II"));
        ranks.put(config.getInt("Ranks.EMERALD.I.Points"), config.getString("Ranks.EMERALD.I"));

        // Diamond Rank
        ranks.put(config.getInt("Ranks.DIAMOND.III.Points"), config.getString("Ranks.DIAMOND.III"));
        ranks.put(config.getInt("Ranks.DIAMOND.II.Points"), config.getString("Ranks.DIAMOND.II"));
        ranks.put(config.getInt("Ranks.DIAMOND.I.Points"), config.getString("Ranks.DIAMOND.I"));

        // OBSIDIAN Rank
        ranks.put(config.getInt("Ranks.OBSIDIAN.Points"), config.getString("Ranks.OBSIDIAN"));
    }

    public void createTable() {
        Main.mysql.update("CREATE TABLE IF NOT EXISTS RANK(UUID varchar(36), POINTS int, NAME varchar(16), RANK varchar(12));");
    }

    public boolean canRankup(String uuid, int rankPoints) {
        int points = MySQLSetterGetter.getPoints(uuid);
        int listPoints = Integer.parseInt(ranks.get(rankPoints));

        return points >= listPoints;
    }

    public void rankUP(String uuid, int needPoints, Player player) {
        String nowRank = MySQLSetterGetter.getRank(uuid);

        if (canRankup(uuid, needPoints)) {
            Iterator<String> now = ranks.values().iterator();
            String nextRank = now.next();
            if (ranks.containsValue(nowRank)) {
                if (!getNextRank().equals(nowRank)) {
                    MySQLSetterGetter.setRank(uuid, nextRank);
                    MySQLSetterGetter.addPoints(uuid, 5);
                    player.sendMessage(ChatColor.AQUA + "You're up in the ranks!");
                    Bukkit.broadcastMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.AQUA + " has moved up in the ranks to " + ChatColor.GOLD + nextRank);
                }
            } else {
                
                player.sendMessage("You can't rank up.");
            }
        }
    }

    private String setColorRank(Player player) {
        String nowRank = MySQLSetterGetter.getRank(player.getUniqueId().toString());
        String prefix = "";

        for(String wood : Main.getInstance().getConfig().getConfigurationSection("Ranks.WOOD").getKeys(false)) {
            if(nowRank.equals(wood)) {
                prefix = ChatColor.GRAY + wood;
            }
        }
        for(String stone : Main.getInstance().getConfig().getConfigurationSection("Ranks.STONE").getKeys(false)) {
            if(nowRank.equals(stone)) {
                prefix = ChatColor.GRAY + stone;
            }
        }
        for(String iron : Main.getInstance().getConfig().getConfigurationSection("Ranks.IRON").getKeys(false)) {
            if(nowRank.equals(iron)) {
                prefix = ChatColor.WHITE + iron;
            }
        }
        for(String gold : Main.getInstance().getConfig().getConfigurationSection("Ranks.GOLD").getKeys(false)) {
            if(nowRank.equals(gold)) {
                prefix = ChatColor.YELLOW + gold;
            }
        }
        for(String emerald : Main.getInstance().getConfig().getConfigurationSection("Ranks.EMERALD").getKeys(false)) {
            if(nowRank.equals(emerald)) {
                prefix = ChatColor.GREEN + emerald;
            }
        }
        for(String diamond : Main.getInstance().getConfig().getConfigurationSection("Ranks.DIAMOND").getKeys(false)) {
            if(nowRank.equals(diamond)) {
                prefix = ChatColor.AQUA + diamond;
            }
        }
        if(nowRank.equals(Main.getInstance().getConfig().getString("Ranks.OBSIDIAN"))) {
            String obsidian = Main.getInstance().getConfig().getString("Ranks.OBSIDIAN");
            prefix = ChatColor.DARK_PURPLE + obsidian;
        }
        return prefix;
    }


    private String getNextRank() {
        Iterator<String> now = ranks.values().iterator();
        return now.next();
    }


    @EventHandler
    public void onNotice(PlayerJoinMatchEvent e) {
        MatchPlayer player = e.getPlayer();
        if (canRankup(player.getId().toString(), MySQLSetterGetter.getPoints(player.getId().toString()))) {
            player.sendMessage( ChatColor.AQUA + "You can rank up! Just type" + ChatColor.DARK_AQUA + "/rankup");
        }
    }

    @EventHandler
    public void chatPrefix(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        e.setFormat(setColorRank(p) + e.getFormat());
    }

    public RankUp(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        createTable();
        setRanks();
    }
}
