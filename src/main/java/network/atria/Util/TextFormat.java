package network.atria.Util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class TextFormat {

  public static String format(Component component) {
    return LegacyComponentSerializer.legacySection().serialize(component);
  }

  public static Component formatSection(String input) {
    return LegacyComponentSerializer.legacySection().deserialize(input);
  }

  public static Component formatAmpersand(String input) {
    return LegacyComponentSerializer.legacyAmpersand().deserialize(input);
  }
}
