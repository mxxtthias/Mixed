package xps.Command;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
import xps.Database.MySQLSetterGetter;
import xps.Main;

public class RankingCommand implements CommandExecutor, Listener {

    static HashMap<Integer, String> rang = new HashMap<>();
    static HashMap<Integer, String> rang_2 = new HashMap<>();
    static HashMap<Integer, String> rang_3 = new HashMap<>();
    static HashMap<Integer, String> rang_4 = new HashMap<>();
    static HashMap<Integer, String> rang_5 = new HashMap<>();
    static HashMap<Integer, String> rang_6 = new HashMap<>();

    private final ArrayList<String> kill_lores = new ArrayList<>();
    private final ArrayList<String> death_lores = new ArrayList<>();
    private final ArrayList<String> wools_lores = new ArrayList<>();
    private final ArrayList<String> flags_lores = new ArrayList<>();
    private final ArrayList<String> cores_lores = new ArrayList<>();
    private final ArrayList<String> monuments_lores = new ArrayList<>();

    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        Player p = (Player) s;

        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_AQUA + "Ranking");

        if (c.getName().equals("ranking") | c.getName().equals("tops")) {

            ResultSet rs_kill = Main.mysql.query("SELECT UUID FROM STATS ORDER BY KILLS DESC LIMIT 5");
            ResultSet rs_death = Main.mysql.query("SELECT UUID FROM STATS ORDER BY DEATHS DESC LIMIT 5");
            ResultSet rs_wool = Main.mysql.query("SELECT UUID FROM STATS ORDER BY WOOLS DESC LIMIT 5");
            ResultSet rs_flag = Main.mysql.query("SELECT UUID FROM STATS ORDER BY FLAGS DESC LIMIT 5");
            ResultSet rs_core = Main.mysql.query("SELECT UUID FROM STATS ORDER BY CORES DESC LIMIT 5");
            ResultSet rs_monuments = Main.mysql.query("SELECT UUID FROM STATS ORDER BY MONUMENTS DESC LIMIT 5");

            int i = 0;
            try {
                while (rs_kill.next() | rs_death.next() | rs_wool.next() | rs_flag.next() | rs_core.next() | rs_monuments.next()) {
                    i++;
                    rang.put(i, rs_kill.getString("UUID"));
                    rang_2.put(i, rs_death.getString("UUID"));
                    rang_3.put(i, rs_wool.getString("UUID"));
                    rang_4.put(i, rs_flag.getString("UUID"));
                    rang_5.put(i, rs_core.getString("UUID"));
                    rang_6.put(i, rs_monuments.getString("UUID"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (i = 0; i < 5; i++) {
                int id = i + 1;

                String name = ChatColor.RED + MySQLSetterGetter.getName(rang.get(id)) + " ";
                String name_2 = ChatColor.RED + MySQLSetterGetter.getName(rang_2.get(id)) + " ";
                String name_3 = ChatColor.RED + MySQLSetterGetter.getName(rang_3.get(id)) + " ";
                String name_4 = ChatColor.RED + MySQLSetterGetter.getName(rang_4.get(id)) + " ";
                String name_5 = ChatColor.RED + MySQLSetterGetter.getName(rang_5.get(id)) + " ";
                String name_6 = ChatColor.RED + MySQLSetterGetter.getName(rang_6.get(id)) + " ";

                String kills = ChatColor.GOLD + MySQLSetterGetter.getKills(rang.get(id)).toString() + " kills";
                String deaths = ChatColor.GOLD + MySQLSetterGetter.getDeaths(rang_2.get(id)).toString() + " deaths";
                String wools = ChatColor.GOLD + MySQLSetterGetter.getWools(rang_3.get(id)).toString() + " wools";
                String flags = ChatColor.GOLD + MySQLSetterGetter.getFlags(rang_4.get(id)).toString() + " flags";
                String cores = ChatColor.GOLD + MySQLSetterGetter.getCores(rang_5.get(id)).toString() + " cores";
                String monuments = ChatColor.GOLD + MySQLSetterGetter.getMonuments(rang_6.get(id)).toString() + " monuments";

                String rank = ChatColor.RED + "" + id + " ";

                if (id == 1) {
                    kill_lores.add(rank + name + kills);
                    death_lores.add(rank + name_2 + deaths);
                    wools_lores.add(rank + name_3 + wools);
                    flags_lores.add(rank + name_4 + flags);
                    cores_lores.add(rank + name_5 + cores);
                    monuments_lores.add(rank + name_6 + monuments);
                }
                if (id == 2) {
                    kill_lores.add(rank + name + kills);
                    death_lores.add(rank + name_2 + deaths);
                    wools_lores.add(rank + name_3 + wools);
                    flags_lores.add(rank + name_4 + flags);
                    cores_lores.add(rank + name_5 + cores);
                    monuments_lores.add(rank + name_6 + monuments);
                }
                if(id == 3) {
                    kill_lores.add(rank + name + kills);
                    death_lores.add(rank + name_2 + deaths);
                    wools_lores.add(rank + name_3 + wools);
                    flags_lores.add(rank + name_4 + flags);
                    cores_lores.add(rank + name_5 + cores);
                    monuments_lores.add(rank + name_6 + monuments);
                }
                if(id == 4) {
                    kill_lores.add(rank + name + kills);
                    death_lores.add(rank + name_2 + deaths);
                    wools_lores.add(rank + name_3 + wools);
                    flags_lores.add(rank + name_4 + flags);
                    cores_lores.add(rank + name_5 + cores);
                    monuments_lores.add(rank + name_6 + monuments);
                }
                if (id == 5) {
                    kill_lores.add(rank + name + kills);
                    death_lores.add(rank + name_2 + deaths);
                    wools_lores.add(rank + name_3 + wools);
                    flags_lores.add(rank + name_4 + flags);
                    cores_lores.add(rank + name_5 + cores);
                    monuments_lores.add(rank + name_6 + monuments);
                }
            }
            inv.setItem(0, meta(material(Material.STONE_SWORD), ChatColor.AQUA + "TOP 5 KILLS", kill_lores));
            inv.setItem(2, meta(material(Material.REDSTONE), ChatColor.AQUA + "TOP 5 DEATHS", death_lores));
            inv.setItem(4, meta(material(Material.WOOL), ChatColor.AQUA + "TOP 5 WOOLS", wools_lores));
            inv.setItem(6, meta(material(Material.SIGN), ChatColor.AQUA + "TOP 5 FLAGS", flags_lores));
            inv.setItem(8, meta(material(Material.OBSIDIAN), ChatColor.AQUA + "TOP 5 CORES", cores_lores));
            inv.setItem(18, meta(material(Material.DIAMOND_PICKAXE), ChatColor.AQUA + "TOP 5 Monuments", monuments_lores));

            p.openInventory(inv);
            kill_lores.clear(); death_lores.clear(); wools_lores.clear(); flags_lores.clear(); cores_lores.clear(); monuments_lores.clear();
        }
        return true;
    }

    private ItemStack material(final Material material) {

        return new ItemStack(material, 1);
    }

    private ItemStack meta(final ItemStack item,final String name, final List<String> lore) {
        ItemMeta meta = (ItemMeta) item.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Ranking")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if(e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Ranking")) {
            e.setCancelled(true);
        }
    }
}