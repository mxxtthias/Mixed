package network.atria.Discord;

import java.awt.*;
import net.dv8tion.jda.api.EmbedBuilder;
import network.atria.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Discord implements Listener {

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    String name = event.getPlayer().getName();
    String headURL = "http://cravatar.eu/helmavatar/" + name;

    EmbedBuilder embed = new EmbedBuilder();
    embed.setAuthor(name + " joined the server", null, headURL);
    embed.setColor(new Color(34, 178, 76));
    Main.getInstance()
        .getGuild()
        .getTextChannelById("741629295486697544")
        .sendMessage(embed.build())
        .queue();
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    String name = event.getPlayer().getName();
    String headURL = "http://cravatar.eu/helmavatar/" + name;

    EmbedBuilder embed = new EmbedBuilder();
    embed.setAuthor(name + " left the server", null, headURL);
    embed.setColor(Color.RED);
    Main.getInstance()
        .getGuild()
        .getTextChannelById("741629295486697544")
        .sendMessage(embed.build())
        .queue();
  }
}
