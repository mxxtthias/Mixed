package network.atria.Commands;

import static net.kyori.adventure.text.Component.text;
import static network.atria.MySQL.SQLQuery.*;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import network.atria.Mixed;
import network.atria.MySQL;
import network.atria.UserProfile.UserProfile;
import network.atria.Util.Fetcher;
import network.atria.Util.TextFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tc.oc.pgm.api.PGM;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.util.LegacyFormatUtils;

public class StatsCommand {

  @Command(
      aliases = {"stats"},
      desc = "Show Player Stats",
      usage = "[Target]")
  public void stats(@Sender Player player, @Nullable String playerName) {
    MatchPlayer matchPlayer = PGM.get().getMatchManager().getPlayer(player);
    if (matchPlayer == null) return;
    if (playerName == null) {
      showStats(matchPlayer.getId(), player);
    } else if (Bukkit.getPlayer(playerName) != null) {
      MatchPlayer target = PGM.get().getMatchManager().getPlayer(Bukkit.getPlayer(playerName));
      if (target != null) showStats(target.getId(), player);
    } else if (MySQL.SQLQuery.playerExists(Fetcher.getUUID(playerName))) {
      showStats(Fetcher.getUUID(playerName), player);
    } else {
      Audience audience = Mixed.get().getAudience().player(matchPlayer.getId());
      audience.sendMessage(Component.text("The player not found", NamedTextColor.RED));
    }
  }

  private void showStats(UUID uuid, Player sender) {
    Map<String, Integer> stats;
    MatchPlayer player =
        PGM.get().getMatchManager().getPlayer(uuid) != null
            ? PGM.get().getMatchManager().getPlayer(uuid)
            : null;
    String prefixedName =
        player != null
            ? player.getPrefixedName()
            : TextFormat.format(text(Fetcher.getName(uuid), NamedTextColor.DARK_AQUA));
    Audience audience = Mixed.get().getAudience().player(sender);
    UserProfile profile = Mixed.get().getProfileManager().getProfile(uuid);

    if (profile != null) {
      audience.sendMessage(
          text(LegacyFormatUtils.horizontalLineHeading(prefixedName, ChatColor.WHITE)));
      audience.sendMessage(formatStats("Kills: ", profile.getKills()));
      audience.sendMessage(formatStats("Deaths: ", profile.getDeaths()));
      audience.sendMessage(
          text("K/D: ", NamedTextColor.AQUA)
              .append(
                  text(
                      kd(profile.getKills(), profile.getDeaths()).doubleValue(),
                      NamedTextColor.BLUE)));
      audience.sendMessage(formatStats("Wool Placed: ", profile.getWools()));
      audience.sendMessage(formatStats("Cores Leaked: ", profile.getCores()));
      audience.sendMessage(formatStats("Monuments Destroyed: ", profile.getMonuments()));
      audience.sendMessage(formatStats("Flag Captured: ", profile.getFlags()));
    } else {
      stats = getStats(uuid);
      audience.sendMessage(
          text(LegacyFormatUtils.horizontalLineHeading(prefixedName, ChatColor.WHITE)));
      audience.sendMessage(formatStats("Kills: ", stats.get("KILLS")));
      audience.sendMessage(formatStats("Deaths: ", stats.get("DEATHS")));
      audience.sendMessage(
          text("KDR: ", NamedTextColor.AQUA)
              .append(
                  text(
                      kd(stats.get("KILLS"), stats.get("DEATHS")).doubleValue(),
                      NamedTextColor.BLUE)));
      audience.sendMessage(formatStats("Wool Placed: ", stats.get("WOOLS")));
      audience.sendMessage(formatStats("Cores Leaked: ", stats.get("CORES")));
      audience.sendMessage(formatStats("Monuments Destroyed: ", stats.get("MONUMENTS")));
      audience.sendMessage(formatStats("Flag Captured: ", stats.get("FLAGS")));
    }
    audience.sendMessage(text(LegacyFormatUtils.horizontalLine(ChatColor.WHITE, 300)));
  }

  private TextComponent formatStats(String ladder, int value) {
    return text()
        .append(text(ladder, NamedTextColor.AQUA))
        .append(text(value, NamedTextColor.BLUE))
        .build();
  }

  private BigDecimal kd(int kills, int deaths) {
    BigDecimal bd1 = new BigDecimal(kills);
    BigDecimal bd2 = new BigDecimal(deaths);
    BigDecimal result;
    try {
      result = bd1.divide(bd2, 2, RoundingMode.HALF_UP);
    } catch (ArithmeticException e) {
      result = BigDecimal.ZERO;
    }
    return result;
  }

  private Map<String, Integer> getStats(UUID uuid) {
    Map<String, Integer> stats = Maps.newHashMap();
    Connection connection = null;
    ResultSet rs = null;
    PreparedStatement statement = null;
    String query =
        "SELECT KILLS, DEATHS, CORES, WOOLS, MONUMENTS, FLAGS, WINS, LOSES FROM STATS WHERE UUID = ?";
    try {
      connection = MySQL.get().getHikari().getConnection();
      statement = connection.prepareStatement(query);
      statement.setString(1, uuid.toString());
      rs = statement.executeQuery();
      if (rs.next()) {
        stats.put("KILLS", rs.getInt("KILLS"));
        stats.put("DEATHS", rs.getInt("DEATHS"));
        stats.put("CORES", rs.getInt("CORES"));
        stats.put("WOOLS", rs.getInt("WOOLS"));
        stats.put("MONUMENTS", rs.getInt("MONUMENTS"));
        stats.put("FLAGS", rs.getInt("FLAGS"));
        stats.put("WINS", rs.getInt("WINS"));
        stats.put("LOSES", rs.getInt("LOSES"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeConnection(connection);
      closeStatement(statement);
      closeResultSet(rs);
    }
    return stats;
  }
}
