package xps.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

            String mouments = ChatColor.DARK_PURPLE + "Wools: " + ChatColor.GOLD + wools + " " + ChatColor.DARK_PURPLE + "Cores: " + ChatColor.GOLD + cores + " " + ChatColor.DARK_PURPLE + "Flags: " + ChatColor.GOLD + flags;
            try {
                BigDecimal bd1 = new BigDecimal(String.valueOf(kills));
                BigDecimal bd2 = new BigDecimal(String.valueOf(deaths));
                BigDecimal kd =(bd1.divide(bd2,2, RoundingMode.HALF_UP));

                p.sendMessage(ChatColor.RED + "──── " + ChatColor.DARK_AQUA + p.getName() + ChatColor.RED + " ────");
                p.sendMessage(ChatColor.DARK_PURPLE + "Kills: " + ChatColor.GOLD + kills + " " + ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.GOLD + deaths + " " + ChatColor.DARK_PURPLE + "K/D: " + ChatColor.GOLD + kd.doubleValue());
                p.sendMessage(" ");
                p.sendMessage(mouments);

            } catch (ArithmeticException e) {
                p.sendMessage(ChatColor.RED + "──── " + ChatColor.DARK_AQUA + p.getName() + ChatColor.RED + " ────");
                p.sendMessage(ChatColor.DARK_PURPLE + "Kills: " + ChatColor.GOLD + kills + " " + ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.GOLD + deaths);
                p.sendMessage(" ");
                p.sendMessage(mouments);
            }
        } else {
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    p.sendMessage("そのプレイヤーは現在オンラインではありません");
                } else {

                    int deaths_2 = MySQLSetterGetter.getDeaths(target.getUniqueId().toString());
                    int kills_2 = MySQLSetterGetter.getKills(target.getUniqueId().toString());
                    int flags_2 = MySQLSetterGetter.getFlags(target.getUniqueId().toString());
                    int cores_2 = MySQLSetterGetter.getCores(target.getUniqueId().toString());
                    int wools_2 = MySQLSetterGetter.getWools(target.getUniqueId().toString());

                    String monuments = ChatColor.DARK_PURPLE + "Wools: " + ChatColor.GOLD + wools_2 + " " + ChatColor.DARK_PURPLE + "Cores: " + ChatColor.GOLD + cores_2 + " " + ChatColor.DARK_PURPLE + "Flags: " + ChatColor.GOLD + flags_2;
                    try {
                        BigDecimal bd1 = new BigDecimal(String.valueOf(kills_2));
                        BigDecimal bd2 = new BigDecimal(String.valueOf(deaths_2));
                        BigDecimal kd_2 =(bd1.divide(bd2,2, RoundingMode.HALF_UP));


                        p.sendMessage(ChatColor.RED + "──── " + ChatColor.DARK_AQUA + target.getName() + ChatColor.RED + " ────");
                        p.sendMessage(ChatColor.DARK_PURPLE + "Kills: " + ChatColor.GOLD + kills_2 + " " + ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.GOLD + deaths_2 + " " + ChatColor.DARK_PURPLE + "K/D: " + ChatColor.GOLD + kd_2.doubleValue());
                        p.sendMessage(" ");
                        p.sendMessage(monuments);
                    } catch (ArithmeticException e) {
                        p.sendMessage(ChatColor.RED + "──── " + ChatColor.DARK_AQUA + target.getName() + ChatColor.RED + " ────");
                        p.sendMessage(ChatColor.DARK_PURPLE + "Kills: " + ChatColor.GOLD + kills_2 + " " + ChatColor.DARK_PURPLE + "Deaths: " + ChatColor.GOLD + deaths_2);
                        p.sendMessage(" ");
                        p.sendMessage(monuments);
                    }
                }
            }
        }
        return true;
    }
}