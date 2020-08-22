package xps.KillEffects;

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
import xps.Config.KillEffectsConfig;
import xps.Database.MySQLSetterGetter;

import static xps.KillEffects.DefaultGUI.createGuiItem;

public class KillSoundsGUI implements Listener {

    public static Inventory SoundsGUI;
    private String uuid;

    @EventHandler
    private void getPlayer(InventoryOpenEvent e) {
        if(SoundsGUI == e.getInventory()) {
            uuid = e.getPlayer().toString();
            addIconItems();
        }
    }

    public KillSoundsGUI() {
        SoundsGUI = Bukkit.createInventory(null, 27, "Kill Sound Selector");
    }

    private void addIconItems() {

        ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
        ItemMeta reset_meta = reset.getItemMeta();

        reset_meta.setDisplayName(ChatColor.RED + "Reset Kill Sound");
        reset.setItemMeta(reset_meta);

        ItemStack back = new ItemStack(Material.ARROW, 1);
        ItemMeta back_meta = back.getItemMeta();

        back_meta.setDisplayName(ChatColor.RED + "Go to previous page ➡");
        back.setItemMeta(back_meta);

        SoundsGUI.setItem(9, createGuiItem(Material.GHAST_TEAR, ChatColor.AQUA + "DEFAULT","", getPlayerData.canUseEffects(uuid, 0)));
        SoundsGUI.setItem(10, createGuiItem(Material.REDSTONE, ChatColor.AQUA + "VILLAGER","", getPlayerData.canUseEffects(uuid, getSoundPoint("VILLAGER"))));
        SoundsGUI.setItem(11, createGuiItem(Material.BONE, ChatColor.AQUA + "HOWL", "", getPlayerData.canUseEffects(uuid, getSoundPoint("HOWL"))));
        SoundsGUI.setItem(12, createGuiItem(Material.TNT, ChatColor.AQUA + "BOMB", "", getPlayerData.canUseEffects(uuid, getSoundPoint("BOMB"))));
        SoundsGUI.setItem(13, createGuiItem(Material.SEEDS, ChatColor.AQUA + "BURP","", getPlayerData.canUseEffects(uuid, getSoundPoint("BURP"))));
        SoundsGUI.setItem(26, reset);
        SoundsGUI.setItem(8, back);
    }

    private Integer getSoundPoint(String sound) {
        return KillEffectsConfig.getCustomConfig().getInt(sound);
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
                        if (getItemName.equals(ChatColor.RED + "Go to previous page ➡")) {
                            player.openInventory(DefaultGUI.gui);
                        }
                        if (getItemName.equals(ChatColor.RED + "Reset Kill Sound")) {
                            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "NONE");
                            player.sendMessage(ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + "Kill Sound");
                        } else if (getItemName.equals(ChatColor.AQUA + "HOWL")) {
                            if(getPlayerData.hasRequirePoint(player.getUniqueId().toString(), getPlayerData.getRequirePoints("HOWL"))) {
                                MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "HOWL");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "HOWL Kill Sound");
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have enought points");
                            }
                        } else if (getItemName.equals(ChatColor.AQUA + "VILLAGER")) {
                            if(getPlayerData.hasRequirePoint(player.getUniqueId().toString(), getPlayerData.getRequirePoints("VILLAGER"))) {
                                MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "VILLAGER");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "VILLAGER Kill Sound");
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have enought points");
                            }
                        } else if (getItemName.equals(ChatColor.AQUA + "DEFAULT")) {
                            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "DEFAULT");
                            player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "DEFAULT Kill Sound");
                        } else if (getItemName.equals(ChatColor.AQUA + "BOMB")) {
                            if (getPlayerData.hasRequirePoint(player.getUniqueId().toString(), getPlayerData.getRequirePoints("BOMB"))) {
                                MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "BOMB");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "BOMB Kill Sound");
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have enought points");
                            }
                        } else if (getItemName.equals(ChatColor.AQUA + "BURP")) {
                            if(getPlayerData.hasRequirePoint(player.getUniqueId().toString(), getPlayerData.getRequirePoints("BURP"))) {
                                MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "BURP");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "BURP Kill Sound");
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