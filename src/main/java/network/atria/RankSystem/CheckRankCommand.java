package network.atria.RankSystem;

import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckRankCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

    final Player player = (Player) s;
    final UUID uuid = player.getUniqueId();
    final String current = Ranks.getCurrentRank(uuid);
    final String next = Ranks.getNextRank(uuid);

    if (args.length == 0) {
      player.sendMessage(
          ChatColor.DARK_AQUA
              + "Rank: "
              + ChatColor.AQUA
              + current.replace("_", " ").toUpperCase());
      player.sendMessage(
          ChatColor.DARK_AQUA
              + "Your current points are "
              + ChatColor.AQUA
              + ""
              + ChatColor.BOLD
              + Ranks.getCurrentPoint(uuid));
      if (next.equalsIgnoreCase(current)) {
        player.sendMessage(
            ChatColor.DARK_AQUA + "You can't ranked up because you're " + current.toUpperCase());
      } else {
        player.sendMessage(
            ChatColor.DARK_AQUA
                + "You need "
                + ChatColor.AQUA
                + Ranks.getRequirePoint(uuid)
                + ChatColor.DARK_AQUA
                + " more points to be "
                + Ranks.getRankNext(uuid));
      }
    }
    return true;
  }
}
