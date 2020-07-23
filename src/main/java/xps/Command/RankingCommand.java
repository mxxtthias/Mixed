package xps.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xps.Database.MySQLSetterGetter;
import xps.Main;

public class RankingCommand implements CommandExecutor, Listener {
    static HashMap<Integer, String> rang = new HashMap<>();
    static HashMap<Integer, String> rang_2 = new HashMap<>();
    static HashMap<Integer, String> rang_3 = new HashMap<>();
    static HashMap<Integer, String> rang_4 = new HashMap<>();
    static HashMap<Integer, String> rang_5 = new HashMap<>();

    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        Player p = (Player) s;


        Inventory inv = Bukkit.createInventory(null, 54, "RANKING");

        if (c.getName().equals("ranking") | c.getName().equals("tops")) {

            ResultSet rs_kill = Main.mysql.query("SELECT UUID FROM STATS ORDER BY KILLS DESC LIMIT 3");
            ResultSet rs_death = Main.mysql.query("SELECT UUID FROM STATS ORDER BY DEATHS DESC LIMIT 3");
            ResultSet rs_wool = Main.mysql.query("SELECT UUID FROM STATS ORDER BY WOOLS DESC LIMIT 3");
            ResultSet rs_flag = Main.mysql.query("SELECT UUID FROM STATS ORDER BY FLAGS DESC LIMIT 3");
            ResultSet rs_core = Main.mysql.query("SELECT UUID FROM STATS ORDER BY CORES DESC LIMIT 3");

            int i = 0;
            try {
                while (rs_kill.next() | rs_death.next() | rs_wool.next() | rs_flag.next() | rs_core.next()) {
                    i++;
                    rang.put(i, rs_kill.getString("UUID"));
                    rang_2.put(i, rs_death.getString("UUID"));
                    rang_3.put(i, rs_wool.getString("UUID"));
                    rang_4.put(i, rs_flag.getString("UUID"));
                    rang_5.put(i, rs_core.getString("UUID"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            inv.setItem(0, item(material(Material.STONE_SWORD), ChatColor.AQUA + "" + ChatColor.BOLD + "TOP 3 KILLS", ""));
            inv.setItem(2, item(material(Material.REDSTONE), ChatColor.AQUA + "" + ChatColor.BOLD + "TOP 3 DEATHS", ""));
            inv.setItem(4, item(material(Material.WOOL), ChatColor.AQUA + "" + ChatColor.BOLD + "TOP 3 WOOLS", ""));
            inv.setItem(6, item(material(Material.SIGN), ChatColor.AQUA + "" + ChatColor.BOLD + "TOP 3 FLAGS", ""));
            inv.setItem(8, item(material(Material.OBSIDIAN), ChatColor.AQUA + "" + ChatColor.BOLD + "TOP 3 CORES", ""));

            for (i = 0; i < 3; ) {
                int id = i + 1;

                String name = MySQLSetterGetter.getName(rang.get(id));
                String name_2 = MySQLSetterGetter.getName(rang_2.get(id));
                String name_3 = MySQLSetterGetter.getName(rang_3.get(id));
                String name_4 = MySQLSetterGetter.getName(rang_4.get(id));
                String name_5 = MySQLSetterGetter.getName(rang_5.get(id));

                String kills = MySQLSetterGetter.getKills(rang.get(id)).toString() + " kills";
                String deaths = MySQLSetterGetter.getDeaths(rang_2.get(id)).toString() + " deaths";
                String wools = MySQLSetterGetter.getWools(rang_3.get(id)).toString() + " wools";
                String flags = MySQLSetterGetter.getFlags(rang_4.get(id)).toString() + " flags";
                String cores = MySQLSetterGetter.getCores(rang_5.get(id)).toString() + " cores";

                if (i == 0) {
                    inv.setItem(18, skull(name, id + "位" + " " + name, kills));
                    inv.setItem(20, skull(name_2, id + "位" + " " + name_2, deaths));
                    inv.setItem(22, skull(name_3, id + "位" + " " + name_3, wools));
                    inv.setItem(24, skull(name_4, id + "位" + " " + name_4, flags));
                    inv.setItem(26, skull(name_5, id + "位" + " " + name_5, cores));
                }

                if (i == 1) {
                    inv.setItem(27, skull(name, id + "位" + " " + name, kills));
                    inv.setItem(29, skull(name_2, id + "位" + " " + name_2, deaths));
                    inv.setItem(31, skull(name_3, id + "位" + " " + name_3, wools));
                    inv.setItem(33, skull(name_4, id + "位" + " " + name_4, flags));
                    inv.setItem(35, skull(name_5, id + "位" + " " + name_5, cores));
                }

                if (i == 2) {
                    inv.setItem(36, skull(name, id + "位" + " " + name, kills));
                    inv.setItem(38, skull(name_2, id + "位" + " " + name_2, deaths));
                    inv.setItem(40, skull(name_3, id + "位" + " " + name_3, wools));
                    inv.setItem(42, skull(name_4, id + "位" + " " + name_4, flags));
                    inv.setItem(44, skull(name_5, id + "位" + " " + name_5, cores));
                }
                i++;
            }
            p.openInventory(inv);
        }
        return true;
    }

    private ItemStack skull(String name, String item_name, String lore) {

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setLore(Arrays.asList(ChatColor.GOLD + "" + ChatColor.BOLD + lore));
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + item_name);
        meta.setOwner(name);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack material(Material material) {

        return new ItemStack(material, 1);
    }

    private ItemStack item(ItemStack item, String name, String lore) {
        ItemMeta meta = (ItemMeta) item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getView().getTitle().equals("RANKING")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getView().getTitle().equals("RANKING")) {
            e.setCancelled(true);
        }
    }
}