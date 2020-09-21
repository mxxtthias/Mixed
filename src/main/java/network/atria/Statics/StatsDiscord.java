package network.atria.Statics;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import network.atria.Database.MySQLSetterGetter;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class StatsDiscord extends ListenerAdapter {

  @Override
  public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

    String[] command = event.getMessage().getContentRaw().split(" ");

    if (event.getChannel().getId().equalsIgnoreCase("756818564429643826")) {
      if (command.length == 2) {
        if (command[0].equalsIgnoreCase("/stats")
            && MySQLSetterGetter.playerExists(getUUID(command[1]))) {
          EmbedStats(event.getChannel(), command[1]);
        } else {
          event.getChannel().sendMessage("The player not found").queue();
        }
      }
    }
    super.onGuildMessageReceived(event);
  }

  private String getUUID(String playerName) {
    String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
    String UUID = null;
    try {
      String UUIDJson = IOUtils.toString(new URL(url));

      JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);

      StringBuilder sb = new StringBuilder(UUIDObject.get("id").toString());
      sb.insert(8, "-");
      sb.insert(13, "-");
      sb.insert(18, "-");
      sb.insert(23, "-");

      UUID = sb.toString();
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }

    return UUID;
  }

  private void EmbedStats(TextChannel channel, String playerName) {
    EmbedBuilder embed = new EmbedBuilder();
    String headURL = "http://cravatar.eu/helmavatar/" + playerName + "/128.png";

    embed.setThumbnail(headURL);
    embed.setColor(new Color(252, 68, 68));
    embed.setDescription("**" + playerName + "**'s Atria Network statistics");
    embed.addField("Kills", MySQLSetterGetter.getKills(getUUID(playerName)).toString(), true);
    embed.addField("Deaths", MySQLSetterGetter.getDeaths(getUUID(playerName)).toString(), true);
    embed.addField(
        "K/D",
        String.valueOf(
            kd(
                    MySQLSetterGetter.getKills(getUUID(playerName)),
                    MySQLSetterGetter.getDeaths(getUUID(playerName)))
                .doubleValue()),
        true);
    embed.addField("Rank", MySQLSetterGetter.getRank(getUUID(playerName)), true);
    embed.addField("Points", MySQLSetterGetter.getPoints(getUUID(playerName)).toString(), true);

    channel.sendMessage(embed.build()).queue();
  }

  private BigDecimal kd(int kills, int deaths) {
    BigDecimal bd1 = new BigDecimal(kills);
    BigDecimal bd2 = new BigDecimal(deaths);
    BigDecimal result = null;
    try {
      result = bd1.divide(bd2, 2, RoundingMode.HALF_UP);
    } catch (ArithmeticException ignored) {
    }
    return result;
  }
}
