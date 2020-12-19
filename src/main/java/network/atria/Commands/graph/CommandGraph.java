package network.atria.Commands.graph;

import app.ashcon.intake.bukkit.graph.BasicBukkitCommandGraph;
import app.ashcon.intake.fluent.DispatcherNode;
import network.atria.Commands.RankCommand;
import network.atria.Commands.ServerInfoCommand;
import network.atria.Commands.StatsCommand;
import network.atria.Effects.GUI.DefaultGUI;
import network.atria.Mixed;

public class CommandGraph extends BasicBukkitCommandGraph {

  public CommandGraph() {
    this.registerAll();
  }

  public void registerAll() {
    register(new ServerInfoCommand());
    register(new StatsCommand());
    register(new DefaultGUI(Mixed.get()));
    register(new RankCommand());
  }

  public void register(Object command, String... aliases) {
    DispatcherNode node = getRootDispatcherNode();
    if (aliases.length > 0) {
      node = node.registerNode(aliases);
    }
    node.registerCommands(command);
  }
}
