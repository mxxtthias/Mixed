package network.atria.RankSystem;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tc.oc.pgm.api.PGM;
import tc.oc.pgm.api.event.NameDecorationChangeEvent;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.tablist.MatchTabManager;
import tc.oc.pgm.util.tablist.PlayerTabEntry;

import java.util.UUID;

public class ChatPrefix implements Listener {

    public void setPrefixPermission(UUID uuid) {
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getUserManager().getUser(uuid);

        removeGroup(user, Ranks.getCurrentRank(uuid.toString()));

        addGroup(user, Ranks.getNextRank(uuid.toString()));

        api.getUserManager().saveUser(user);
    }

    private static void addGroup(User user, String groupName) {
        PermissionNode node = PermissionNode.builder("pgm.group." + groupName).build();
        user.data().add(node);
    }

    private static void removeGroup(User user, String groupName) {
        PermissionNode node = PermissionNode.builder("pgm.group." + groupName).build();
        user.data().remove(node);
    }

    @EventHandler
    public void onNameDecorationChange(NameDecorationChangeEvent e) {
        Player player = Bukkit.getPlayer(e.getUUID());
        MatchPlayer matchPlayer = PGM.get().getMatchManager().getPlayer(player);

        matchPlayer.getBukkit().setDisplayName(PGM.get().getNameDecorationRegistry().getDecoratedName(player, matchPlayer.getParty()));

        MatchTabManager tab = PGM.get().getMatchTabManager();

        if(tab != null) {
            PlayerTabEntry tabEntry = (PlayerTabEntry) tab.getPlayerEntryOrNull(player);
            if(tabEntry != null) {
                tabEntry.invalidate();
            }
        }
    }
}

