package xps;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xps.Command.PingCommand;
import xps.Command.RankUPCommand;
import xps.Command.StatsCommand;
import xps.Database.*;
import xps.Task.BroadCastMesseage;

import java.io.File;
import java.io.IOException;


public class Main extends JavaPlugin implements Listener, CommandExecutor {

    public static MySQL mysql;
    public static Main instance;

    FileConfiguration config = getConfig();

    private File customConfigFile;
    private FileConfiguration customConfig;

    @Override
    public void onEnable() {
        instance = this;

        createCustomConfig();
        config.options().copyDefaults(true);
        saveConfig();

        ConnectMySQL();
        tableCreate();

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        new PlayerStats(this);
        new RankUp(this);
        new Weekly(this);

        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("rankup").setExecutor(new RankUPCommand());

        BroadCastMesseage broadCastMesseage = new BroadCastMesseage();
        broadCastMesseage.randomMesseage();
    }

    private void ConnectMySQL() {
        (mysql = new MySQL(MySQL.getDatabase(), MySQL.getUser(), MySQL.getPassword(), MySQL.getPort()))
                .update("CREATE TABLE IF NOT EXISTS STATS(UUID varchar(64), KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, NAME varchar(64));");
    }

    private void tableCreate() {
        Main.mysql.update("CREATE TABLE IF NOT EXISTS WEEK_STATS(" +
                "UUID varchar(64), " +
                "KILLS int, " +
                "DEATHS int, " +
                "FLAGS int, " +
                "CORES int, " +
                "WOOLS int, " +
                "MONUMENTS int, " +
                "NAME varchar(64)");
    }

    public static Main getInstance() {
        return instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!p.hasPlayedBefore()) {
            MySQLSetterGetter.createPlayer(p.getUniqueId().toString());
            MySQLSetterGetter.setName(p.getUniqueId().toString(), p.getName());
            MySQLSetterGetter.addRank(p.getUniqueId().toString(), getConfig().getString("Ranks.Default"));
        }
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "ranks.yml");

        customConfig = new YamlConfiguration();

        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
