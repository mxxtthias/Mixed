package xps.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xps.Database.MySQLSetterGetter;
import xps.Database.RankUp;
import org.bukkit.entity.Player;
import xps.Main;

public class RankUPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        RankUp rankUp = new RankUp(Main.getInstance());
        Player p = (Player) s;
        if (args.length == 0) {
            if (rankUp.canRankup(p.getUniqueId().toString(), MySQLSetterGetter.getPoints(p.getUniqueId().toString()))) {

                rankUp.rankUP(p.getUniqueId().toString(), MySQLSetterGetter.getPoints(p.getUniqueId().toString()), p);
            }
        }
        return true;
    }
}
