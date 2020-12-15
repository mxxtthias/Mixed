package network.atria.Effects.GUI;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQL;
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
  }

  private void addIconItems(Player player) {
    ItemStack close = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
    ItemMeta meta = close.getItemMeta();
    HashMap<String, String> effects = getSelectingEffects(player.getUniqueId());

    meta.setDisplayName(
        TextFormat.format(
            Component.text("Close the GUI", NamedTextColor.RED, TextDecoration.BOLD)));
    close.setItemMeta(meta);

    gui.setItem(
        10,
        createGuiItem(
            Material.REDSTONE,
            TextFormat.format(
                Component.text("Kill Effects", NamedTextColor.GREEN, TextDecoration.BOLD)),
            Component.empty(),
            Component.text("Now: ", NamedTextColor.GRAY)
                .append(Component.text(effects.get("EFFECT"), NamedTextColor.GREEN))));
    gui.setItem(
        12,
        createGuiItem(
            Material.RECORD_3,
            TextFormat.format(
                Component.text("Kill Sounds", NamedTextColor.GREEN, TextDecoration.BOLD)),
            Component.empty(),
            Component.text("Now: ", NamedTextColor.GRAY)
                .append(Component.text(effects.get("SOUND"), NamedTextColor.GREEN))));
    gui.setItem(
        14,
        createGuiItem(
            Material.BOW,
            TextFormat.format(
                Component.text("Projectile Trails", NamedTextColor.GREEN, TextDecoration.BOLD)),
            Component.empty(),
            Component.text("Now: ", NamedTextColor.GRAY)
                .append(Component.text(effects.get("PROJECTILE"), NamedTextColor.GREEN))));
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

  private HashMap<String, String> getSelectingEffects(UUID uuid) {
    final HashMap<String, String> effects = new HashMap<>();
    ResultSet rs = null;
    PreparedStatement statement = null;
    Connection connection = null;

    try {
      connection = MySQL.getHikari().getConnection();
      statement =
          connection.prepareStatement(
              "SELECT EFFECT, SOUND, PROJECTILE FROM RANKS WHERE UUID = '"
                  + uuid.toString()
                  + "';");
      rs = statement.executeQuery();

      if (rs.next()) {
        effects.put("EFFECT", rs.getString("EFFECT"));
        effects.put("SOUND", rs.getString("SOUND"));
        effects.put("PROJECTILE", rs.getString("PROJECTILE"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
    return effects;
  }

  @Command(
      aliases = {"effect", "sound", "projectile"},
      desc = "Open Select GUI")
  public void gui(@Sender Player sender) {
    addIconItems(sender);
    sender.openInventory(gui);
  }
}
