package network.atria.Commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
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
    Audience audience = Mixed.get().getAudience().player(sender);

    audience.sendMessage(
        TextComponent.ofChildren(
            Component.text("Rank: ", NamedTextColor.DARK_AQUA), NOW.getColoredName()));
    audience.sendMessage(
        TextComponent.ofChildren(
            Component.text("Your current points are ", NamedTextColor.DARK_AQUA),
            Component.text(
                MySQLSetterGetter.getPoints(uuid), NamedTextColor.AQUA, TextDecoration.BOLD)));

    if (NEXT.getName().equalsIgnoreCase(NOW.getName())) {
      TextComponent.Builder rejected = Component.text();
      rejected.append(Component.text("You can't rank up because you're ", NamedTextColor.RED));
      rejected.append(NOW.getColoredName());

      Mixed.get().getAudience().player(sender).sendMessage(rejected.build());
    } else {
      TextComponent.Builder NEED = Component.text();
      NEED.append(Component.text("You need ", NamedTextColor.DARK_AQUA));
      NEED.append(
          Component.text(
              String.valueOf(rankManager.getRequirePoint(uuid)),
              NamedTextColor.AQUA,
              TextDecoration.BOLD));
      NEED.append(Component.text(" more points to be ", NamedTextColor.DARK_AQUA));
      NEED.append(NEXT.getColoredName());

      Mixed.get().getAudience().player(sender).sendMessage(NEED.build());
    }
  }
}
