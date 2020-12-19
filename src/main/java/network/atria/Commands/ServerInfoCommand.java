package network.atria.Commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Mixed;
import org.bukkit.entity.Player;

public class ServerInfoCommand {

  @Command(
      aliases = {"discord"},
      desc = "Send Atria Network's Discord Link")
  public void discord(@Sender Player sender) {
    TextComponent component =
        TextComponent.ofChildren(
            Component.text("Discord: ", NamedTextColor.DARK_AQUA, TextDecoration.BOLD),
            Component.text("https://discord.gg/X3F6S6m", NamedTextColor.AQUA, TextDecoration.BOLD));
    Mixed.get().getAudience().player(sender).sendMessage(component);
  }

  @Command(
      aliases = {"site", "website"},
      desc = "Send Atria Network's Website Link")
  public void site(@Sender Player sender) {
    TextComponent component =
        TextComponent.ofChildren(
            Component.text("Website: ", NamedTextColor.DARK_AQUA, TextDecoration.BOLD),
            Component.text("https://atria.network/", NamedTextColor.AQUA, TextDecoration.BOLD));
    Mixed.get().getAudience().player(sender).sendMessage(component);
  }

  @Command(
      aliases = {"uptime"},
      desc = "Show Server uptime")
  public void uptime(@Sender Player sender) {
    TextComponent component =
        TextComponent.ofChildren(
            Component.text("Server Uptime: ", NamedTextColor.GREEN),
            Component.text(getUptime(), NamedTextColor.GREEN));
    Mixed.get().getAudience().player(sender).sendMessage(component);
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
