package network.atria.KillEffects;

import static network.atria.KillEffects.DefaultGUI.createGuiItem;

import network.atria.Database.MySQLSetterGetter;
import network.atria.Util.KillEffectsConfig;
import network.atria.Util.getPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KillEffectsGUI implements Listener {

  public static Inventory EffectsGUI;

  public KillEffectsGUI() {
    EffectsGUI = Bukkit.createInventory(null, 27, "Kill Effect Selector");
  }

  @EventHandler
  public void getPlayer(InventoryOpenEvent e) {
    if (e.getView().getTitle().equals("Kill Effect Selector")) {
      String uuid = e.getPlayer().getUniqueId().toString();
      addIconItems(uuid);
    }
  }

  private void addIconItems(String uuid) {

    ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
    ItemMeta reset_meta = reset.getItemMeta();

    reset_meta.setDisplayName(ChatColor.RED + "Reset Kill Effect");
    reset.setItemMeta(reset_meta);

    ItemStack back = new ItemStack(Material.ARROW, 1);
    ItemMeta back_meta = back.getItemMeta();

    back_meta.setDisplayName(ChatColor.RED + "Go to previous page ➡");
    back.setItemMeta(back_meta);

    EffectsGUI.setItem(
        0,
        createGuiItem(
            Material.REDSTONE,
            ChatColor.AQUA + "BLOOD",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("BLOOD"))));
    EffectsGUI.setItem(
        1,
        createGuiItem(
            Material.LAVA_BUCKET,
            ChatColor.AQUA + "FLAME",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("FLAME"))));
    EffectsGUI.setItem(
        2,
        createGuiItem(
            Material.GOLDEN_APPLE,
            ChatColor.AQUA + "HEART",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("HEART"))));
    EffectsGUI.setItem(
        3,
        createGuiItem(
            Material.FIREWORK_CHARGE,
            ChatColor.AQUA + "SMOKE",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("SMOKE"))));
    EffectsGUI.setItem(
        4,
        createGuiItem(
            Material.ENCHANTED_BOOK,
            ChatColor.AQUA + "MAGIC",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("MAGIC"))));
    EffectsGUI.setItem(
        5,
        createGuiItem(
            Material.ENDER_PEARL,
            ChatColor.AQUA + "SPHERE",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("SPHERE"))));
    EffectsGUI.setItem(
        6,
        createGuiItem(
            Material.NETHER_STAR,
            ChatColor.DARK_PURPLE + "RAINBOW",
            "",
            getPlayerData.canUseEffects(uuid, effectPoint("RAINBOW"))));
    EffectsGUI.setItem(
        18,
        createGuiItem(
            Material.GOLD_NUGGET, ChatColor.GOLD + "DONOR", "", ChatColor.RED + "- Donor Only -"));
    EffectsGUI.setItem(26, reset);
    EffectsGUI.setItem(8, back);
  }

  private Integer effectPoint(String effectName) {
    return KillEffectsConfig.getCustomConfig().getInt(effectName + ".points");
  }

  @EventHandler
  public void onGuiClick(final InventoryClickEvent e) {
    try {
      if (e.getView().getTitle().equals("Kill Effect Selector")) {
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        final Player player = (Player) e.getWhoClicked();

        String getItemName = clickedItem.getItemMeta().getDisplayName();

        if (clickedItem.hasItemMeta()) {
          if (clickedItem.getItemMeta().hasDisplayName()) {
            if (getItemName.equals(ChatColor.RED + "Go to previous page ➡")) {
              player.openInventory(DefaultGUI.gui);
            }
            if (getItemName.equals(ChatColor.RED + "Reset Kill Effect")) {
              MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "NONE");
              player.sendMessage(
                  ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + "Kill Effect");
            } else if (getItemName.equals(ChatColor.AQUA + "BLOOD")) {
              if (getPlayerData.hasRequirePoint(
                  player.getUniqueId().toString(), getPlayerData.getRequirePoints("BLOOD"))) {
                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "BLOOD");
                player.sendMessage(
                    ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "BLOOD Kill Effect");
              } else {
                player.sendMessage(ChatColor.RED + "You don't have enough points");
              }
            } else if (getItemName.equals(ChatColor.AQUA + "HEART")) {
              if (getPlayerData.hasDonorRank(player)
                  || getPlayerData.hasRequirePoint(
                      player.getUniqueId().toString(), getPlayerData.getRequirePoints("HEART"))) {
                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "HEART");
                player.sendMessage(
                    ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "HEART kill effect.");
              } else {
                player.sendMessage(ChatColor.RED + "You don't have enought points");
              }
            } else if (getItemName.equals(ChatColor.AQUA + "SMOKE")) {
              if (getPlayerData.hasDonorRank(player)
                  || getPlayerData.hasRequirePoint(
                      player.getUniqueId().toString(), getPlayerData.getRequirePoints("SMOKE"))) {
                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "SMOKE");
                player.sendMessage(
                    ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "SMOKE kill effect.");
              } else {
                player.sendMessage(ChatColor.RED + "You don't have enought points");
              }
            } else if (getItemName.equals(ChatColor.AQUA + "FLAME")) {
              if (getPlayerData.hasDonorRank(player)
                  || getPlayerData.hasRequirePoint(
                      player.getUniqueId().toString(), getPlayerData.getRequirePoints("FLAME"))) {
                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "FLAME");
                player.sendMessage(
                    ChatColor.GREEN + "You selected" + ChatColor.YELLOW + " FLAME kill effect.");
              } else {
                player.sendMessage(ChatColor.RED + "You don't have enought points");
              }
            } else if (getItemName.equals(ChatColor.DARK_PURPLE + "RAINBOW")) {
              if (getPlayerData.hasRequirePoint(
                  player.getUniqueId().toString(), getPlayerData.getRequirePoints("RAINBOW"))) {
                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "RAINBOW");
                player.sendMessage(
                    ChatColor.GREEN + "You selected" + ChatColor.YELLOW + " RAINBOW kill effect.");
              } else {
                player.sendMessage(ChatColor.RED + "You don't have enought points");
              }
            } else if (getItemName.equals(ChatColor.GOLD + "DONOR")) {
              if (player.hasPermission("pgm.group.donor")) {
                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "DONOR");
                player.sendMessage(
                    ChatColor.GREEN + "You selected" + ChatColor.YELLOW + " DONOR kill effect.");
              } else {
                player.sendMessage(ChatColor.RED + "You don't have the donor rank");
              }
            } else if (getItemName.equals(ChatColor.AQUA + "SPHERE")) {
              if (getPlayerData.hasDonorRank(player)
                  || getPlayerData.hasRequirePoint(
                      player.getUniqueId().toString(), getPlayerData.getRequirePoints("SPHERE"))) {
                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "SPHERE");
                player.sendMessage(
                    ChatColor.GREEN + "You selected" + ChatColor.YELLOW + " SPHERE kill effect.");
              } else {
                player.sendMessage(ChatColor.RED + "You don't have enought points");
              }
            } else if (getItemName.equals(ChatColor.AQUA + "MAGIC")) {
              if (getPlayerData.hasDonorRank(player)
                  || getPlayerData.hasRequirePoint(
                      player.getUniqueId().toString(), getPlayerData.getRequirePoints("MAGIC"))) {
                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "MAGIC");
                player.sendMessage(
                    ChatColor.GREEN + "You selected" + ChatColor.YELLOW + " MAGIC kill effect.");
              } else {
                player.sendMessage(ChatColor.RED + "You don't have enought points");
              }
            }
          }
        }
      }
    } catch (NullPointerException ignored) {
    }
  }
}
