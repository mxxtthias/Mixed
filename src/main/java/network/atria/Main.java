package network.atria;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import network.atria.Commands.graph.CommandExecutor;
import network.atria.Commands.graph.CommandGraph;
import network.atria.Database.*;
import network.atria.KillEffects.*;
import network.atria.Listener.MatchEvents;
import network.atria.Task.BroadCastMessage;
import network.atria.Util.KillEffectsConfig;
import network.atria.Util.RanksConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

  public static Main instance;
  private long uptime;

  private RanksConfig ranks;
  private KillEffectsConfig effects;

  private final FileConfiguration config = getConfig();
  MySQL database = new MySQL();

  @Override
  public void onEnable() {
    instance = this;

    ranks = new RanksConfig(this, "ranks.yml");
    effects = new KillEffectsConfig(this, "killeffects.yml");

    config.options().copyDefaults();
    saveDefaultConfig();
    database.connect();
    database.createTables();

    registerCommands();
    registerEvents();

    uptime = System.currentTimeMillis();

    BroadCastMessage broadCastMessage = new BroadCastMessage();
    broadCastMessage.randomMessage();

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
    final PluginManager pm = Bukkit.getServer().getPluginManager();

    pm.registerEvents(this, this);
    new KillEffectsGUI(this);
    new KillSoundsGUI(this);
    new DefaultGUI(this);
    new ProjectileGUI(this);
    new MatchEvents(this);
    new KillEffects(this);
    new KillSounds(this);
    new ProjectileTrails(this);
  }

  private void registerCommands() {
    final CommandGraph graph = new CommandGraph();
    new CommandExecutor(this, graph).register();
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    ;
    final LuckPerms api = LuckPermsProvider.get();
    final User user = api.getUserManager().getUser(player.getUniqueId());
    final PermissionNode node = PermissionNode.builder("pgm.group.wood_iii").build();

    if (!MySQLSetterGetter.playerExists(player.getUniqueId().toString())) {
      MySQLSetterGetter.createPlayer(player.getUniqueId().toString());
      MySQLSetterGetter.setName(player.getUniqueId().toString(), player.getName());

      user.data().add(node);
      api.getUserManager().saveUser(user);
    } else if (!MySQLSetterGetter.getName(player.getUniqueId().toString())
        .equals(player.getName())) {
      MySQLSetterGetter.setName(player.getUniqueId().toString(), player.getName());
    }
  }

  public long getUptime() {
    return uptime;
  }

  public static Main getInstance() {
    return instance;
  }
}
