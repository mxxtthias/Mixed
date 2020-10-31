package network.atria.KillEffects;

import static network.atria.KillEffects.DefaultGUI.createGuiItem;

import java.util.UUID;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Util.KillEffectsConfig;
import network.atria.Util.getPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KillEffectsGUI implements Listener {

  public static Inventory EffectsGUI;

  public KillEffectsGUI() {
    EffectsGUI = Bukkit.createInventory(null, 27, "Kill Effect Selector");
  }

  @EventHandler
  public void getPlayer(InventoryOpenEvent e) {
    if (e.getView().getTitle().equals("Kill Effect Selector")) {
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

    EffectsGUI.setItem(
        0,
        createGuiItem(
            Material.REDSTONE,
            ChatColor.AQUA + "BLOOD",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("BLOOD"))));
    EffectsGUI.setItem(
        1,
        createGuiItem(
            Material.LAVA_BUCKET,
            ChatColor.AQUA + "FLAME",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("FLAME"))));
    EffectsGUI.setItem(
        2,
        createGuiItem(
            Material.GOLDEN_APPLE,
            ChatColor.AQUA + "HEART",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("HEART"))));
    EffectsGUI.setItem(
        3,
        createGuiItem(
            Material.FIREWORK_CHARGE,
            ChatColor.AQUA + "SMOKE",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("SMOKE"))));
    EffectsGUI.setItem(
        4,
        createGuiItem(
            Material.ENCHANTED_BOOK,
            ChatColor.AQUA + "MAGIC",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("MAGIC"))));
    EffectsGUI.setItem(
        5,
        createGuiItem(
            Material.ENDER_PEARL,
            ChatColor.AQUA + "SPHERE",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("SPHERE"))));
    EffectsGUI.setItem(
        6,
        createGuiItem(
            Material.NETHER_STAR,
            ChatColor.DARK_PURPLE + "RAINBOW",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("RAINBOW"))));
    EffectsGUI.setItem(
        18,
        createGuiItem(
            Material.GOLD_NUGGET, ChatColor.GOLD + "DONOR", "", ChatColor.RED + "- Donor Only -"));
    EffectsGUI.setItem(26, reset);
    EffectsGUI.setItem(8, back);
  }

  private Integer effectPoint(String effectName) {
    return KillEffectsConfig.getCustomConfig().getInt(effectName + ".points");
  }

  @EventHandler
  public void onGuiClick(final InventoryClickEvent e) {
    if (e.getView().getTitle().equals("Kill Effect Selector")) {
      e.setCancelled(true);

      final ItemStack clickedItem = e.getCurrentItem();
      final Player player = (Player) e.getWhoClicked();
      final String getItemName = clickedItem.getItemMeta().getDisplayName().substring(2);

      switch (getItemName) {
        case "Go to previous page ➡":
          player.openInventory(DefaultGUI.gui);
          break;
        case "NONE":
          MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "NONE");
          player.sendMessage(ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + " Kill Effect");
          break;
        case "BLOOD":
          selectEffect(player, "BLOOD", false);
          break;
        case "RAINBOW":
          selectEffect(player, "RAINBOW", false);
          break;
        case "HEART":
          selectEffect(player, "HEART", true);
          break;
        case "SMOKE":
          selectEffect(player, "SMOKE", true);
          break;
        case "FLAME":
          selectEffect(player, "FLAME", true);
          break;
        case "SPHERE":
          selectEffect(player, "SPHERE", true);
          break;
        case "MAGIC":
          selectEffect(player, "MAGIC", true);
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

  private void selectEffect(Player player, String effect, Boolean donor) {
    final UUID uuid = player.getUniqueId();
    final int require = getPlayerData.getRequirePoints(effect);

    if (donor) {
      if (getPlayerData.hasDonorRank(player)) {
        if (getPlayerData.hasRequirePoint(uuid, require)) {
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
      if (getPlayerData.hasRequirePoint(uuid, require)) {
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
