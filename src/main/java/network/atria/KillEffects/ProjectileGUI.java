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

public class ProjectileGUI extends EffectUtils implements Listener {

  public static Inventory projectile;
  protected String title = "Projectile Trails Selector";
  protected int size = 27;
  private final FileConfiguration config = KillEffectsConfig.getCustomConfig();

  public ProjectileGUI(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    projectile = createGUI(size, title);
  }

  @EventHandler
  public void getOpenGUI(InventoryOpenEvent e) {
    if (e.getView().getTitle().equals(title)) {
      final UUID uuid = e.getPlayer().getUniqueId();
      addIconItems(uuid);
    }
  }

  private void addIconItems(UUID uuid) {
    final ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
    final ItemMeta reset_meta = reset.getItemMeta();

    reset_meta.setDisplayName(ChatColor.RED + "Reset Projectile Trails");
    reset.setItemMeta(reset_meta);

    final ItemStack back = new ItemStack(Material.ARROW, 1);
    final ItemMeta back_meta = back.getItemMeta();

    back_meta.setDisplayName(ChatColor.RED + "Go to previous page ➡");
    back.setItemMeta(back_meta);

    for (String projectile : config.getConfigurationSection("PROJECTILE_TRAILS").getKeys(false)) {
      final int number = config.getInt("PROJECTILE_TRAILS." + projectile + ".number");
      final Material material =
          Material.valueOf(
              config.getString("PROJECTILE_TRAILS." + projectile + ".material").toUpperCase());

      setItemGUI(
          ProjectileGUI.projectile,
          number,
          material,
          projectile,
          "",
          canUseEffects(uuid, getProjectilePoint(projectile)));
    }
    projectile.setItem(26, reset);
    projectile.setItem(8, back);
  }

  private Integer getProjectilePoint(String projectile) {
    return config.getInt("PROJECTILE_TRAILS." + projectile + ".points");
  }

  @EventHandler
  public void onGuiClick(final InventoryClickEvent e) {
    if (e.getView().getTitle().equals("Projectile Trails Selector")) {
      e.setCancelled(true);

      final ItemStack clickedItem = e.getCurrentItem();
      final Player player = (Player) e.getWhoClicked();
      final String getItemName = clickedItem.getItemMeta().getDisplayName();
      final Set<String> projectile =
          config.getConfigurationSection("PROJECTILE_TRAILS").getKeys(false);

      if (projectile.contains(getItemName)) {
        selectProjectile(player, getItemName);
      } else {
        switch (getItemName.substring(2)) {
          case "Go to previous page ➡":
            player.openInventory(DefaultGUI.gui);
            break;
          case "Reset Projectile Trails":
            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "NONE");
            player.sendMessage(
                ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + " Projectile Trails");
            break;
        }
      }
    }
  }

  private void selectProjectile(Player player, String projectile) {
    final UUID uuid = player.getUniqueId();
    final int require = getRequirePoints("PROJECTILE_TRAILS", projectile);

    if (hasRequirePoint(uuid, require)) {
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
