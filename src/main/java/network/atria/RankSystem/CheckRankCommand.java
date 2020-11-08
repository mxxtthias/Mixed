package network.atria.RankSystem;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.util.UUID;
import network.atria.Database.MySQLSetterGetter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CheckRankCommand {

  @Command(
      aliases = {"rank", "check"},
      desc = "Show Currently Rank Points")
  public void rank(@Sender Player sender) {

    final UUID uuid = sender.getUniqueId();
    final String current = MySQLSetterGetter.getRank(uuid);
    final String next = Ranks.getNextRank(uuid);

    sender.sendMessage(
        ChatColor.DARK_AQUA + "Rank: " + ChatColor.AQUA + current.replace("_", " ").toUpperCase());
    sender.sendMessage(
        ChatColor.DARK_AQUA
            + "Your current points are "
            + ChatColor.AQUA
            + ""
            + ChatColor.BOLD
            + Ranks.getCurrentPoint(uuid));
    if (next.equalsIgnoreCase(current)) {
      sender.sendMessage(
          ChatColor.DARK_AQUA + "You can't ranked up because you're " + current.toUpperCase());
    } else {
      sender.sendMessage(
          ChatColor.DARK_AQUA
              + "You need "
              + ChatColor.AQUA
              + Ranks.getRequirePoint(uuid)
              + ChatColor.DARK_AQUA
              + " more points to be "
              + Ranks.getRankNext(uuid));
    }
  }
}
