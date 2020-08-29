package network.atria.KillEffects;

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
import network.atria.Util.KillEffectsConfig;
import network.atria.Database.MySQLSetterGetter;

import static network.atria.KillEffects.DefaultGUI.createGuiItem;

public class ProjectileGUI implements Listener {

    public static Inventory ProjectileGUI;

    public ProjectileGUI() {
        ProjectileGUI = Bukkit.createInventory(null, 27, "Projectile Trails Selector");
    }

    @EventHandler
    public void getPlayer(InventoryOpenEvent e) {
        if (e.getView().getTitle().equals("Projectile Trails Selector")) {
            String uuid = e.getPlayer().getUniqueId().toString();
            addIconItems(uuid);
        }
    }

    private void addIconItems(String uuid) {

        ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
        ItemMeta reset_meta = reset.getItemMeta();

        reset_meta.setDisplayName(ChatColor.RED + "Reset Projectile Trail");
        reset.setItemMeta(reset_meta);

        ItemStack back = new ItemStack(Material.ARROW, 1);
        ItemMeta back_meta = back.getItemMeta();

        back_meta.setDisplayName(ChatColor.RED + "Go to previous page ➡");
        back.setItemMeta(back_meta);

        ItemStack itemStack = new ItemStack(Material.INK_SACK, 1, (short) DyeColor.LIME.getData());

        ProjectileGUI.setItem(10, createGuiItem(Material.GOLDEN_APPLE, ChatColor.AQUA + "HEART", "", getPlayerData.canUseEffects(uuid, getProjectilePoint("HEART_TRAIL"))));
        ProjectileGUI.setItem(11, createGuiItem(Material.POTION, ChatColor.AQUA + "WITCH", "", getPlayerData.canUseEffects(uuid, getProjectilePoint("WITCH"))));
        ProjectileGUI.setItem(12, createGuiItem(Material.NETHER_STAR, ChatColor.AQUA + "RAINBOW", "", getPlayerData.canUseEffects(uuid, getProjectilePoint("RAINBOW_TRAIL"))));
        ProjectileGUI.setItem(13, createGuiItem(itemStack.getType(), ChatColor.AQUA + "GREEN", "", getPlayerData.canUseEffects(uuid, getProjectilePoint("GREEN"))));
        ProjectileGUI.setItem(14, createGuiItem(Material.NOTE_BLOCK, ChatColor.AQUA + "NOTE", "", getPlayerData.canUseEffects(uuid, getProjectilePoint("NOTE"))));

        ProjectileGUI.setItem(26, reset);
        ProjectileGUI.setItem(8, back);
    }

    private Integer getProjectilePoint(String projectile) {
        return KillEffectsConfig.getCustomConfig().getInt(projectile + ".points");
    }

    @EventHandler
    public void onGuiClick(final InventoryClickEvent e) {
        try {
            if (e.getView().getTitle().equals("Projectile Trails Selector")) {
                e.setCancelled(true);

                final ItemStack clickedItem = e.getCurrentItem();

                final Player player = (Player) e.getWhoClicked();

                String getItemName = clickedItem.getItemMeta().getDisplayName();

                if (clickedItem.hasItemMeta()) {
                    if (clickedItem.getItemMeta().hasDisplayName()) {
                        if (getItemName.equals(ChatColor.RED + "Go to previous page ➡")) {
                            player.openInventory(DefaultGUI.gui);
                        }
                        if (getItemName.equals(ChatColor.RED + "Reset Projectile Trail")) {
                            MySQLSetterGetter.setKillSound(player.getUniqueId().toString(), "NONE");
                            player.sendMessage(ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + "Kill Sound");
                        } else if (getItemName.equals(ChatColor.AQUA + "HEART")) {
                            if (getPlayerData.hasRequirePoint(player.getUniqueId().toString(), getPlayerData.getRequirePoints("HEART_TRAIL"))) {
                                MySQLSetterGetter.setProjectileTrails(player.getUniqueId().toString(), "HEART");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "HEART Projectile Trail");
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have enought points");
                            }
                        } else if (getItemName.equals(ChatColor.AQUA + "WITCH")) {
                            if (getPlayerData.hasRequirePoint(player.getUniqueId().toString(), getPlayerData.getRequirePoints("WITCH"))) {
                                MySQLSetterGetter.setProjectileTrails(player.getUniqueId().toString(), "WITCH");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "WITCH Projectile Trail");
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have enought points");
                            }
                        } else if (getItemName.equals(ChatColor.AQUA + "RAINBOW")) {
                            if (getPlayerData.hasRequirePoint(player.getUniqueId().toString(), getPlayerData.getRequirePoints("RAINBOW_TRAIL"))) {
                                MySQLSetterGetter.setProjectileTrails(player.getUniqueId().toString(), "RAINBOW_TRAIL");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "RAINBOW Projectile Trail");
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have enought points");
                            }
                        } else if (getItemName.equals(ChatColor.AQUA + "GREEN")) {
                            if (getPlayerData.hasRequirePoint(player.getUniqueId().toString(), getPlayerData.getRequirePoints("GREEN"))) {
                                MySQLSetterGetter.setProjectileTrails(player.getUniqueId().toString(), "GREEN");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "GREEN Projectile Trail");
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have enought points");
                            }
                        } else {
                            if (getItemName.equals(ChatColor.AQUA + "NOTE")) {
                                if (getPlayerData.hasRequirePoint(player.getUniqueId().toString(), getPlayerData.getRequirePoints("NOTE"))) {
                                    MySQLSetterGetter.setProjectileTrails(player.getUniqueId().toString(), "NOTE");
                                    player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "NOTE Projectile Trail");
                                } else {
                                    player.sendMessage(ChatColor.RED + "You don't have enought points");
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
    }
}