package network.atria.Ranks;

import java.util.UUID;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import network.atria.Database.MySQLSetterGetter;

public class ChatPrefix {

  public void setPrefixPermission(UUID uuid) {
    LuckPerms api = LuckPermsProvider.get();
    User user = api.getUserManager().getUser(uuid);
    RankManager rankManager = new RankManager();

    if (user != null) {
      removeGroup(user, rankManager.getRank(MySQLSetterGetter.getRank(uuid)).getName());
      addGroup(user, rankManager.getNextRank(uuid).getName());
      api.getUserManager().saveUser(user);
    }
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
