package network.atria.RankSystem;

import java.util.UUID;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;

public class ChatPrefix {

  public void setPrefixPermission(UUID uuid) {
    final LuckPerms api = LuckPermsProvider.get();
    final User user = api.getUserManager().getUser(uuid);

    if (user != null) {
      removeGroup(user, Ranks.getCurrentRank(uuid));
      addGroup(user, Ranks.getNextRank(uuid));
      api.getUserManager().saveUser(user);
    }
  }

  private static void addGroup(User user, String groupName) {
    final PermissionNode node = PermissionNode.builder("pgm.group." + groupName).build();
    user.data().add(node);
  }

  private static void removeGroup(User user, String groupName) {
    final PermissionNode node = PermissionNode.builder("pgm.group." + groupName).build();
    user.data().remove(node);
  }
}
