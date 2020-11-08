package network.atria.Commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import network.atria.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ServerInfoCommand {

  @Command(
      aliases = {"discord"},
      desc = "Send Atria Network's Discord Link")
  public void discord(@Sender Player sender) {
    sender.sendMessage(
        ChatColor.DARK_AQUA
            + ""
            + ChatColor.BOLD
            + "Discord: "
            + ChatColor.AQUA
            + "https://discord.gg/X3F6S6m");
  }

  @Command(
      aliases = {"site", "website"},
      desc = "Send Atria Network's Webiste Link")
  public void site(@Sender Player sender) {
    sender.sendMessage(
        ChatColor.DARK_AQUA
            + ""
            + ChatColor.BOLD
            + "Website: "
            + ChatColor.AQUA
            + "https://atria.network/");
  }

  @Command(
      aliases = {"uptime"},
      desc = "Show Server uptime")
  public void uptime(@Sender Player sender) {
    sender.sendMessage(ChatColor.GREEN + "Server Uptime: " + getUptime());
  }

  private String getUptime() {
    long now = System.currentTimeMillis();
    long diff = now - Main.getInstance().getUptime();
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
