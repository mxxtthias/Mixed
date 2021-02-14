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

public class KillSoundsGUI extends CustomGUI implements Listener {

  public static Inventory sound;
  private final FileConfiguration config = KillEffectsConfig.getCustomConfig();
  protected TextComponent title = text("Kill Sound Select Menu", Style.style(TextDecoration.BOLD));

  public KillSoundsGUI(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    sound = createGUI(title, 27);
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

    reset_meta.setDisplayName(TextFormat.format(text("Reset Kill Sound", NamedTextColor.RED)));
    reset.setItemMeta(reset_meta);
    sound.setItem(26, reset);

    config
        .getConfigurationSection("KILL_SOUND")
        .getKeys(false)
        .forEach(
            sound -> {
              Material material =
                  Material.valueOf(
                      config.getString("KILL_SOUND." + sound + ".material").toUpperCase());
              int number = config.getInt("KILL_SOUND." + sound + ".number");

              setItem(
                  KillSoundsGUI.sound,
                  number,
                  material,
                  text(sound, NamedTextColor.GREEN, TextDecoration.BOLD),
                  empty(),
                  canUseEffects(uuid, getSoundPoint(sound)));
            });
  }

  private Integer getSoundPoint(String sound) {
    return config.getInt("KILL_SOUND." + sound + ".points");
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
      Optional<Effect> sound =
          Mixed.get()
              .getEffectManager()
              .findEffect(TextFormat.format(clickedItem.getItemMeta().getDisplayName()));
      if (sound.isPresent()) {
        selectSound(player, sound.get());
        player.closeInventory();
      } else {
        if ("DEFAULT".equals(TextFormat.format(clickedItem.getItemMeta().getDisplayName()))) {
          MySQL.SQLQuery.update("RANKS", "SOUND", "DEFAULT", player.getUniqueId());
          profile.setKillsound(new Effect("SOUND", text("SOUND", NamedTextColor.GRAY), 0, false));
          audience.sendMessage(
              text("You selected ", NamedTextColor.GREEN)
                  .append(text("DEFAULT Kill Sound", NamedTextColor.YELLOW)));
          player.closeInventory();
        }
      }
    }
  }

  private void selectSound(Player player, Effect sound) {
    UUID uuid = player.getUniqueId();
    Audience audience = Mixed.get().getAudience().player(player);
    EffectManager manager = Mixed.get().getEffectManager();
    UserProfile profile = Mixed.get().getProfileManager().getProfile(uuid);

    if (sound.canUseDonor() || manager.hasRequirePoint(sound, uuid)) {
      MySQL.SQLQuery.update("RANKS", "SOUND", sound.getName(), uuid);
      profile.setKillsound(sound);
      audience.sendMessage(
          text("You selected ", NamedTextColor.GREEN)
              .append(sound.getColoredName())
              .append(text(" kill sound.", NamedTextColor.GREEN)));
    } else {
      audience.sendMessage(text("You don't have enough points.", NamedTextColor.RED));
    }
  }
}
