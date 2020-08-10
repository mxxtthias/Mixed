package xps.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xps.Database.MySQLSetterGetter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        Player p = (Player) s;

        if (args.length == 0) {
            String uuid = p.getUniqueId().toString();

            stats(p, p.getName(), getKillStats(uuid), getDeathStats(uuid), getFlagStats(uuid), getCoreStats(uuid), getWoolStats(uuid), getMonumentStats(uuid));

        } else {
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (target == null) {
                    if (MySQLSetterGetter.playerExists(offlinePlayer.getUniqueId().toString())) {

                        String uuid = offlinePlayer.getUniqueId().toString();

                        stats(p, getName(uuid), getKillStats(uuid), getDeathStats(uuid), getFlagStats(uuid), getCoreStats(uuid), getWoolStats(uuid), getMonumentStats(uuid));

                    } else {
                        p.sendMessage("The player not found");
                    }
                } else {

                    String uuid = target.getUniqueId().toString();

                    stats(p, target.getName(), getKillStats(uuid), getDeathStats(uuid), getFlagStats(uuid), getCoreStats(uuid), getWoolStats(uuid), getMonumentStats(uuid));
                }
            }
        }
        return true;
    }

    private BigDecimal kd(int kills, int deaths) {
        BigDecimal bd1 = new BigDecimal(kills);
        BigDecimal bd2 = new BigDecimal(deaths);
        BigDecimal result = null;
        try {
            result = bd1.divide(bd2, 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException ignored) {
        }
        return result;
    }

    private void stats(Player p, String name, int kills, int deaths, int flags, int cores, int wools, int dtm) {
        p.sendMessage(ChatColor.RED + "──── " + ChatColor.DARK_AQUA + name + ChatColor.RED + " ────");
        try {
            p.sendMessage(ChatColor.DARK_PURPLE + "Kills: " + ChatColor.GOLD + kills + " " + ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.GOLD + deaths + " " + ChatColor.DARK_PURPLE + "K/D: " + ChatColor.GOLD + kd(kills, deaths).doubleValue());
        } catch (NullPointerException ignored) {
            p.sendMessage(ChatColor.DARK_PURPLE + "Kills: " + ChatColor.GOLD + 0 + " " + ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.GOLD + 0);
        }
        p.sendMessage(ChatColor.DARK_PURPLE + "Wools: " + ChatColor.GOLD + wools + " " + ChatColor.DARK_PURPLE + "Cores: " + ChatColor.GOLD + cores);
        p.sendMessage(ChatColor.DARK_PURPLE + "Flags: " + ChatColor.GOLD + flags + " " + ChatColor.DARK_PURPLE + "Monuments: " + ChatColor.GOLD + dtm);
    }

    private Integer getKillStats(String uuid) {
        return MySQLSetterGetter.getKills(uuid);
    }

    private Integer getDeathStats(String uuid) {
        return MySQLSetterGetter.getDeaths(uuid);
    }

    private Integer getFlagStats(String uuid) {
        return MySQLSetterGetter.getFlags(uuid);
    }

    private Integer getCoreStats(String uuid) {
        return MySQLSetterGetter.getCores(uuid);
    }

    private Integer getWoolStats(String uuid) {
        return MySQLSetterGetter.getWools(uuid);
    }

    private Integer getMonumentStats(String uuid) {
        return MySQLSetterGetter.getMonuments(uuid);
    }

    private String getName(String uuid) {
        return MySQLSetterGetter.getName(uuid);
    }
}