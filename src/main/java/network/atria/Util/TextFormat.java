package network.atria.Util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

public class TextFormat {

  public static String format(Component component) {
    return LegacyComponentSerializer.legacySection().serialize(component);
  }

  public static String format(String content) {
    return ChatColor.stripColor(content);
  }

  public static Component formatAmpersand(String content) {
    return LegacyComponentSerializer.legacyAmpersand().deserialize(content);
  }

  public static Component formatSection(String content) {
    return LegacyComponentSerializer.legacySection().deserialize(content);
  }
}
