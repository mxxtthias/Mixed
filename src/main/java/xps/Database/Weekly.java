package xps.Database;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.events.PlayerJoinMatchEvent;
import xps.Main;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class Weekly implements Listener {

    public void WeeklyRanking() {

        LocalDate whenRankingWeekStarted = LocalDate.parse(MySQLSetterGetter.getDate());
        LocalDate now = LocalDate.now();
        Period interval = Period.between(whenRankingWeekStarted, now);

        if (interval.getDays() > 7) {
            MySQLSetterGetter.setDate(now.toString());
            Main.mysql.update("UPDATE mixed.WEEK_STATS SET KILLS = FLAGS = CORES = WOOLS = MONUMENTS = 0;");
        }
    }

    @EventHandler
    public void addStatsOnJoinServer(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        Main.mysql.update("INSERT INTO mixed.WEEK_STATS(UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME)" +
                "SELECT UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME" +
                "FROM mixed.STATS WHERE UUID = '" + uuid +"'" +
                "AND UUID = (SELECT UUID FROM mixed.WEEK_STATS WHERE UUID = '" + uuid + "');");
    }

    @EventHandler
    public void addStatsOnJoinMatch(PlayerJoinMatchEvent e) {
        UUID uuid = e.getPlayer().getId();

        Main.mysql.update("INSERT INTO mixed.WEEK_STATS(UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME)" +
                "SELECT UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME" +
                "FROM mixed.STATS WHERE UUID = '" + uuid +"'" +
                "AND UUID = (SELECT UUID FROM mixed.WEEK_STATS WHERE UUID = '" + uuid + "');");
    }

    public Weekly(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        WeeklyRanking();
    }
}
