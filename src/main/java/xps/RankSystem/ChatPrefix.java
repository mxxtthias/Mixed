package xps.RankSystem;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Listener;
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
}

