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

public class ProjectileGUI implements Listener {

  public static Inventory ProjectileGUI;

  public ProjectileGUI() {
    ProjectileGUI = Bukkit.createInventory(null, 27, "Projectile Trails Selector");
  }

  @EventHandler
  public void getPlayer(InventoryOpenEvent e) {
    if (e.getView().getTitle().equals("Projectile Trails Selector")) {
      final UUID uuid = e.getPlayer().getUniqueId();
      addIconItems(uuid);
    }
  }

  private void addIconItems(UUID uuid) {

    final ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
    final ItemMeta reset_meta = reset.getItemMeta();

    reset_meta.setDisplayName(ChatColor.RED + "Reset Projectile Trail");
    reset.setItemMeta(reset_meta);

    final ItemStack back = new ItemStack(Material.ARROW, 1);
    final ItemMeta back_meta = back.getItemMeta();

    back_meta.setDisplayName(ChatColor.RED + "Go to previous page ➡");
    back.setItemMeta(back_meta);

    ItemStack itemStack = new ItemStack(Material.INK_SACK, 1, (short) DyeColor.LIME.getData());

    ProjectileGUI.setItem(
        10,
        createGuiItem(
            Material.GOLDEN_APPLE,
            ChatColor.AQUA + "HEART",
            "",
            getPlayerData.canUseEffects(uuid, getProjectilePoint("HEART_TRAIL"))));
    ProjectileGUI.setItem(
        11,
        createGuiItem(
            Material.POTION,
            ChatColor.AQUA + "WITCH",
            "",
            getPlayerData.canUseEffects(uuid, getProjectilePoint("WITCH"))));
    ProjectileGUI.setItem(
        12,
        createGuiItem(
            Material.NETHER_STAR,
            ChatColor.AQUA + "RAINBOW",
            "",
            getPlayerData.canUseEffects(uuid, getProjectilePoint("RAINBOW_TRAIL"))));
    ProjectileGUI.setItem(
        13,
        createGuiItem(
            itemStack.getType(),
            ChatColor.AQUA + "GREEN",
            "",
            getPlayerData.canUseEffects(uuid, getProjectilePoint("GREEN"))));
    ProjectileGUI.setItem(
        14,
        createGuiItem(
            Material.NOTE_BLOCK,
            ChatColor.AQUA + "NOTE",
            "",
            getPlayerData.canUseEffects(uuid, getProjectilePoint("NOTE"))));

    ProjectileGUI.setItem(26, reset);
    ProjectileGUI.setItem(8, back);
  }

  private Integer getProjectilePoint(String projectile) {
    return KillEffectsConfig.getCustomConfig().getInt(projectile + ".points");
  }

  @EventHandler
  public void onGuiClick(final InventoryClickEvent e) {
    if (e.getView().getTitle().equals("Projectile Trails Selector")) {
      e.setCancelled(true);

      final ItemStack clickedItem = e.getCurrentItem();
      final Player player = (Player) e.getWhoClicked();
      final String getItemName = clickedItem.getItemMeta().getDisplayName().substring(2);

      switch (getItemName) {
        case "Go to previous page ➡":
          player.openInventory(DefaultGUI.gui);
          break;
        case "Reset Projectile Trail":
          MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "NONE");
          player.sendMessage(ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + " Kill Sound");
          break;
        case "HEART":
          selectProjectile(player, "HEART");
          break;
        case "WITCH":
          selectProjectile(player, "WITCH");
          break;
        case "RAINBOW":
          selectProjectile(player, "RAINBOW_TRAIL");
          break;
        case "GREEN":
          selectProjectile(player, "GREEN");
          break;
        case "NOTE":
          selectProjectile(player, "NOTE");
          break;
      }
    }
  }

  private void selectProjectile(Player player, String projectile) {
    final UUID uuid = player.getUniqueId();
    final int require = getPlayerData.getRequirePoints(projectile);

    if (getPlayerData.hasRequirePoint(uuid, require)) {
      MySQLSetterGetter.setProjectileTrails(uuid.toString(), projectile);
      player.sendMessage(
          ChatColor.GREEN
              + "You selected "
              + ChatColor.YELLOW
              + projectile.toUpperCase()
              + " Projectile Trail");
    } else {
      player.sendMessage(ChatColor.RED + "You don't have enough points");
    }
  }
}
