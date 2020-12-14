package network.atria.Listener;

import java.sql.*;
import java.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQL;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Mixed;
import network.atria.RankSystem.ChatPrefix;
import network.atria.RankSystem.Rank;
import network.atria.RankSystem.Ranks;
import network.atria.Util.TextFormat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.match.MatchModule;
import tc.oc.pgm.api.match.MatchScope;
import tc.oc.pgm.api.match.event.MatchFinishEvent;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.MatchPlayerState;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;
import tc.oc.pgm.core.CoreLeakEvent;
import tc.oc.pgm.destroyable.DestroyableDestroyedEvent;
import tc.oc.pgm.events.ListenerScope;
import tc.oc.pgm.flag.event.FlagCaptureEvent;
import tc.oc.pgm.wool.PlayerWoolPlaceEvent;

@ListenerScope(MatchScope.RUNNING)
public class MatchEvents implements Listener, MatchModule {

  private final HashMap<UUID, Integer> kills = new HashMap<>();
  private final HashMap<UUID, Integer> deaths = new HashMap<>();
  private final HashMap<UUID, Integer> points = new HashMap<>();
  private final HashMap<UUID, Integer> wools = new HashMap<>();
  private final HashMap<UUID, Integer> cores = new HashMap<>();
  private final HashMap<UUID, Integer> flags = new HashMap<>();
  private final HashMap<UUID, Integer> monuments = new HashMap<>();
  private final HashMap<UUID, String> result = new HashMap<>();

  @EventHandler(priority = EventPriority.HIGH)
  public void onKill(MatchPlayerDeathEvent event) {
    MatchPlayer victim = event.getPlayer();
    MatchPlayer murder;

    if (event.isTeamKill()) return;

    addStats(deaths, victim.getId(), 1);
    addResultMap(victim.getId());

    if (event.getKiller() != null && !event.isSelfKill() && !event.isSuicide()) {
      murder = event.getKiller().getParty().getPlayer(event.getKiller().getId());
      if (murder != null) {
        if (!murder.getParty().equals(victim.getParty())) {
          addStats(kills, murder.getId(), 1);
          addStats(points, murder.getId(), 5);
          addResultMap(murder.getId());
          LevelUp(murder);
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void ctw(PlayerWoolPlaceEvent event) {
    MatchPlayerState player = event.getPlayer();
    addStats(points, player.getId(), 20);
    addStats(wools, player.getId(), 1);
    addResultMap(player.getId());
    LevelUp(player.getPlayer().get());
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void dtc(CoreLeakEvent event) {
    event
        .getCore()
        .getTouchingPlayers()
        .parallelStream()
        .forEach(
            player -> {
              addStats(points, player.getId(), 25);
              addStats(cores, player.getId(), 1);
              LevelUp(player.getPlayer().get());
              addResultMap(player.getId());
            });
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void ctf(FlagCaptureEvent event) {
    MatchPlayer player = event.getCarrier();
    addStats(points, player.getId(), 15);
    addStats(flags, player.getId(), 1);
    LevelUp(player);
    addResultMap(player.getId());
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void dtm(DestroyableDestroyedEvent event) {
    event
        .getDestroyable()
        .getContributions()
        .parallelStream()
        .forEach(
            player -> {
              addStats(monuments, player.getPlayerState().getId(), 1);
              addStats(points, player.getPlayerState().getId(), 25);
              addResultMap(player.getPlayerState().getId());
              LevelUp(player.getPlayerState().getPlayer().get());
            });
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void endMatch(MatchFinishEvent event) {
    event
        .getMatch()
        .getPlayers()
        .parallelStream()
        .forEach(
            player -> {
              addStats(points, player.getId(), 10);
              LevelUp(player);
            });
    sendStatsData();
  }

  private void LevelUp(MatchPlayer player) {
    UUID uuid = player.getId();
    ChatPrefix chatPrefix = new ChatPrefix();
    Ranks ranks = new Ranks();
    Rank next = ranks.getNextRank(uuid);

    TextComponent RANK_UP =
        TextComponent.ofChildren(
            Component.text("〓〓〓〓〓〓", NamedTextColor.YELLOW, TextDecoration.BOLD),
            Component.text(" Rank UP! ", NamedTextColor.RED, TextDecoration.BOLD),
            Component.text("〓〓〓〓〓〓", NamedTextColor.YELLOW, TextDecoration.BOLD),
            Component.newline(),
            ranks.getRank(MySQLSetterGetter.getRank(uuid)).getColoredName(),
            Component.text(" ⇒ ", NamedTextColor.GRAY, TextDecoration.BOLD),
            next.getColoredName(),
            Component.newline(),
            Component.text("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓", NamedTextColor.YELLOW, TextDecoration.BOLD));

    TextComponent BROADCAST_RANK_UP =
        TextComponent.ofChildren(
            Component.text(player.getPrefixedName()),
            Component.text(" has rank up to ", NamedTextColor.RED),
            next.getColoredName());

    if (ranks.RankUP(uuid)) {
      Bukkit.broadcastMessage(TextFormat.format(BROADCAST_RANK_UP));
      player
          .getBukkit()
          .playSound(player.getBukkit().getLocation(), Sound.LEVEL_UP, 10, (float) 0.3);
      Mixed.get().getAudience().player(player.getId()).sendMessage(RANK_UP);

      chatPrefix.setPrefixPermission(uuid);
      MySQLSetterGetter.setRank(player.getId().toString(), next.getName());
    }
  }

  private void addStats(HashMap<UUID, Integer> map, UUID uuid, Integer score) {
    if (map.containsKey(uuid)) {
      int current = map.get(uuid) != 0 ? map.get(uuid) : 0;
      int stats = current + score;
      map.replace(uuid, stats);
    } else {
      map.put(uuid, 0);
      map.replace(uuid, score);
    }
  }

  private void sendStatsData() {
    sortStats(kills, "KILLS");
    sortStats(deaths, "DEATHS");
    sortStats(wools, "WOOLS");
    sortStats(monuments, "MONUMENTS");
    sortStats(cores, "CORES");
    sortStats(flags, "FLAGS");
    update();

    points
        .entrySet()
        .parallelStream()
        .filter(p -> p.getValue() != 0)
        .forEach(p -> MySQLSetterGetter.addPoints(p.getKey().toString(), p.getValue()));

    clearMap();
  }

  private void sortStats(HashMap<UUID, Integer> map, String column) {
    if (map.isEmpty()) return;
    map.entrySet()
        .parallelStream()
        .filter(stats -> stats.getValue() != 0)
        .filter(stats -> result.containsKey(stats.getKey()))
        .forEach(
            stats -> {
              Map<String, Integer> data = getStats(stats.getKey());
              int score = stats.getValue() + data.get(column);
              result.replace(
                  stats.getKey(), result.get(stats.getKey()) + column + " = '" + score + "', ");
            });
    map.clear();
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
    result
        .entrySet()
        .parallelStream()
        .forEach(
            map -> {
              Connection connection = null;
              PreparedStatement statement = null;
              String query =
                  "UPDATE STATS SET "
                      + map.getValue().substring(0, map.getValue().length() - 2)
                      + " WHERE UUID = '"
                      + map.getKey()
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

    multipleMap.put(uuid, "");
    if (!kills.isEmpty() && kills.containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "KILLS, ");
    if (!deaths.isEmpty() && deaths.containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "DEATHS, ");
    if (!cores.isEmpty() && cores.containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "CORES, ");
    if (!monuments.isEmpty() && monuments.containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "MONUMENTS, ");
    if (!flags.isEmpty() && flags.containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "FLAGS, ");
    if (!wools.isEmpty() && wools.containsKey(uuid))
      multipleMap.put(uuid, multipleMap.get(uuid) + "WOOLS, ");

    return Collections.unmodifiableMap(multipleMap);
  }

  private void addResultMap(UUID uuid) {
    if (result.containsKey(uuid)) return;
    result.put(uuid, "");
  }

  private void clearMap() {
    if (!points.isEmpty()) points.clear();
    if (!result.isEmpty()) result.clear();
    if (!kills.isEmpty()) kills.clear();
    if (!deaths.isEmpty()) deaths.clear();
    if (!cores.isEmpty()) cores.clear();
    if (!monuments.isEmpty()) monuments.clear();
    if (!flags.isEmpty()) flags.clear();
    if (!wools.isEmpty()) wools.clear();
  }

  public MatchEvents(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
