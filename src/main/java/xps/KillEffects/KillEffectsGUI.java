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
import xps.Config.KillEffectsConfig;
import xps.Database.MySQLSetterGetter;

import static xps.KillEffects.DefaultGUI.createGuiItem;

public class KillEffectsGUI implements Listener {

    public static Inventory EffectsGUI;
    private final getPlayerData playerData = new getPlayerData();

    public KillEffectsGUI() {
        EffectsGUI = Bukkit.createInventory(null, 27, "Kill Effect Selector");
        addIconItems();
    }

    private void addIconItems() {

        ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
        ItemMeta reset_meta = reset.getItemMeta();

        reset_meta.setDisplayName(ChatColor.RED + "Reset Kill Effect");
        reset.setItemMeta(reset_meta);

        ItemStack back = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
        ItemMeta back_meta = reset.getItemMeta();

        back_meta.setDisplayName(ChatColor.RED + "Go to previous page ⇒");
        reset.setItemMeta(back_meta);

        EffectsGUI.setItem(1, createGuiItem(Material.REDSTONE, ChatColor.AQUA + "BLOOD", ChatColor.YELLOW + "Click to Select!"));
        EffectsGUI.setItem(19, createGuiItem(Material.GOLDEN_APPLE, ChatColor.AQUA + "HEART", ChatColor.YELLOW + "Click to select!", "", ChatColor.RED + "- Donor Only -"));
        EffectsGUI.setItem(20, createGuiItem(Material.FIREWORK_CHARGE, ChatColor.AQUA + "SMOKE", ChatColor.YELLOW + "Click to select!", "", ChatColor.RED + "- Donor Only -"));
        EffectsGUI.setItem(2, createGuiItem(Material.LAVA_BUCKET, ChatColor.AQUA + "FLAME", ChatColor.YELLOW + "Click to select!"));
        EffectsGUI.setItem(3, createGuiItem(Material.NETHER_STAR, ChatColor.DARK_PURPLE + "RAINBOW", ChatColor.YELLOW + "Click to select!"));
        EffectsGUI.setItem(21, createGuiItem(Material.GOLD_NUGGET, ChatColor.GOLD + "DONOR", ChatColor.YELLOW + "Click to select!", "", ChatColor.RED + "- Donor Only -"));
        EffectsGUI.setItem(27, reset);
        EffectsGUI.setItem(8, back);
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
                        if(getItemName.equals(ChatColor.RED + "Go to previous page ⇒")) {
                            player.openInventory(DefaultGUI.gui);
                        }
                        if (getItemName.equals(ChatColor.RED + "Reset Kill Effect")) {
                            MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "NONE");
                            player.sendMessage(ChatColor.GREEN + "Reset your " + ChatColor.YELLOW + "Kill Effect");
                            player.closeInventory();
                        } else if (getItemName.equals(ChatColor.AQUA + "BLOOD")) {
                            if (playerData.hasRequirePoint(player.getUniqueId().toString(), KillEffectsConfig.getCustomConfig().getInt("BLOOD.points"))) {
                                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "BLOOD");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "BLOOD Kill Effect");
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have enough points");
                            }
                            player.updateInventory();
                        } else if (getItemName.equals(ChatColor.AQUA + "HEART")) {
                            if (playerData.hasDonorRank(player)) {
                                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "HEART");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "HEART kill effect.");
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have the donor rank");
                            }
                            player.updateInventory();

                        } else if (getItemName.equals(ChatColor.AQUA + "SMOKE")) {
                            if (playerData.hasDonorRank(player)) {
                                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "SMOKE");
                                player.sendMessage(ChatColor.GREEN + "You selected " + ChatColor.YELLOW + "SMOKE kill effect.");
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have the donor rank");
                            }
                            player.updateInventory();

                        } else if (getItemName.equals(ChatColor.AQUA + "FLAME")) {
                            if (playerData.hasRequirePoint(player.getUniqueId().toString(), KillEffectsConfig.getCustomConfig().getInt("FLAME.points"))) {
                                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "FLAME");
                                player.sendMessage(ChatColor.GREEN + "You selected" + ChatColor.YELLOW + " FLAME kill effect.");
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have enought points");
                            }
                            player.updateInventory();

                        } else if (getItemName.equals(ChatColor.DARK_PURPLE + "RAINBOW")) {
                            if (playerData.hasRequirePoint(player.getUniqueId().toString(), KillEffectsConfig.getCustomConfig().getInt("RAINBOW.points"))) {
                                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "RAINBOW");
                                player.sendMessage(ChatColor.GREEN + "You selected" + ChatColor.YELLOW + " RAINBOW kill effect.");
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have enought points");
                            }
                            player.updateInventory();

                        } else if (getItemName.equals(ChatColor.GOLD + "DONOR")) {
                            if (player.hasPermission("pgm.group.donor")) {
                                MySQLSetterGetter.setKillEffect(player.getUniqueId().toString(), "DONOR");
                                player.sendMessage(ChatColor.GREEN + "You selected" + ChatColor.YELLOW + " DONOR kill effect.");
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have the donor rank");
                            }
                            player.updateInventory();
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {

        }
    }
}