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

public class KillEffectsGUI extends EffectUtils implements Listener {

  public static Inventory effect;
  protected String title = "Kill Effect Selector";
  protected int size = 27;
  private final FileConfiguration config = KillEffectsConfig.getCustomConfig();

  public KillEffectsGUI(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    effect = createGUI(size, title);
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

    reset_meta.setDisplayName(ChatColor.RED + "Reset Kill Effect");
    reset.setItemMeta(reset_meta);

    final ItemStack back = new ItemStack(Material.ARROW, 1);
    final ItemMeta back_meta = back.getItemMeta();

    back_meta.setDisplayName(ChatColor.RED + "Go to previous page ➡");
    back.setItemMeta(back_meta);

    for (String effect : config.getConfigurationSection("KILL_EFFECT").getKeys(false)) {
      final Material material =
          Material.valueOf(config.getString("KILL_EFFECT." + effect + ".material").toUpperCase());
      final int number = config.getInt("KILL_EFFECT." + effect + ".number");

      setItemGUI(
          KillEffectsGUI.effect,
          number,
          material,
          effect,
          "",
          canUseEffects(uuid, effectPoint(effect)));
    }
    setItemGUI(
        effect,
        18,
        Material.GOLD_NUGGET,
        ChatColor.GOLD + "DONOR",
        "",
        ChatColor.RED + "- Donor Only -");
    effect.setItem(26, reset);
    effect.setItem(8, back);
  }

  private Integer effectPoint(String effectName) {
    return config.getInt("KILL_EFFECT." + effectName + ".points");
  }

  @EventHandler
  public void onGuiClick(final InventoryClickEvent e) {
    if (e.getView().getTitle().equals(title)) {
      e.setCancelled(true);

      final ItemStack clickedItem = e.getCurrentItem();
      final Player player = (Player) e.getWhoClicked();
      final String getItemName = clickedItem.getItemMeta().getDisplayName();
      final Set<String> effect = config.getConfigurationSection("KILL_EFFECT").getKeys(false);

      if (effect.contains(getItemName)) {
        selectEffect(player, getItemName, false);
      } else {
        switch (getItemName.substring(2)) {
          case "Go to previous page ➡":
            player.openInventory(DefaultGUI.gui);
            break;
          case "Reset Kill Effect":
            MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "NONE");
            player.sendMessage(ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + " Kill Effect");
            break;
          case "DONOR":
            if (player.hasPermission("pgm.group.donor")) {
              MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "DONOR");
              player.sendMessage(
                  ChatColor.GREEN + "You selected" + ChatColor.YELLOW + " DONOR kill effect.");
            } else {
              player.sendMessage(ChatColor.RED + "You don't have the donor rank");
            }
            break;
        }
      }
    }
  }

  private void selectEffect(Player player, String effect, Boolean donor) {
    final UUID uuid = player.getUniqueId();
    final int require = getRequirePoints("KILL_EFFECT", effect);

    if (donor) {
      if (hasDonorRank(player)) {
        if (hasRequirePoint(uuid, require)) {
          MySQLSetterGetter.setKillEffect(uuid.toString(), effect);
          player.sendMessage(
              ChatColor.GREEN
                  + "You selected "
                  + ChatColor.YELLOW
                  + effect.toUpperCase()
                  + " kill effect.");
        } else {
          player.sendMessage(ChatColor.RED + "You don't have enough points");
        }
      }
    } else {
      if (hasRequirePoint(uuid, require)) {
        MySQLSetterGetter.setKillEffect(uuid.toString(), effect);
        player.sendMessage(
            ChatColor.GREEN
                + "You selected "
                + ChatColor.YELLOW
                + effect.toUpperCase()
                + " kill effect.");
      } else {
        player.sendMessage(ChatColor.RED + "You don't have enough points");
      }
    }
  }
}
