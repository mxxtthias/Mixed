package network.atria.Commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.annotation.Nullable;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import network.atria.Database.MySQL;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Mixed;
import network.atria.Util.UUIDFetcher;
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
      showStats(matchPlayer, player);
    } else {
      if (Bukkit.getPlayer(playerName).isOnline()) {
        MatchPlayer target = PGM.get().getMatchManager().getPlayer(Bukkit.getPlayer(playerName));
        if (target != null) showStats(target, player);
      } else {
          if (MySQLSetterGetter.playerExists(UUIDFetcher.getUUID(playerName).toString())) {
            MatchPlayer offline = PGM.get().getMatchManager().getPlayer(UUIDFetcher.getUUID(playerName));
            if (offline != null) showStats(offline, player);
          } else {
            Audience audience = Mixed.get().getAudience().player(matchPlayer.getId());
            audience.sendMessage(Component.text("The player not found", NamedTextColor.RED));
          }
        }
      }
    }

  private void showStats(MatchPlayer player, Player sender) {
    Map<String, Integer> stats = getStats(player.getId());
    TextComponent.Builder component = Component.text();
    component.append(Component.text(
            LegacyFormatUtils.horizontalLineHeading(player.getPrefixedName(), ChatColor.WHITE)));
    component.append(Component.newline());
    component.append(formatStats("Kills: ", stats.get("KILLS")));
    component.append(formatStats("Deaths: ", stats.get("DEATHS")));
    component.append(Component.text("K/D: ", NamedTextColor.AQUA)
            .append(
                    Component.text(
                            kd(stats.get("KILLS"), stats.get("DEATHS")).doubleValue(),
                            NamedTextColor.BLUE)));
    component.append(Component.newline());
    component.append(formatStats("Wool Placed: ", stats.get("WOOLS")));
    component.append(formatStats("Cores Leaked: ", stats.get("CORES")));
    component.append(formatStats("Monuments Destroyed: ", stats.get("MONUMENTS")));
    component.append(formatStats("Flag Captured: ", stats.get("POINTS")));
    component.append(Component.text(LegacyFormatUtils.horizontalLine(ChatColor.WHITE, 300)));

    Mixed.get().getAudience().player(sender).sendMessage(component.build());
  }

  private TextComponent formatStats(String ladder, int value) {
    return Component.text().append(Component.text(ladder, NamedTextColor.AQUA)).append(Component.text(value, NamedTextColor.BLUE)).append(Component.newline()).build();
  }

  private BigDecimal kd(int kills, int deaths) {
    BigDecimal bd1 = new BigDecimal(kills);
    BigDecimal bd2 = new BigDecimal(deaths);
    BigDecimal result = null;
    try {
      result = bd1.divide(bd2, 2, RoundingMode.HALF_UP);
    } catch (ArithmeticException e) {
      result = BigDecimal.ZERO;
    }
    return result;
  }

  private Map<String, Integer> getStats(UUID uuid) {
    Map<String, Integer> stats = new HashMap<>();

    Connection connection = null;
    ResultSet rs = null;
    PreparedStatement statement = null;
    String query =
        "SELECT KILLS, DEATHS, CORES, WOOLS, MONUMENTS, FLAGS FROM STATS WHERE UUID = '"
            + uuid.toString()
            + "';";
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement(query);
      rs = statement.executeQuery();
      if (rs.next()) {
        int kills = rs.getInt("KILLS");
        int deaths = rs.getInt("DEATHS");
        int cores = rs.getInt("CORES");
        int wools = rs.getInt("WOOLS");
        int dtm = rs.getInt("MONUMENTS");
        int flags = rs.getInt("FLAGS");

        stats.put("KILLS", kills);
        stats.put("DEATHS", deaths);
        stats.put("CORES", cores);
        stats.put("WOOLS", wools);
        stats.put("MONUMENTS", dtm);
        stats.put("FLAGS", flags);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return stats;
  }
}
