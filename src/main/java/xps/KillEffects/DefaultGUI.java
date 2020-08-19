package xps.KillEffects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class DefaultGUI implements Listener, CommandExecutor {

    public static Inventory gui;

    public DefaultGUI() {
        gui = Bukkit.createInventory(null, 27, "Kill Effect/Sound Selector");
        addIconItems();
    }

    private void addIconItems() {

        ItemStack reset = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
        ItemMeta meta = reset.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Close the GUI");
        reset.setItemMeta(meta);

        gui.setItem(10, createGuiItem(Material.REDSTONE, ChatColor.GREEN + "Kill Effects"));
        gui.setItem(13, createGuiItem(Material.RECORD_3, ChatColor.GREEN + "Kill Sounds"));
        gui.setItem(16, reset);
    }

    @EventHandler
    public void onGuiClick(final InventoryClickEvent e) {
        try {
            if (e.getView().getTitle().equals("Kill Effect/Sound Selector")) {
                e.setCancelled(true);

                final ItemStack clickedItem = e.getCurrentItem();

                final Player player = (Player) e.getWhoClicked();

                String getItemName = clickedItem.getItemMeta().getDisplayName();

                if (clickedItem.hasItemMeta()) {
                    if (clickedItem.getItemMeta().hasDisplayName()) {
                        if (getItemName.equals(ChatColor.RED + "Close the GUI")) {
                            player.closeInventory();
                        } else if (getItemName.equals(ChatColor.GREEN + "Kill Effects")) {
                            player.openInventory(KillEffectsGUI.EffectsGUI);
                        } else if (getItemName.equals(ChatColor.GREEN + "Kill Sounds")) {
                            player.openInventory(KillSoundsGUI.SoundsGUI);
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    protected static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        Player player = (Player) s;

        if(args.length == 0) {
            player.openInventory(gui);
        }
        return true;
    }
}
