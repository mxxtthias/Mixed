package network.atria.Statistics;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQL;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Mixed;
import network.atria.Ranks.ChatPrefix;
import network.atria.Ranks.Rank;
import network.atria.Ranks.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
import tc.oc.pgm.flag.event.FlagCaptureEvent;
import tc.oc.pgm.wool.PlayerWoolPlaceEvent;

@ListenerScope(MatchScope.RUNNING)
public class MatchEvents implements Listener, MatchModule {

  private HashMap<UUID, String> result;

  @EventHandler(priority = EventPriority.MONITOR)
  public void onMatchStart(MatchStartEvent event) {
    Mixed.get().getStatistics().newMatch();
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onJoinMatch(PlayerParticipationStartEvent event) {
    addResultMap(event.getPlayer().getId());
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
    event
        .getMatch()
        .getPlayers()
        .forEach(
            player -> {
              Mixed.get().getStatistics().addPoint(player.getId(), 10);
              RankUP(player);
            });
    sendStatsData();
  }

  private void RankUP(MatchPlayer player) {
    UUID uuid = player.getId();
    ChatPrefix chatPrefix = new ChatPrefix();
    RankManager rankManager = new RankManager();
    Rank next = rankManager.getNextRank(uuid);

    TextComponent.Builder RANK_UP = Component.text();
    RANK_UP.append(Component.text("〓〓〓〓〓〓", NamedTextColor.YELLOW, TextDecoration.BOLD));
    RANK_UP.append(Component.text(" Rank UP! ", NamedTextColor.RED, TextDecoration.BOLD));
    RANK_UP.append(Component.text("〓〓〓〓〓〓", NamedTextColor.YELLOW, TextDecoration.BOLD));
    RANK_UP.append(Component.newline());
    RANK_UP.append(rankManager.getRank(MySQLSetterGetter.getRank(uuid)).getColoredName());
    RANK_UP.append(Component.text(" ⇒ ", NamedTextColor.GRAY, TextDecoration.BOLD));
    RANK_UP.append(next.getColoredName());
    RANK_UP.append(Component.newline());
    RANK_UP.append(Component.text("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓", NamedTextColor.YELLOW, TextDecoration.BOLD));

    TextComponent.Builder BROADCAST_RANK_UP = Component.text();
    BROADCAST_RANK_UP.append(Component.text(player.getPrefixedName()));
    BROADCAST_RANK_UP.append(Component.text(" has rank up to ", NamedTextColor.RED));
    BROADCAST_RANK_UP.append(next.getColoredName());

    if (rankManager.RankUP(uuid)) {
      Mixed.get().getAudience().players().sendMessage(BROADCAST_RANK_UP.build());
      player
          .getBukkit()
          .playSound(player.getBukkit().getLocation(), Sound.LEVEL_UP, 10, (float) 0.3);
      Mixed.get().getAudience().player(player.getId()).sendMessage(RANK_UP.build());

      chatPrefix.setPrefixPermission(uuid);
      MySQLSetterGetter.setRank(player.getId().toString(), next.getName());
    }
  }

  private void sendStatsData() {
    StoreStatistics store = Mixed.get().getStatistics().getStoreStatistics();

    Bukkit.getScheduler()
        .scheduleSyncDelayedTask(
            Mixed.get(),
            new Runnable() {
              @Override
              public void run() {
                sortStats(store.getKills(), "KILLS");
                sortStats(store.getDeaths(), "DEATHS");
                sortStats(store.getWools(), "WOOLS");
                sortStats(store.getMonuments(), "MONUMENTS");
                sortStats(store.getCores(), "CORES");
                sortStats(store.getFlags(), "FLAGS");
                sortStats(store.getPoints(), "POINTS");
                update();
              }
            },
            10L);
  }

  private void sortStats(Map<UUID, AtomicInteger> map, String column) {
    if (map.isEmpty()) return;
    map.entrySet().stream()
        .filter(stats -> stats.getValue().get() != 0)
        .filter(stats -> result.containsKey(stats.getKey()))
        .forEach(
            stats -> {
              Map<String, Integer> data = getStats(stats.getKey());
              int score = stats.getValue().get() + data.get(column);
              result.put(
                  stats.getKey(), result.get(stats.getKey()) + column + " = '" + score + "', ");
            });
  }

  private Map<String, Integer> getStats(UUID uuid) {
    Map<UUID, String> maps = new HashMap<>(includeMaps(uuid));
    Map<String, Integer> stats = new HashMap<>();
    String column =
        Arrays.toString(maps.values().toArray()).trim().replace("[", "").replace("]", "");
    String query =
        "SELECT "
            + column.substring(0, column.length() - 2)
            + " FROM STATS WHERE UUID = '"
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

  private void update() {
    result.forEach(
        (key, value) -> {
          Connection connection = null;
          PreparedStatement statement = null;
          String query =
              "UPDATE STATS SET "
                  + value.substring(0, value.length() - 2)
                  + " WHERE UUID = '"
                  + key
                  + "';";
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

    return Collections.unmodifiableMap(multipleMap);
  }

  private void addResultMap(UUID uuid) {
    if (result == null) result = new HashMap<>();
    if (result.containsKey(uuid)) return;
    result.put(uuid, "");
  }

  public MatchEvents(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
