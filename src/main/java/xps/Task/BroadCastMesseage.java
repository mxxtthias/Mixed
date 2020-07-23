package xps.Task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import xps.Main;

import java.util.*;

public class BroadCastMesseage {

    private final String prefix = ChatColor.WHITE + "" + ChatColor.BOLD  + "[" + ChatColor.BLUE + "" + ChatColor.BOLD + "TIP" + ChatColor.WHITE + "" + ChatColor.BOLD + "] " + ChatColor.DARK_AQUA;

    public void randomMesseage() {

        List<String> messeages = Main.getInstance().getConfig().getStringList("Messeages");

        Random random = new Random();
        int index = random.nextInt(messeages.size());
        String result = messeages.get(index).toString();

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', result));
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20 * 60 * 4);
    }
}
