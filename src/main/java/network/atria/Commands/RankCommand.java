package network.atria.Commands;

import static net.kyori.adventure.text.Component.text;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Manager.RankManager;
import network.atria.Mixed;
import network.atria.Ranks.Rank;
import network.atria.UserProfile.UserProfile;
import org.bukkit.entity.Player;

public class RankCommand {

  @Command(
      aliases = {"rank"},
      desc = "Show Currently your Rank and Points")
  public void rank(@Sender Player sender) {
    UUID uuid = sender.getUniqueId();
    RankManager rankManager = Mixed.get().getRankManager();
    Rank NEXT = rankManager.getNextRank(uuid);
    Audience audience = Mixed.get().getAudience().player(sender);
    UserProfile profile = Mixed.get().getProfileManager().getProfile(uuid);

    audience.sendMessage(
        text("Rank: ", NamedTextColor.DARK_AQUA).append(profile.getRank().getColoredName()));
    audience.sendMessage(
        text()
            .append(text("You now have ", NamedTextColor.DARK_AQUA))
            .append(text(profile.getPoints(), NamedTextColor.AQUA, TextDecoration.BOLD))
            .append(text(" points", NamedTextColor.DARK_AQUA)));

    if (NEXT.getName().equalsIgnoreCase(profile.getRank().getName())) {
      audience.sendMessage(Component.text("You cannot rank up now!", NamedTextColor.RED));
    } else {
      audience.sendMessage(
          text()
              .append(text("You need ", NamedTextColor.DARK_AQUA))
              .append(
                  text(rankManager.getRequirePoint(uuid), NamedTextColor.AQUA, TextDecoration.BOLD))
              .append(text(" more points to be ", NamedTextColor.DARK_AQUA))
              .append(NEXT.getColoredName()));
    }
  }
}
