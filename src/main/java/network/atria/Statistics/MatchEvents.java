package network.atria.Statistics;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import network.atria.Database.MySQL;
import network.atria.Mixed;
import network.atria.Ranks.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.match.MatchModule;
import tc.oc.pgm.api.match.MatchScope;
import tc.oc.pgm.api.match.event.MatchFinishEvent;
import tc.oc.pgm.api.match.event.MatchStartEvent;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.MatchPlayerState;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;
import tc.oc.pgm.core.CoreLeakEvent;
import tc.oc.pgm.destroyable.DestroyableDestroyedEvent;
import tc.oc.pgm.events.ListenerScope;
import tc.oc.pgm.events.PlayerParticipationStartEvent;
import tc.oc.pgm.events.PlayerParticipationStopEvent;
import tc.oc.pgm.flag.event.FlagCaptureEvent;
import tc.oc.pgm.wool.PlayerWoolPlaceEvent;

@ListenerScope(MatchScope.RUNNING)
public class MatchEvents implements Listener, MatchModule {

  private HashMap<UUID, String> totalMap;
  private HashMap<UUID, String> weeklyMap;

  @EventHandler(priority = EventPriority.MONITOR)
  public void onMatchStart(MatchStartEvent event) {
    MatchStatistics statistics = Mixed.get().getStatistics();

    statistics.newMatch();
    event
        .getMatch()
        .getPlayers()
        .forEach(
            player -> {
              statistics.countPlaytime(player.getId(), event.getMatch());
              addResultMap(player.getId());
            });
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onJoinMatch(PlayerParticipationStartEvent event) {
    if (!event.isCancelled() && event.getMatch().isRunning()) {
      Mixed.get().getStatistics().countPlaytime(event.getPlayer().getId(), event.getMatch());
      addResultMap(event.getPlayer().getId());
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onLeaveMatch(PlayerParticipationStopEvent event) {
    if (!event.isCancelled() && event.getMatch().isRunning()) {
      Mixed.get().getStatistics().removePlaytime(event.getPlayer().getId());
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerDeath(MatchPlayerDeathEvent event) {
    MatchPlayer victim = event.getPlayer();
    MatchPlayer murder;

    if (event.isTeamKill()) return;
    Mixed.get().getStatistics().addDeath(victim.getId());

    if (event.getKiller() != null && !event.isSelfKill() && !event.isSuicide()) {
      murder = event.getKiller().getParty().getPlayer(event.getKiller().getId());
      if (murder != null) {
        if (!murder.getParty().equals(victim.getParty())) {
          Mixed.get().getStatistics().addKill(murder.getId());
          Mixed.get().getStatistics().addPoint(murder.getId(), 5);
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onWoolCapture(PlayerWoolPlaceEvent event) {
    MatchPlayerState player = event.getPlayer();

    Mixed.get().getStatistics().addPoint(player.getId(), 20);
    Mixed.get().getStatistics().addWool(player.getId());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onCoreBreak(CoreLeakEvent event) {
    event
        .getCore()
        .getTouchingPlayers()
        .forEach(
            player -> {
              Mixed.get().getStatistics().addPoint(player.getId(), 25);
              Mixed.get().getStatistics().addCore(player.getId());
            });
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onFlagCapture(FlagCaptureEvent event) {
    MatchPlayer player = event.getCarrier();

    Mixed.get().getStatistics().addPoint(player.getId(), 15);
    Mixed.get().getStatistics().addFlag(player.getId());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onDestroyMonument(DestroyableDestroyedEvent event) {
    event
        .getDestroyable()
        .getContributions()
        .forEach(
            player -> {
              Mixed.get().getStatistics().addMonument(player.getPlayerState().getId());
              Mixed.get().getStatistics().addPoint(player.getPlayerState().getId(), 25);
            });
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onMatchFinish(MatchFinishEvent event) {
    RankManager manager = new RankManager();
    event
        .getMatch()
        .getPlayers()
        .forEach(
            player -> {
              Mixed.get().getStatistics().addPoint(player.getId(), 10);
              manager.RankUP(player);
            });
    sendStatsData();
    Mixed.get().getStatistics().endMatch();
  }

  private void sendStatsData() {
    Bukkit.getScheduler()
        .scheduleSyncDelayedTask(
            Mixed.get(),
            new Runnable() {
              @Override
              public void run() {
                totalStatsUpdate();
                weeklyStatsUpdate();
              }
            },
            20L);
  }

  private void totalStatsUpdate() {
    StoreStatistics store = Mixed.get().getStatistics().getStoreStatistics();
    sortStats(store.getKills(), totalMap, "KILLS", "STATS");
    sortStats(store.getDeaths(), totalMap, "DEATHS", "STATS");
    sortStats(store.getWools(), totalMap, "WOOLS", "STATS");
    sortStats(store.getMonuments(), totalMap, "MONUMENTS", "STATS");
    sortStats(store.getCores(), totalMap, "CORES", "STATS");
    sortStats(store.getFlags(), totalMap, "FLAGS", "STATS");
    sortStats(store.getPoints(), totalMap, "POINTS", "STATS");
    sortStats(store.getPlaytime(), totalMap, "PLAYTIME", "STATS");
    update(totalMap, "STATS");
  }

  private void weeklyStatsUpdate() {
    StoreStatistics store = Mixed.get().getStatistics().getStoreStatistics();
    sortStats(store.getKills(), weeklyMap, "KILLS", "WEEK_STATS");
    sortStats(store.getDeaths(), weeklyMap, "DEATHS", "WEEK_STATS");
    sortStats(store.getWools(), weeklyMap, "WOOLS", "WEEK_STATS");
    sortStats(store.getMonuments(), weeklyMap, "MONUMENTS", "WEEK_STATS");
    sortStats(store.getCores(), weeklyMap, "CORES", "WEEK_STATS");
    sortStats(store.getFlags(), weeklyMap, "FLAGS", "WEEK_STATS");
    sortStats(store.getPoints(), weeklyMap, "POINTS", "WEEK_STATS");
    sortStats(store.getPlaytime(), weeklyMap, "PLAYTIME", "WEEK_STATS");
    update(weeklyMap, "WEEK_STATS");
  }

  private void sortStats(
      Map<UUID, AtomicInteger> map, HashMap<UUID, String> result, String column, String table) {
    if (map.isEmpty()) return;
    map.entrySet().stream()
        .filter(stats -> stats.getValue().get() != 0)
        .forEach(
            stats -> {
              Map<String, Integer> data = getStats(stats.getKey(), table);
              int score = stats.getValue().get() + data.get(column);
              result.put(
                  stats.getKey(), result.get(stats.getKey()) + column + " = '" + score + "', ");
            });
  }

  private Map<String, Integer> getStats(UUID uuid, String table) {
    Map<UUID, String> maps = new HashMap<>(includeMaps(uuid));
    Map<String, Integer> stats = new HashMap<>();
    String column =
        Arrays.toString(maps.values().toArray()).trim().replace("[", "").replace("]", "");
    String query =
        "SELECT "
            + column.substring(0, column.length() - 2)
            + " FROM "
            + table
            + " WHERE UUID = '"
            + uuid.toString()
            + "';";
    maps.clear();
    Connection connection = null;
    ResultSet rs = null;
    PreparedStatement statement = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement(query);
      rs = statement.executeQuery();
      if (rs.next()) {
        Set<String> columns = new HashSet<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
          columns.add(rsmd.getColumnName(i));
        }
        if (columns.contains("KILLS")) stats.put("KILLS", rs.getInt("KILLS"));
        if (columns.contains("DEATHS")) stats.put("DEATHS", rs.getInt("DEATHS"));
        if (columns.contains("CORES")) stats.put("CORES", rs.getInt("CORES"));
        if (columns.contains("WOOLS")) stats.put("WOOLS", rs.getInt("WOOLS"));
        if (columns.contains("MONUMENTS")) stats.put("MONUMENTS", rs.getInt("MONUMENTS"));
        if (columns.contains("FLAGS")) stats.put("FLAGS", rs.getInt("FLAGS"));
        if (columns.contains("POINTS")) stats.put("POINTS", rs.getInt("POINTS"));
        if (columns.contains("PLAYTIME")) stats.put("PLAYTIME", rs.getInt("PLAYTIME"));
        columns.clear();
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
    return Collections.unmodifiableMap(stats);
  }

  private void update(HashMap<UUID, String> map, String table) {
    map.forEach(
        (key, value) -> {
          Connection connection = null;
          PreparedStatement statement = null;
          String columns = value.substring(0, value.length() - 2);
          String query = "UPDATE " + table + " SET " + columns + " WHERE UUID = '" + key + "';";
          try {
            connection = MySQL.getHikari().getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
          } catch (SQLException e) {
            e.printStackTrace();
          } finally {
            if (connection != null) {
              try {
                connection.close();
              } catch (SQLException e) {
                e.printStackTrace();
              }
              if (statement != null) {
                try {
                  statement.close();
                } catch (SQLException e) {
                  e.printStackTrace();
                }
              }
            }
          }
        });
  }

  private Map<UUID, String> includeMaps(UUID uuid) {
    Map<UUID, String> multipleMap = new HashMap<>();
    StoreStatistics store = Mixed.get().getStatistics().getStoreStatistics();

    multipleMap.put(uuid, "");
    if (store.getKills().containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "KILLS, ");
    if (store.getDeaths().containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "DEATHS, ");
    if (store.getCores().containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "CORES, ");
    if (store.getMonuments().containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "MONUMENTS, ");
    if (store.getFlags().containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "FLAGS, ");
    if (store.getWools().containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "WOOLS, ");
    if (store.getPoints().containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "POINTS, ");
    if (store.getPlaytime().containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "PLAYTIME, ");

    return Collections.unmodifiableMap(multipleMap);
  }

  private void addResultMap(UUID uuid) {
    if (weeklyMap == null) weeklyMap = new HashMap<>();
    if (totalMap == null) totalMap = new HashMap<>();
    if (weeklyMap.containsKey(uuid)) return;
    if (totalMap.containsKey(uuid)) return;
    totalMap.put(uuid, "");
    weeklyMap.put(uuid, "");
  }

  public MatchEvents(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
