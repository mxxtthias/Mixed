package network.atria.Ranks;

import net.kyori.adventure.text.Component;

public class Rank {

  private final String Name;
  private final Component coloredName;
  private final int point;

  public Rank(String Name, Component coloredName, int point) {
    this.Name = Name;
    this.coloredName = coloredName;
    this.point = point;
  }

  public String getName() {
    return Name;
  }

  public Component getColoredName() {
    return coloredName;
  }

  public int getPoint() {
    return point;
  }
}
