package xps.RankSystem;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckRankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        Player player = (Player) s;
        String uuid = player.getUniqueId().toString();
        if (args.length == 0) {
            s.sendMessage(ChatColor.DARK_AQUA + "Rank: " + Ranks.getRankCurrent(uuid).replace("_", " ").toUpperCase());
            s.sendMessage(ChatColor.DARK_AQUA + "Your current points are " + ChatColor.AQUA + "" + ChatColor.BOLD + Ranks.getCurrentPoint(uuid));
            s.sendMessage(ChatColor.DARK_AQUA + "You need " + ChatColor.AQUA + Ranks.getRequirePoint(uuid) + ChatColor.DARK_AQUA + " more points to be " + Ranks.getRankNext(uuid));
        }
        return true;
    }
}

