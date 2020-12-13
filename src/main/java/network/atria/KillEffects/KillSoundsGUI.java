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

public class KillSoundsGUI extends EffectUtils implements Listener {

  public static Inventory sound;
  private final FileConfiguration config = KillEffectsConfig.getCustomConfig();
  protected TextComponent title =
      Component.text("Kill Sound Select Menu", Style.style(TextDecoration.BOLD));

  public KillSoundsGUI(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    sound = createGUI(title);
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

    reset_meta.setDisplayName(
        TextFormat.format(Component.text("Reset Kill Sound", NamedTextColor.RED)));
    reset.setItemMeta(reset_meta);

    ItemStack back = new ItemStack(Material.ARROW, 1);
    ItemMeta back_meta = back.getItemMeta();

    back_meta.setDisplayName(
        TextFormat.format(Component.text("Go to previous page ➡", NamedTextColor.RED)));
    back.setItemMeta(back_meta);

    config
        .getConfigurationSection("KILL_SOUND")
        .getKeys(false)
        .forEach(
            sound -> {
              Material material =
                  Material.valueOf(
                      config.getString("KILL_SOUND." + sound + ".material").toUpperCase());
              int number = config.getInt("KILL_SOUND." + sound + ".number");

              setItemGUI(
                  KillSoundsGUI.sound,
                  number,
                  material,
                  Component.text(sound, NamedTextColor.GREEN, TextDecoration.BOLD),
                  Component.empty(),
                  canUseEffects(uuid, getSoundPoint(sound)));
            });

    sound.setItem(26, reset);
    sound.setItem(8, back);
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

      Set<Effect> sounds = KillSounds.getSounds();
      Optional<Effect> sound =
          sounds.stream()
              .filter(
                  name ->
                      name.getColoredName()
                          .equalsIgnoreCase(clickedItem.getItemMeta().getDisplayName()))
              .findFirst();

      if (sound.isPresent()) {
        selectSound(player, sound.get());
        player.closeInventory();
      } else {
        switch (clickedItem.getItemMeta().getDisplayName().substring(2)) {
          case "Go to previous page ➡":
            player.openInventory(DefaultGUI.gui);
            break;
          case "Reset Kill Sound":
            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "NONE");
            audience.sendMessage(
                Component.text("Reset your ", NamedTextColor.GREEN)
                    .append(Component.text("Kill Sound", NamedTextColor.YELLOW)));
            player.closeInventory();
            break;
          case "DEFAULT":
            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "DEFAULT");
            audience.sendMessage(
                Component.text("You selected ", NamedTextColor.GREEN)
                    .append(Component.text("DEFAULT Kill Sound", NamedTextColor.YELLOW)));
            player.closeInventory();
            break;
        }
      }
    }
  }

  private void selectSound(Player player, Effect sound) {
    UUID uuid = player.getUniqueId();
    Audience audience = Mixed.get().getAudience().player(player);
    MatchPlayer matchPlayer = PGM.get().getMatchManager().getPlayer(player);

    if (sound.canUseDonor(matchPlayer) || sound.hasRequirePoint(matchPlayer.getId())) {
      MySQLSetterGetter.setKillSound(uuid.toString(), sound.getName());
      audience.sendMessage(
          Component.text("You selected ", NamedTextColor.GREEN)
              .append(Component.text(sound.getName().toUpperCase(), NamedTextColor.YELLOW))
              .append(Component.text(" kill sound.", NamedTextColor.GREEN)));
    } else {
      audience.sendMessage(Component.text("You don't have enough points.", NamedTextColor.RED));
    }
  }
}
