package xps.Database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import xps.RankSystem.Ranks;

@ListenerScope(MatchScope.RUNNING)
public class PlayerStats implements Listener, MatchModule {

    private final String blank = "                    ";
    private final String line_1 = ChatColor.YELLOW + "" + ChatColor.BOLD + "〓〓〓〓〓〓〓" + ChatColor.RED + "" + ChatColor.BOLD + " Rank UP! " + ChatColor.YELLOW + "" + ChatColor.BOLD + "〓〓〓〓〓〓〓";
    private final String line_2 = ChatColor.YELLOW + "" + ChatColor.BOLD + "〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓";

    @EventHandler
    public void onKill(MatchPlayerDeathEvent e) {
        MatchPlayer victim = e.getVictim();
        MatchPlayer murder = null;


        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            MySQLSetterGetter.addKills(murder.getId().toString(), 1);
            MySQLSetterGetter.addPoints(murder.getId().toString(), 5);

            MySQLSetterGetter.addDeaths(victim.getId().toString(), 1);

            if (Ranks.canRankUp(murder.getId().toString())) {
                murder.sendMessage(line_1);
                murder.sendMessage(blank + Ranks.getRankCurrent(murder.getId().toString()) + ChatColor.GRAY + " ⇒ " + Ranks.getRankNext(murder.getId().toString()));
                murder.sendMessage(line_2);

                Bukkit.broadcastMessage(murder.getPrefixedName() + ChatColor.RED + " has been Rank up! " + ChatColor.YELLOW + Ranks.getRankNext(murder.getId().toString()));

                MySQLSetterGetter.setRank(murder.getId().toString(), Ranks.getNextRank(murder.getId().toString()));
            }
        } else {
            MySQLSetterGetter.addDeaths(victim.getId().toString(), 1);
        }
    }

    @EventHandler
    public void ctw(PlayerWoolPlaceEvent e) {
        MatchPlayerState player = e.getPlayer();
        MySQLSetterGetter.addPoints(player.getId().toString(), 20);

        if (player.getId() != null) {
            MySQLSetterGetter.addWools(player.getId().toString(), 1);
            if (Ranks.canRankUp(player.getId().toString())) {
                player.sendMessage(line_1);
                player.sendMessage(blank + Ranks.getRankCurrent(player.getId().toString()) + ChatColor.GRAY + "" + ChatColor.BOLD + " ⇒ " + Ranks.getRankNext(player.getId().toString()));
                player.sendMessage(line_2);

                Bukkit.broadcastMessage(player.getPlayer().get().getPrefixedName() + ChatColor.RED + " has been Rank up! " + ChatColor.YELLOW + Ranks.getRankNext(player.getId().toString()));

                MySQLSetterGetter.setRank(player.getId().toString(), Ranks.getNextRank(player.getId().toString()));
            }
        }
    }

    @EventHandler
    public void dtc(CoreLeakEvent e) {
        for (ParticipantState ps : e.getCore().getTouchingPlayers()) {
            MySQLSetterGetter.addCores(ps.getId().toString(), 1);
            MySQLSetterGetter.addPoints(ps.getId().toString(), 25);

            if (Ranks.canRankUp(ps.getId().toString())) {
                ps.sendMessage(line_1);
                ps.sendMessage(blank + Ranks.getRankCurrent(ps.getId().toString()) + ChatColor.GRAY + "" + ChatColor.BOLD + " ⇒ " + Ranks.getRankNext(ps.getId().toString()));
                ps.sendMessage(line_2);

                Bukkit.broadcastMessage(ps.getPlayer().get().getPrefixedName() + ChatColor.RED + " has been Rank up! " + ChatColor.YELLOW + Ranks.getNextRank(ps.getId().toString()));

                MySQLSetterGetter.setRank(ps.getId().toString(), Ranks.getNextRank(ps.getId().toString()));
            }
        }
    }

    @EventHandler
    public void ctf(FlagCaptureEvent e) {
        MatchPlayer player = e.getCarrier();
        if (player.getId() != null) {
            MySQLSetterGetter.addFlags(player.getId().toString(), 1);
            MySQLSetterGetter.addPoints(player.getId().toString(), 15);

            if (Ranks.canRankUp(player.getId().toString())) {
                player.sendMessage(line_1);
                player.sendMessage(blank + Ranks.getRankCurrent(player.getId().toString()) + ChatColor.GRAY + "" + ChatColor.BOLD + " ⇒ " + Ranks.getRankNext(player.getId().toString()));
                player.sendMessage(line_2);

                Bukkit.broadcastMessage(player.getPrefixedName() + ChatColor.RED + " has been Rank up! " + ChatColor.YELLOW + Ranks.getRankNext(player.getId().toString()));

                MySQLSetterGetter.setRank(player.getId().toString(), Ranks.getNextRank(player.getId().toString()));
            }
        }
    }

    @EventHandler
    public void dtm(DestroyableDestroyedEvent e) {
        for (DestroyableContribution dc : e.getDestroyable().getContributions()) {
            MySQLSetterGetter.addMonuments(dc.getPlayerState().getId().toString(), 1);
            MySQLSetterGetter.addPoints(dc.getPlayerState().getId().toString(), 25);

            if (Ranks.canRankUp(dc.getPlayerState().getId().toString())) {
                dc.getPlayerState().sendMessage(line_1);
                dc.getPlayerState().sendMessage(blank + Ranks.getRankCurrent(dc.getPlayerState().getId().toString()) + ChatColor.GRAY + "" + ChatColor.BOLD + " ⇒ " + Ranks.getRankNext(dc.getPlayerState().getId().toString()));
                dc.getPlayerState().sendMessage(line_2);

                Bukkit.broadcastMessage(dc.getPlayerState().getPlayer().get().getPrefixedName() + ChatColor.RED + " has been Rank up! " + ChatColor.YELLOW + Ranks.getRankNext(dc.getPlayerState().getId().toString()));

                MySQLSetterGetter.setRank(dc.getPlayerState().getId().toString(), Ranks.getNextRank(dc.getPlayerState().getId().toString()));
            }
        }
    }

    @EventHandler
    public void endMatch(MatchFinishEvent e) {
        for (MatchPlayer player : e.getMatch().getPlayers()) {
            MySQLSetterGetter.addPoints(player.getId().toString(), 10);
            if (Ranks.canRankUp(player.getId().toString())) {

                player.sendMessage(line_1);
                player.sendMessage(blank + Ranks.getRankCurrent(player.getId().toString()) + ChatColor.GRAY + "" + ChatColor.BOLD + " ⇒ " + Ranks.getRankNext(player.getId().toString()));
                player.sendMessage(line_2);

                Bukkit.broadcastMessage(player.getPrefixedName() + ChatColor.RED + " has been Rank up! " + ChatColor.YELLOW + Ranks.getRankNext(player.getId().toString()));

                MySQLSetterGetter.setRank(player.getId().toString(), Ranks.getNextRank(player.getId().toString()));
            }
        }
    }

    public PlayerStats(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
