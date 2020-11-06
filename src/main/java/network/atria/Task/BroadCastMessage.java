package network.atria.Task;

import java.util.*;
import network.atria.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadCastMessage {

  private final String prefix =
      ChatColor.WHITE
          + ""
          + ChatColor.BOLD
          + "["
          + ChatColor.BLUE
          + ""
          + ChatColor.BOLD
          + "TIP"
          + ChatColor.WHITE
          + ""
          + ChatColor.BOLD
          + "] "
          + ChatColor.DARK_AQUA;

  public void randomMessage() {

    new BukkitRunnable() {
      @Override
      public void run() {
        Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', random()));
      }
    }.runTaskTimer(Main.getInstance(), 0L, 20 * 60 * 5);
  }

  private String random() {
    final List<String> messages = Main.getInstance().getConfig().getStringList("Messages");
    final String msg;

    final int index = (new Random()).nextInt(messages.size());
    msg = messages.get(index);
    return msg;
  }
}
