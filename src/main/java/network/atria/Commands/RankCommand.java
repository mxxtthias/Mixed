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
import network.atria.Ranks.Rank;
import network.atria.Ranks.RankManager;
import org.bukkit.entity.Player;

public class RankCommand {

  @Command(
      aliases = {"rank"},
      desc = "Show Currently your Rank and Points")
  public void rank(@Sender Player sender) {
    RankManager rankManager = new RankManager();
    UUID uuid = sender.getUniqueId();
    Rank NOW = rankManager.getRank(MySQLSetterGetter.getRank(uuid));
    Rank NEXT = rankManager.getNextRank(uuid);

    TextComponent points =
        TextComponent.ofChildren(
            Component.text("Rank: ", NamedTextColor.DARK_AQUA),
            NOW.getColoredName(),
            Component.newline(),
            Component.text("Your current points are ", NamedTextColor.DARK_AQUA),
            Component.text(
                MySQLSetterGetter.getPoints(uuid), NamedTextColor.AQUA, TextDecoration.BOLD));

    Mixed.get().getAudience().player(sender).sendMessage(points);

    if (NEXT.getName().equalsIgnoreCase(NOW.getName())) {
      TextComponent rejected =
          TextComponent.ofChildren(
              Component.text("You can't rank up because you're ", NamedTextColor.RED),
              NOW.getColoredName());
      Mixed.get().getAudience().player(sender).sendMessage(rejected);
    } else {
      TextComponent NEED =
          TextComponent.ofChildren(
              Component.text("You need ", NamedTextColor.DARK_AQUA),
              Component.text(
                  String.valueOf(rankManager.getRequirePoint(uuid)),
                  NamedTextColor.AQUA,
                  TextDecoration.BOLD),
              Component.text(" more points to be ", NamedTextColor.DARK_AQUA),
              NEXT.getColoredName());

      Mixed.get().getAudience().player(sender).sendMessage(NEED);
    }
  }
}
