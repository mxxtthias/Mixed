package network.atria.Commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Mixed;
import network.atria.RankSystem.Ranks;
import org.bukkit.entity.Player;

public class CheckRankCommand {

  @Command(
      aliases = {"rank"},
      desc = "Show Currently Effect Points")
  public void rank(@Sender Player sender) {

    UUID uuid = sender.getUniqueId();
    String current = Ranks.getRankCurrent(uuid);
    String next = Ranks.getNextRank(uuid);
    int point = Ranks.getCurrentPoint(uuid);

    TextComponent points =
        TextComponent.ofChildren(
            Component.text("Effect: ", NamedTextColor.DARK_AQUA),
            Component.text(current),
            Component.newline(),
            Component.text("Your current points are ", NamedTextColor.DARK_AQUA),
            Component.text(String.valueOf(point), NamedTextColor.AQUA, TextDecoration.BOLD));

    Mixed.get().getAudience().player(sender).sendMessage(points);

    if (next.equalsIgnoreCase(current)) {
      TextComponent rejected =
          TextComponent.ofChildren(
              Component.text("You can't rank up because you're "),
              Component.text(current.toUpperCase(), NamedTextColor.DARK_AQUA));
      Mixed.get().getAudience().player(sender).sendMessage(rejected);
    } else {
      TextComponent need =
          TextComponent.ofChildren(
              Component.text("You need ", NamedTextColor.DARK_AQUA),
              Component.text(
                  String.valueOf(Ranks.getRequirePoint(uuid)),
                  NamedTextColor.AQUA,
                  TextDecoration.BOLD),
              Component.text(" more points to be ", NamedTextColor.DARK_AQUA),
              Component.text(Ranks.getRankNext(uuid), NamedTextColor.DARK_AQUA));

      Mixed.get().getAudience().player(sender).sendMessage(need);
    }
  }
}
