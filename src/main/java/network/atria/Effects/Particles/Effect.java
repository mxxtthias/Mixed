package network.atria.Effects.Particles;

import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Effect {

  private final String Name;
  private final Component coloredName;
  private final int point;
  private final boolean donor;

  public Effect(String Name, Component coloredName, int point, boolean donor) {
    this.Name = Name;
    this.coloredName = coloredName;
    this.point = point;
    this.donor = donor;
  }

  public String getName() {
    return this.Name;
  }

  public Component getColoredName() {
    return this.coloredName;
  }

  public Integer getPoint() {
    return this.point;
  }

  public boolean canUseDonor() {
    return this.donor;
  }

  public boolean canUseDonorOnly(Player player) {
    return this.donor && player.hasPermission("pgm.group.donor");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Effect effect = (Effect) o;
    return Name.equals(effect.Name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Name);
  }

  @Override
  public String toString() {
    return "Effect{"
        + "Name='"
        + Name
        + '\''
        + ", coloredName="
        + coloredName
        + ", point="
        + point
        + ", donor="
        + donor
        + '}';
  }
}
