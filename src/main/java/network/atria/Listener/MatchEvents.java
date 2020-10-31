package network.atria.Listener;

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

  @EventHandler
  public void onKill(MatchPlayerDeathEvent e) {
    final MatchPlayer victim = e.getVictim();
    MatchPlayer murder = null;

    if (e.getKiller() != null) {
      murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
      if (!murder.getParty().equals(victim.getParty())) {

        MySQLSetterGetter.addKills(murder.getId().toString(), 1);
        MySQLSetterGetter.addPoints(murder.getId().toString(), 5);
        MySQLSetterGetter.addDeaths(victim.getId().toString(), 1);
        LevelUp(murder);
      }
    } else {
      MySQLSetterGetter.addDeaths(victim.getId().toString(), 1);
    }
  }

  @EventHandler
  public void ctw(PlayerWoolPlaceEvent e) {
    final MatchPlayerState player = e.getPlayer();
    MySQLSetterGetter.addPoints(player.getId().toString(), 20);
    MySQLSetterGetter.addWools(player.getId().toString(), 1);
    LevelUp(player.getPlayer().get());
  }

  @EventHandler
  public void dtc(CoreLeakEvent e) {
    for (ParticipantState ps : e.getCore().getTouchingPlayers()) {
      MySQLSetterGetter.addCores(ps.getId().toString(), 1);
      MySQLSetterGetter.addPoints(ps.getId().toString(), 25);
      LevelUp(ps.getPlayer().get());
    }
  }

  @EventHandler
  public void ctf(FlagCaptureEvent e) {
    final MatchPlayer player = e.getCarrier();
    MySQLSetterGetter.addFlags(player.getId().toString(), 1);
    MySQLSetterGetter.addPoints(player.getId().toString(), 15);
    LevelUp(player);
  }

  @EventHandler
  public void dtm(DestroyableDestroyedEvent e) {
    for (DestroyableContribution dc : e.getDestroyable().getContributions()) {
      MySQLSetterGetter.addMonuments(dc.getPlayerState().getId().toString(), 1);
      MySQLSetterGetter.addPoints(dc.getPlayerState().getId().toString(), 25);
      LevelUp(dc.getPlayerState().getPlayer().get());
    }
  }

  @EventHandler
  public void endMatch(MatchFinishEvent e) {
    for (MatchPlayer player : e.getMatch().getPlayers()) {
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

      /*
      Rankの処理
       */
      MySQLSetterGetter.setRank(player.getId().toString(), Ranks.getNextRank(uuid));
      chatPrefix.setPrefixPermission(uuid);
    }
  }

  public MatchEvents(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
