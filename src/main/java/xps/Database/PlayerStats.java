package xps.Database;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.match.MatchModule;
import tc.oc.pgm.api.match.MatchScope;
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
import xps.Main;

@ListenerScope(MatchScope.RUNNING)
public class PlayerStats implements Listener, MatchModule {

    @EventHandler
    public void onKill(MatchPlayerDeathEvent e) {
        MatchPlayer victim = e.getVictim();
        MatchPlayer murder = null;

        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            MySQLSetterGetter.addKills(murder.getId().toString(), 1);
            MySQLSetterGetter.addPoints(murder.getId().toString(), ready());
            MySQLSetterGetter.addDeaths(victim.getId().toString(), 1);
        } else {
            MySQLSetterGetter.addDeaths(victim.getId().toString(), 1);
        }
    }

    @EventHandler
    public void ctw(PlayerWoolPlaceEvent e) {
        MatchPlayerState player = e.getPlayer();

        if (player.getId() != null) {
            MySQLSetterGetter.addWools(player.getId().toString(), 1);
            MySQLSetterGetter.addPoints(player.getId().toString(), ready());
        }
    }

    @EventHandler
    public void dtc(CoreLeakEvent e) {
        for (ParticipantState ps : e.getCore().getTouchingPlayers()) {
            MySQLSetterGetter.addCores(ps.getId().toString(), 1);
            MySQLSetterGetter.addPoints(ps.getId().toString(), ready());
        }
    }

    @EventHandler
    public void ctf(FlagCaptureEvent e) {
        MatchPlayer player = e.getCarrier();

        if(player.getId() != null) {
            MySQLSetterGetter.addFlags(player.getId().toString(), 1);
            MySQLSetterGetter.addPoints(player.getId().toString(), ready());
        }
    }

    @EventHandler
    public void dtm(DestroyableDestroyedEvent e) {
        for (DestroyableContribution dc : e.getDestroyable().getContributions()) {
            MySQLSetterGetter.addMonuments(dc.getPlayerState().getId().toString(), 1);
            MySQLSetterGetter.addPoints(dc.getPlayerState().getId().toString(), ready());
        }
    }

    @EventHandler
    public void removeBannedPlayerStats(PlayerQuitEvent e) {
        if(e.getPlayer().isBanned()) {
            Main.mysql.update("DELETE FROM STATS WHERE UUID= '" + e.getPlayer().getUniqueId() + "'");
        }
    }

    private Integer ready() {
        return 0;
    }

    public PlayerStats(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
