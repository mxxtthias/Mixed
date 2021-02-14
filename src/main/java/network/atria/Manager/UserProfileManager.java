package network.atria.Manager;

import static net.kyori.adventure.text.Component.text;
import static network.atria.MySQL.SQLQuery.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import network.atria.Effects.Particles.Effect;
import network.atria.Mixed;
import network.atria.MySQL;
import network.atria.UserProfile.UserProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserProfileManager implements Listener {

  private final Map<UUID, UserProfile> profiles;

  public UserProfileManager() {
    this.profiles = Maps.newConcurrentMap();
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    UUID uuid = event.getPlayer().getUniqueId();
    UserProfile profile = Mixed.get().getProfileManager().getProfile(uuid);

    pushProfile(profile);
  }

  public UserProfile createProfile(String name, UUID uuid) {
    EffectManager manager = Mixed.get().getEffectManager();
    HashMap<String, Integer> stats = getUserData(uuid, name);

    Effect killeffect =
        manager.findEffect(getAsString("RANKS", "EFFECT", uuid)).isPresent()
            ? manager.findEffect(getAsString("RANKS", "EFFECT", uuid)).get()
            : new Effect("NONE", text("NONE"), 0, false);
    Effect killsound =
        manager.findEffect(getAsString("RANKS", "SOUND", uuid)).isPresent()
            ? manager.findEffect(getAsString("RANKS", "SOUND", uuid)).get()
            : new Effect("NONE", text("NONE"), 0, false);
    Effect projectile =
        manager.findEffect(getAsString("RANKS", "PROJECTILE", uuid)).isPresent()
            ? manager.findEffect(getAsString("RANKS", "PROJECTILE", uuid)).get()
            : new Effect("DEFAULT", text("DEFAULT"), 0, false);
    return new UserProfile(
        name,
        uuid,
        Mixed.get().getRankManager().getRank(getAsString("RANKS", "GAMERANK", uuid)),
        killeffect,
        projectile,
        killsound,
        stats.get("KILLS"),
        stats.get("DEATHS"),
        stats.get("FLAGS"),
        stats.get("CORES"),
        stats.get("WOOLS"),
        stats.get("MONUMENTS"),
        stats.get("PLAYTIME"),
        stats.get("POINTS"),
        stats.get("WINS"),
        stats.get("LOSES"),
        stats.get("WEEKLY_KILLS"),
        stats.get("WEEKLY_DEATHS"),
        stats.get("WEEKLY_FLAGS"),
        stats.get("WEEKLY_CORES"),
        stats.get("WEEKLY_WOOLS"),
        stats.get("WEEKLY_MONUMENTS"),
        stats.get("WEEKLY_PLAYTIME"),
        stats.get("WEEKLY_WINS"),
        stats.get("WEEKLY_LOSES"));
  }

  public void pushProfile(UserProfile profile) {
    Connection connection = null;
    PreparedStatement statement_1;
    PreparedStatement statement_2;
    String sql_1 =
        "UPDATE STATS SET KILLS = ?, DEATHS = ?, FLAGS = ?, CORES = ?, WOOLS = ?, MONUMENTS = ?, PLAYTIME = ?, POINTS = ?, WINS = ?, LOSES = ? WHERE UUID = ?";
    String sql_2 =
        "UPDATE RANKS SET GAMERANK = ?, EFFECT = ?, SOUND = ?, PROJECTILE = ? WHERE UUID = ?";
    try {
      connection = MySQL.get().getHikari().getConnection();
      statement_1 = connection.prepareStatement(sql_1);
      statement_1.setInt(1, profile.getKills());
      statement_1.setInt(2, profile.getDeaths());
      statement_1.setInt(3, profile.getFlags());
      statement_1.setInt(4, profile.getCores());
      statement_1.setInt(5, profile.getWools());
      statement_1.setInt(6, profile.getMonuments());
      statement_1.setInt(7, profile.getPlaytime());
      statement_1.setInt(8, profile.getPoints());
      statement_1.setString(9, profile.getUUID().toString());
      statement_1.executeUpdate();
      statement_1.close();

      statement_2 = connection.prepareStatement(sql_2);
      statement_2.setString(1, profile.getRank().getName());
      statement_2.setString(2, profile.getKilleffect().getName());
      statement_2.setString(3, profile.getKillsound().getName());
      statement_2.setString(4, profile.getProjectile().getName());
      statement_2.setString(5, profile.getUUID().toString());
      statement_2.executeUpdate();
      statement_2.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
      this.profiles.remove(profile.getUUID());
    }
  }

  private HashMap<String, Integer> getUserData(UUID uuid, String name) {
    HashMap<String, Integer> stats = Maps.newHashMap();
    Connection connection = null;
    PreparedStatement statement_1 = null;
    PreparedStatement statement_2 = null;
    ResultSet rs_1 = null;
    ResultSet rs_2 = null;
    String sql_1 =
        "SELECT KILLS, DEATHS, CORES, WOOLS, MONUMENTS, FLAGS, PLAYTIME, POINTS FROM STATS WHERE UUID = ?";
    String sql_2 =
        "SELECT KILLS, DEATHS, CORES, WOOLS, MONUMENTS, FLAGS, PLAYTIME FROM WEEKLY_STATS WHERE UUID = ?";

    if (!MySQL.SQLQuery.playerExist_in_weekly_table(uuid)) {
      create_weekly_table(uuid, name);
    }
    try {
      connection = MySQL.get().getHikari().getConnection();
      statement_1 = connection.prepareStatement(sql_1);
      statement_1.setString(1, uuid.toString());
      rs_1 = statement_1.executeQuery();

      if (rs_1.next()) {
        stats.put("KILLS", rs_1.getInt("KILLS"));
        stats.put("DEATHS", rs_1.getInt("DEATHS"));
        stats.put("CORES", rs_1.getInt("CORES"));
        stats.put("WOOLS", rs_1.getInt("WOOLS"));
        stats.put("MONUMENTS", rs_1.getInt("MONUMENTS"));
        stats.put("FLAGS", rs_1.getInt("FLAGS"));
        stats.put("PLAYTIME", rs_1.getInt("PLAYTIME"));
        stats.put("POINTS", rs_1.getInt("POINTS"));
        stats.put("WINS", rs_1.getInt("WINS"));
        stats.put("LOSES", rs_1.getInt("LOSES"));
      }

      statement_2 = connection.prepareStatement(sql_2);
      statement_2.setString(1, uuid.toString());
      rs_2 = statement_2.executeQuery();

      if (rs_2.next()) {
        stats.put("WEEKLY_KILLS", rs_2.getInt("KILLS"));
        stats.put("WEEKLY_DEATHS", rs_2.getInt("DEATHS"));
        stats.put("WEEKLY_CORES", rs_2.getInt("CORES"));
        stats.put("WEEKLY_WOOLS", rs_2.getInt("WOOLS"));
        stats.put("WEEKLY_MONUMENTS", rs_2.getInt("MONUMENTS"));
        stats.put("WEEKLY_FLAGS", rs_2.getInt("FLAGS"));
        stats.put("WEEKLY_PLAYTIME", rs_2.getInt("PLAYTIME"));
        stats.put("WEEKLYLY_WINS", rs_2.getInt("WINS"));
        stats.put("WEEKLYLY_LOSES", rs_2.getInt("LOSES"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeConnection(connection);
      closeResultSet(rs_1);
      closeResultSet(rs_2);
      closeStatement(statement_1);
      closeStatement(statement_2);
    }
    return stats;
  }

  public void addProfile(UUID uuid, UserProfile profile) {
    this.profiles.put(uuid, profile);
  }

  public UserProfile getProfile(UUID uuid) {
    return this.profiles.get(uuid) == null ? null : this.profiles.get(uuid);
  }

  public Collection<UserProfile> getProfiles() {
    return ImmutableList.copyOf(this.profiles.values());
  }
}
