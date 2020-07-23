package xps.Task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import xps.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BroadCastMesseage {

    private final String prefix = ChatColor.WHITE + "" + ChatColor.BOLD  + "[" + ChatColor.BLUE + "" + ChatColor.BOLD + "TIP" + ChatColor.WHITE + "" + ChatColor.BOLD + "] " + ChatColor.DARK_AQUA;

    public final List<String> messeages = new ArrayList<String>(Arrays.asList
            (prefix + "Atria Networkの公式Discordに入ろう！" + ChatColor.AQUA + " https://discord.gg/X3F6S6m",
                    prefix + ChatColor.AQUA + "/stats" + ChatColor.DARK_AQUA + "で自分の戦績を確認できます!",
                    prefix + ChatColor.AQUA + "/toggle" + ChatColor.DARK_AQUA + "で様々な機能を切り替えること出来ます!",
                    prefix + ChatColor.DARK_AQUA + "もしルールを破っている人を見かけたら" + ChatColor.AQUA + "/report" +  ChatColor.DARK_AQUA + "でスタッフに報告して下さい",
                    prefix + ChatColor.DARK_AQUA + "全体にチャットをする際は" + ChatColor.AQUA + " /g" + ChatColor.DARK_AQUA + " を使って切り替えることが出来ます！",
                    prefix + ChatColor.DARK_AQUA + "サーバーリストで投票すると2時間premiumランクが適応されます！ " + ChatColor.AQUA + "https://minecraft.jp/servers/play.atria.network",
                    prefix + ChatColor.AQUA + "/ranking " + ChatColor.DARK_AQUA + "で現在のランキング上位3名が表示されます！"));

    public void randomBroadCast() {

        String result = random(null);

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(random(result));
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20 * 60 * 4);
    }

    public String random(String result) {
        int index = new Random().nextInt(messeages.size());
        result = messeages.get(index);

        return result;
    }
}
