package network.atria.Commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Mixed;
import network.atria.RankSystem.Rank;
import network.atria.RankSystem.Ranks;
import org.bukkit.entity.Player;

public class CheckRankCommand {

  @Command(
      aliases = {"rank"},
      desc = "Show Currently Effect Points")
  public void rank(@Sender Player sender) {
    Ranks ranks = new Ranks();

    UUID uuid = sender.getUniqueId();
    Rank now = ranks.getRank(MySQLSetterGetter.getRank(uuid));
    Rank next = ranks.getNextRank(uuid);

    TextComponent points =
        TextComponent.ofChildren(
            Component.text("Rank: ", NamedTextColor.DARK_AQUA),
            now.getColoredName(),
            Component.newline(),
            Component.text("Your current points are ", NamedTextColor.DARK_AQUA),
            Component.text(
                String.valueOf(now.getPoint()), NamedTextColor.AQUA, TextDecoration.BOLD));

    Mixed.get().getAudience().player(sender).sendMessage(points);

    if (next.getName().equalsIgnoreCase(now.getName())) {
      TextComponent rejected =
          TextComponent.ofChildren(
              Component.text("You can't rank up because you're "), now.getColoredName());
      Mixed.get().getAudience().player(sender).sendMessage(rejected);
    } else {
      TextComponent need =
          TextComponent.ofChildren(
              Component.text("You need ", NamedTextColor.DARK_AQUA),
              Component.text(
                  String.valueOf(ranks.getRequirePoint(uuid)),
                  NamedTextColor.AQUA,
                  TextDecoration.BOLD),
              Component.text(" more points to be ", NamedTextColor.DARK_AQUA),
              ranks.getNextRank(uuid).getColoredName());

      Mixed.get().getAudience().player(sender).sendMessage(need);
    }
  }
}
