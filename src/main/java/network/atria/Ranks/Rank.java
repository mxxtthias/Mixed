package network.atria.Ranks;

import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rank rank = (Rank) o;
    return Objects.equals(Name, rank.Name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Name);
  }

  @Override
  public String toString() {
    return "Rank{"
        + "Name='"
        + Name
        + '\''
        + ", coloredName="
        + coloredName
        + ", point="
        + point
        + '}';
  }
}
