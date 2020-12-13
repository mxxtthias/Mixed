package network.atria.KillEffects;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.util.Arrays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
    gui = Bukkit.createInventory(null, 27, TextFormat.format(Component.text(" ")));
    addIconItems();
  }

  private void addIconItems() {

    ItemStack close = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
    ItemMeta meta = close.getItemMeta();

    meta.setDisplayName(
        TextFormat.format(
            Component.text("Close the GUI", NamedTextColor.RED, TextDecoration.BOLD)));
    close.setItemMeta(meta);

    gui.setItem(
        10,
        createGuiItem(
            Material.REDSTONE,
            TextFormat.format(
                Component.text("Kill Effects", NamedTextColor.GREEN, TextDecoration.BOLD))));
    gui.setItem(
        12,
        createGuiItem(
            Material.RECORD_3,
            TextFormat.format(
                Component.text("Kill Sounds", NamedTextColor.GREEN, TextDecoration.BOLD))));
    gui.setItem(
        14,
        createGuiItem(
            Material.BOW,
            TextFormat.format(
                Component.text("Projectile Trails", NamedTextColor.GREEN, TextDecoration.BOLD))));
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
                    Component.text("Close the GUI", NamedTextColor.RED, TextDecoration.BOLD)))) {
              player.closeInventory();
            } else if (getItemName.equals(
                TextFormat.format(
                    Component.text("Kill Effects", NamedTextColor.GREEN, TextDecoration.BOLD)))) {
              player.openInventory(KillEffectsGUI.effect);
            } else if (getItemName.equals(
                TextFormat.format(
                    Component.text("Kill Sounds", NamedTextColor.GREEN, TextDecoration.BOLD)))) {
              player.openInventory(KillSoundsGUI.sound);
            } else if (getItemName.equals(
                TextFormat.format(
                    Component.text(
                        "Projectile Trails", NamedTextColor.GREEN, TextDecoration.BOLD)))) {
              player.openInventory(ProjectileGUI.projectile);
            }
          }
        }
      }
    } catch (NullPointerException ignored) {
    }
  }

  protected static ItemStack createGuiItem(Material material, String name, String... lore) {
    ItemStack item = new ItemStack(material, 1);
    ItemMeta meta = item.getItemMeta();

    meta.setDisplayName(name);
    meta.setLore(Arrays.asList(lore));
    item.setItemMeta(meta);

    return item;
  }

  @Command(
      aliases = {"effect", "sound", "projectile"},
      desc = "Open Select GUI")
  public void gui(@Sender Player sender) {
    sender.openInventory(gui);
  }
}
