package xps.RankSystem;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.PGM;
import tc.oc.pgm.events.PlayerJoinMatchEvent;
import tc.oc.pgm.events.PlayerPartyChangeEvent;

public class ChatPrefix implements Listener {

    @EventHandler
    public void matchJoinPrefix(PlayerJoinMatchEvent e) {
        Player player = e.getPlayer().getBukkit();
        player.setDisplayName(PGM.get().getNameDecorationRegistry().getDecoratedName(player, e.getNewParty()) + Ranks.getPrefix(player.getUniqueId().toString()));
    }

    @EventHandler
    public void partyChange(PlayerPartyChangeEvent e) {
        Player player = e.getPlayer().getBukkit();

        player.setDisplayName(PGM.get().getNameDecorationRegistry().getDecoratedName(player, e.getNewParty()) + Ranks.getPrefix(player.getUniqueId().toString()));
    }

    public ChatPrefix(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
