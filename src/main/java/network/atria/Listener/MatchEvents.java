package network.atria.Listener;

import java.util.HashMap;
import java.util.UUID;
import network.atria.Database.MySQLSetterGetter;
import network.atria.RankSystem.ChatPrefix;
import network.atria.RankSystem.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.match.MatchModule;
import tc.oc.pgm.api.match.MatchScope;
import tc.oc.pgm.api.match.event.MatchFinishEvent;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.MatchPlayerState;
import tc.oc.pgm.api.player.ParticipantState;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;
import tc.oc.pgm.core.CoreLeakEvent;
import tc.oc.pgm.destroyable.DestroyableContribution;
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

  @EventHandler
  public void onKill(MatchPlayerDeathEvent event) {
    final MatchPlayer victim = event.getPlayer();
    MatchPlayer murder = null;

    addStats(deaths, victim.getId(), 1);

    if (event.getKiller() != null) {
      murder = event.getKiller().getParty().getPlayer(event.getKiller().getId());
      if (murder != null) {
        if (!murder.getParty().equals(victim.getParty())) {
          addStats(kills, murder.getId(), 1);
          addStats(points, murder.getId(), 5);
        }
      }
    }
  }

  @EventHandler
  public void ctw(PlayerWoolPlaceEvent event) {
    MatchPlayerState player = event.getPlayer();
    if (player.getPlayer().isPresent()) {
      addStats(points, player.getId(), 20);
      addStats(wools, player.getId(), 1);
    }
  }

  @EventHandler
  public void dtc(CoreLeakEvent event) {
    for (ParticipantState ps : event.getCore().getTouchingPlayers()) {
      if (ps.getPlayer().isPresent()) {
        addStats(points, ps.getId(), 25);
        addStats(cores, ps.getId(), 1);
      }
    }
  }

  @EventHandler
  public void ctf(FlagCaptureEvent event) {
    final MatchPlayer player = event.getCarrier();
    addStats(points, player.getId(), 15);
    addStats(flags, player.getId(), 1);
  }

  @EventHandler
  public void dtm(DestroyableDestroyedEvent event) {
    for (DestroyableContribution dc : event.getDestroyable().getContributions()) {
      if (dc.getPlayerState().getPlayer().isPresent()) {
        addStats(monuments, dc.getPlayerState().getId(), 1);
        addStats(points, dc.getPlayerState().getId(), 25);
      }
    }
  }

  @EventHandler
  public void endMatch(MatchFinishEvent event) {
    if (!kills.isEmpty()) {
      for (UUID uuid : kills.keySet()) {
        MySQLSetterGetter.addKills(uuid.toString(), kills.get(uuid));
        removeCached(uuid, kills);
      }
    }
    if (!deaths.isEmpty()) {
      for (UUID uuid : deaths.keySet()) {
        MySQLSetterGetter.addDeaths(uuid.toString(), deaths.get(uuid));
        removeCached(uuid, deaths);
      }
    }
    if (!points.isEmpty()) {
      for (UUID uuid : points.keySet()) {
        MySQLSetterGetter.addPoints(uuid.toString(), points.get(uuid));
        removeCached(uuid, points);
      }
    }
    if (!wools.isEmpty()) {
      for (UUID uuid : wools.keySet()) {
        MySQLSetterGetter.addWools(uuid.toString(), wools.get(uuid));
        removeCached(uuid, wools);
      }
    }
    if (!cores.isEmpty()) {
      for (UUID uuid : cores.keySet()) {
        MySQLSetterGetter.addCores(uuid.toString(), cores.get(uuid));
        removeCached(uuid, cores);
      }
    }
    if (!flags.isEmpty()) {
      for (UUID uuid : flags.keySet()) {
        MySQLSetterGetter.addFlags(uuid.toString(), flags.get(uuid));
        removeCached(uuid, flags);
      }
    }
    if (!monuments.isEmpty()) {
      for (UUID uuid : monuments.keySet()) {
        MySQLSetterGetter.addMonuments(uuid.toString(), monuments.get(uuid));
        removeCached(uuid, monuments);
      }
    }
    for (MatchPlayer player : event.getMatch().getPlayers()) {
      MySQLSetterGetter.addPoints(player.getId().toString(), 10);
      LevelUp(player);
    }
  }

  private void LevelUp(MatchPlayer player) {
    final UUID uuid = player.getId();
    final ChatPrefix chatPrefix = new ChatPrefix();
    final String blank = "            ";
    final String line_1 =
        ChatColor.YELLOW
            + ""
            + ChatColor.BOLD
            + "〓〓〓〓〓〓"
            + ChatColor.RED
            + ""
            + ChatColor.BOLD
            + " Rank UP! "
            + ChatColor.YELLOW
            + ""
            + ChatColor.BOLD
            + "〓〓〓〓〓〓";
    final String line_2 = ChatColor.YELLOW + "" + ChatColor.BOLD + "〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓";

    if (Ranks.canRankUp(uuid)) {
      Bukkit.broadcastMessage(
          player.getPrefixedName()
              + ChatColor.RED
              + " has ranked up to "
              + ChatColor.YELLOW
              + Ranks.getNextRank(uuid));
      player
          .getBukkit()
          .playSound(player.getBukkit().getLocation(), Sound.LEVEL_UP, 10, (float) 0.3);
      player.getBukkit().sendMessage(line_1);
      player
          .getBukkit()
          .sendMessage(
              blank
                  + Ranks.getRankCurrent(uuid).replace("_", " ").toUpperCase()
                  + ChatColor.GRAY
                  + ""
                  + ChatColor.BOLD
                  + " ⇒ "
                  + Ranks.getRankNext(uuid));
      player.getBukkit().sendMessage(line_2);

      MySQLSetterGetter.setRank(player.getId().toString(), Ranks.getNextRank(uuid));
      chatPrefix.setPrefixPermission(uuid);
    }
  }

  private void addStats(HashMap<UUID, Integer> map, UUID uuid, Integer score) {
    if (map.containsKey(uuid)) {
      final int stats = map.get(uuid) != 0 ? map.get(uuid) : 0;
      map.replace(uuid, stats + score);
    } else {
      map.put(uuid, 0);
      map.replace(uuid, score);
    }
  }

  private void removeCached(UUID uuid, HashMap<UUID, Integer> cache) {
    if (!cache.containsKey(uuid)) return;
    cache.remove(uuid);
  }

  public MatchEvents(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
