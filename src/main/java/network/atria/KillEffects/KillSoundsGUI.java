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

public class KillSoundsGUI implements Listener {

  public static Inventory SoundsGUI;

  public KillSoundsGUI() {
    SoundsGUI = Bukkit.createInventory(null, 27, "Kill Sound Selector");
  }

  @EventHandler
  public void getPlayer(InventoryOpenEvent e) {
    if (e.getView().getTitle().equals("Kill Sound Selector")) {
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

    SoundsGUI.setItem(
        10,
        createGuiItem(
            Material.GHAST_TEAR,
            ChatColor.AQUA + "DEFAULT",
            "",
            getPlayerData.canUseEffects(uuid, 0)));
    SoundsGUI.setItem(
        11,
        createGuiItem(
            Material.REDSTONE,
            ChatColor.AQUA + "VILLAGER",
            "",
            getPlayerData.canUseEffects(uuid, getSoundPoint("VILLAGER"))));
    SoundsGUI.setItem(
        12,
        createGuiItem(
            Material.BONE,
            ChatColor.AQUA + "HOWL",
            "",
            getPlayerData.canUseEffects(uuid, getSoundPoint("HOWL"))));
    SoundsGUI.setItem(
        13,
        createGuiItem(
            Material.TNT,
            ChatColor.AQUA + "BOMB",
            "",
            getPlayerData.canUseEffects(uuid, getSoundPoint("BOMB"))));
    SoundsGUI.setItem(
        14,
        createGuiItem(
            Material.SEEDS,
            ChatColor.AQUA + "BURP",
            "",
            getPlayerData.canUseEffects(uuid, getSoundPoint("BURP"))));
    SoundsGUI.setItem(26, reset);
    SoundsGUI.setItem(8, back);
  }

  private Integer getSoundPoint(String sound) {
    return KillEffectsConfig.getCustomConfig().getInt(sound + ".points");
  }

  @EventHandler
  public void onGuiClick(final InventoryClickEvent e) {
    if (e.getView().getTitle().equals("Kill Sound Selector")) {
      e.setCancelled(true);

      final ItemStack clickedItem = e.getCurrentItem();
      final Player player = (Player) e.getWhoClicked();
      final String getItemName = clickedItem.getItemMeta().getDisplayName().substring(2);

      switch (getItemName) {
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
        case "HOWL":
          selectSound(player, "HOWL");
          break;
        case "VILLAGER":
          selectSound(player, "VILLAGER");
          break;
        case "BOMB":
          selectSound(player, "BOMB");
          break;
        case "BURP":
          selectSound(player, "BURP");
          break;
      }
    }
  }

  private void selectSound(Player player, String sound) {
    final UUID uuid = player.getUniqueId();
    final int require = getPlayerData.getRequirePoints(sound);

    if (getPlayerData.hasRequirePoint(uuid, require)) {
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
