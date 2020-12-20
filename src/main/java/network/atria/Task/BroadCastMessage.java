package network.atria.Task;

import java.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Mixed;
import network.atria.Util.TextFormat;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class BroadCastMessage {

  public void randomMessage() {
    new BukkitRunnable() {
      @Override
      public void run() {
        TextComponent.Builder component = Component.text();
        component.append(Component.text("[", NamedTextColor.WHITE, TextDecoration.BOLD));
        component.append(Component.text("TIP", NamedTextColor.BLUE, TextDecoration.BOLD));
        component.append(Component.text("] ", NamedTextColor.WHITE, TextDecoration.BOLD));
        component.append(TextFormat.formatAmpersand(random()));

        Mixed.get().getAudience().players().sendMessage(component.build());
      }
    }.runTaskTimer(Mixed.get(), 0L, 20 * 60 * 5);
  }

  private String random() {
    List<String> messages = Mixed.get().getConfig().getStringList("Tips");
    String msg;

    int index = (new Random()).nextInt(messages.size());
    msg = messages.get(index);
    return msg;
  }
}
