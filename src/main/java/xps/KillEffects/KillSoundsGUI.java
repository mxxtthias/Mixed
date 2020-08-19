package xps.KillEffects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xps.Database.MySQLSetterGetter;

import static xps.KillEffects.DefaultGUI.createGuiItem;

public class KillSoundsGUI implements Listener {

    public static Inventory SoundsGUI;
    private final getPlayerData playerData = new getPlayerData();

    public KillSoundsGUI() {
        SoundsGUI = Bukkit.createInventory(null, 27, "Kill Sound Selector");
        addIconItems();
    }

    private void addIconItems() {

        ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
        ItemMeta reset_meta = reset.getItemMeta();

        reset_meta.setDisplayName(ChatColor.RED + "Reset Kill Sound");
        reset.setItemMeta(reset_meta);

        ItemStack back = new ItemStack(Material.ARROW, 1);
        ItemMeta back_meta = reset.getItemMeta();

        back_meta.setDisplayName(ChatColor.RED + "Go to previous page ⇒");
        back.setItemMeta(back_meta);

        SoundsGUI.setItem(9, createGuiItem(Material.GHAST_TEAR, ChatColor.AQUA + "DEFAULT", ChatColor.YELLOW + "Click to Select!"));
        SoundsGUI.setItem(15, createGuiItem(Material.BONE, ChatColor.AQUA + "HOWL", ChatColor.YELLOW + "Click to select!"));
        SoundsGUI.setItem(13, createGuiItem(Material.REDSTONE, ChatColor.AQUA + "VILLAGER", ChatColor.YELLOW + "Click to select!"));
        SoundsGUI.setItem(11, createGuiItem(Material.IRON_INGOT, ChatColor.AQUA + "GOLEM", ChatColor.YELLOW + "Click to Select!"));
        SoundsGUI.setItem(26, reset);
        SoundsGUI.setItem(8, back);
    }

    @EventHandler
    public void onGuiClick(final InventoryClickEvent e) {
        try {
            if (e.getView().getTitle().equals("Kill Sound Selector")) {
                e.setCancelled(true);

                final ItemStack clickedItem = e.getCurrentItem();

                final Player player = (Player) e.getWhoClicked();

                String getItemName = clickedItem.getItemMeta().getDisplayName();

                if (clickedItem.hasItemMeta()) {
                    if (clickedItem.getItemMeta().hasDisplayName()) {
                        if(getItemName.equals(ChatColor.RED + "Go to previous page ⇒")) {
                            player.openInventory(DefaultGUI.gui);
                        }
                        if (getItemName.equals(ChatColor.RED + "Reset Kill Sound")) {
                            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "NONE");
                            player.sendMessage(ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + "Kill Sound");
                        } else if (getItemName.equals(ChatColor.AQUA + "HOWL")) {
                            if(playerData.hasRequirePoint(player.getUniqueId().toString(), playerData.getRequirePoints("HOWL"))) {
                                MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "HOWL");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "HOWL Kill Sound");
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have enought points");
                            }
                        } else if (getItemName.equals(ChatColor.AQUA + "VILLAGER")) {
                            if(playerData.hasRequirePoint(player.getUniqueId().toString(), playerData.getRequirePoints("VILLAGER"))) {
                                MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "VILLAGER");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "VILLAGER Kill Sound");
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have eought points");
                            }
                        } else if (getItemName.equals(ChatColor.AQUA + "DEFAULT")) {
                            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "DEFAULT");
                            player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "DEFAULT Kill Sound");
                        } else if (getItemName.equals(ChatColor.AQUA + "GOLEM")) {
                            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "GOLEM");
                            player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "GOLEM Kill Sound");
                        }
                        player.closeInventory();
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
    }
}