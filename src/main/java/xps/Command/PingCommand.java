package xps.Command;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        Player p = (Player) s;

        if(args.length == 0) {
            s.sendMessage(ChatColor.AQUA  + "Your ping is " + getPing(p) + " ms");
        }
        return true;
    }

    public int getPing(Player p) {
        CraftPlayer pingcraft = (CraftPlayer) p;
        EntityPlayer pingentity = pingcraft.getHandle();
        return pingentity.ping;
    }
}
