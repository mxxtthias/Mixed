package network.atria;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import network.atria.RankSystem.ChatPrefix;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import network.atria.Command.PingCommand;
import network.atria.Util.KillEffectsConfig;
import network.atria.Util.RanksConfig;
import network.atria.KillEffects.*;
import network.atria.RankSystem.CheckRankCommand;
import network.atria.Statics.StatsCommand;
import network.atria.Database.*;
import network.atria.RankSystem.RankSystemManager;
import network.atria.Task.BroadCastMesseage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    public static MySQL mysql;
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
        if(MySQL.getHikari() != null) {
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
        Bukkit.getServer().getPluginManager().registerEvents(new ChatPrefix(), this);

        new RankSystemManager(this);
        new KillEffects(this);
        new KillSounds(this);
        new ProjectileTrails(this);
    }

    private void registerCommands() {
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("rank").setExecutor(new CheckRankCommand());
        getCommand("effect").setExecutor(new DefaultGUI());
        getCommand("sound").setExecutor(new DefaultGUI());
    }

    private void ConnectMySQL() {
        try {
            Connection connection = MySQL.getConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS STATS(UUID varchar(64), KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, NAME varchar(64));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS WEEK_STATS(UUID varchar(64), KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, NAME varchar(64));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS RANKS(UUID varchar(64), NAME varchar(64), POINTS int, GAMERANK varchar(64), EFFECT varchar(64), SOUND varchar(64));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Main getInstance() {
        return instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        LuckPerms api = LuckPermsProvider.get();
        User user = api.getUserManager().getUser(p.getUniqueId());

        if (!MySQLSetterGetter.playerExists(p.getUniqueId().toString())) {
            MySQLSetterGetter.createPlayer(p.getUniqueId().toString());
            MySQLSetterGetter.setName(p.getUniqueId().toString(), p.getName());

            PermissionNode addGroup = PermissionNode.builder("pgm.group.wood_iii").build();
            user.data().add(addGroup);
            api.getUserManager().saveUser(user);
        }
    }
}
