package network.atria;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.api.Particles_1_8;
import com.github.fierioziy.particlenativeapi.plugin.ParticleNativePlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import network.atria.Commands.graph.CommandExecutor;
import network.atria.Commands.graph.CommandGraph;
import network.atria.Database.*;
import network.atria.Effects.GUI.*;
import network.atria.Effects.Particles.KillEffects;
import network.atria.Effects.Particles.ProjectileTrails;
import network.atria.Effects.Sounds.KillSounds;
import network.atria.Ranks.RankManager;
import network.atria.Statistics.MatchEvents;
import network.atria.Statistics.MatchStatistics;
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

public class Mixed extends JavaPlugin implements Listener {

  private static Mixed instance;
  private long uptime;
  private MatchStatistics statistics;
  private BukkitAudiences audiences;
  private final FileConfiguration config = getConfig();

  @Override
  public void onEnable() {
    instance = this;
    new RanksConfig(this, "ranks.yml");
    new KillEffectsConfig(this, "killeffects.yml");

    config.options().copyDefaults();
    saveDefaultConfig();

    MySQL database = new MySQL();
    database.connect();
    database.createTables();
    registerCommands();
    registerEvents();
    this.audiences = BukkitAudiences.create(this);
    this.uptime = System.currentTimeMillis();
    statistics = new MatchStatistics();
    RankManager rankManager = new RankManager();
    rankManager.createRank();
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
    PluginManager pm = Bukkit.getServer().getPluginManager();

    pm.registerEvents(this, this);
    pm.registerEvents(new CustomGUI(), this);
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
    CommandGraph graph = new CommandGraph();
    new CommandExecutor(this, graph).register();
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    LuckPerms api = LuckPermsProvider.get();
    User user = api.getUserManager().getUser(player.getUniqueId());
    PermissionNode node = PermissionNode.builder("pgm.group.wood_iii").build();

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

  public static Mixed get() {
    return instance;
  }

  public BukkitAudiences getAudience() {
    return this.audiences;
  }

  public Particles_1_8 getParticles() {
    ParticleNativeAPI api = ParticleNativePlugin.getAPI();
    return api.getParticles_1_8();
  }

  public MatchStatistics getStatistics() {
    return statistics;
  }
}
