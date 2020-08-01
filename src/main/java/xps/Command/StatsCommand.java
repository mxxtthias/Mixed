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
            int deaths = MySQLSetterGetter.getDeaths(p.getUniqueId().toString());
            int kills = MySQLSetterGetter.getKills(p.getUniqueId().toString());
            int flags = MySQLSetterGetter.getFlags(p.getUniqueId().toString());
            int cores = MySQLSetterGetter.getCores(p.getUniqueId().toString());
            int wools = MySQLSetterGetter.getWools(p.getUniqueId().toString());
            int dtm = MySQLSetterGetter.getMonuments(p.getUniqueId().toString());

            stats(p, p.getName(), kills, deaths, flags, cores, wools, dtm);

        } else {
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (target == null) {
                    if (MySQLSetterGetter.playerExists(offlinePlayer.getUniqueId().toString())) {
                        int deaths = MySQLSetterGetter.getDeaths(offlinePlayer.getUniqueId().toString());
                        int kills = MySQLSetterGetter.getKills(offlinePlayer.getUniqueId().toString());
                        int flags = MySQLSetterGetter.getFlags(offlinePlayer.getUniqueId().toString());
                        int cores = MySQLSetterGetter.getCores(offlinePlayer.getUniqueId().toString());
                        int wools = MySQLSetterGetter.getWools(offlinePlayer.getUniqueId().toString());
                        int dtm = MySQLSetterGetter.getMonuments(offlinePlayer.getUniqueId().toString());
                        String name = MySQLSetterGetter.getName(offlinePlayer.getUniqueId().toString());

                        stats(p, name, kills, deaths, flags, cores, wools, dtm);

                    } else {
                        p.sendMessage("The player not found");
                    }
                } else {
                    int deaths = MySQLSetterGetter.getDeaths(target.getUniqueId().toString());
                    int kills = MySQLSetterGetter.getKills(target.getUniqueId().toString());
                    int flags = MySQLSetterGetter.getFlags(target.getUniqueId().toString());
                    int cores = MySQLSetterGetter.getCores(target.getUniqueId().toString());
                    int wools = MySQLSetterGetter.getWools(target.getUniqueId().toString());
                    int dtm = MySQLSetterGetter.getMonuments(target.getUniqueId().toString());

                    stats(p, target.getName(), kills, deaths, flags, cores, wools, dtm);
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
        p.sendMessage(ChatColor.DARK_PURPLE + "Kills: " + ChatColor.GOLD + kills + " " + ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.GOLD + deaths + " " + ChatColor.DARK_PURPLE + "K/D: " + ChatColor.GOLD + kd(kills, deaths).doubleValue());
        p.sendMessage(ChatColor.DARK_PURPLE + "Wools: " + ChatColor.GOLD + wools + " " + ChatColor.DARK_PURPLE + "Cores: " + ChatColor.GOLD + cores);
        p.sendMessage(ChatColor.DARK_PURPLE + "Flags: " + ChatColor.GOLD + flags + " " + ChatColor.DARK_PURPLE + "Monuments: " + ChatColor.GOLD + dtm);
    }
}