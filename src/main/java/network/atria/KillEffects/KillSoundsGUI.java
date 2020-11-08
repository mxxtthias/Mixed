package network.atria.KillEffects;

import java.util.Set;
import java.util.UUID;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Util.EffectUtils;
import network.atria.Util.KillEffectsConfig;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class KillSoundsGUI extends EffectUtils implements Listener {

  public static Inventory sound;
  protected String title = "Kill Sound Selector";
  protected int size = 27;
  private final FileConfiguration config = KillEffectsConfig.getCustomConfig();

  public KillSoundsGUI(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    sound = createGUI(size, title);
  }

  @EventHandler
  public void openGUI(InventoryOpenEvent e) {
    if (e.getView().getTitle().equals(title)) {
      final UUID uuid = e.getPlayer().getUniqueId();
      addIconItems(uuid);
    }
  }

  private void addIconItems(UUID uuid) {
    final ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
    final ItemMeta reset_meta = reset.getItemMeta();

    reset_meta.setDisplayName(ChatColor.RED + "Reset Kill Sound");
    reset.setItemMeta(reset_meta);

    final ItemStack back = new ItemStack(Material.ARROW, 1);
    final ItemMeta back_meta = back.getItemMeta();

    back_meta.setDisplayName(ChatColor.RED + "Go to previous page ➡");
    back.setItemMeta(back_meta);

    for (String effect : config.getConfigurationSection("KILL_SOUND").getKeys(false)) {
      final int number = config.getInt("KILL_SOUND." + effect + ".number");
      final Material material =
          Material.valueOf(config.getString("KILL_SOUND." + effect + ".material").toUpperCase());

      setItemGUI(sound, number, material, effect, canUseEffects(uuid, getSoundPoint(effect)));
    }
    sound.setItem(26, reset);
    sound.setItem(8, back);
  }

  private Integer getSoundPoint(String sound) {
    return config.getInt("KILL_SOUND." + sound + ".points");
  }

  @EventHandler
  public void onGuiClick(final InventoryClickEvent e) {
    if (e.getView().getTitle().equals(title)) {
      e.setCancelled(true);

      final ItemStack clickedItem = e.getCurrentItem();
      final Player player = (Player) e.getWhoClicked();
      final String getItemName = clickedItem.getItemMeta().getDisplayName();
      final Set<String> sound = config.getConfigurationSection("KILL_SOUND").getKeys(false);

      if (sound.contains(getItemName)) {
        selectSound(player, getItemName);
      } else {
        switch (getItemName.substring(2)) {
          case "Go to previous page ➡":
            player.openInventory(DefaultGUI.gui);
            break;
          case "Reset Kill Sound":
            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "NONE");
            player.sendMessage(ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + "Kill Sound");
            break;
          case "DEFAULT":
            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "DEFAULT");
            player.sendMessage(
                ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "DEFAULT Kill Sound");
            break;
        }
      }
    }
  }

  private void selectSound(Player player, String sound) {
    final UUID uuid = player.getUniqueId();
    final int require = getRequirePoints("KILL_SOUND", sound);

    if (hasRequirePoint(uuid, require)) {
      MySQLSetterGetter.setKillSound(uuid.toString(), sound);
      player.sendMessage(
          ChatColor.GREEN
              + "You selected "
              + ChatColor.YELLOW
              + sound.toUpperCase()
              + " Kill Sound");
    } else {
      player.sendMessage(ChatColor.RED + "You don't have enough points");
    }
  }
}
