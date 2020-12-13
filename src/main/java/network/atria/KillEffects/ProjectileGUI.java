package network.atria.KillEffects;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Mixed;
import network.atria.Util.EffectUtils;
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
import tc.oc.pgm.api.PGM;
import tc.oc.pgm.api.player.MatchPlayer;

public class ProjectileGUI extends EffectUtils implements Listener {

  public static Inventory projectile;
  private final FileConfiguration config = KillEffectsConfig.getCustomConfig();
  protected TextComponent title =
      Component.text("Projectile Trails Select Menu", Style.style(TextDecoration.BOLD));

  public ProjectileGUI(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    projectile = createGUI(title);
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
        TextFormat.format(Component.text("Reset Projectile Trails", NamedTextColor.RED)));
    reset.setItemMeta(reset_meta);

    ItemStack back = new ItemStack(Material.ARROW, 1);
    ItemMeta back_meta = back.getItemMeta();

    back_meta.setDisplayName(
        TextFormat.format(Component.text("Go to previous page ➡", NamedTextColor.RED)));
    back.setItemMeta(back_meta);

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

              setItemGUI(
                  ProjectileGUI.projectile,
                  number,
                  material,
                  Component.text(projectile, NamedTextColor.GREEN, TextDecoration.BOLD),
                  Component.empty(),
                  canUseEffects(uuid, getProjectilePoint(projectile)));
            });

    projectile.setItem(26, reset);
    projectile.setItem(8, back);
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
      Audience audience = Mixed.get().getAudience().player(player);

      Set<Effect> projectiles = ProjectileTrails.getProjectiles();
      Optional<Effect> projectile =
          projectiles.stream()
              .filter(
                  name ->
                      name.getColoredName()
                          .equalsIgnoreCase(clickedItem.getItemMeta().getDisplayName()))
              .findFirst();

      if (projectile.isPresent()) {
        selectProjectile(player, projectile.get());
        player.closeInventory();
      } else {
        switch (clickedItem.getItemMeta().getDisplayName().substring(2)) {
          case "Go to previous page ➡":
            player.openInventory(DefaultGUI.gui);
            break;
          case "Reset Projectile Trails":
            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "NONE");
            audience.sendMessage(
                Component.text("Reset your ", NamedTextColor.GREEN)
                    .append(Component.text("Projectile Trails", NamedTextColor.YELLOW)));
            player.closeInventory();
            break;
        }
      }
      player.closeInventory();
    }
  }

  private void selectProjectile(Player player, Effect projectile) {
    UUID uuid = player.getUniqueId();
    Audience audience = Mixed.get().getAudience().player(player);
    MatchPlayer matchPlayer = PGM.get().getMatchManager().getPlayer(player);

    if (projectile.canUseDonor(matchPlayer) || projectile.hasRequirePoint(matchPlayer.getId())) {
      MySQLSetterGetter.setProjectileTrails(uuid.toString(), projectile.getName());
      audience.sendMessage(
          Component.text("You selected ", NamedTextColor.GREEN)
              .append(Component.text(projectile.getName().toUpperCase(), NamedTextColor.YELLOW))
              .append(Component.text(" projectile trails.", NamedTextColor.GREEN)));
    } else {
      audience.sendMessage(Component.text("You don't have enough points.", NamedTextColor.RED));
    }
  }
}
