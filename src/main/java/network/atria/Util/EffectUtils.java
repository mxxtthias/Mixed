package network.atria.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Mixed;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EffectUtils {

  /*

  GUI

   */

  public Inventory createGUI(Component title) {
    return Bukkit.createInventory(null, 27, TextFormat.format(title));
  }

  public void setItemGUI(
      Inventory gui, int index, Material material, Component name, Component... lore) {
    ItemStack item = new ItemStack(material, 1);
    ItemMeta meta = item.getItemMeta();
    List<String> lores = new ArrayList<>();
    Arrays.stream(lore).forEachOrdered(x -> lores.add(TextFormat.format(x)));
    meta.setDisplayName(TextFormat.format(name));
    meta.setLore(lores);
    item.setItemMeta(meta);

    gui.setItem(index, item);
    lores.clear();
  }

  private String formatPoints(int points) {
    String rank = null;
    if (points <= 0) {
      rank = format("WOOD III", NamedTextColor.GOLD);
    } else if (points <= 250) {
      rank = format("WOOD II", NamedTextColor.GOLD);
    } else if (points <= 500) {
      rank = format("WOOD I", NamedTextColor.GOLD);
    } else if (points <= 1000) {
      rank = format("STONE III", NamedTextColor.GRAY);
    } else if (points <= 1500) {
      rank = format("STONE II", NamedTextColor.GRAY);
    } else if (points <= 2000) {
      rank = format("STONE I", NamedTextColor.GRAY);
    } else if (points <= 3000) {
      rank = format("IRON III", NamedTextColor.WHITE);
    } else if (points <= 4000) {
      rank = format("IRON II", NamedTextColor.WHITE);
    } else if (points <= 5000) {
      rank = format("IRON I", NamedTextColor.WHITE);
    } else if (points <= 6000) {
      rank = format("GOLD III", NamedTextColor.YELLOW);
    } else if (points <= 8000) {
      rank = format("GOLD I", NamedTextColor.YELLOW);
    } else if (points <= 10000) {
      rank = format("GOLD I", NamedTextColor.YELLOW);
    } else if (points <= 12000) {
      rank = format("EMERALD III", NamedTextColor.GREEN);
    } else if (points <= 15000) {
      rank = format("EMERALD II", NamedTextColor.GREEN);
    } else if (points <= 20000) {
      rank = format("EMERALD I", NamedTextColor.GREEN);
    } else if (points <= 30000) {
      rank = format("DIAMOND III", NamedTextColor.AQUA);
    } else if (points <= 40000) {
      rank = format("DIAMOND II", NamedTextColor.AQUA);
    } else if (points <= 50000) {
      rank = format("DIAMOND I", NamedTextColor.AQUA);
    } else if (points <= 100000) {
      rank = format("OBSIDIAN", NamedTextColor.DARK_PURPLE);
    }
    return rank;
  }

  private static String format(String rank, NamedTextColor color) {
    return TextFormat.format(Component.text(rank, color, TextDecoration.BOLD));
  }

  public TextComponent canUseEffects(UUID uuid, Integer require) {
    int current = MySQLSetterGetter.getPoints(uuid);

    if (current >= require) {
      return Component.text("✔ Unlocked", NamedTextColor.GREEN, TextDecoration.BOLD);
    } else {
      return TextComponent.ofChildren(
          Component.text("✖ ", NamedTextColor.RED, TextDecoration.BOLD),
          Component.text(formatPoints(require)),
          Component.text(" is required", NamedTextColor.RED, TextDecoration.BOLD));
    }
  }

  /*

  Effects, Sounds, Particles

   */

  public void sendEffectPacket(Player player, Object packet) {
    Mixed.get().getParticles().createPlayerConnection(player).sendPacket(packet);
  }
}
