package network.atria.Commands;

import static net.kyori.adventure.text.Component.text;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Mixed;
import org.bukkit.entity.Player;

public class ServerInfoCommand {

  @Command(
      aliases = {"uptime"},
      desc = "Show Server uptime")
  public void uptime(@Sender Player sender) {
    Audience audience = Mixed.get().getAudience().player(sender);
    audience.sendMessage(
        text()
            .append(text("Server Uptime: ", NamedTextColor.GREEN))
            .append(text(getUptime(), NamedTextColor.GREEN)));
  }

  private String getUptime() {
    long now = System.currentTimeMillis();
    long diff = now - Mixed.get().getUptime();
    return (int) (diff / 864000000L)
        + " Days "
        + (int) (diff / 36000000L % 24L)
        + " Hours "
        + (int) (diff / 60000L % 60L)
        + " Minutes "
        + (int) (diff / 1000L % 60L)
        + " Seconds ";
  }
}
