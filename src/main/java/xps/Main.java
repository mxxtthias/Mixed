package xps;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xps.Command.PingCommand;
import xps.RankSystem.ChatPrefix;
import xps.RankSystem.CheckRankCommand;
import xps.Command.StatsCommand;
import xps.Database.*;
import xps.Task.BroadCastMesseage;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    public static MySQL mysql;
    public static Main instance;

    CustomConfig ranks;

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        instance = this;

        ranks = new CustomConfig(this, "ranks.yml");

        config.options().copyDefaults();
        saveDefaultConfig();

        ConnectMySQL();

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        new PlayerStats(this);
        new ChatPrefix(this);
        // new Weekly(this);

        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("rank").setExecutor(new CheckRankCommand());
        getCommand("point").setExecutor(new CheckRankCommand());
        getCommand("points").setExecutor(new CheckRankCommand());

        BroadCastMesseage broadCastMesseage = new BroadCastMesseage();
        broadCastMesseage.randomMesseage();
    }

    private void ConnectMySQL() {
        (mysql = new MySQL(MySQL.getHost(), MySQL.getDatabase(), MySQL.getUser(), MySQL.getPassword(), MySQL.getPort())).update("CREATE TABLE IF NOT EXISTS STATS(UUID varchar(64), KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, NAME varchar(64));");
        mysql.update("CREATE TABLE IF NOT EXISTS WEEK_STATS(UUID varchar(64), KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, NAME varchar(64));");
        mysql.update("CREATE TABLE IF NOT EXISTS RANKS(UUID varchar(64), NAME varchar(64), POINTS int, GAMERANK varchar(64));");
    }

    public static Main getInstance() {
        return instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        MySQLSetterGetter.createPlayer(p.getUniqueId().toString());
        MySQLSetterGetter.setName(p.getUniqueId().toString(), p.getName());
    }
}
