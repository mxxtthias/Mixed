package network.atria.Commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import app.ashcon.intake.parametric.annotation.Text;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import network.atria.Database.MySQL;
import network.atria.Database.MySQLSetterGetter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import tc.oc.pgm.util.LegacyFormatUtils;

public class StatsCommand {

  @Command(
      aliases = {"stats"},
      desc = "Show Player Stats",
      usage = "[Target]")
  public void stats(@Sender Player player, @Text String playerName) {
    final Map<String, Integer> statsMap = getStats(player.getUniqueId());
    if (playerName == null) {
      showStats(
          player,
          player.getName(),
          statsMap.get("KILLS"),
          statsMap.get("DEATHS"),
          statsMap.get("FLAGS"),
          statsMap.get("CORES"),
          statsMap.get("WOOLS"),
          statsMap.get("MONUMENTS"));
    } else {
      final Player target = Bukkit.getPlayer(playerName);
      final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
      if (target == null) {
        if (MySQLSetterGetter.playerExists(offlinePlayer.getUniqueId().toString())) {
          final Map<String, Integer> offlineStats = getStats(offlinePlayer.getUniqueId());
          showStats(
              player,
              offlinePlayer.getName(),
              offlineStats.get("KILLS"),
              offlineStats.get("DEATHS"),
              offlineStats.get("FLAGS"),
              offlineStats.get("CORES"),
              offlineStats.get("WOOLS"),
              offlineStats.get("MONUMENTS"));
        } else {
          player.sendMessage("The player not found");
        }
      }
    }
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

  private void showStats(
      final Player player,
      final String name,
      final int kills,
      final int deaths,
      final int flags,
      final int cores,
      final int wools,
      final int dtm) {
    player.sendMessage(
        LegacyFormatUtils.horizontalLineHeading(
            ChatColor.AQUA + name + "'s Stats", net.md_5.bungee.api.ChatColor.WHITE));
    player.sendMessage(ChatColor.AQUA + "Kills: " + ChatColor.BLUE + kills);
    player.sendMessage(ChatColor.AQUA + "Deaths: " + ChatColor.BLUE + deaths);
    player.sendMessage(ChatColor.AQUA + "K/D: " + ChatColor.BLUE + kd(kills, deaths));
    player.sendMessage(ChatColor.AQUA + "Wool Placed: " + ChatColor.BLUE + wools);
    player.sendMessage(ChatColor.AQUA + "Cores Leaked: " + ChatColor.BLUE + cores);
    player.sendMessage(ChatColor.AQUA + "Monuments Destroyed: " + ChatColor.BLUE + dtm);
    player.sendMessage(ChatColor.AQUA + "Flags Captured: " + ChatColor.BLUE + flags);
    player.sendMessage(LegacyFormatUtils.horizontalLine(net.md_5.bungee.api.ChatColor.WHITE, 300));
  }

  private Map<String, Integer> getStats(UUID uuid) {
    Connection connection = null;
    ResultSet rs = null;
    PreparedStatement statement = null;
    final Map<String, Integer> stats = new HashMap<>();
    final String query =
        "SELECT KILLS, DEATHS, CORES, WOOLS, MONUMENTS, FLAGS FROM STATS WHERE UUID = '"
            + uuid.toString()
            + "';";
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement(query);
      rs = statement.executeQuery();
      if (rs.next()) {
        final int kills = rs.getInt("KILLS");
        final int deaths = rs.getInt("DEATHS");
        final int cores = rs.getInt("CORES");
        final int wools = rs.getInt("WOOLS");
        final int dtm = rs.getInt("MONUMENTS");
        final int flags = rs.getInt("FLAGS");

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
