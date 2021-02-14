package network.atria.Effects.GUI;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
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

public class KillEffectsGUI extends CustomGUI implements Listener {

  public static Inventory effect;
  protected TextComponent title = text("Kill Effect Select Menu", Style.style(TextDecoration.BOLD));
  private final FileConfiguration config = KillEffectsConfig.getCustomConfig();

  public KillEffectsGUI(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    effect = createGUI(title, 27);
  }

  @EventHandler
  public void openGUI(InventoryOpenEvent e) {
    if (e.getView().getTitle().equals(TextFormat.format(title))) {
      UUID uuid = e.getPlayer().getUniqueId();
      addIconItems(uuid);
    }
  }

  private void addIconItems(UUID uuid) {

    ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
    ItemMeta reset_meta = reset.getItemMeta();

    reset_meta.setDisplayName(TextFormat.format(text("Reset Kill Effect", NamedTextColor.RED)));
    reset.setItemMeta(reset_meta);
    effect.setItem(26, reset);

    config
        .getConfigurationSection("KILL_EFFECT")
        .getKeys(false)
        .forEach(
            effect -> {
              Material material =
                  Material.valueOf(
                      config.getString("KILL_EFFECT." + effect + ".material").toUpperCase());
              int number = config.getInt("KILL_EFFECT." + effect + ".number");

              setItem(
                  KillEffectsGUI.effect,
                  number,
                  material,
                  text(effect, NamedTextColor.GREEN, TextDecoration.BOLD),
                  empty(),
                  canUseEffects(uuid, effectPoint(effect)));
            });

    setItem(
        effect,
        18,
        Material.GOLD_NUGGET,
        text("DONOR", NamedTextColor.GOLD),
        empty(),
        text("- Donor Only -", NamedTextColor.RED, TextDecoration.BOLD));
  }

  private Integer effectPoint(String effectName) {
    return config.getInt("KILL_EFFECT." + effectName + ".points");
  }

  @EventHandler
  public void onGuiClick(InventoryClickEvent e) {
    if (e.getView().getTitle().equals(TextFormat.format(title))) {
      e.setCancelled(true);

      ItemStack clickedItem = e.getCurrentItem();
      Player player = (Player) e.getWhoClicked();
      Audience audience = Mixed.get().getAudience().player(player);
      UserProfile profile = Mixed.get().getProfileManager().getProfile(player.getUniqueId());

      if (clickedItem.getType() == Material.AIR) return;
      Optional<Effect> killeffect =
          Mixed.get()
              .getEffectManager()
              .findEffect(TextFormat.format(clickedItem.getItemMeta().getDisplayName()));
      if (killeffect.isPresent()) {
        selectEffect(player, killeffect.get());
        player.closeInventory();
      } else if (TextFormat.format(clickedItem.getItemMeta().getDisplayName())
              .equalsIgnoreCase("DONOR")
          && player.hasPermission("pgm.group.donor")) {
        MySQL.SQLQuery.update("RANKS", "EFFECT", "DONOR", player.getUniqueId());
        profile.setKilleffect(new Effect("DONOR", text("DONOR", NamedTextColor.GOLD), 0, true));
        audience.sendMessage(
            text("You selected ", NamedTextColor.GREEN)
                .append(text("DONOR", NamedTextColor.YELLOW))
                .append(text(" kill effect.", NamedTextColor.GREEN)));
        player.closeInventory();
      }
    }
  }

  private void selectEffect(Player player, Effect effect) {
    Audience audience = Mixed.get().getAudience().player(player);
    EffectManager manager = Mixed.get().getEffectManager();
    UserProfile profile = Mixed.get().getProfileManager().getProfile(player.getUniqueId());

    if (effect.canUseDonor() || manager.hasRequirePoint(effect, player.getUniqueId())) {
      MySQL.SQLQuery.update("RANKS", "EFFECT", effect.getName(), player.getUniqueId());
      profile.setKilleffect(effect);
      audience.sendMessage(
          text("You selected ", NamedTextColor.GREEN)
              .append(effect.getColoredName())
              .append(text(" kill effect.", NamedTextColor.GREEN)));
    } else {
      audience.sendMessage(text("You don't have enough points.", NamedTextColor.RED));
    }
  }
}
