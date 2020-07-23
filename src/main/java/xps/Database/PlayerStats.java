package xps.Database;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.api.match.MatchModule;
import tc.oc.pgm.api.match.MatchScope;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.MatchPlayerState;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;
import tc.oc.pgm.core.CoreBlockBreakEvent;
import tc.oc.pgm.destroyable.DestroyableEvent;
import tc.oc.pgm.destroyable.DestroyableHealthChangeEvent;
import tc.oc.pgm.events.ListenerScope;
import tc.oc.pgm.flag.event.FlagCaptureEvent;
import tc.oc.pgm.wool.PlayerWoolPlaceEvent;
import xps.Database.MySQLSetterGetter;

import java.util.Optional;

@ListenerScope(MatchScope.RUNNING)
public class PlayerStats implements Listener, MatchModule {

    @EventHandler
    public void onKill(MatchPlayerDeathEvent e) {
        MatchPlayer victim = e.getVictim();
        MatchPlayer murder = null;

        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            MySQLSetterGetter.addKills(murder.getId().toString(), 1);
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
        }
    }

    @EventHandler
    public void dtc(CoreBlockBreakEvent e) {
        MatchPlayerState player = e.getPlayer();

        if(player.getId() != null) {
            MySQLSetterGetter.addCores(player.getId().toString(), 1);
        }
    }

    @EventHandler
    public void ctf(FlagCaptureEvent e) {
        MatchPlayer player = e.getCarrier();

        if(player.getId() != null) {
            MySQLSetterGetter.addFlags(player.getId().toString(), 1);
        }
    }

    public PlayerStats(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
