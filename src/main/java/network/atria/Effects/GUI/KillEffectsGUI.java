package network.atria.Effects.GUI;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Effects.Particles.Effect;
import network.atria.Effects.Particles.KillEffects;
import network.atria.Mixed;
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

public class KillEffectsGUI extends CustomGUI implements Listener {

  public static Inventory effect;
  private final FileConfiguration config = KillEffectsConfig.getCustomConfig();
  protected TextComponent title = text("Kill Effect Select Menu", Style.style(TextDecoration.BOLD));

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

      Set<Effect> effects = KillEffects.getEffects();
      Optional<Effect> effect =
          effects.stream()
              .filter(
                  name ->
                      name.getColoredName()
                          .equalsIgnoreCase(clickedItem.getItemMeta().getDisplayName()))
              .findFirst();

      if (effect.isPresent()) {
        selectEffect(player, effect.get());
        player.closeInventory();
      } else if (clickedItem.getItemMeta().getDisplayName().substring(2).equalsIgnoreCase("DONOR"))
        if (player.hasPermission("pgm.group.donor")) {
          MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "DONOR");
          audience.sendMessage(
              text("You selected ", NamedTextColor.GREEN)
                  .append(
                      text("DONOR", NamedTextColor.YELLOW)
                          .append(text(" kill effect.", NamedTextColor.GREEN))));
          player.closeInventory();
        } else {
          audience.sendMessage(text("You don't have the donor rank", NamedTextColor.RED));
        }
    }
  }

  private void selectEffect(Player player, Effect effect) {
    UUID uuid = player.getUniqueId();
    Audience audience = Mixed.get().getAudience().player(player);
    MatchPlayer matchPlayer = PGM.get().getMatchManager().getPlayer(player);

    if (effect.canUseDonor(matchPlayer) || effect.hasRequirePoint(matchPlayer.getId())) {
      MySQLSetterGetter.setKillEffect(uuid.toString(), effect.getName());
      audience.sendMessage(
          text("You selected ", NamedTextColor.GREEN)
              .append(text(effect.getName().toUpperCase(), NamedTextColor.YELLOW))
              .append(text(" kill effect.", NamedTextColor.GREEN)));
    } else {
      audience.sendMessage(text("You don't have enough points.", NamedTextColor.RED));
    }
  }
}
