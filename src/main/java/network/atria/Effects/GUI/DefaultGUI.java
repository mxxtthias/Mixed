package network.atria.Effects.GUI;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Mixed;
import network.atria.UserProfile.UserProfile;
import network.atria.Util.TextFormat;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class DefaultGUI implements Listener {

  public static Inventory gui;

  public DefaultGUI(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    gui = Bukkit.createInventory(null, 27, TextFormat.format(text(" ")));
  }

  private void addIconItems(Player player) {
    ItemStack close = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
    ItemMeta meta = close.getItemMeta();

    UserProfile profile = Mixed.get().getProfileManager().getProfile(player.getUniqueId());
    meta.setDisplayName(
        TextFormat.format(text("Close the GUI", NamedTextColor.RED, TextDecoration.BOLD)));
    close.setItemMeta(meta);
    gui.setItem(
        10,
        createGuiItem(
            Material.REDSTONE,
            TextFormat.format(text("Kill Effects", NamedTextColor.GREEN, TextDecoration.BOLD)),
            empty(),
            text("Now: ", NamedTextColor.GRAY).append(profile.getKilleffect().getColoredName())));
    gui.setItem(
        12,
        createGuiItem(
            Material.RECORD_3,
            TextFormat.format(text("Kill Sounds", NamedTextColor.GREEN, TextDecoration.BOLD)),
            empty(),
            text("Now: ", NamedTextColor.GRAY).append(profile.getKillsound().getColoredName())));
    gui.setItem(
        14,
        createGuiItem(
            Material.BOW,
            TextFormat.format(text("Projectile Trails", NamedTextColor.GREEN, TextDecoration.BOLD)),
            empty(),
            text("Now: ", NamedTextColor.GRAY).append(profile.getProjectile().getColoredName())));
    gui.setItem(17, close);
  }

  @EventHandler
  public void onGuiClick(InventoryClickEvent e) {
    try {
      if (e.getView().getTitle().equals(" ")) {
        e.setCancelled(true);

        ItemStack clickedItem = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        String getItemName = clickedItem.getItemMeta().getDisplayName();

        if (clickedItem.hasItemMeta()) {
          if (clickedItem.getItemMeta().hasDisplayName()) {
            if (getItemName.equals(
                TextFormat.format(
                    text("Close the GUI", NamedTextColor.RED, TextDecoration.BOLD)))) {
              player.closeInventory();
            } else if (getItemName.equals(
                TextFormat.format(
                    text("Kill Effects", NamedTextColor.GREEN, TextDecoration.BOLD)))) {
              player.openInventory(KillEffectsGUI.effect);
            } else if (getItemName.equals(
                TextFormat.format(
                    text("Kill Sounds", NamedTextColor.GREEN, TextDecoration.BOLD)))) {
              player.openInventory(KillSoundsGUI.sound);
            } else if (getItemName.equals(
                TextFormat.format(
                    text("Projectile Trails", NamedTextColor.GREEN, TextDecoration.BOLD)))) {
              player.openInventory(ProjectileGUI.projectile);
            }
          }
        }
      }
    } catch (NullPointerException ignored) {
    }
  }

  protected static ItemStack createGuiItem(Material material, String name, Component... lore) {
    ItemStack item = new ItemStack(material, 1);
    ItemMeta meta = item.getItemMeta();
    List<String> lores = new ArrayList<>();
    Arrays.stream(lore).forEachOrdered(x -> lores.add(TextFormat.format(x)));

    meta.setDisplayName(name);
    meta.setLore(lores);
    item.setItemMeta(meta);

    return item;
  }

  @Command(
      aliases = {"effect", "sound", "projectile"},
      desc = "Open Select GUI")
  public void gui(@Sender Player sender) {
    addIconItems(sender);
    sender.openInventory(gui);
  }
}
