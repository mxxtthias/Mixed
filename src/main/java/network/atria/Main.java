package network.atria;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import network.atria.Database.*;
import network.atria.KillEffects.*;
import network.atria.Listener.MatchEvents;
import network.atria.RankSystem.CheckRankCommand;
import network.atria.Statics.StatsCommand;
import network.atria.Task.BroadCastMesseage;
import network.atria.Util.KillEffectsConfig;
import network.atria.Util.RanksConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

  public static Main instance;

  RanksConfig ranks;
  KillEffectsConfig effects;

  FileConfiguration config = getConfig();

  @Override
  public void onEnable() {
    instance = this;

    ranks = new RanksConfig(this, "ranks.yml");
    effects = new KillEffectsConfig(this, "killeffects.yml");

    config.options().copyDefaults();
    saveDefaultConfig();
    MySQL.connect();
    ConnectMySQL();

    registerCommands();
    registerEvents();

    BroadCastMesseage broadCastMesseage = new BroadCastMesseage();
    broadCastMesseage.randomMesseage();

    super.onEnable();
  }

  @Override
  public void onDisable() {
    if (MySQL.getHikari() != null) {
      MySQL.getHikari().close();
    }
    super.onDisable();
  }

  private void registerEvents() {
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
    Bukkit.getServer().getPluginManager().registerEvents(new KillEffectsGUI(), this);
    Bukkit.getServer().getPluginManager().registerEvents(new KillSoundsGUI(), this);
    Bukkit.getServer().getPluginManager().registerEvents(new DefaultGUI(), this);
    Bukkit.getServer().getPluginManager().registerEvents(new ProjectileGUI(), this);
    new MatchEvents(this);
    new KillEffects(this);
    new KillSounds(this);
    new ProjectileTrails(this);
  }

  private void registerCommands() {
    getCommand("stats").setExecutor(new StatsCommand());
    getCommand("rank").setExecutor(new CheckRankCommand());
    getCommand("effect").setExecutor(new DefaultGUI());
    getCommand("sound").setExecutor(new DefaultGUI());
  }

  private void ConnectMySQL() {
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      Statement statement = connection.createStatement();

      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS STATS(UUID varchar(36) NOT NULL PRIMARY KEY, KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, NAME varchar(20));");
      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS WEEK_STATS(UUID varchar(36) NOT NULL PRIMARY KEY, KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, NAME varchar(20));");
      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS RANKS(UUID varchar(36) NOT NULL PRIMARY KEY, NAME varchar(20), POINTS int, GAMERANK varchar(20), EFFECT varchar(20), SOUND varchar(20), PROJECTILE varchar(20));");

      statement.close();
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
    }
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    final LuckPerms api = LuckPermsProvider.get();
    final User user = api.getUserManager().getUser(player.getUniqueId());
    final PermissionNode node = PermissionNode.builder("pgm.group.wood_iii").build();

    if (!MySQLSetterGetter.playerExists(player.getUniqueId().toString())) {
      MySQLSetterGetter.createPlayer(player.getUniqueId().toString());
      MySQLSetterGetter.setName(player.getUniqueId().toString(), player.getName());

      user.data().add(node);
      api.getUserManager().saveUser(user);
    }
  }

  public static Main getInstance() {
    return instance;
  }
}
