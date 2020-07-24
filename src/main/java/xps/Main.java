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
import xps.Command.RankingCommand;
import xps.Command.StatsCommand;
import xps.Database.MySQL;
import xps.Database.MySQLSetterGetter;
import xps.Database.PlayerStats;
import xps.Task.BroadCastMesseage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main extends JavaPlugin implements Listener, CommandExecutor {

    public static MySQL mysql;
    public static Main instance;

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.options().copyDefaults(true);
        saveConfig();

        BroadCastMesseage broadCastMesseage = new BroadCastMesseage();
        instance = this;
        ConnectMySQL();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        new PlayerStats(this);

        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("ranking").setExecutor(new RankingCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("tops").setExecutor(new RankingCommand());

        Bukkit.getServer().getPluginManager().registerEvents(new RankingCommand(), this);

        broadCastMesseage.randomMesseage();
    }

    private void ConnectMySQL() {
        (mysql = new MySQL(MySQL.getHost(), MySQL.getDatabase(), MySQL.getUser(), MySQL.getPassword(), MySQL.getPort()))
                .update("CREATE TABLE IF NOT EXISTS STATS(UUID varchar(64), KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, NAME varchar(64), DATE varchar(10));");
    }

    public static Main getInstance() {
        return instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        MySQLSetterGetter.createPlayer(p.getUniqueId().toString());
        MySQLSetterGetter.addName(p.getUniqueId().toString(), p.getName());
        MySQLSetterGetter.addDate(p.getUniqueId().toString(), getTime());
    }

    private String getTime() {
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      return   df.format(now);
    }
}