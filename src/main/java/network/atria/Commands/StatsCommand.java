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
  public void stats(@Sender Player player, @Nullable String playerName) {
    if (playerName == null) {
      showStats(player, player.getName());

    } else {
      final Player target = Bukkit.getPlayer(playerName);
      final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
      if (target == null) {
        if (MySQLSetterGetter.playerExists(offlinePlayer.getUniqueId().toString())) {
          showStats(player, offlinePlayer.getName());
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

  private void showStats(Player player, String name) {

    final Map<String, Integer> stats = getStats(player.getUniqueId());
    player.sendMessage(
        LegacyFormatUtils.horizontalLineHeading(
            ChatColor.AQUA + name + "'s Stats", net.md_5.bungee.api.ChatColor.WHITE));
    player.sendMessage(ChatColor.AQUA + "Kills: " + ChatColor.BLUE + stats.get("KILLS"));
    player.sendMessage(ChatColor.AQUA + "Deaths: " + ChatColor.BLUE + stats.get("DEATHS"));
    player.sendMessage(
        ChatColor.AQUA + "K/D: " + ChatColor.BLUE + kd(stats.get("KILLS"), stats.get("DEATHS")));
    player.sendMessage(ChatColor.AQUA + "Wool Placed: " + ChatColor.BLUE + stats.get("WOOLS"));
    player.sendMessage(ChatColor.AQUA + "Cores Leaked: " + ChatColor.BLUE + stats.get("CORES"));
    player.sendMessage(
        ChatColor.AQUA + "Monuments Destroyed: " + ChatColor.BLUE + stats.get("MONUMENTS"));
    player.sendMessage(ChatColor.AQUA + "Flags Captured: " + ChatColor.BLUE + stats.get("FLAGS"));
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
