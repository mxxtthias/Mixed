package network.atria.Commands.graph;

import app.ashcon.intake.CommandException;
import app.ashcon.intake.InvalidUsageException;
import app.ashcon.intake.InvocationCommandException;
import app.ashcon.intake.bukkit.BukkitIntake;
import app.ashcon.intake.util.auth.AuthorizationException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.util.text.TextException;

public class CommandExecutor extends BukkitIntake {

  public CommandExecutor(Plugin plugin, CommandGraph commandGraph) {
    super(plugin, commandGraph);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    try {
      return this.getCommandGraph()
          .getRootDispatcherNode()
          .getDispatcher()
          .call(this.getCommand(command, args), this.getNamespace(sender));
    } catch (AuthorizationException e) {
      sender.sendMessage(ChatColor.RED + "You don't have permission!");
    } catch (InvocationCommandException e) {
      if (e.getCause() instanceof TextException) {
        sender.sendMessage(ChatColor.RED + "Unknown Error");
      } else {
        sender.sendMessage(ChatColor.RED + "Unknown Error");
        e.printStackTrace();
      }
    } catch (InvalidUsageException e) {
      if (e.getMessage() != null) {
        sender.sendMessage(e.getMessage());
      }

      if (e.isFullHelpSuggested()) {
        sender.sendMessage(e.getCommand().getDescription().getUsage());
      }
    } catch (CommandException e) {
      sender.sendMessage(e.getMessage());
    }

    return false;
  }
}
