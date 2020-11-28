package network.atria.RankSystem;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CheckRankCommand {

  @Command(
      aliases = {"rank", "check"},
      desc = "Show Currently Rank Points")
  public void rank(@Sender Player sender) {

    final UUID uuid = sender.getUniqueId();
    final String current = Ranks.getRankCurrent(uuid);
    final String next = Ranks.getNextRank(uuid);
    final int point = Ranks.getCurrentPoint(uuid);

    final String line_1 = ChatColor.DARK_AQUA + "Rank: " + ChatColor.AQUA + current;
    final String line_2 =
        ChatColor.DARK_AQUA
            + "Your current points are "
            + ChatColor.AQUA
            + ""
            + ChatColor.BOLD
            + point;

    sender.sendMessage(line_1 + "\n" + line_2);

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
