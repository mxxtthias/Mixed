package network.atria.Util;

import java.util.Arrays;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import network.atria.Database.MySQLSetterGetter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EffectUtils {

  public EffectUtils() {
    super();
  }

  public Inventory createGUI(int size, String title) {
    return Bukkit.createInventory(null, size, title);
  }

  public void setItemGUI(Inventory gui, int index, Material material, String name, String... lore) {
    final ItemStack item = new ItemStack(material, 1);
    final ItemMeta meta = item.getItemMeta();

    meta.setDisplayName(name);
    meta.setLore(Arrays.asList(lore));
    item.setItemMeta(meta);

    gui.setItem(index, item);
  }

  public boolean hasRequirePoint(UUID uuid, int require) {
    return MySQLSetterGetter.getPoints(uuid) >= require;
  }

  public boolean hasDonorRank(Player player) {
    return player.hasPermission("pgm.group.donor");
  }

  public Integer getRequirePoints(String section, String effect) {
    return KillEffectsConfig.getCustomConfig().getInt(section + "." + effect + ".points");
  }

  private String formatPoints(int points) {
    String rank = null;
    if (points <= 0) {
      rank = ChatColor.GOLD + "" + ChatColor.BOLD + "WOOD III";
    } else if (points <= 250) {
      rank = ChatColor.GOLD + "" + ChatColor.BOLD + "WOOD II";
    } else if (points <= 500) {
      rank = ChatColor.GOLD + "" + ChatColor.BOLD + "WOOD I";
    } else if (points <= 1000) {
      rank = ChatColor.GRAY + "" + ChatColor.BOLD + "STONE III";
    } else if (points <= 1500) {
      rank = ChatColor.GRAY + "" + ChatColor.BOLD + "STONE II";
    } else if (points <= 2000) {
      rank = ChatColor.GRAY + "" + ChatColor.BOLD + "STONE I";
    } else if (points <= 3000) {
      rank = ChatColor.WHITE + "" + ChatColor.BOLD + "IRON III";
    } else if (points <= 4000) {
      rank = ChatColor.WHITE + "" + ChatColor.BOLD + "IRON II";
    } else if (points <= 5000) {
      rank = ChatColor.WHITE + "" + ChatColor.BOLD + "IRON I";
    } else if (points <= 6000) {
      rank = ChatColor.GOLD + "" + ChatColor.BOLD + "GOLD III";
    } else if (points <= 8000) {
      rank = ChatColor.GOLD + "" + ChatColor.BOLD + "GOLD II";
    } else if (points <= 10000) {
      rank = ChatColor.GOLD + "" + ChatColor.BOLD + "GOLD I";
    } else if (points <= 12000) {
      rank = ChatColor.GREEN + "" + ChatColor.BOLD + "EMERALD III";
    } else if (points <= 15000) {
      rank = ChatColor.GREEN + "" + ChatColor.BOLD + "EMERALD II";
    } else if (points <= 20000) {
      rank = ChatColor.GREEN + "" + ChatColor.BOLD + "EMERALD I";
    } else if (points <= 30000) {
      rank = ChatColor.AQUA + "" + ChatColor.BOLD + "DIAMOND III";
    } else if (points <= 40000) {
      rank = ChatColor.AQUA + "" + ChatColor.BOLD + "DIAMOND II";
    } else if (points <= 50000) {
      rank = ChatColor.AQUA + "" + ChatColor.BOLD + "DIAMOND I";
    } else if (points <= 100000) {
      rank = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "OBSIDIAN";
    }
    return rank;
  }

  public String canUseEffects(UUID uuid, Integer require) {
    final int current = MySQLSetterGetter.getPoints(uuid);

    if (current >= require) {
      return ChatColor.GREEN + "" + ChatColor.BOLD + "✔ Unlocked";
    } else {
      final int result = require - current;
      return ChatColor.RED
          + ""
          + ChatColor.BOLD
          + "✖ "
          + formatPoints(require)
          + ChatColor.RED
          + " is required";
    }
  }
}
