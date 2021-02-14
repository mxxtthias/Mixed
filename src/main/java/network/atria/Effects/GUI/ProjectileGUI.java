package network.atria.Effects.GUI;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Effects.Particles.Effect;
import network.atria.Manager.EffectManager;
import network.atria.Mixed;
import network.atria.MySQL;
import network.atria.UserProfile.UserProfile;
import network.atria.Util.KillEffectsConfig;
import network.atria.Util.TextFormat;
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

public class ProjectileGUI extends CustomGUI implements Listener {

  public static Inventory projectile;
  private final FileConfiguration config = KillEffectsConfig.getCustomConfig();
  protected TextComponent title =
      Component.text("Projectile Trails Select Menu", Style.style(TextDecoration.BOLD));

  public ProjectileGUI(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    projectile = createGUI(title, 27);
  }

  @EventHandler
  public void getOpenGUI(InventoryOpenEvent e) {
    if (e.getView().getTitle().equals(TextFormat.format(title))) {
      UUID uuid = e.getPlayer().getUniqueId();
      addIconItems(uuid);
    }
  }

  private void addIconItems(UUID uuid) {
    ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
    ItemMeta reset_meta = reset.getItemMeta();

    reset_meta.setDisplayName(
        TextFormat.format(text("Reset Projectile Trails", NamedTextColor.RED)));
    reset.setItemMeta(reset_meta);
    projectile.setItem(26, reset);

    config
        .getConfigurationSection("PROJECTILE_TRAILS")
        .getKeys(false)
        .forEach(
            projectile -> {
              Material material =
                  Material.valueOf(
                      config
                          .getString("PROJECTILE_TRAILS." + projectile + ".material")
                          .toUpperCase());
              int number = config.getInt("PROJECTILE_TRAILS." + projectile + ".number");

              setItem(
                  ProjectileGUI.projectile,
                  number,
                  material,
                  Component.text(projectile, NamedTextColor.GREEN, TextDecoration.BOLD),
                  empty(),
                  canUseEffects(uuid, getProjectilePoint(projectile)));
            });
  }

  private Integer getProjectilePoint(String projectile) {
    return config.getInt("PROJECTILE_TRAILS." + projectile + ".points");
  }

  @EventHandler
  public void onGuiClick(InventoryClickEvent event) {
    if (event.getView().getTitle().equals(TextFormat.format(title))) {
      event.setCancelled(true);

      ItemStack clickedItem = event.getCurrentItem();
      Player player = (Player) event.getWhoClicked();
      Mixed.get().getAudience().player(player);

      if (clickedItem.getType() == Material.AIR) return;
      Optional<Effect> projectile =
          Mixed.get()
              .getEffectManager()
              .findEffect(TextFormat.format(clickedItem.getItemMeta().getDisplayName()));
      if (projectile.isPresent()) {
        selectProjectile(player, projectile.get());
        player.closeInventory();
      }
    }
  }

  private void selectProjectile(Player player, Effect projectile) {
    UUID uuid = player.getUniqueId();
    Audience audience = Mixed.get().getAudience().player(player);
    EffectManager manager = Mixed.get().getEffectManager();
    UserProfile profile = Mixed.get().getProfileManager().getProfile(uuid);

    if (projectile.canUseDonor() || manager.hasRequirePoint(projectile, uuid)) {
      MySQL.SQLQuery.update("RANKS", "PROJECTILE", projectile.getName(), player.getUniqueId());
      profile.setProjectile(projectile);
      audience.sendMessage(
          Component.text("You selected ", NamedTextColor.GREEN)
              .append(projectile.getColoredName())
              .append(Component.text(" projectile trails.", NamedTextColor.GREEN)));
    } else {
      audience.sendMessage(Component.text("You don't have enough points.", NamedTextColor.RED));
    }
  }
}
